package kartbllansh.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeNow {
    public static String dateTimeNow(LocalDateTime localDateTime){

        // Создаем объект DateTimeFormatter с желаемым форматом
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");

        // Форматируем LocalDateTime с помощью DateTimeFormatter
        return localDateTime.format(formatter);
    }
}
