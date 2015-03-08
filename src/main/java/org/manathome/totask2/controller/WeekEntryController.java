package org.manathome.totask2.controller;

import org.manathome.totask2.model.TaskInWeek;
import org.manathome.totask2.model.User;
import org.manathome.totask2.model.UserRepository;
import org.manathome.totask2.model.WorkEntryRepository;
import org.manathome.totask2.service.ReportGenerator;
import org.manathome.totask2.service.ReportGenerator.ReportOutputFormat;
import org.manathome.totask2.service.UserDetailsServiceImpl;
import org.manathome.totask2.service.WeekEntryService;
import org.manathome.totask2.util.AAssert;
import org.manathome.totask2.util.Authorisation;
import org.manathome.totask2.util.DurationConverter;
import org.manathome.totask2.util.InvalidClientArgumentsException;
import org.manathome.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;








import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;

/**
 * main entry page for uses.
 * <ul>
 * <li>display exactly one week of work</li>
 * <li>all {@link org.manathome.totask2.model.Task}s assigned to user, one entry
 * per task and day.</li>
 * </ul>
 * 
 * @see org.manathome.totask2.model.WorkEntry
 * @see org.manathome.totask2.model.Task
 * 
 * @author man-at-home
 * @since 2014-08-09
 */
@Transactional
@Controller
public class WeekEntryController {

    private static final Logger LOG = LoggerFactory.getLogger(WeekEntryController.class);
    
    /** work entry repository. */
    @Autowired
    private WorkEntryRepository workEntryRepository;

    /** user. */
    @Autowired
    private UserRepository userRepository;

    /** reporting code for excel export. */
    @Autowired
    private ReportGenerator reportGenerator;

    @Autowired
    private WeekEntryService weekEntryService;
    

    private final Histogram      tasksPerWeekHistogramm;
    private final Timer          weekEntryGetResponseTimer;
    
    @Autowired
    public WeekEntryController(final MetricRegistry metricRegistry) {
            this.tasksPerWeekHistogramm    = metricRegistry.histogram("TOTASK2XX.controller.weekEntry.TaskInWeek.hist");
            this.weekEntryGetResponseTimer = metricRegistry.timer("TOTASK2XX.controller.weekEntry.request.get.timed");
    }
    

    private WeekEntryService getService() {
        return weekEntryService;
    }

    private List<TaskInWeek> getWeek(final LocalDate dt) {

        User user = userRepository
                .findByUserName(UserDetailsServiceImpl.getCurrentUserName())
                .stream().findFirst().get(); // (2L /* TestConstants.TestUser
                                             // */); .
        List<TaskInWeek> tasksInWeek = getService().getWorkWeek(user, dt);
        return tasksInWeek;
    }

    /** add view data to model for a given work week. */
    private void buildWeekModel(final Model model, final LocalDate dt) {

        model.addAttribute("currentWeek", dt.toString());
        model.addAttribute("previousWeek", dt.minusWeeks(1).toString());
        model.addAttribute("nextWeek", dt.plusWeeks(1).toString());
        model.addAttribute("date", LocalDateConverter.toDate(dt));
        
        try {
            List<TaskInWeek> tiw = getWeek(dt);
            model.addAttribute("tasksInWeek", tiw);
            
            if (tasksPerWeekHistogramm != null) {
                tasksPerWeekHistogramm.update(tiw.size());
            }
        } catch (Exception ex) {
            LOG.error("error getting week data", ex);
            model.addAttribute("flashMessage", ex.getMessage());
        }

        LOG.debug("model has weekEntry with for " + dt);
    }

    /**
     * GET: provide {@link org.manathome.totask2.model.WorkEntry} data of current
     * week and {@link org.manathome.totask2.model.User}.
     * 
     * @see org.manathome.totask2.model.WorkEntry
     */
    @Secured(Authorisation.ROLE_USER)
    @RequestMapping(value = "/weekEntry", method = RequestMethod.GET)
    public String weekEntry(final Model model) {
        LOG.debug("weekEntry(default)");
        LocalDate dt = LocalDate.now();
        return weekEntry(model, dt.toString());
    }

