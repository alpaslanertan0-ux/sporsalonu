package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.Odeme;
import com.drifit.sporsalonu.repository.OdemeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;


import java.util.Locale;
import java.util.LinkedHashMap;


@Service
public class OdemeService {

    private final OdemeRepository odemeRepository;

    public OdemeService(OdemeRepository odemeRepository) {
        this.odemeRepository = odemeRepository;
    }

    // --- İŞ KURALLARI VE KONTROLLER ---

    public List<Map<String, Object>> ikiTarihArasiFinansRaporu(LocalDateTime baslangic, LocalDateTime bitis) {
        // KURAL: Başlangıç tarihi, bitiş tarihinden sonra olamaz!
        if (baslangic != null && bitis != null && baslangic.isAfter(bitis)) {
            throw new IllegalArgumentException("Hata: Başlangıç tarihi, bitiş tarihinden daha ileri bir tarih olamaz!");
        }

        return odemeRepository.ikiTarihArasiFinansRaporu(baslangic, bitis);
    }

    // --- RAPORLAMA VE LİSTELEME İŞLEMLERİ ---

    public List<Map<String, Object>> muhasebeOdemeRaporunuGetir() {
        return odemeRepository.muhasebeOdemeRaporunuGetir();
    }

    public List<Odeme> tumOdemeleriGetir() {
        return odemeRepository.findAll();
    }
    // Rapordaki Procedure'ü çalıştırır
    public List<Map<String, Object>> finansRaporuAl(LocalDateTime baslangic, LocalDateTime bitis) {
        return odemeRepository.ikiTarihArasiFinansRaporu(baslangic, bitis);
    }

    // View'daki tüm verileri getirir
    public List<Map<String, Object>> muhasebeRaporuAl() {
        return odemeRepository.muhasebeOdemeRaporunuGetir();
    }
    public void odemeKaydet(Odeme yeniOdeme) {
        // Ödeme tarihi boş gelirse, o anki zamanı otomatik atasın
        if (yeniOdeme.getOdemeTarihi() == null) {
            yeniOdeme.setOdemeTarihi(LocalDateTime.now());
        }
        odemeRepository.save(yeniOdeme);
    }
    public void odemeSil(Integer id) {
        odemeRepository.deleteById(id);
    }



// ... mevcut kodlar ...

    public Map<String, Double> getSon6AyGelirAnalizi() { // Boşluk silindi
        List<Odeme> tumOdemeler = odemeRepository.findAll();
        Locale trLocale = new Locale("tr", "TR");

        return tumOdemeler.stream()
                .filter(o -> o.getOdemeTarihi() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getOdemeTarihi().getMonth().getDisplayName(TextStyle.SHORT, trLocale),
                        LinkedHashMap::new,
                        Collectors.summingDouble(o -> o.getTutar() != null ? o.getTutar().doubleValue() : 0.0)
                ));
    }
    public Double getBuAykiToplamGelir() {
        List<Odeme> tumOdemeler = odemeRepository.findAll();

        // İçinde bulunduğumuz ay ve yılı alıyoruz
        int mevcutAy = java.time.LocalDate.now().getMonthValue();
        int mevcutYil = java.time.LocalDate.now().getYear();

        // Sadece bu ay ve yıla ait ödemeleri filtreleyip topluyoruz
        return tumOdemeler.stream()
                .filter(o -> o.getOdemeTarihi() != null &&
                        o.getOdemeTarihi().getMonthValue() == mevcutAy &&
                        o.getOdemeTarihi().getYear() == mevcutYil)
                .mapToDouble(o -> o.getTutar() != null ? o.getTutar().doubleValue() : 0.0)
                .sum();
    }

}