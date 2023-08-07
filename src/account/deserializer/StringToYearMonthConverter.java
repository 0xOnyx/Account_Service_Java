package account.deserializer;

import org.springframework.core.convert.converter.Converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class StringToYearMonthConverter implements Converter<String, YearMonth> {

    public YearMonth convert(String source) {
        return YearMonth.parse(source, DateTimeFormatter.ofPattern("MM-yyyy"));
    }

}
