package org.manathome.totask2.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/** LocalDate to util.Date conversion. 
 * 
 *  @see LocalDate
 *  @see Date
 *  
 *  @author man-at-home
 */
public abstract class LocalDateConverter {
    
    /** either copy of dateToBeCloned or null.
     * @param dateToBeCloned
     * @return new instance of given date or null.  
     */
    public static Date cloneSave(Date dateToBeCloned) {
        return dateToBeCloned == null ? null : new Date(dateToBeCloned.getTime());
    }
    
    /** localdate (java8) to util.Date conversion. */
    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /** util.Date to LocalDate (java8) conversion. */
    public static LocalDate toLocalDate(Date at) {
        Instant instant = new Date(at.getTime()).toInstant(); 
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate ld = zdt.toLocalDate();
        return ld;
    }    
    
    /** 
     * plusDays for old Date.
     * 
     * @see LocalDate#plusDays
     * @see LocalDate#now()
     * 
     * @param days to add to current Date
     * @return now + days
     */
    public static Date getDate(int days) {
        
        return toDate(LocalDate.now().plusDays(days));        
    }    
    
}
