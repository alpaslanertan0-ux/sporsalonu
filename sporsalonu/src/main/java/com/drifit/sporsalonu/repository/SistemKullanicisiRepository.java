package com.drifit.sporsalonu.repository;

import com.drifit.sporsalonu.model.SistemKullanicisi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SistemKullanicisiRepository extends JpaRepository<SistemKullanicisi, Integer> {

    // Sisteme giriş (Login) yaparken kullanıcıyı bulmak için
    SistemKullanicisi findByKullaniciAdi(String kullaniciAdi);
}