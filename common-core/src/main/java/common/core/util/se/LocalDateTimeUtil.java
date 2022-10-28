package common.core.util.se;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.experimental.UtilityClass;

/**
 * @author asd <br>
 * @create 2021-07-27 10:30 AM <br>
 * @project common-core <br>
 */
@UtilityClass
public class LocalDateTimeUtil {

    public Date localDateTime2Date(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public LocalDateTime date2LocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
