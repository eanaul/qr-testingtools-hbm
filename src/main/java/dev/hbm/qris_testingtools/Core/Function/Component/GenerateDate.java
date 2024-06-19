package dev.hbm.qris_testingtools.Core.Function.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component("GenerateDate")
public class GenerateDate implements FunctionProcessor {
    @Override
    public Object run(ObjectNode args) {
        Calendar calendar = Calendar.getInstance();

        String format = args.get("format").asText("");
        String timeZone = args.get("timeZone").asText("");
        String additionalTime = args.get("additionalTime").asText("");

        calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(additionalTime));

        Date date = calendar.getTime();

        SimpleDateFormat outputFormat = new SimpleDateFormat(format);
        outputFormat.setTimeZone(TimeZone.getTimeZone(timeZone));

        return outputFormat.format(date);
    }
}
