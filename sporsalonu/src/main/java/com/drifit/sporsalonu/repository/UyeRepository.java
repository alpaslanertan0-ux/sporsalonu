package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.Uye;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UyeRepository extends JpaRepository<Uye, Integer> {

    // Spring Boot temel işlemleri (Ekle, Sil, Güncelle, Tümünü Listele) otomatik tanır!
    // Sadece isimlendirme kurallarına uyarak özel metotlar da yazabiliriz:

    // Örnek: TC No'ya göre üye getiren metot (SQL yazmaya gerek yok!)
    Uye findByTcNo(String tcNo);

    // Örnek: Durumu 'Aktif' olanları listele
    List<Uye> findByDurum(String durum);
    @Modifying
    @Transactional
    @Query(value = "EXEC sp_YeniUyeVeAbonelikKayit :tcNo, :adSoyad, :telefon, :paketId, :antrenorId", nativeQuery = true)
    void yeniUyeVeAbonelikKayit(
            @Param("tcNo") String tcNo,
            @Param("adSoyad") String adSoyad,
            @Param("telefon") String telefon,
            @Param("paketId") Integer paketId,
            @Param("antrenorId") Integer antrenorId);
    @Query(value = "SELECT * FROM Vw_AktifUyeBilgileri", nativeQuery = true)
    List<Map<String, Object>> aktifUyeBilgileriniGetir();
    // HTML tablosunun istediği birleşik verileri getiren özel SQL Sorgusu

    @Query(value = "SELECT " +
            "u.UyeID AS uyeId, " +
            "u.AdSoyad AS adSoyad, " +
            "u.Telefon AS telefon, " +
            "p.PaketAdi AS paketAdi, " +
            "a.BaslangicTarihi AS baslangicTarihi, " +
            "a.BitisTarihi AS bitisTarihi, " +
            "a.Durum AS durum " +
            "FROM Uyeler u " +
            "LEFT JOIN Abonelikler a ON u.UyeID = a.UyeID " +
            "LEFT JOIN Paketler p ON a.PaketID = p.PaketID", nativeQuery = true)
    List<Map<String, Object>> tumUyeVeAbonelikDetaylariniGetir();
    @Modifying
    @Transactional
    @Query(value = "EXEC sp_MevcutUyeyeAbonelikYenile :tcNo, :adSoyad, :telefon, :paketId, :antrenorId", nativeQuery = true)
    void yeniUyeKaydetProseduru(
            @Param("tcNo") String tcNo,
            @Param("adSoyad") String adSoyad,
            @Param("telefon") String telefon,
            @Param("paketId") Integer paketId,
            @Param("antrenorId") Integer antrenorId
    );
    // Kullanıcının güncellediği bilgileri veritabanına yazar
    @Modifying
    @Transactional
    @Query(value = "UPDATE Uyeler SET AdSoyad = :adSoyad, Telefon = :telefon, TCNo = :tcNo WHERE UyeID = :uyeId", nativeQuery = true)
    void uyeBilgileriniGuncelle(
            @Param("uyeId") Integer uyeId,
            @Param("adSoyad") String adSoyad,
            @Param("telefon") String telefon,
            @Param("tcNo") String tcNo
    );
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Uyeler WHERE UyeID = :id", nativeQuery = true)
    void uyeSil(@Param("id") Integer id);
    // 1. Abonelikleri silen komut
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Abonelikler WHERE UyeID = :id", nativeQuery = true)
    void abonelikleriSil(@Param("id") Integer id);
    @Query(value = "SELECT u.UyeID, u.AdSoyad, u.Telefon, p.PaketAdi, a.BaslangicTarihi, a.BitisTarihi, a.Durum " +
            "FROM Uyeler u " +
            "LEFT JOIN Abonelikler a ON u.UyeID = a.UyeID " +
            "LEFT JOIN Paketler p ON a.PaketID = p.PaketID " +
            "WHERE (:kelime = '' OR u.AdSoyad LIKE %:kelime% OR u.TCNo LIKE %:kelime%) " +
            "AND (:durum = 'hepsi' OR (:durum = 'aktif' AND a.Durum = 'Aktif') OR (:durum = 'pasif' AND a.Durum <> 'Aktif'))",
            nativeQuery = true)
    List<Map<String, Object>> tumUyeDetaylariniGetirFiltreli(
            @Param("kelime") String kelime,
            @Param("durum") String durum
    );
    // Üyeye ait aboneliğin ödemelerini silen SQL sorgusu
    @org.springframework.data.jpa.repository.Modifying
    @jakarta.transaction.Transactional
    @org.springframework.data.jpa.repository.Query(value = "DELETE FROM Odemeler WHERE AbonelikID IN (SELECT AbonelikID FROM Abonelikler WHERE UyeID = :uyeId)", nativeQuery = true)
    void uyeyeAitOdemeleriSil(@org.springframework.data.repository.query.Param("uyeId") Integer uyeId);
}