package com.example.Slearning.Backend.Java.domain.entities;

import com.example.Slearning.Backend.Java.utils.enums.RecurrenceEvent;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.util.DateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {
    @Id
    @Column(name = "calendar_event_id", nullable = false)
    private String eventId;

    @Column(name = "summary")
    private String title;

    private String description;

    private Integer duration;

    @Enumerated(EnumType.STRING)
    private RecurrenceEvent recurrenceEvent;

    private String[] dateOfWeeks;

    private Integer timeBefore;

    @Column(name = "time_unit_before")
    private String timeUnitBefore;

    private String reminderMethod;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date startDate;

    @JsonFormat(pattern = "h:mm a")
    private LocalTime startTime;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date untilDate;

    @Column(name = "event_link")
    private String htmlLink;

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ssa")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ssa")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
