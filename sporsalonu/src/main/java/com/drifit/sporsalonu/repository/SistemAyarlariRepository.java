package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.SistemAyarlari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SistemAyarlariRepository extends JpaRepository<SistemAyarlari, Integer> {
}
