package com.tasca.tasca_backend.controllers;
import com.tasca.tasca_backend.dtos.EventSpecialUpsertDTO;
import com.tasca.tasca_backend.models.EventSpecial;
import com.tasca.tasca_backend.services.EventSpecialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/events")
@RequiredArgsConstructor
public class EventSpecialAdminController {

    private final EventSpecialService service;

    @GetMapping
    public ResponseEntity<Page<EventSpecial>> list(@RequestParam(required=false) String type,
                                                   @RequestParam(required=false) String q,
                                                   @RequestParam(defaultValue="0") int page,
                                                   @RequestParam(defaultValue="20") int size) {
        return ResponseEntity.ok(service.list(type, q, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSpecial> get(@PathVariable Long id){
        return ResponseEntity.of(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<EventSpecial> create(@RequestBody EventSpecialUpsertDTO dto){
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventSpecial> update(@PathVariable Long id, @RequestBody EventSpecialUpsertDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean value){
        service.setActive(id, value);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
