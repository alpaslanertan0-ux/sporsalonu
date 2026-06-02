package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.Abonelik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;

import java.util.List;

@Repository
public interface AbonelikRepository extends JpaRepository<Abonelik, Integer> {

    // Sadece belirli bir durumda olan abonelikleri getirmek için (Örn: 'Aktif' olanlar)
    List<Abonelik> findByDurum(String durum);

    // Belirli bir üyeye ait tüm abonelik geçmişini getirmek için
    List<Abonelik> findByUye_UyeId(Integer uyeId);

    @Modifying
    @Transactional
    @Query(value = "EXEC sp_MevcutUyeyeAbonelikYenile :uyeId, :paketId", nativeQuery = true)
    void mevcutUyeyeAbonelikYenile(@Param("uyeId") Integer uyeId, @Param("paketId") Integer paketId);
    @Query(value = "SELECT * FROM Vw_SuresiDolacaklar", nativeQuery = true)
    List<Map<String, Object>> suresiDolacakUyeleriGetir();
}


