package com.shohan.portfolio_ai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateTimeTool {

    @Value("${app.calender.timezone}")
    private String timeZone;

    @Tool(description = "Get the current date and time in the user's timezone")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(ZoneId.of(timeZone)).toString();
    }

    @Tool(description = "Get tomorrow's date in the user's timezone")
    public String getTomorrowsDate() {
        return LocalDate.now().plusDays(1).toString();
    }

}
