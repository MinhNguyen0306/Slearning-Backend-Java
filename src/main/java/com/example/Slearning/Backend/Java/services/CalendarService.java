package com.example.Slearning.Backend.Java.services;

import com.example.Slearning.Backend.Java.domain.entities.CalendarEvent;

import java.io.IOException;
import java.util.UUID;

public interface CalendarService {
    CalendarEvent createEvent(CalendarEvent calendarEvent, UUID userId, boolean isAM) throws IOException;
}
