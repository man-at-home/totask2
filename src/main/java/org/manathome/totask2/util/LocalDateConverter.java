package org.manathome.totask2.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;



import javax.validation.constraints.NotNull;

/** LocalDate to util.Date conversion. 
 * 
 *  @see LocalDate
 *  @see Date
 *  
 *  @author man-at-home
 */
public abstract class LocalDateConverter {
    
    /** german/austrian/swiss formatting dd.MM.yyyy. */
    private static final DateTimeFormatter DE_DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");
    private static final DateTimeFormatter ISO_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    
    /** either copy of dateToBeCloned or null.
     * @param dateToBeCloned
     * @return new instance of given date or null.  
     */
    public static Date cloneSave(Date dateToBeCloned) {
        return dateToBeCloned == null ? null : new Date(dateToBeCloned.getTime());
    }
    
    /** local date (java8) to util.Date conversion. */
    public static Date toDate(@NotNull final LocalDate date) {
        return Date.from(AAssert.checkNotNull(date)
                   .atStartOfDay()
                   .atZone(ZoneId.systemDefault())
                   .toInstant());
    }

    /** util.Date to LocalDate (java8) conversion. */
    public static LocalDate toLocalDate(@NotNull final Date at) {
        Instant instant = new Date(AAssert.checkNotNull(at).getTime()).toInstant(); 
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
    public static Date getDate(final int days) {
        
        return toDate(LocalDate.now().plusDays(days));        
    }    
    
    /** format DE_de. */
    public static String format(final LocalDate ld) {
        return ld == null ? "" :  ld.format(DE_DATE_FORMATTER);
    }

    /** 
     * parse DE_de.
     * 
     * @param localDateString local date, format shall be: dd.mm.yyyy 
     * @return LocalDate or null (if string was null or empty)
     */
    public static LocalDate parse(String localDateString) {
        
        if (localDateString == null || localDateString.length() == 0) {
            return null;
        } else {
            return LocalDate.parse(localDateString, DE_DATE_FORMATTER);
        }
    }

    /** format to yyyy.Mm.dd. */
    public static String isoFormat(LocalDate ld) {
        if (ld == null) {
            return null;
        } else {
            return ld.format(ISO_DATE_FORMATTER);
        }
    }
    
    /** format to yyyy.Mm.dd. */
    public static LocalDate parseIso(String isoDateString) {
        if (isoDateString == null || isoDateString.length() == 0) {
            return null;
        } else {
            return LocalDate.parse(isoDateString, ISO_DATE_FORMATTER);
        }
    }
}
