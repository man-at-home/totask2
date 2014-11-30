/** 
 * totask2 controller (springMVC).
 * 
 * springMVC spring controllers.
 * 
 * <img alt="package-uml" src="doc-files/totask2.design.controllers.png">
 * 
 * @see <a href="http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html">http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html</a>
 * 
 * @author man-at-home
 * @since  2014-11-07 
 *
 * @category diagram
 */
/*
 * 
@startuml doc-files/totask2.design.controllers.png

title short communication overview

actor     User

box "web" #LightBlue
  boundary  Controller
  boundary  ViewTemplate
end box
control   ModelRepository
entity    Model 
database  Table

User            -> Controller      : Browser GET/POST
activate Controller
loop 1..n times
    Controller      -> ModelRepository : retrieve models
    activate ModelRepository 
    ModelRepository -> Model           : load
    activate Model 
    Model           -> Table           : read db
    Table           --> Model          : result of db query 
    Model           --> ModelRepository
    deactivate Model 
    ModelRepository --> Controller     : found models
end
deactivate ModelRepository
Controller      -> ViewTemplate    : render result
activate ViewTemplate
deactivate Controller
ViewTemplate    -> User            : show as html    
deactivate ViewTemplate

@enduml
 */

package org.regele.totask2.controller;