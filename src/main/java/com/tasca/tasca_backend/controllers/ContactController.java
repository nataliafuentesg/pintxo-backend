package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.ContactRequest;
import com.tasca.tasca_backend.dtos.ContactResponse;
import com.tasca.tasca_backend.models.ContactMessage;
import com.tasca.tasca_backend.repositories.ContactRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ContactController {

    private final ContactRepository repo;

    @Value("${tasca.mail.enabled:false}")
    private boolean mailEnabled; // reservado para futuro (cuando activemos correo)

    public ContactController(ContactRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/contact")
    public ResponseEntity<ContactResponse> create(@Valid @RequestBody ContactRequest body,
                                                  HttpServletRequest req) {
        ContactMessage cm = new ContactMessage();
        cm.setName(body.getName());
        cm.setEmail(body.getEmail());
        cm.setPhone(body.getPhone());
        cm.setSubject(body.getSubject());
        cm.setMessage(body.getMessage());
        cm.setSourcePath(req.getHeader("X-Page-Path")); // opcional: pásalo desde el front
        cm.setUserAgent(req.getHeader("User-Agent"));
        String ip = req.getHeader("X-Forwarded-For");
        cm.setIp(ip != null ? ip.split(",")[0].trim() : req.getRemoteAddr());

        ContactMessage saved = repo.save(cm);

        // Si en el futuro activas correo (tasca.mail.enabled=true), enviaríamos aquí.

        ContactResponse resp = new ContactResponse(true, saved.getId());
        return ResponseEntity.created(URI.create("/api/contact/" + saved.getId())).body(resp);
    }
}