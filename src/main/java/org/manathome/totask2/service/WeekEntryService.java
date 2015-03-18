package org.manathome.totask2.service;

import static java.time.temporal.TemporalAdjusters.*;

import org.manathome.totask2.model.TaskAssignment;
import org.manathome.totask2.model.TaskAssignmentRepository;
import org.manathome.totask2.model.TaskInWeek;
import org.manathome.totask2.model.User;
import org.manathome.totask2.model.WorkEntry;
import org.manathome.totask2.model.WorkEntryRepository;
import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;


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
     *  @author man-at-home
     */
    public List<TaskInWeek> getWorkWeek(@NotNull final User user, @NotNull final LocalDate dt) {

        AAssert.checkNotNull(user, "no user for WorkWeek retrieval");        
        LocalDate date  = AAssert.checkNotNull(dt, "no date for WorkWeek retrieval")
                            .with(previousOrSame(DayOfWeek.MONDAY));
        
        Date from  = LocalDateConverter.toDate(date);
        Date until = LocalDateConverter.toDate(date.with(nextOrSame(DayOfWeek.SUNDAY)));
        
        LOG.debug("retrieving entries for user " + user + " between " + from + " - " + until + " rep: " + workEntryRepository);        
        
        // add all tasks with already existing workEntries from database        
        List<WorkEntry> entries = workEntryRepository.findForUserAndTimespan(user.getId(), from, until); 
        
        // create exactly one (distinct) taskInWeek (map) for every task with 0 or more entries
        List<TaskInWeek> tasksInWeek =        
         entries.stream()
                .map(we -> new TaskInWeek(we.getTask()))
                .distinct()
                .collect(Collectors.toList());
        
        LOG.debug("given already booked week tasks count: " + tasksInWeek.size());
        
        // add potential suitable tasks (with no workEntries yet).
        List<TaskAssignment> assignments = taskAssignmentRepository.findByUserAndPeriod(user.getId(), from, until);
        LOG.debug("possible tasks count: " + assignments.size());
        
        assignments.stream()
                   .filter(ta -> !tasksInWeek.stream().anyMatch(tw -> tw.getTask().getId() == ta.getTask().getId()))
                   .distinct()
                   .forEach(found -> tasksInWeek.add(new TaskInWeek(found.getTask())))
                   ;
        
        LOG.debug("booked AND allowed tasks count: " + tasksInWeek.size());
        
        // with each task: fill all seven weekdays wie existing workentry (or new 0 one) 
        for (TaskInWeek tiw : tasksInWeek) {
            for (int dayOffset = 0; dayOffset <= 6; dayOffset++) {
                final long offset = dayOffset;
                WorkEntry entryOfDay = entries.stream()
                        .filter(we -> we.getTask().getId() == tiw.getTask().getId() && we.getAtDate().equals(date.plusDays(offset)))
                        .findFirst()                                                          // suitable entry for task and day OR
                        .orElse(new WorkEntry(user, tiw.getTask(), date.plusDays(offset)));   // .. new empty entry for task
                
                // allows an current assignment to edit this entry (task/day)?
                entryOfDay.setEditable(
                                      assignments.stream()
                                                 .anyMatch(a -> 
                                                    a.getTask().getId() == entryOfDay.getTask().getId() &&   // assignment for task
                                                    a.isInRange(entryOfDay.getAtDate())                      // and date
                                                    )
                                     );
                
                
                
                tiw.setDailyEntry(dayOffset, entryOfDay);
            }
        }
        
        return tasksInWeek;
    }

    /** 
     * save weekEntries for given week. 
     * 
     * @return count of saved workEntries.
     * */
    public int saveWeek(@NotNull List<TaskInWeek> tasksInWeek) {
        
        int saveCount = 0;
        
        LOG.trace("saveWeek()");
        
        for (TaskInWeek tiw : AAssert.checkNotNull(tasksInWeek)) {
            if (tiw.isNewOrModified()) {
                LOG.debug("saveWeek() Task " + tiw.getTask().getName());
                for (int dayOffset = 0; dayOffset <= 6; dayOffset++) {
                    WorkEntry we = tiw.getDailyEntry(dayOffset);
                    
                    if (AAssert.checkNotNull(we, "we").isModifiedByUser() || we.isNew()) {
                        LOG.debug("  save workEntry: " + we + " insert: " + we.isNew());
                        we = this.workEntryRepository.save(we);
                        tiw.setDailyEntry(dayOffset, we);
                        saveCount++;
                    }
                }
                this.workEntryRepository.flush();   // per task..
            }
        } 
        return saveCount;
    }

}
