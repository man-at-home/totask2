
/** 
 * totask2 data model.
 * 
 * <img alt="package-uml" src="doc-files/totask2.design.datamodel.png">
 * 
 * @see <a href="http://hibernate.org">http://hibernate.org</a>
 * 
 * @author man-at-home
 * @since  2014-11-07 
 *
 */
// tag::developer-manual-plantuml[]
/*
 * 
@startuml doc-files/totask2.design.datamodel.png

 Project         "1" *-- "n" Task : contains
 Project         "n"  -- "n" User            : leads 
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
//end::developer-manual-plantuml[]