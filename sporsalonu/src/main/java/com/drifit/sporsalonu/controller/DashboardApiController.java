package com.drifit.sporsalonu.controller; // Projenizin gerçek paket yolu eklendi

import com.drifit.sporsalonu.service.OdemeService;     // sporsalonu eklendi
import com.drifit.sporsalonu.service.AbonelikService;  // sporsalonu eklendi
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.drifit.sporsalonu.service.OdemeService;
import com.drifit.sporsalonu.service.AbonelikService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final OdemeService odemeService;
    private final AbonelikService abonelikService;

    public DashboardApiController(OdemeService odemeService, AbonelikService abonelikService) {
        this.odemeService = odemeService;
        this.abonelikService = abonelikService;
    }

    @GetMapping("/grafik-verileri")
    public Map<String, Object> getGrafikVerileri() {
        Map<String, Object> response = new HashMap<>();

        // Servislerden gelen dinamik verileri haritaya ekliyoruz
        response.put("gelirler", odemeService.getSon6AyGelirAnalizi());
        response.put("paketler", abonelikService.getPaketDagilimiAnalizi());

        return response;
    }
}