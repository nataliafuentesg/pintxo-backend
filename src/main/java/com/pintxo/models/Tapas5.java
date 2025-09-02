package com.pintxo.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "tapas5",
        uniqueConstraints = @UniqueConstraint(name="uk_tapas5_code", columnNames="code"))
public class Tapas5 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String code = "TAPAS5";

    private String title = "$5 Tapas";
    private boolean enabled = true;
    private String daysCsv = "MON,TUE,WED";
    private String timezone = "America/New_York";

    @Column(nullable = false)
    private LocalTime startTime = LocalTime.of(16, 0);
    @Column(nullable = false)
    private LocalTime endTime   = LocalTime.of(18, 0);

    @Column(nullable = false)
    private double price = 5.00;
}
