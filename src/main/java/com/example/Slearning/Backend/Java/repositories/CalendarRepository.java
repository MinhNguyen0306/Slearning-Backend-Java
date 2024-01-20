package com.example.Slearning.Backend.Java.repositories;

import com.example.Slearning.Backend.Java.domain.entities.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEvent, String> {
}
