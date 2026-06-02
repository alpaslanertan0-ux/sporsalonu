package com.drifit.sporsalonu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Odemeler")
public class Odeme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OdemeID")
    private Integer odemeId;

    @ManyToOne
    @JoinColumn(name = "AbonelikID", nullable = false)
    private Abonelik abonelik;

    @Column(name = "Tutar", nullable = false, precision = 10, scale = 2)
    private BigDecimal tutar;

    @Column(name = "OdemeTipi", nullable = false, length = 20)
    private String odemeTipi;

    @Column(name = "OdemeTarihi")
    private LocalDateTime odemeTarihi;

    // Getter ve Setter Metotları
    public Integer getOdemeId() { return odemeId; }
    public void setOdemeId(Integer odemeId) { this.odemeId = odemeId; }

    public Abonelik getAbonelik() { return abonelik; }
    public void setAbonelik(Abonelik abonelik) { this.abonelik = abonelik; }

    public BigDecimal getTutar() { return tutar; }
    public void setTutar(BigDecimal tutar) { this.tutar = tutar; }

    public String getOdemeTipi() { return odemeTipi; }
    public void setOdemeTipi(String odemeTipi) { this.odemeTipi = odemeTipi; }

    public LocalDateTime getOdemeTarihi() { return odemeTarihi; }
    public void setOdemeTarihi(LocalDateTime odemeTarihi) { this.odemeTarihi = odemeTarihi; }
}