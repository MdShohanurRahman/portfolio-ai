package com.shohan.portfolio_ai.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalenderService {

    private final Calendar calendar;
    private final String calenderId;
    private final String timeZone;
    private final boolean enableServiceAccount;

    public CalenderService(
            Calendar calendar,
            @Value("${app.calender.email}") String calenderId,
            @Value("${app.calender.timezone}") String timeZone,
            @Value("${app.calender.enableServiceAccount}") boolean enableServiceAccount
    ) {
        this.calendar = calendar;
        this.calenderId = calenderId;
        this.timeZone = timeZone;
        this.enableServiceAccount = enableServiceAccount;
    }

    /**
     * Check if a specific time slot is available in the Google Calendar.
     *
     * @param startTime The start date and time in ISO 8601 format (e.g., "2025-07-18T14:00:00").
     * @param endTime   The end date and time in ISO 8601 format (e.g., "2025-07-18T15:00:00").
     * @return true if the time slot is available, false otherwise.
     * @throws IOException if there is an error communicating with the Google Calendar API.
     */
    public boolean isTimeSlotAvailable(String startTime, String endTime) throws IOException {
        // Parse the input strings into LocalDateTime objects
        LocalDateTime startLdt = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endLdt = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Convert LocalDateTime to ZonedDateTime with a specific time zone (e.g., "Asia/Kuala_Lumpur")
        ZoneId zoneId = ZoneId.of(timeZone);
        ZonedDateTime startZdt = startLdt.atZone(zoneId);
        ZonedDateTime endZdt = endLdt.atZone(zoneId);

        // Convert ZonedDateTime to Google's DateTime format
        DateTime timeMin = new DateTime(startZdt.toInstant().toEpochMilli());
        DateTime timeMax = new DateTime(endZdt.toInstant().toEpochMilli());

        // Query events within the specified time range
        Events events = calendar.events().list(calenderId)
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setSingleEvents(true) // Expand recurring events into individual instances
                .setOrderBy("startTime")
                .execute();

        List<Event> items = events.getItems();

        // If no events are found, the time slot is available
        return items == null || items.isEmpty();
    }

    /**
     * Create a calendar event in Google Calendar.
     *
     * @param summary      The summary of the event.
     * @param description  The description of the event.
     * @param startTime    The start date and time in ISO 8601 format (e.g., "2025-07-18T14:00:00").
     * @param endTime      The end date and time in ISO 8601 format (e.g., "2025-07-18T15:00:00").
     * @param attendees    A list of email addresses of attendees.
     * @return The created Event object.
     * @throws IOException if there is an error communicating with the Google Calendar API.
     */
    public Event createCalendarEvent(String summary, String description, String startTime, String endTime, List<String> attendees) throws IOException {
        // Parse the input strings into LocalDateTime objects
        LocalDateTime startLdt = LocalDateTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime endLdt = LocalDateTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Convert LocalDateTime to ZonedDateTime with a specific time zone
        ZoneId zoneId = ZoneId.of(timeZone); // Adjust to your desired timezone
        ZonedDateTime startZdt = startLdt.atZone(zoneId);
        ZonedDateTime endZdt = endLdt.atZone(zoneId);

        // Create EventDateTime objects for start and end times
        EventDateTime eventStartTime = new EventDateTime()
                .setDateTime(new DateTime(startZdt.toInstant().toEpochMilli()))
                .setTimeZone(zoneId.getId());
        EventDateTime eventEndTime = new EventDateTime()
                .setDateTime(new DateTime(endZdt.toInstant().toEpochMilli()))
                .setTimeZone(zoneId.getId());

        // Create the Google Calendar Event object
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description)
                .setStart(eventStartTime)
                .setEnd(eventEndTime);


        // Add attendees if provided
        if (!enableServiceAccount && (attendees != null && !attendees.isEmpty())) {
            event.setAttendees(attendees.stream()
                    .map(email -> new EventAttendee().setEmail(email))
                    .toList());
            event.setGuestsCanInviteOthers(true);
        }

        // Insert the event into the specified calendar
        return calendar
                .events().insert(calenderId, event)
                .setSendNotifications(true)
                .execute();
    }

    /**
     * Create a calendar event in Google Calendar with no attendees.
     *
     * @param summary     The summary of the event.
     * @param description The description of the event.
     * @param startTime   The start date and time in ISO 8601 format (e.g., "2025-07-18T14:00:00").
     * @param endTime     The end date and time in ISO 8601 format (e.g., "2025-07-18T15:00:00").
     * @return The created Event object.
     * @throws IOException if there is an error communicating with the Google Calendar API.
     */
    public Event createCalendarEvent(String summary, String description, String startTime, String endTime) throws IOException {
        return createCalendarEvent(summary, description, startTime, endTime, new ArrayList<>());
    }
}
