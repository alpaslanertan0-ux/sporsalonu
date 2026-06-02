package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.Antrenor;
import com.drifit.sporsalonu.repository.AntrenorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AntrenorService {

    private final AntrenorRepository antrenorRepository;

    public AntrenorService(AntrenorRepository antrenorRepository) {
        this.antrenorRepository = antrenorRepository;
    }

    public List<Map<String, Object>> antrenorPerformansRaporunuGetir() {
        return antrenorRepository.antrenorPerformansRaporunuGetir();
    }

    public List<Antrenor> tumAntrenorleriGetir() {
        return antrenorRepository.findAll();
    }
}