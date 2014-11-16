package org.regele.totask2.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.regele.totask2.controller.ProjectPlanData;
import org.regele.totask2.model.Project;
import org.regele.totask2.model.Task;
import org.regele.totask2.model.TaskAssignmentRepository;
import org.regele.totask2.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** loading and converting project data. */
@Service
public class ProjectPlanDataService {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectPlanDataService.class);    
    
    @Autowired private TaskRepository  taskRepository;   
    @Autowired private TaskAssignmentRepository taskAssignmentRepository;
    
    public ProjectPlanDataService() {
    }

    
    
    /** create plan data from project model. */
    public List<ProjectPlanData> createProjectPlanData(@NotNull final Project project)
    {
        LOG.debug("retrieving project plan data: " + project);
        
        List<Task> tasks = taskRepository.findByProjectId(project.getId());                  
         
        List<ProjectPlanData> list = tasks
                .stream()
                .map(t -> new ProjectPlanData(t))
                .collect(Collectors.toList());
        
        list.forEach( ppd -> ppd.SetSeries(taskAssignmentRepository));
        return list;
    }
}
