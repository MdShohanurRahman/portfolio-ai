package com.shohan.portfolio_ai.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AppointmentRequest {
    private String summary;
    private String description;
    private String startDateTime; // ISO 8601 format e.g., 2025-07-18T14:00:00
    private Integer duration = 15; // Duration in minutes, default is 15 minutes
    List<String> attendeeEmails = new ArrayList<>();
}
