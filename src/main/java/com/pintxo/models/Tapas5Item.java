package com.pintxo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tapas5_item")
public class Tapas5Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tapas_id")
    private Tapas5 tapas;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="tapas5_item_allergen", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="allergen", length = 64)
    private Set<String> allergens = new LinkedHashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="tapas5_item_tag", joinColumns=@JoinColumn(name="item_id"))
    @Column(name="tag", length = 64)
    private Set<String> tags = new LinkedHashSet<>();

    @Column(nullable = false)
    private boolean available = true;
}
