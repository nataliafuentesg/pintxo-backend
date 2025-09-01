package com.tasca.tasca_backend.controllers;

import com.tasca.tasca_backend.dtos.Tapas5PublicResponse;
import com.tasca.tasca_backend.services.Tapas5ItemService;
import com.tasca.tasca_backend.services.Tapas5Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tapas5")
public class PublicTapas5Controller {
    private final Tapas5Service tapas;
    private final Tapas5ItemService items;

    public PublicTapas5Controller(Tapas5Service tapas, Tapas5ItemService items){
        this.tapas = tapas; this.items = items;
    }

    @GetMapping
    public Tapas5PublicResponse get(){
        var res = new Tapas5PublicResponse();
        var t = tapas.getOrCreate();
        res.setActive(tapas.isActiveNow());
        res.setTitle(t.getTitle());
        res.setWindowText(tapas.windowText());
        res.setPrice(t.getPrice());
        res.setItems(res.isActive() ? items.list(true) : java.util.List.of());
        return res;
    }
}