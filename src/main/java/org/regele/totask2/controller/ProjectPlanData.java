package org.regele.totask2.controller;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.regele.totask2.model.Task;
import org.regele.totask2.model.TaskAssignment;
import org.regele.totask2.model.TaskAssignmentRepository;
import org.regele.totask2.util.LocalDateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/** JSON Wrapper of {@link Task} for ganttView. */
@ApiModel(value="ProjectPlanData", description="project task data for ganttView")
@Component
public class ProjectPlanData {
       

    private static final Logger LOG = LoggerFactory.getLogger(ProjectPlanData.class);    
    
    @Autowired TaskAssignmentRepository taskAssignmentRepository;
    
    /** JSON Wrapper of {@link Task} for ganttView. */
    @ApiModel(value="ProjectPlanItem", description="project task data for ganttView (one line of data")
    public class PlanDataItem
    {
        private String name;
        private Date start;
        private Date end;
        
        PlanDataItem(final TaskAssignment ta)
        {
           this.name = ta.getUser().getDisplayName();
           this.start = ta.getStartingFrom();
           this.end   = ta.getValidUntil();
        }
        
        PlanDataItem(final String name, Date start, Date end) {
            this.name = name;
            this.start = start;
            this.end = end;
        }
        
        @ApiModelProperty(value="task part name",  required=true, position=1)
        public String getName() { return this.name;  }       
        
        @ApiModelProperty(value="start date", position=2)
        @DateTimeFormat(iso=ISO.DATE)
        @JsonSerialize(using=JsonDateSerializer.class)
        public Date getStart()  { return this.start; }
        
        @ApiModelProperty(value="end date", position=3)
        @DateTimeFormat(iso=ISO.DATE)
        @JsonSerialize(using=JsonDateSerializer.class)
        public Date getEnd()    { return this.end; }
        public void setEnd(Date end) { this.end = end; }
        
        @Override public String toString() { return "[" + getName() + "]"; }
    }    

    private Task task;
    private List<PlanDataItem> dataItems;
    
    public ProjectPlanData() {}
    
        
    public ProjectPlanData(@NotNull final Task task) {
        this();
        if( task == null)
            throw new NullPointerException("ProjectPlanData may only constructed from task, null not allowed.");
        LOG.debug("creating plandata for " + task.getName());
        this.task = task;
    }
    
    @ApiModelProperty(value="unique task id", required=true, position=0)
    public long getId() { return this.task.getId(); }

    @ApiModelProperty(value="task name", required=true, position=1)
    public String getName() { return this.task.getName(); }
    
    @ApiModelProperty(value="subtask timeline data", position=2)
    public List<PlanDataItem> getSeries()
    { 
        return this.dataItems;
    }
    
    public void SetSeries(TaskAssignmentRepository taskAssignmentRepository)
    {
        if( taskAssignmentRepository == null)
            throw new NullPointerException("SetSeries(taskAssignmentRepository) param is null");
        
        List<PlanDataItem> planData = taskAssignmentRepository
                .findByTask(task)
                .stream()
                .map( a -> new PlanDataItem(a) )
                .collect(Collectors.toList());

        if( planData.size() > 0) {
            Date min = planData.stream().map( pd -> pd.getStart() ).min(Comparator.comparing(d -> d.getTime())).orElse( new Date());
            Date max = planData.stream().filter(pd -> pd.getEnd() != null).map( pd -> pd.getEnd()).max(Comparator.comparing(d -> d.getTime())).orElse( LocalDateConverter.getDate(100));

            planData.add(0, new PlanDataItem("complete task", min, max));
            
            planData.stream().filter( pd -> pd.getEnd() == null).forEach(pd -> pd.setEnd(max));
        }
        
        this.dataItems = planData;        
        LOG.debug("created task " + this.getName() + " data series with: " + dataItems.size() + " rows.");
    }

    
    @Override
    public String toString() {
        return "[" + getId() +  ", " + getName() + "]";
    }    

}
