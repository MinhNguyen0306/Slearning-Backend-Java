package com.example.Slearning.Backend.Java.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.YearMonth;

@Table(name = "work_experiences")
@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    private String company;

    @NotNull
    @NotEmpty
    private String position;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth startDate;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth endDate;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}
