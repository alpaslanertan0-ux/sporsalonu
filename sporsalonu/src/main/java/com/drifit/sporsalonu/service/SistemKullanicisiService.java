package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.SistemKullanicisi;
import com.drifit.sporsalonu.repository.SistemKullanicisiRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SistemKullanicisiService {

    private final SistemKullanicisiRepository repository;

    public SistemKullanicisiService(SistemKullanicisiRepository repository) {
        this.repository = repository;
    }

    public SistemKullanicisi girisKontrol(String kullaniciAdi, String sifre) {
        // Veritabanından kullanıcı adı ile sorgulama yapıyoruz
        SistemKullanicisi kullanici = repository.findByKullaniciAdi(kullaniciAdi);

        // Kullanıcı bulunduysa ve şifre eşleşiyorsa (Şu an düz metin kontrolü yapıyoruz)
        if (kullanici != null && kullanici.getSifreHash().equals(sifre)) {
            // Canlılık testi: Son giriş tarihini şu anki zaman yapalım
            kullanici.setSonGirisTarihi(LocalDateTime.now());
            repository.save(kullanici); // Tarihi veritabanına kaydet
            return kullanici; // Giriş başarılı, kullanıcıyı döndür
        }

        return null; // Kullanıcı yoksa veya şifre yanlışsa null döndür
    }
    public boolean sifreGuncelle(String kullaniciAdi, String yeniSifre) {
        SistemKullanicisi kullanici = repository.findByKullaniciAdi(kullaniciAdi);
        if (kullanici != null) {
            kullanici.setSifreHash(yeniSifre); // Yeni şifreyi kaydet
            repository.save(kullanici);
            return true;
        }
        return false;
    }
    public SistemKullanicisi kullaniciBul(String kullaniciAdi) {
        return repository.findByKullaniciAdi(kullaniciAdi);
    }
}