package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.Uye;
import com.drifit.sporsalonu.repository.UyeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UyeService {

    // UyeRepository'yi (Veritabanı kapımızı) bu servise bağlıyoruz
    private final UyeRepository uyeRepository;

    public UyeService(UyeRepository uyeRepository) {
        this.uyeRepository = uyeRepository;
    }

    // --- İŞ KURALLARI VE KONTROLLER (BUSINESS LOGIC) ---

    public void yeniUyeKaydet(String tcNo, String adSoyad, String telefon, Integer paketId, Integer antrenorId) {
        // Eğer kod buraya kadar hata vermeden geldiyse, kişi tamamen yasaldır.
        // Artık yazdığın o muhteşem SQL Prosedürünü çağırıp veritabanına yazdırabiliriz:
        uyeRepository.yeniUyeVeAbonelikKayit(tcNo, adSoyad, telefon, paketId, antrenorId);
    }

    // --- RAPORLAMA VE LİSTELEME İŞLEMLERİ ---

    // Özel yazdığımız View'ı çağıran servis metodu
    public List<Map<String, Object>> aktifUyeDökümünüAl() {
        return uyeRepository.aktifUyeBilgileriniGetir();
    }

    // Sisteme kayıtlı tüm üyeleri getiren standart metot
    public List<Uye> tumUyeleriGetir() {
        return uyeRepository.findAll();
    }

    // HTML tablosu için 3 tablonun birleştiği özel dökümü getirir
    public List<Map<String, Object>> tumUyeDetaylariniGetir(String kelime, String durum) {
        // Eğer kelime null ise SQL'de sorun çıkmaması için boş string yapıyoruz
        if (kelime == null) {
            kelime = "";
        }

        // Eğer durum null veya "hepsi" ise SQL'e hepsini getirmesini söyleyeceğiz
        if (durum == null) {
            durum = "hepsi";
        }

        // Repository'ye temizlenmiş filtreleri gönderiyoruz
        return uyeRepository.tumUyeDetaylariniGetirFiltreli(kelime.trim(), durum);
    }

    // Sadece ID'si verilen tek bir üyeyi bulup getirir
    public Uye uyeGetir(Integer id) {
        // findById, Spring Boot'un kendi içindeki otomatik arama motorudur
        return uyeRepository.findById(id).orElse(null);
    }

    // Controller'dan gelen güncel bilgileri Repository'ye (SQL'e) iletir
    public void uyeGuncelle(Integer uyeId, String adSoyad, String telefon, String tcNo) {
        uyeRepository.uyeBilgileriniGuncelle(uyeId, adSoyad, telefon, tcNo);
    }

    // --- DEĞİŞTİRİLEN KISIM: Hiyerarşik Silme İşlemi ---
    @Transactional // Tüm işlemlerin tek bir bütün olarak gerçekleşmesini sağlar (Biri patlarsa hepsi geri alınır)
    public void uyeSil(Integer id) {
        // 1. Önce bu üyeye (ve aboneliğine) ait olan tüm ödemeleri / faturaları sil
        uyeRepository.uyeyeAitOdemeleriSil(id);

        // 2. Ödemeler silindiğine göre artık bu üyeye ait abonelikleri güvenle silebiliriz
        uyeRepository.abonelikleriSil(id);

        // 3. Alt kayıtların hepsi temizlendi, artık ana üyeyi sistemden tamamen silebiliriz
        uyeRepository.uyeSil(id);
    }

    public Uye uyeyiKaydet(Uye yeniUye) {
        // Veritabanına kaydeder ve kaydedilen nesneyi (ID'si atanmış haliyle) geri döndürür
        return uyeRepository.save(yeniUye);
    }
}