package org.regele.totask2.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/** LocalDate to util.Date conversion. */
public final class LocalDateConverter {
    
    public static Date toDate(LocalDate date) {
        return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date at) {
        Instant instant = new Date(at.getTime()).toInstant(); 
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate ld = zdt.toLocalDate();
        return ld;
    }    
    
}
