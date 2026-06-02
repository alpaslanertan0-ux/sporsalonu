package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Paketler")
public class Paket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaketID")
    private Integer paketId;

    @Column(name = "PaketAdi", nullable = false, length = 100)
    private String paketAdi;

    @Column(name = "SureAy", nullable = false)
    private Integer sureAy;

    @Column(name = "Ucret", nullable = false, precision = 10, scale = 2)
    private BigDecimal ucret;

    // Getter ve Setter Metotları
    public Integer getPaketId() { return paketId; }
    public void setPaketId(Integer paketId) { this.paketId = paketId; }

    public String getPaketAdi() { return paketAdi; }
    public void setPaketAdi(String paketAdi) { this.paketAdi = paketAdi; }

    public Integer getSureAy() { return sureAy; }
    public void setSureAy(Integer sureAy) { this.sureAy = sureAy; }

    public BigDecimal getUcret() { return ucret; }
    public void setUcret(BigDecimal ucret) { this.ucret = ucret; }
}