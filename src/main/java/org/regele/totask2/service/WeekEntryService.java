package org.regele.totask2.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.regele.totask2.model.TaskInWeek;
import org.regele.totask2.model.User;
import org.regele.totask2.model.WorkEntry;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * packaging and handling workEntries for weekly based operations.
 * 
 * @author Manfred
 * @since 2014-08-09
 */
public class WeekEntryService {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryService.class);   
    
    /** work entry repository under test. */
    private WorkEntryRepository workEntryRepository;    

    /** .ctor */
    public WeekEntryService(WorkEntryRepository workEntryRepository) {
        this.workEntryRepository = workEntryRepository;
    }
    
    /** all work done on a given week. */
    public List<TaskInWeek> getWorkWeek(User user, LocalDate date) {

        LOG.debug("retrieving entries for user date " + date);
        
        Date dt = LocalDateConverter.toDate(date);
        List<WorkEntry> entries = workEntryRepository.findForUserAndDay(user.getId(), dt);
        
        // an entry for each task.
        List<TaskInWeek> tasksInWeek = 
                entries.stream()
                .map( we -> new TaskInWeek( we.getTask()) )
                .collect( Collectors.toList() );
        
        LOG.debug("given week tasks count: " + tasksInWeek.size());
        
        // suitable daily workEntries for task
        
        for( TaskInWeek tiw : tasksInWeek)
        {
            for(int dayOffset = 0; dayOffset<=4 ;dayOffset++)
            {
                final long offset = dayOffset;
                WorkEntry entryOfDay = entries.stream()
                        .filter( we -> we.getTask().getId() == tiw.getTask().getId() && we.getAtDate().equals(date.plusDays(offset)) )
                        .findFirst()
                        .orElse( new WorkEntry(user, tiw.getTask(), date.plusDays(offset)));   // new empty entry
                
                tiw.getDailyEntries()[dayOffset] = entryOfDay;
            }
        }
        
        return tasksInWeek;
    }

}
