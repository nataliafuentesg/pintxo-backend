package com.tasca.tasca_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
