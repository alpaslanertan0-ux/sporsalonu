package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SistemKullanicilari")
public class SistemKullanicisi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KullaniciID")
    private Integer kullaniciId;

    @Column(name = "KullaniciAdi", nullable = false, unique = true, length = 50)
    private String kullaniciAdi;

    @Column(name = "SifreHash", nullable = false, length = 255)
    private String sifreHash;

    @Column(name = "Rol", length = 20)
    private String rol;

    @Column(name = "SonGirisTarihi")
    private LocalDateTime sonGirisTarihi;

    // Getter ve Setter Metotları
    public Integer getKullaniciId() { return kullaniciId; }
    public void setKullaniciId(Integer kullaniciId) { this.kullaniciId = kullaniciId; }

    public String getKullaniciAdi() { return kullaniciAdi; }
    public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }

    public String getSifreHash() { return sifreHash; }
    public void setSifreHash(String sifreHash) { this.sifreHash = sifreHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getSonGirisTarihi() { return sonGirisTarihi; }
    public void setSonGirisTarihi(LocalDateTime sonGirisTarihi) { this.sonGirisTarihi = sonGirisTarihi; }
}