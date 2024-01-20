package com.example.Slearning.Backend.Java.controllers;

import com.example.Slearning.Backend.Java.domain.entities.CalendarEvent;
import com.example.Slearning.Backend.Java.services.CalendarService;
import com.example.Slearning.Backend.Java.utils.enums.RecurrenceEvent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final Calendar calendar;

    private final CalendarService calendarService;

    @PostMapping("/event/create")
    public ResponseEntity<CalendarEvent> createCalendarEvent(
            @RequestBody CalendarEvent calendarEvent,
            @RequestParam("userId") UUID userId,
            @RequestParam("am") boolean isAM
    ) throws IOException {
        CalendarEvent savedEvent = calendarService.createEvent(calendarEvent, userId, isAM);
        return new ResponseEntity<>(calendarEvent, HttpStatus.CREATED);
    }

    @GetMapping
    public void getCalendar() throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = calendar.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                for(EventReminder reminder : event.getReminders().getOverrides()){
                    System.err.printf("%s (%d)", reminder.getMethod(), reminder.getMinutes());
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }
}
