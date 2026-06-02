package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.Paket;
import com.drifit.sporsalonu.repository.PaketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaketService {
    private final PaketRepository paketRepository;

    // Sadece bu constructor yeterli, yukarıdaki @Autowired'ları ve çift tanımları silebilirsiniz
    public PaketService(PaketRepository paketRepository) {
        this.paketRepository = paketRepository;
    }

    public Paket paketBul(Integer paketId) {
        return paketRepository.findById(paketId).orElse(null);
    }

    public List<Paket> tumPaketleriGetir() {
        return paketRepository.findAll();
    }

    public List<Map<String, Object>> paketSatisIstatistikleriniGetir() {
        return paketRepository.paketSatisIstatistikleriniGetir();
    }





}