package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.Antrenor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface AntrenorRepository extends JpaRepository<Antrenor, Integer> {
    @Query(value = "SELECT * FROM Vw_AntrenorPerformansRaporu", nativeQuery = true)
    List<Map<String, Object>> antrenorPerformansRaporunuGetir();
}
