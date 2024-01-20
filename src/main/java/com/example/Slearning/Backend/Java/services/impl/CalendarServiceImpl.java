package com.example.Slearning.Backend.Java.services.impl;

import com.example.Slearning.Backend.Java.domain.entities.CalendarEvent;
import com.example.Slearning.Backend.Java.domain.entities.User;
import com.example.Slearning.Backend.Java.exceptions.ResourceNotFoundException;
import com.example.Slearning.Backend.Java.repositories.CalendarRepository;
import com.example.Slearning.Backend.Java.repositories.UserRepository;
import com.example.Slearning.Backend.Java.services.CalendarService;
import com.example.Slearning.Backend.Java.utils.AppUtils;
import com.example.Slearning.Backend.Java.utils.enums.RecurrenceEvent;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.time.*;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final Calendar calendar;

    private final CalendarRepository calendarRepository;

    private final UserRepository userRepository;

    @Override
    public CalendarEvent createEvent(CalendarEvent calendarEvent, UUID userId, boolean isAM) throws IOException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        Integer duration = calendarEvent.getDuration();
        Date startDate = calendarEvent.getStartDate();
        LocalTime startTime = calendarEvent.getStartTime();
        Date endDate = calendarEvent.getUntilDate();
        RecurrenceEvent recurrenceEvent = calendarEvent.getRecurrenceEvent();
        Integer timeBefore = calendarEvent.getTimeBefore();
        String timeBeforeUnit = calendarEvent.getTimeUnitBefore();
        String reminderMethod = calendarEvent.getReminderMethod();
        String[] dateOfWeeks = calendarEvent.getDateOfWeeks();

        endDate.setHours(startTime.getHour());
        endDate.setMinutes(startTime.getMinute());
        endDate.setSeconds(startTime.getSecond());
        System.err.printf("Duration: %d, startDate: %s, startTime: %s, endDate: %s, recurrenceEvent: %s, " +
                        "timeBefore: %d, timeBeforeUnit: %s, reminderMethod: %s, dateOfWeeks: %d", duration,
                AppUtils.formatDateToString(startDate, "yyyy-MM-dd'T'hh:mm:ss a"), startTime.toString(),
                AppUtils.formatDateToString(endDate, "yyyy-MM-dd'T'hh:mm:ss a"), recurrenceEvent.toString(),
                timeBefore, timeBeforeUnit, reminderMethod,
                calendarEvent.getDateOfWeeks().length);

        // TODO: Tạo event
        Event event = new Event()
                .setSummary(calendarEvent.getTitle())
                .setDescription(calendarEvent.getDescription());

        Date startDateClone = startDate;
        startDateClone.setHours(isAM ? startTime.getHour() : startTime.getHour() + 12);
        startDateClone.setMinutes(startTime.getMinute());
        startDateClone.setSeconds(startTime.getSecond());
        DateTime startDateTime = new DateTime(AppUtils.formatDateToString(startDateClone, "yyyy-MM-dd'T'hh:mm:ss"+"+07:00"));

        LocalTime endTime = startTime.plusMinutes(duration);
        Date startDateCloneEnd = startDateClone;
        startDateCloneEnd.setHours(endTime.getHour());
        startDateCloneEnd.setMinutes(endTime.getMinute());
        startDateCloneEnd.setSeconds(endTime.getSecond());
        DateTime endDateTime = new DateTime(AppUtils.formatDateToString(startDateCloneEnd, "yyyy-MM-dd'T'hh:mm:ss"+"+07:00"));

        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Ho_Chi_Minh");
        event.setEnd(end);

        // TODO: Tạo Reminder
        if(recurrenceEvent.equals(RecurrenceEvent.DAILY) && endDate != null) {
            String[] recurrence = new String[] {
                    "RRULE:FREQ=DAILY;UNTIL="+AppUtils.formatDateToString(endDate, "yyyyMMdd'T'hhmmss'Z'")
            };
            event.setRecurrence(Arrays.asList(recurrence));
        }

        if(recurrenceEvent.equals(RecurrenceEvent.WEEKLY) && endDate != null) {
            String[] recurrence = new String[] {
                    "RRULE:FREQ=WEEKLY;BYDAY="+String.join(",", dateOfWeeks)+";UNTIL="+
                            AppUtils.formatDateToString(endDate, "yyyyMMdd'T'hhmmss'Z'")
            };

            Arrays.asList(recurrence).stream().forEach(r -> System.err.println(r));
            event.setRecurrence(Arrays.asList(recurrence));
        }

        if(recurrenceEvent.equals(RecurrenceEvent.MONTHLY) && endDate != null) {
            String[] recurrence = new String[] {
                    "RRULE:FREQ=MONTHLY;UNTIL="+AppUtils.formatDateToString(endDate, "yyyyMMdd'T'hhmmss'Z'")
            };
            event.setRecurrence(Arrays.asList(recurrence));
        }

        Integer minutes = timeBefore;
        if(timeBeforeUnit == "hours") {
            minutes = timeBefore * 60;
        }

        if(timeBeforeUnit == "days") {
            minutes = timeBefore * 60 * 24;
        }

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod(reminderMethod == "email" ? "email" : "popup").setMinutes(minutes),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        // TODO: Thêm event vào calendar
        String calendarId = "primary";
        event = calendar.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());

        // TODO: Cập nhật event vào Database
        calendarEvent.setHtmlLink(event.getHtmlLink());
        calendarEvent.setEventId(event.getId());
        CalendarEvent savedEvent = this.calendarRepository.save(calendarEvent);
        return savedEvent;
    }
}
