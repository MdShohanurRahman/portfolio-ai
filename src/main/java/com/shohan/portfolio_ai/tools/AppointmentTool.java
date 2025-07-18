package com.shohan.portfolio_ai.tools;

import com.google.api.services.calendar.model.Event;
import com.shohan.portfolio_ai.service.CalenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@Slf4j
public class AppointmentTool {

    private static final int DEFAULT_DURATION_MINUTES = 30;
    private final CalenderService calenderService;

    public AppointmentTool(CalenderService calenderService) {
        this.calenderService = calenderService;
    }

    @Tool(description = """
            Schedule Meeting/Book Appointment.
            required inputs:
             - Summary of the appointment (e.g., 'HR Meeting'),
             - Preferred date/time(e.g., 'tomorrow at 3 PM', 'next Monday at 5 PM', 'July 20, 2025 10 AM'),
             - Email address for invitation (e.g., 'john@gmail.com')
            """
    )
    public String bookAppointment(
            @ToolParam(description = "Summary of the appointment") String summary,
            @ToolParam(description = "Date/time in ISO 8601 format (YYYY-MM-DDTHH:MM:SS)") String startTime,
            @ToolParam(description = "Email address") String email
    ) {
        log.info("Received request to book appointment: Summary={}, NaturalDateTime={}, Email={}", summary, startTime, email);
        // Validate required inputs
        if (startTime == null || startTime.trim().isEmpty()) {
            return "Date and time are required.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email address is required for sending the invitation.";
        }
        if (!isValidEmail(email)) {
            return "Invalid email address format. Please provide a valid email.";
        }

        if (!isValidDateFormat(startTime)) {
            return "Couldn't understand the date/time. Please try formats like 'tomorrow 9 pm' or '10 July 8 am'";
        }

        if (LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME).isBefore(LocalDateTime.now())) {
            return "Datetime cannot be in the past. Please provide a future date/time.";
        }

        String endTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .plusMinutes(DEFAULT_DURATION_MINUTES)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try {
            // Parse attendees if provided
            List<String> attendeeEmails = List.of(email);

            // Check time slot availability
            boolean available = calenderService.isTimeSlotAvailable(startTime, endTime);
            if (!available) {
                return String.format("Error: The requested time slot (%s to %s) is not available.", startTime, endTime);
            }

            String description = "Scheduled via Portfolio Assistant for discussion on " + summary;
            log.info("Booking appointment: Summary={}, StartTime={}, EndTime={}, Attendees={}, Duration={}", summary, startTime, endTime, email, DEFAULT_DURATION_MINUTES);
            Event createdEvent = calenderService.createCalendarEvent(summary, description, startTime, endTime, attendeeEmails);
            log.info("Appointment booked successfully: Event ID={}, Link={}", createdEvent.getId(), createdEvent.getHtmlLink());

            // Return human-readable response
            return String.format("""
                            Appointment booked successfully!
                            Summary: %s
                            Time: %s to %s
                            Duration: %d minutes
                            Invitation Link: %s
                            """,
                    createdEvent.getSummary(),
                    startTime,
                    endTime,
                    DEFAULT_DURATION_MINUTES,
                    createdEvent.getHtmlLink());
        } catch (Exception e) {
            log.error("Error booking appointment", e);
            return "Error: Unable to book the appointment. Please try again with a different time.";
        }
    }

    /**
     * Validates if the input string matches the date format yyyy-MM-dd'T'HH:mm:ss (e.g., 2025-07-18T14:00:00).
     *
     * @param dateString The date string to validate.
     * @return true if the string is in the correct format, false otherwise.
     */
    public boolean isValidDateFormat(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            // Use strict parsing to ensure exact match
            formatter.withResolverStyle(java.time.format.ResolverStyle.STRICT);
            LocalDateTime.parse(dateString, formatter);
            // Ensure the string exactly matches the expected length (19 characters for yyyy-MM-dd'T'HH:mm:ss)
            return dateString.length() == 19;
        } catch (DateTimeParseException e) {
            return false;
        }
    }


    /**
     * Validates if the provided email address is in a valid format.
     *
     * @param email The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Simple regex for basic email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}