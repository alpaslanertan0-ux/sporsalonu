package com.drifit.sporsalonu.service;

import com.drifit.sporsalonu.model.Abonelik;
import com.drifit.sporsalonu.repository.AbonelikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AbonelikService {

    private final AbonelikRepository abonelikRepository;

    // Tek bir constructor yeterli
    public AbonelikService(AbonelikRepository abonelikRepository) {
        this.abonelikRepository = abonelikRepository;
    }

    public void abonelikKaydet(Abonelik abonelik) {
        abonelikRepository.save(abonelik);
    }

    // --- İŞ KURALLARI VE KONTROLLER ---

    public void mevcutUyeyeAbonelikYenile(Integer uyeId, Integer paketId) {
        // KURAL: Üye ve Paket ID'leri boş olamaz
        if (uyeId == null || uyeId <= 0) {
            throw new IllegalArgumentException("Hata: Geçerli bir Üye ID belirtilmelidir!");
        }
        if (paketId == null || paketId <= 0) {
            throw new IllegalArgumentException("Hata: Geçerli bir Paket ID belirtilmelidir!");
        }

        // Kurallardan geçtiyse, SQL Prosedürünü ateşle
        abonelikRepository.mevcutUyeyeAbonelikYenile(uyeId, paketId);
    }

    // --- RAPORLAMA VE LİSTELEME İŞLEMLERİ ---

    public List<Map<String, Object>> suresiDolacakUyeleriGetir() {
        return abonelikRepository.suresiDolacakUyeleriGetir();
    }

    public List<Abonelik> aktifAbonelikleriGetir() {
        return abonelikRepository.findByDurum("Aktif");
    }

    public List<Abonelik> tumAbonelikleriGetir() {
        return abonelikRepository.findAll();
    }

    // --- YENİ EKLENEN: GRAFİK İÇİN PAKET DAĞILIMI ANALİZİ ---

    public Map<String, Long> getPaketDagilimiAnalizi() {
        List<Abonelik> tumAbonelikler = abonelikRepository.findAll();

        return tumAbonelikler.stream()
                .collect(Collectors.groupingBy(
                        ab -> (ab.getPaket() != null && ab.getPaket().getPaketAdi() != null)
                                ? ab.getPaket().getPaketAdi()
                                : "Bilinmeyen Paket",
                        Collectors.counting()
                ));
    }
    public List<Abonelik> getSonEklenenAbonelikler() {
        List<Abonelik> tumAbonelikler = abonelikRepository.findAll();

        // Listeyi tersine çeviriyoruz ki en son eklenenler en başa gelsin
        java.util.Collections.reverse(tumAbonelikler);

        // Sadece ilk 5 kaydı alıp HTML'e gönderiyoruz (sayfa çok uzamasın diye)
        return tumAbonelikler.stream()
                .limit(5)
                .collect(Collectors.toList());
    }


}