package com.drifit.sporsalonu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sistem_ayarlari")
public class SistemAyarlari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String salonAdi;
    private String telefon;
    private String adres;

    private boolean uyelikBitisHatirlatici = true; // Varsayılan olarak açık gelsin
    private boolean otomatikYedekleme = false;     // Varsayılan olarak kapalı gelsin

    // Getter ve Setter Metotları
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSalonAdi() {
        return salonAdi;
    }

    public void setSalonAdi(String salonAdi) {
        this.salonAdi = salonAdi;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }
    public boolean isUyelikBitisHatirlatici() {
        return uyelikBitisHatirlatici;
    }

    public void setUyelikBitisHatirlatici(boolean uyelikBitisHatirlatici) {
        this.uyelikBitisHatirlatici = uyelikBitisHatirlatici;
    }

    public boolean isOtomatikYedekleme() {
        return otomatikYedekleme;
    }

    public void setOtomatikYedekleme(boolean otomatikYedekleme) {
        this.otomatikYedekleme = otomatikYedekleme;
    }
}