package com.pintxo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name="idx_contact_created", columnList = "createdAt DESC")
})
public class ContactMessage {

    public enum Status { NEW, CONTACTED, CLOSED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=120)
    private String name;

    @Column(nullable=false, length=180)
    private String email;

    @Column(length=32)
    private String phone;

    @Column(length=180)
    private String subject;

    @Column(nullable=false, length=4000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Status status = Status.NEW;

    @Column(length=512)
    private String notes;          // para seguimiento interno

    @Column(nullable=false, updatable=false)
    private Instant createdAt = Instant.now();

    // (opcionales útiles)
    @Column(length=120) private String sourcePath;   // ruta del sitio desde donde envió (ej: "/contact")
    @Column(length=200) private String userAgent;
    @Column(length=64)  private String ip;

}
