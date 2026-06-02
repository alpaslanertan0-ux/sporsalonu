package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.Paket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PaketRepository extends JpaRepository<Paket, Integer> {
    @Query(value = "SELECT * FROM Vw_PaketSatisIstatistikleri", nativeQuery = true)
    List<Map<String, Object>> paketSatisIstatistikleriniGetir();
}
