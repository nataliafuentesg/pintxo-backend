package com.pintxo.controllers;

import com.pintxo.dtos.Tapas5DTO;
import com.pintxo.dtos.Tapas5ItemDTO;
import com.pintxo.services.Tapas5ItemService;
import com.pintxo.services.Tapas5Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tapas5")
public class AdminTapas5Controller {
    private final Tapas5Service tapas;
    private final Tapas5ItemService items;

    public AdminTapas5Controller(Tapas5Service tapas, Tapas5ItemService items){
        this.tapas = tapas; this.items = items;
    }

    // “Tapas5” (campos globales del especial)
    @GetMapping
    public Tapas5DTO get(){ return tapas.getDTO(); }

    @PutMapping
    public Tapas5DTO save(@RequestBody Tapas5DTO dto){ return tapas.save(dto); }

    // Items CRUD
    @GetMapping("/items")
    public List<Tapas5ItemDTO> list(@RequestParam(defaultValue="false") boolean onlyAvailable){
        return items.list(onlyAvailable);
    }

    @PostMapping("/items")
    public Tapas5ItemDTO create(@RequestBody Tapas5ItemDTO dto){ return items.create(dto); }

    @GetMapping("/items/{id}")
    public Tapas5ItemDTO getItem(@PathVariable Long id){ return items.get(id); }

    @PutMapping("/items/{id}")
    public Tapas5ItemDTO update(@PathVariable Long id, @RequestBody Tapas5ItemDTO dto){ return items.update(id, dto); }

    @PatchMapping("/items/{id}/availability")
    public Map<String,Object> availability(@PathVariable Long id, @RequestParam boolean value){
        return Map.of("ok", true, "available", items.setAvailability(id, value));
    }

    @DeleteMapping("/items/{id}")
    public Map<String,Object> remove(@PathVariable Long id){ items.delete(id); return Map.of("ok", true); }
}