package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Abonelikler")
public class Abonelik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AbonelikID")
    private Integer abonelikId;

    @ManyToOne
    @JoinColumn(name = "UyeID", nullable = false)
    private Uye uye;

    @ManyToOne
    @JoinColumn(name = "PaketID", nullable = false)
    private Paket paket;

    @Column(name = "BaslangicTarihi", nullable = false)
    private LocalDate baslangicTarihi;

    @Column(name = "BitisTarihi", nullable = false)
    private LocalDate bitisTarihi;

    @Column(name = "Durum", length = 10)
    private String durum;

    // Getter ve Setter Metotları
    public Integer getAbonelikId() { return abonelikId; }
    public void setAbonelikId(Integer abonelikId) { this.abonelikId = abonelikId; }

    public Uye getUye() { return uye; }
    public void setUye(Uye uye) { this.uye = uye; }

    public Paket getPaket() { return paket; }
    public void setPaket(Paket paket) { this.paket = paket; }

    public LocalDate getBaslangicTarihi() { return baslangicTarihi; }
    public void setBaslangicTarihi(LocalDate baslangicTarihi) { this.baslangicTarihi = baslangicTarihi; }

    public LocalDate getBitisTarihi() { return bitisTarihi; }
    public void setBitisTarihi(LocalDate bitisTarihi) { this.bitisTarihi = bitisTarihi; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }
}