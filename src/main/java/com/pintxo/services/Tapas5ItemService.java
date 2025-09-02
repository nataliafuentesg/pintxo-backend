package com.pintxo.services;

import com.pintxo.dtos.Tapas5ItemDTO;
import com.pintxo.models.Tapas5Item;
import com.pintxo.repositories.Tapas5ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class Tapas5ItemService {
    private final Tapas5ItemRepository itemRepo;
    private final Tapas5Service tapasService;

    public Tapas5ItemService(Tapas5ItemRepository itemRepo, Tapas5Service tapasService) {
        this.itemRepo = itemRepo; this.tapasService = tapasService;
    }

    private Long parentId() { return tapasService.getOrCreate().getId(); }

    public List<Tapas5ItemDTO> list(boolean onlyAvailable){
        var tid = parentId();
        var list = onlyAvailable
                ? itemRepo.findAllByTapasIdAndAvailableTrueOrderByNameAsc(tid)
                : itemRepo.findAllByTapasIdOrderByNameAsc(tid);
        return list.stream().map(this::toDto).toList();
    }

    public Tapas5ItemDTO get(Long id){
        return toDto(itemRepo.findById(id).orElseThrow());
    }

    public Tapas5ItemDTO create(Tapas5ItemDTO b){
        var e = fromDto(new Tapas5Item(), b);
        e.setTapas(tapasService.getOrCreate());
        itemRepo.save(e);
        return toDto(e);
    }

    public Tapas5ItemDTO update(Long id, Tapas5ItemDTO b){
        var e = itemRepo.findById(id).orElseThrow();
        fromDto(e, b);
        itemRepo.save(e);
        return toDto(e);
    }

    public void delete(Long id){ itemRepo.deleteById(id); }

    public boolean setAvailability(Long id, boolean v){
        var e = itemRepo.findById(id).orElseThrow();
        e.setAvailable(v);
        itemRepo.save(e);
        return v;
    }

    private Tapas5Item fromDto(Tapas5Item e, Tapas5ItemDTO b){
        e.setName(b.getName());
        e.setDescription(b.getDescription());
        e.setAvailable(b.isAvailable());
        e.getAllergens().clear();
        e.getAllergens().addAll(clean(b.getAllergens()));
        e.getTags().clear();
        e.getTags().addAll(clean(b.getTags()));
        return e;
    }
    private Tapas5ItemDTO toDto(Tapas5Item it){
        var d = new Tapas5ItemDTO();
        d.setId(it.getId());
        d.setName(it.getName());
        d.setDescription(it.getDescription());
        d.setAllergens(it.getAllergens());
        d.setTags(it.getTags());
        d.setAvailable(it.isAvailable());
        return d;
    }

    private static Set<String> clean(Set<String> in){
        if (in == null) return Set.of();
        return in.stream()
                .map(s -> s == null ? "" : s.trim())
                .filter(s -> !s.isEmpty())
                .limit(50)                 // defensivo
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
    }

    private static String n(String s){ return s == null ? "" : s.trim(); }
    private static String z(String s){ return (s == null || s.isBlank()) ? null : s.trim(); }
}