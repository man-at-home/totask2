package org.regele.totask2.service;

import static java.time.temporal.TemporalAdjusters.*;


import org.regele.totask2.model.TaskAssignment;
import org.regele.totask2.model.TaskAssignmentRepository;
import org.regele.totask2.model.TaskInWeek;
import org.regele.totask2.model.User;
import org.regele.totask2.model.WorkEntry;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.util.ApplicationAssert;
import org.regele.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * packaging and handling workEntries for weekly based operations.
 * 
 * @author man-at-home
 * @since 2014-08-09
 */
@Service
public class WeekEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryService.class);   
    
    /** already used tasks. work entry repository. */
    @Autowired private WorkEntryRepository workEntryRepository;   
    
    /** possible tasks. */
    @Autowired private TaskAssignmentRepository taskAssignmentRepository; 

    /** .ctor. */
    public WeekEntryService() {
    }
    
    /** 
     * all work done on a given week.
     * 
     *  @param user given user to log work for.
     *  @param dt   date in week (always full week is returned MON-SUN)
     *  @throws IllegalArgumentException
     *  @author man-at-home
     */
    public List<TaskInWeek> getWorkWeek(final User user, final LocalDate dt) {

        if (user == null)
            throw new IllegalArgumentException("no user for WorkWeek retrieval");
        
        if (dt == null)
            throw new IllegalArgumentException("no date for WorkWeek retrieval");
        
        LocalDate date  = dt.with(previousOrSame(DayOfWeek.MONDAY));
        
        Date from  = LocalDateConverter.toDate(date);
        Date until = LocalDateConverter.toDate(date.with(nextOrSame(DayOfWeek.SUNDAY)));
        
        LOG.debug("retrieving entries for user " + user + " between " + from + " - " + until + " rep: " + workEntryRepository);        
        
        List<WorkEntry> entries = workEntryRepository.findForUserAndTimespan(user.getId(), from, until); 
        
        // an entry for each task.
        List<TaskInWeek> tasksInWeek =        
         entries.stream()
                .map( we -> new TaskInWeek( we.getTask()) )
                .distinct()
                .collect( Collectors.toList() );
        
        LOG.debug("given already booked week tasks count: " + tasksInWeek.size());
        
        List<TaskAssignment> assignments = taskAssignmentRepository.findByUserAndPeriod(user.getId(), from, until);
        LOG.debug("possible tasks count: " + assignments.size());
        
        assignments.stream()
                   .filter(ta -> !tasksInWeek.stream().anyMatch(tw -> tw.getTask().getId() == ta.getTask().getId()))
                   .distinct()
                   .forEach(found -> tasksInWeek.add(new TaskInWeek(found.getTask())))
                   ;
        
        LOG.debug("booked AND allowed tasks count: " + tasksInWeek.size());
        
        for (TaskInWeek tiw : tasksInWeek) {
            for (int dayOffset = 0; dayOffset <= 6; dayOffset++) {
                final long offset = dayOffset;
                WorkEntry entryOfDay = entries.stream()
                        .filter(we -> we.getTask().getId() == tiw.getTask().getId() && we.getAtDate().equals(date.plusDays(offset)))
                        .findFirst()
                        .orElse(new WorkEntry(user, tiw.getTask(), date.plusDays(offset)));   // new empty entry
                
                tiw.getDailyEntries()[dayOffset] = entryOfDay;
            }
        }
        
        return tasksInWeek;
    }

    /** 
     * save weekEntries for given week. 
     * 
     * @return count of saved workEntries.
     * */
    public int saveWeek(List<TaskInWeek> tasksInWeek) {
        
        int saveCount = 0;
        
        LOG.debug("saveWeek()");
        ApplicationAssert.assertNotNull("tasksInWeek", tasksInWeek);
        
        for (TaskInWeek tiw : tasksInWeek) {
            if (tiw.isModifiedByUser()) {
                LOG.debug("saveWeek() Task " + tiw.getTask().getName());
                for (int dayOffset = 0; dayOffset <= 6; dayOffset++) {
                    WorkEntry we = tiw.getDailyEntries()[dayOffset];
                    ApplicationAssert.assertNotNull("we", we);
                    
                    if (we.isModifiedByUser()) {
                        LOG.debug("  save workEntry: " + we);
                        we = this.workEntryRepository.save(we);
                        tiw.getDailyEntries()[dayOffset] = we;
                        saveCount++;
                    }
                }
                this.workEntryRepository.flush();   // per task..
            }
        } 
        return saveCount;
    }

}
