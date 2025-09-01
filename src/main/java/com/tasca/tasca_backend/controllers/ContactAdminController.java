package com.tasca.tasca_backend.controllers;
import com.tasca.tasca_backend.dtos.ContactUpdateRequest;
import com.tasca.tasca_backend.models.ContactMessage;
import com.tasca.tasca_backend.repositories.ContactRepository;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;


@RestController
@RequestMapping("/api/admin/contacts")
public class ContactAdminController {

    private final ContactRepository repo;
    public ContactAdminController(ContactRepository repo){ this.repo = repo; }

    @GetMapping
    public Page<ContactMessage> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "") String q
    ){
        Pageable p = PageRequest.of(Math.max(0,page), Math.min(100, size), Sort.by(Sort.Direction.DESC,"createdAt"));
        if (q != null && !q.isBlank()) {
            return repo.findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(q, q, p);
        }
        return repo.findAll(p);
    }

    @GetMapping("/{id}")
    public ContactMessage get(@PathVariable Long id){
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public ContactMessage update(@PathVariable Long id, @RequestBody ContactUpdateRequest body){
        ContactMessage cm = repo.findById(id).orElseThrow();
        if (body.getStatus()!=null && !body.getStatus().isBlank()) {
            cm.setStatus(ContactMessage.Status.valueOf(body.getStatus().toUpperCase()));
        }
        if (body.getNotes()!=null) cm.setNotes(body.getNotes());
        return repo.save(cm);
    }

    @GetMapping("/export.csv")
    public ResponseEntity<byte[]> exportCsv() {
        List<ContactMessage> all = repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        StringBuilder sb = new StringBuilder("id,createdAt,status,name,email,phone,subject,message,sourcePath,ip\n");
        for (ContactMessage c : all) {
            sb.append(c.getId()).append(',')
                    .append(c.getCreatedAt()).append(',')
                    .append(c.getStatus()).append(',')
                    .append(csv(c.getName())).append(',')
                    .append(csv(c.getEmail())).append(',')
                    .append(csv(c.getPhone())).append(',')
                    .append(csv(c.getSubject())).append(',')
                    .append(csv(c.getMessage())).append(',')
                    .append(csv(c.getSourcePath())).append(',')
                    .append(csv(c.getIp())).append('\n');
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.TEXT_PLAIN);
        h.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.csv");
        return new ResponseEntity<>(bytes, h, HttpStatus.OK);
    }

    private String csv(String s){
        if (s == null) return "";
        String x = s.replace("\"","\"\"");
        return "\"" + x + "\"";
    }
}