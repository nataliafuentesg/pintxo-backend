package com.pintxo.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "event_specials")
public class EventSpecial {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private EventType type;              // EVENT | SPECIAL

    @Column(nullable = false)
    private String title;

    @Column(length = 600)
    private String description;          // desc en el front

    private String imageUrl;

    // CTA simple (si quieres 2 CTAs, añade secundarios como en Offers)
    @Column(length = 160)
    private String ctaLabel;             // e.g. "Reserve", "Details"
    @Column(length = 160)
    private String ctaTo;                // e.g. "/reservations"

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private ScheduleKind scheduleKind;   // FIXED_DATE o WEEKLY

    // FIXED_DATE window
    private LocalDateTime startsAt;      // si allDay, solo fecha relevante
    private LocalDateTime endsAt;
    private boolean allDay;              // “All Day”

    // WEEKLY rule
    @Column(length = 60)
    private String weeklyDays;           // "FRI" o "FRI|SAT"
    private LocalTime weeklyTime;        // 19:00

    @Builder.Default
    private boolean active = true;

    private Integer displayOrder;        // orden en carrusel
}