    /**
     * GET: provide {@link org.manathome.totask2.model.WorkEntry} data of week
     * "dateString" and {@link User}.
     * 
     * @param dateString
     *            dateString of week to display.
     * @see org.manathome.totask2.model.WorkEntry
     */
    @Secured(Authorisation.ROLE_USER)
    @RequestMapping(value = "/weekEntry/{dateString}", method = RequestMethod.GET)
    public String weekEntry(final Model model,
            @PathVariable final String dateString) {
        LOG.debug("weekEntry( " + dateString + ")");

        try (Timer.Context context = (weekEntryGetResponseTimer == null) ? null : weekEntryGetResponseTimer.time()) {
            buildWeekModel(model, LocalDate.parse(dateString));
            return "weekEntry";
        } 
    }

    /**
     * POST: save all {@link org.manathome.totask2.model.WorkEntry} data for given
     * week and {@link org.manathome.totask2.model.User}.
     * 
     * @see org.manathome.totask2.model.WorkEntry
     * @exception InvalidClientArgumentsException
     * */
    @Secured(Authorisation.ROLE_USER)
    @RequestMapping(value = "/weekEntry/{dateString}", method = RequestMethod.POST)
    public String saveWeekEntry(final Model model,
            @PathVariable final String dateString,
            final RedirectAttributes redirectAttributes,
            final WebRequest request) {
        LOG.debug("saveWeekEntry( " + dateString + ") -> db");

        AAssert.checkNotNullOrEmpty(dateString, "no date given");
        LocalDate dt = LocalDate.parse(dateString);

        List<TaskInWeek> tasksInWeek = getWeek(dt);

        // each task row
        for (TaskInWeek taskRow : tasksInWeek) {
            for (int i = 0; i <= 6; i++) {
                // each of 7 days for current task)
                String newDurationString = request.getParameter("workEntry_"
                        + taskRow.getTask().getId() + "_" + i);

                try {
                    float newDuration = DurationConverter.parse(
                            newDurationString).floatValue();
                    if (newDuration != taskRow.getDailyEntries()[i]
                            .getDuration()) {
                        LOG.debug(taskRow.getTask().getName() + "[" + i + "]: "
                                + newDurationString + "h == " + newDuration
                                + ", old value was:"
                                + taskRow.getDailyEntries()[i]);
                        taskRow.getDailyEntries()[i]
                                .setDuration(DurationConverter.parse(
                                        newDurationString).floatValue());
                    }
                } catch (ParseException pex) {
                    // rely on client side validation for "pretty" user
                    // feedback..
                    throw new InvalidClientArgumentsException(
                            "invalid duration " + newDurationString
                                    + " for task "
                                    + taskRow.getTask().getName(), pex);
                }
            }
        }

        String changedTaskNames = tasksInWeek.stream()
                .filter(t -> t.isModifiedByUser())
                .map(tiw -> tiw.getTask().getName())
                .collect(Collectors.joining(", "));

        LOG.debug("updating: " + changedTaskNames);

        getService().saveWeek(tasksInWeek);

        String updateMessage = changedTaskNames != null
                && changedTaskNames.length() > 1 ? "changed values for: "
                + changedTaskNames : "no change.";
        redirectAttributes.addFlashAttribute("flashMessage", updateMessage);

        return "redirect:/weekEntry";
    }

    /**
     * generates an excel report containing given weeks work data.
     * 
     * @see ReportGenerator
     */
    @Secured(Authorisation.ROLE_USER)
    @RequestMapping(value = "/weekEntry/report/{reportFormat}/{dateString}", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public ModelAndView getWeekEntryReport(
            @PathVariable final String reportFormat,
            @PathVariable final String dateString) {

        LOG.debug("getProjectsReport(" + reportFormat + ", " + dateString + ")");
        LocalDate dt = LocalDate.parse(dateString);

        return reportGenerator
                .createReportModelView(
                        "weekEntryReport.jrxml",
                        reportFormat == null || reportFormat.contains("pdf") ? ReportOutputFormat.pdf
                                : ReportOutputFormat.excel, getWeek(dt));
    }

}// class
