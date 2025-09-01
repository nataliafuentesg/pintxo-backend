package com.tasca.tasca_backend.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;



@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"highlight_date", "menu_item_id"}))
public class MenuHighlightDay {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "highlight_date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false) @JoinColumn(name = "menu_item_id")
    @NotNull
    private MenuItem item;

    private Integer position;      // 1..N (orden del carrusel ese día)
    private String tagOverride;    // badge específico del día (opcional)

    @Builder.Default
    private boolean active = true;
}

