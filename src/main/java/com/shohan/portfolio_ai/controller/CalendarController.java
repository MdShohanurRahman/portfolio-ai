package com.shohan.portfolio_ai.controller;

import com.google.api.services.calendar.model.Event;
import com.shohan.portfolio_ai.service.CalenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalenderService calendarService;

    public CalendarController(CalenderService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/check-availability")
    public ResponseEntity<Map<String, Boolean>> checkAvailability(@RequestBody Map<String, String> request) {
        String startTime = request.get("startTime");
        String endTime = request.get("endTime");

        if (startTime == null || endTime == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start time and end time are required.");
        }

        try {
            boolean available = calendarService.isTimeSlotAvailable(startTime, endTime);
            return ResponseEntity.ok(Map.of("available", available));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking availability: " + e.getMessage());
        }
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody Map<String, String> request) {
        String summary = request.get("summary");
        String description = request.get("description");
        String startTime = request.get("startTime");
        String endTime = request.get("endTime");

        if (summary == null || startTime == null || endTime == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Summary, start time, and end time are required."));
        }

        try {
            // First, check if the time slot is available
            boolean available = calendarService.isTimeSlotAvailable(startTime, endTime);

            if (available) {
                // If available, proceed to create the event
                Event createdEvent = calendarService.createCalendarEvent(summary, description, startTime, endTime);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "Appointment booked successfully!",
                        "eventId", createdEvent.getId(),
                        "eventLink", createdEvent.getHtmlLink()
                ));
            } else {
                // If not available, return a conflict response
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("success", false, "message", "The requested time slot is not available."));
            }
        } catch (IOException e) {
            // Log the exception for debugging
            System.err.println("Error booking appointment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Error booking appointment: " + e.getMessage()));
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "An unexpected error occurred."));
        }
    }

}