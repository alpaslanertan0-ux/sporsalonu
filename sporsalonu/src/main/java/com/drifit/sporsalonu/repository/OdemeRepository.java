package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.Odeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface OdemeRepository extends JpaRepository<Odeme, Integer> {

    // Belirli bir aboneliğe ait ödemeleri getirmek için


    List<Odeme> findByAbonelik_AbonelikId(Integer abonelikId);

    // 1. DAHA ÖNCE EKLEDİĞİMİZ FİNANS RAPORU PROSEDÜRÜ:
    @Query(value = "EXEC sp_IkiTarihArasiFinansRaporu :baslangic, :bitis", nativeQuery = true)
    List<Map<String, Object>> ikiTarihArasiFinansRaporu(
            @Param("baslangic") LocalDateTime baslangic,
            @Param("bitis") LocalDateTime bitis
    );

    // 2. YENİ EKLEDİĞİMİZ Vw_MuhasebeOdemeRaporu VİEW SORGUSU:
    @Query(value = "SELECT * FROM Vw_MuhasebeOdemeRaporu", nativeQuery = true)
    List<Map<String, Object>> muhasebeOdemeRaporunuGetir();

}

