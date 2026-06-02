package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.SistemAyarlari;
import com.drifit.sporsalonu.repository.SistemAyarlariRepository;
import org.springframework.stereotype.Service;

import java.util.List; // <--- List kullanacağımız için bunu eklemeyi unutmayın!
import java.util.Optional;

@Service
public class SistemAyarlariService {

    private final SistemAyarlariRepository sistemAyarlariRepository;

    public SistemAyarlariService(SistemAyarlariRepository sistemAyarlariRepository) {
        this.sistemAyarlariRepository = sistemAyarlariRepository;
    }

    // Ayarları getir. Eğer veritabanında yoksa, boş oluşturup kaydet ve onu getir.
    public SistemAyarlari ayarlariGetir() {

        // 1. Tablodaki tüm ayarları çek (Zaten 1 tane olmasını bekliyoruz)
        List<SistemAyarlari> tumAyarlar = sistemAyarlariRepository.findAll();

        if (!tumAyarlar.isEmpty()) {
            // Eğer veritabanında ayar varsa, tablodaki ilk ayarı döndür
            return tumAyarlar.get(0);
        } else {
            // Eğer hiç ayar yoksa, yeni bir tane oluştur ve kaydet
            SistemAyarlari yeniAyar = new SistemAyarlari();

            // DİKKAT: yeniAyar.setId(1); satırını SİLDİK!
            // Çünkü @GeneratedValue kullandığımız için ID'yi veritabanı kendi verecek.

            yeniAyar.setSalonAdi("Salon Adı Giriniz"); // Varsayılan değerler
            yeniAyar.setTelefon("0000000000");

            // save() metodu artık ID boş olduğu için INSERT yapacağını anlayacak
            return sistemAyarlariRepository.save(yeniAyar);
        }
    }

    // Formdan gelen verileri veritabanına kaydet
    public void ayarlariGuncelle(String salonAdi, String telefon, String adres) {
        SistemAyarlari ayarlar = ayarlariGetir(); // Mevcut ayarları çek
        ayarlar.setSalonAdi(salonAdi);
        ayarlar.setTelefon(telefon);
        ayarlar.setAdres(adres);
        sistemAyarlariRepository.save(ayarlar); // Güncelleyip tekrar kaydet
    }
    // Tercihleri (Toggle butonlarını) veritabanına kaydet
    public void tercihleriGuncelle(boolean hatirlatici, boolean yedekleme) {
        SistemAyarlari ayarlar = ayarlariGetir(); // Mevcut ayarları çek
        ayarlar.setUyelikBitisHatirlatici(hatirlatici);
        ayarlar.setOtomatikYedekleme(yedekleme);
        sistemAyarlariRepository.save(ayarlar); // Güncelleyip kaydet
    }
}