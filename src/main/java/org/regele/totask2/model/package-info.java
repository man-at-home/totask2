
/** 
 * totask2 data model.
 * 
 * <img alt="package-uml" src="doc-files/totask2.design.datamodel.png">
 * 
 * @see <a href="http://hibernate.org">http://hibernate.org</a>
 * 
 * @author Manfred
 * @since  2014-11-07 
 *
 * @category diagram
 */
/*
 * 
@startuml doc-files/totask2.design.datamodel.png

 Project         "1" *-- "n" Task : contains
 Task            "1" *-- "n" TaskAssignment 
 TaskAssignment  "1" -- "n"  User : assigned to
 WorkEntry       "n" -- "1"  User 
 WorkEntry       "n" -- "1"  Task : worked on

 Project   : name
 Task      : name
 
 User      : username
 User      : displayName
 User      : password
 User      : isAdmin : boolean
 
 WorkEntry  : at       : Date
 WorkEntry  : duration : Hours 
 
 TaskAssignment : from  : Date
 TaskAssignment : until : Date
 
 
 @enduml
 */
package org.regele.totask2.model;