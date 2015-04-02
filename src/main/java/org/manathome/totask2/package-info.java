/** 
 * totask2 application.
 * 
 * <h2>Component Diagram</h2>   
 * 
 * <img alt="package-uml" src="doc-files/totask2.design.modules.png">
 * 
 * <br >
 *
 * 
 * <h2>Use Cases for this application</h2>
 * <img alt="package-uml" src="doc-files/totask2.design.usecases.png">
 * 
 * @author man-at-home
 * @since  2014
 *
 */

package org.manathome.totask2;
/*
 * 
@startuml doc-files/totask2.design.modules.png


package "Controller" {
  HTTP - [ProjectController]
  HTTP - [TaskController]
  HTTP - [WeekEntryController]
  HTTP - [Spring-Security]
  
  REST - [UserController]
  REST - [WorkEntryController]
}

package "Model" {
  [Project]
  [Task]
  [TaskAssignment]
  [User]
  [WorkEntry]
}

database "H2" {
  frame "database schema" {
    [TT_Project]
    [TT_Task]
    [TT_WorkEntry]
  }
}


[ProjectController]   --> [Project]
[Project]             --> [TT_Project]
[WeekEntryController] --> [User] 
[WeekEntryController] --> [TaskAssignment] 
[WeekEntryController] --> [WorkEntry] 
[Spring-Security]     --> [User]

@enduml

@startuml  doc-files/totask2.design.usecases.png

User <|-- Admin

    User  -> (Login)
    User  -> (Log Weekly Entry)

    Admin -> (Administer Projects)
    Admin -> (Administer Tasks)
    Admin -> (Assign User to Tasks)
    Admin -> (Login)    

@enduml
 */
