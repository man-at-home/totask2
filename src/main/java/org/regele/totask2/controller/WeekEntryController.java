package org.regele.totask2.controller;


import org.regele.totask2.model.TaskInWeek;
import org.regele.totask2.model.User;
import org.regele.totask2.model.UserRepository;
import org.regele.totask2.model.WorkEntryRepository;
import org.regele.totask2.service.ReportGenerator;
import org.regele.totask2.service.ReportGenerator.ReportOutputFormat;
import org.regele.totask2.service.WeekEntryService;
import org.regele.totask2.util.LocalDateConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;


/** main entry page for uses.
 * - display exactly one week of work
 * - all tasks, one entry per task an day.
 * 
 * @author Manfred
 * @since  2014-08-09
 */
@Controller
public class WeekEntryController {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryController.class);

    /** work entry repository. */
    @Autowired private WorkEntryRepository workEntryRepository;
    
    @Autowired private UserRepository userRepository;
    
    
    @Autowired
    private ReportGenerator reportGenerator;
    
    private WeekEntryService getService() {
        return new WeekEntryService(this.workEntryRepository);
    }
    
    /** list all projects. */
    @RequestMapping("/weekEntry")
    public String weekEntry(final Model model) {
        LocalDate dt   =  LocalDate.now();
        return weekEntry(model, dt.toString());
    }
  
    /** list all projects. */
    @RequestMapping("/weekEntry/{dateString}")
    public String weekEntry(final Model model, @PathVariable final String dateString) {        
        LOG.debug("weekEntry( " + dateString + ")");
        
        LocalDate dt   =  LocalDate.parse(dateString);
        
        model.addAttribute("currentWeek",   dt.toString());
        model.addAttribute("previousWeek",  dt.minusWeeks(1).toString());
        model.addAttribute("nextWeek",      dt.plusWeeks(1).toString());
        model.addAttribute("date" ,         LocalDateConverter.toDate(dt));
        model.addAttribute("tasksInWeek",   getWeek(dt));        
        
        LOG.debug("serving weekEntry for " + dt);
        return "weekEntry";
    }   
    
    
    private List<TaskInWeek> getWeek(final LocalDate dt) {
       User      user = userRepository.getOne(2L /* TestConstants.TestUser */);   // TBF current User.        
       List<TaskInWeek> tasksInWeek = getService().getWorkWeek(user, dt);      
       return tasksInWeek;
    }
    
    
    /**  generate an pdf or excel report from project data. */
    @RequestMapping(value = "/weekEntry/report/{reportFormat}/{dateString}", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public ModelAndView getWeekEntryReport(@PathVariable final String reportFormat, @PathVariable final String dateString) {
        
        LOG.debug("getProjectsReport(" + reportFormat + ", " +  dateString + ")"); 
        LocalDate dt   =  LocalDate.parse(dateString);
        
        return reportGenerator
                .createReportModelView(
                        "weekEntryReport.jrxml" , 
                        reportFormat == null || reportFormat.contains("pdf") ? ReportOutputFormat.pdf : ReportOutputFormat.excel,
                        getWeek(dt)
                        );
    }
        
}//class
