package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Uyeler")
public class Uye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UyeID")
    private Integer uyeId;

    @Column(name = "TCNo", nullable = false, unique = true, length = 11)
    private String tcNo;

    @Column(name = "AdSoyad", nullable = false, length = 100)
    private String adSoyad;

    @Column(name = "Telefon", nullable = false, unique = true, length = 11)
    private String telefon;

    @Column(name = "KanGrubu", length = 5)
    private String kanGrubu;

    @Column(name = "KayitTarihi")
    private LocalDate kayitTarihi;

    @Column(name = "Durum", length = 10)
    private String durum;

    @Column(name = "Cinsiyet", length = 10)
    private String cinsiyet;

    // Getter ve Setter
    public String getCinsiyet() { return cinsiyet; }
    public void setCinsiyet(String cinsiyet) { this.cinsiyet = cinsiyet; }

    // 1. DÜZELTİLEN YER: İlişki (@ManyToOne) yerine doğrudan sütun eşleşmesi yapıldı.
    @Column(name = "AntrenorID")
    private Integer antrenorId;

    // --- Getter ve Setter Metotları ---

    public Integer getUyeId() { return uyeId; }
    public void setUyeId(Integer uyeId) { this.uyeId = uyeId; }

    public String getTcNo() { return tcNo; }
    public void setTcNo(String tcNo) { this.tcNo = tcNo; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getKanGrubu() { return kanGrubu; }
    public void setKanGrubu(String kanGrubu) { this.kanGrubu = kanGrubu; }


    public LocalDate getKayitTarihi() { return kayitTarihi; }
    public void setKayitTarihi(LocalDate kayitTarihi) { this.kayitTarihi = kayitTarihi; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    // 2. DÜZELTİLEN YER: Getter/Setter metotları Antrenor nesnesine değil, antrenorId değişkenine uygun hale getirildi.
    public Integer getAntrenorId() { return antrenorId; }
    public void setAntrenorId(Integer antrenorId) { this.antrenorId = antrenorId; }

}