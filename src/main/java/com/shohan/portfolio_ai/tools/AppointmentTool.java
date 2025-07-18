package com.shohan.portfolio_ai.tools;

import com.google.api.services.calendar.model.Event;
import com.shohan.portfolio_ai.service.CalenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@Slf4j
public class AppointmentTool {

    private final ChatClient chatClient;
    private final CalenderService calenderService;

    public AppointmentTool(ChatClient chatClient, CalenderService calenderService) {
        this.chatClient = chatClient;
        this.calenderService = calenderService;
    }

    @Tool(description = """
            Schedule Meeting/Book Appointment.
            required inputs:
             - Meeting summary ,
             - Preferred date/time,
             - Email address
            """)
    public String bookAppointment(
            @ToolParam(description = "Summary of the appointment (e.g., 'HR Meeting')") String summary,
            @ToolParam(description = "Preferred date/time. Accept natural language date)") String dateTime,
            @ToolParam(description = "Email address for invitation (e.g., 'john@gmail.com')") String email
    ) {
        log.info("Received request to book appointment: Summary={}, NaturalDateTime={}, Email={}", summary, dateTime, email);
        // Validate required inputs
        if (dateTime == null || dateTime.trim().isEmpty()) {
            return "Date and time are required.";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email address is required for sending the invitation.";
        }
        if (!isValidEmail(email)) {
            return "Invalid email address format. Please provide a valid email.";
        }

        String startTime = parseNaturalLanguageDate(dateTime);
        if (!isValidDateFormat(startTime)) {
            return "Couldn't understand the date/time. Please try formats like 'tomorrow 9 pm' or '10 July 8 am'";
        }
        // Set default duration
        int duration = 30;
        String endTime = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                .plusMinutes(duration)
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
            log.info("Booking appointment: Summary={}, StartTime={}, EndTime={}, Attendees={}, Duration={}", summary, startTime, endTime, email, duration);
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
                    duration,
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
    public static boolean isValidDateFormat(String dateString) {
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
     * Converts a natural language date string to an ISO 8601 formatted LocalDateTime string
     * using an AI client.
     *
     * @param naturalDate The natural language date string (e.g., "tomorrow at 3 PM", "next Monday").
     * @return The ISO 8601 formatted date-time string (YYYY-MM-DDTHH:MM:SS), or null if parsing fails.
     */
    public String parseNaturalLanguageDate(String naturalDate) {
        if (naturalDate == null || naturalDate.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDateTime.parse(naturalDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return naturalDate;
        } catch (DateTimeParseException e) {
            System.out.println("Attempting AI parsing for natural date: " + naturalDate);
        }
        try {
            return chatClient.prompt()
                    .user(userSpec -> userSpec.text("""
                                    Convert this natural language date to ISO 8601 format (YYYY-MM-DDTHH:MM:SS).
                                    If the time is not specified, assume 09:00:00.
                                    If the date is not specified, assume today.
                                    Example: "tomorrow" -> "{tomorrow_date}T09:00:00"
                                    Example: "next monday at 5pm" -> "{next_monday_date}T17:00:00"
                                    Example: "July 20, 2025 10am" -> "2025-07-20T10:00:00"
                                    
                                    Current date: {currentDate}
                                    Current time: {currentTime}
                                    Input: {naturalDate}
                                    Respond ONLY with the ISO 8601 format (YYYY-MM-DDTHH:MM:SS), nothing else.
                                    """
                            )
                            .param("naturalDate", naturalDate)
                            .param("currentDate", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE))
                            .param("currentTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                            .param("tomorrow_date", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))
                            .param("next_monday_date", LocalDate.now().plusWeeks(1).with(DayOfWeek.MONDAY).format(DateTimeFormatter.ISO_LOCAL_DATE)))
                    .call()
                    .entity(String.class);
        } catch (DateTimeParseException e) {
            log.error("AI response was not a valid ISO 8601 date: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Error during AI parsing of natural date '{}': {}", naturalDate, e.getMessage());
            return null;
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