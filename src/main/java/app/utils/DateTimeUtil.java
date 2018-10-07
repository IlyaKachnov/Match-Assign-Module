package app.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateTimeUtil {

    public static LocalDateTime convertDatesToLocalDateTime(Date date, Date time) {
        LocalTime localTime = Instant.ofEpochMilli(time.getTime())
                .atZone(ZoneId.systemDefault()).toLocalTime();
        LocalDate localDate = Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return localDateTime;
    }
}
