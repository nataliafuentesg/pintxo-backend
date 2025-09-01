package com.tasca.tasca_backend.repositories;

import com.tasca.tasca_backend.models.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactMessage, Long> {
    Page<ContactMessage> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name, Pageable p);

}

