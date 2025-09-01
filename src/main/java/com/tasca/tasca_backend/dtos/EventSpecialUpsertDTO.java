package com.tasca.tasca_backend.dtos;

import lombok.*;
import java.time.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EventSpecialUpsertDTO {
    private String type;             // EVENT | SPECIAL
    private String title;
    private String description;
    private String imageUrl;
    private String ctaLabel;
    private String ctaTo;

    private String scheduleKind;     // FIXED_DATE | WEEKLY
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private Boolean allDay;

    private String weeklyDays;       // "FRI|SAT"
    private LocalTime weeklyTime;

    private Boolean active;
    private Integer displayOrder;
}
