package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Antrenorler")
public class Antrenor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AntrenorID")
    private Integer antrenorId;

    @Column(name = "AdSoyad", nullable = false, length = 100)
    private String adSoyad;

    @Column(name = "UzmanlikAlani", nullable = false, length = 50)
    private String uzmanlikAlani;

    @Column(name = "Telefon", nullable = false, unique = true, length = 11)
    private String telefon;

    @Column(name = "IseBaslamaTarihi")
    private LocalDate iseBaslamaTarihi;

    // Getter ve Setter Metotları
    public Integer getAntrenorId() { return antrenorId; }
    public void setAntrenorId(Integer antrenorId) { this.antrenorId = antrenorId; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public String getUzmanlikAlani() { return uzmanlikAlani; }
    public void setUzmanlikAlani(String uzmanlikAlani) { this.uzmanlikAlani = uzmanlikAlani; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public LocalDate getIseBaslamaTarihi() { return iseBaslamaTarihi; }
    public void setIseBaslamaTarihi(LocalDate iseBaslamaTarihi) { this.iseBaslamaTarihi = iseBaslamaTarihi; }
}
