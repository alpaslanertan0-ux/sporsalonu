package com.drifit.sporsalonu.controller;

import com.drifit.sporsalonu.model.Abonelik;
import com.drifit.sporsalonu.model.Odeme;
import com.drifit.sporsalonu.model.Paket;
import com.drifit.sporsalonu.model.Uye;
import com.drifit.sporsalonu.model.SistemKullanicisi;
import com.drifit.sporsalonu.service.AbonelikService;
import com.drifit.sporsalonu.service.PaketService;
import com.drifit.sporsalonu.service.UyeService;
import com.drifit.sporsalonu.service.OdemeService;
import com.drifit.sporsalonu.service.SistemKullanicisiService;
import com.drifit.sporsalonu.service.SistemAyarlariService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class SayfaController {

    private final UyeService uyeService;
    private final OdemeService odemeService;
    private final AbonelikService abonelikService;
    private final PaketService paketService;
    private final SistemKullanicisiService sistemKullanicisiService;
    private final SistemAyarlariService sistemAyarlariService;

    public SayfaController(UyeService uyeService,
                           OdemeService odemeService,
                           AbonelikService abonelikService,
                           PaketService paketService,
                           SistemKullanicisiService sistemKullanicisiService,
                           SistemAyarlariService sistemAyarlariService) {

        this.uyeService = uyeService;
        this.odemeService = odemeService;
        this.abonelikService = abonelikService;
        this.paketService = paketService;
        this.sistemKullanicisiService = sistemKullanicisiService;
        this.sistemAyarlariService = sistemAyarlariService;
    }

    // --- 1. GİRİŞ VE ÇIKIŞ İŞLEMLERİ (ÇEREZ DESTEKLİ) ---

    @GetMapping("/login")
    public String loginSayfasiniGoster(HttpSession session, HttpServletRequest request) {
        if (session.getAttribute("oturumKullanici") != null) {
            return "redirect:/";
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("hatirlaKullanici".equals(cookie.getName())) {
                    String kayitliKullaniciAdi = cookie.getValue();
                    var kullanici = sistemKullanicisiService.kullaniciBul(kayitliKullaniciAdi);
                    if (kullanici != null) {
                        session.setAttribute("oturumKullanici", kullanici);
                        return "redirect:/";
                    }
                }
            }
        }

        return "login";
    }

    @PostMapping("/login")
    public String girisYap(
            @RequestParam("kullaniciAdi") String kullaniciAdi,
            @RequestParam("sifre") String sifre,
            @RequestParam(value = "hatirla", required = false) String hatirla,
            HttpSession session,
            HttpServletResponse response,
            Model model
    ) {
        var kullanici = sistemKullanicisiService.girisKontrol(kullaniciAdi, sifre);

        if (kullanici != null) {
            session.setAttribute("oturumKullanici", kullanici);

            if (hatirla != null) {
                Cookie cookie = new Cookie("hatirlaKullanici", kullaniciAdi);
                cookie.setMaxAge(30 * 24 * 60 * 60);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

            return "redirect:/";
        } else {
            model.addAttribute("hataMesaji", "Kullanıcı adı veya şifre hatalı!");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String cikisYap(HttpSession session, HttpServletResponse response) {
        session.invalidate();

        Cookie cookie = new Cookie("hatirlaKullanici", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/login";
    }

    // --- 2. ŞİFRE SIFIRLAMA İŞLEMLERİ ---

    @GetMapping("/sifremi-unuttum")
    public String sifremiUnuttumSayfasi() {
        return "sifremi-unuttum";
    }

    @PostMapping("/sifre-sifirla")
    public String sifreSifirla(
            @RequestParam("kullaniciAdi") String kullaniciAdi,
            @RequestParam("yeniSifre") String yeniSifre,
            RedirectAttributes redirectAttributes
    ) {
        boolean basarili = sistemKullanicisiService.sifreGuncelle(kullaniciAdi, yeniSifre);

        if (basarili) {
            redirectAttributes.addFlashAttribute("basariMesaji", "Şifreniz başarıyla güncellendi. Yeni şifrenizle giriş yapabilirsiniz.");
        } else {
            redirectAttributes.addFlashAttribute("hataMesaji", "Sistemde böyle bir kullanıcı adı bulunamadı!");
        }
        return "redirect:/login";
    }

    // --- 3. GÜVENLİĞE ALINMIŞ SAYFALAR ---

    @GetMapping("/")
    public String anaSayfa(HttpSession session, Model model) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        int toplamUye = uyeService.tumUyeleriGetir().size();
        int aktifUye = abonelikService.aktifAbonelikleriGetir().size();
        int buAyBitenler = abonelikService.suresiDolacakUyeleriGetir().size();
        Double aylikGelir = odemeService.getBuAykiToplamGelir();

        model.addAttribute("toplamUye", toplamUye);
        model.addAttribute("aktifUye", aktifUye);
        model.addAttribute("buAyBitenler", buAyBitenler);
        model.addAttribute("aylikGelir", aylikGelir);
        model.addAttribute("sonUyeler", abonelikService.getSonEklenenAbonelikler());

        return "index";
    }

    @GetMapping("/odemeler")
    public String odemelerSayfasi(HttpSession session, Model model) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        List<Odeme> odemeListesi = odemeService.tumOdemeleriGetir();

        BigDecimal buAyTahsilat = BigDecimal.ZERO;
        BigDecimal bekleyenTahsilat = BigDecimal.ZERO;
        int gecikenSayisi = 0;

        int suAnkiAy = java.time.LocalDateTime.now().getMonthValue();
        int suAnkiYil = java.time.LocalDateTime.now().getYear();

        for (Odeme odeme : odemeListesi) {
            BigDecimal tutar = (odeme.getTutar() != null) ? odeme.getTutar() : BigDecimal.ZERO;

            if (odeme.getOdemeTarihi() != null) {
                if (odeme.getOdemeTarihi().getMonthValue() == suAnkiAy &&
                        odeme.getOdemeTarihi().getYear() == suAnkiYil) {
                    buAyTahsilat = buAyTahsilat.add(tutar);
                }
            } else {
                bekleyenTahsilat = bekleyenTahsilat.add(tutar);
                gecikenSayisi++;
            }
        }

        model.addAttribute("buAyTahsilat", buAyTahsilat);
        model.addAttribute("bekleyenTahsilat", bekleyenTahsilat);
        model.addAttribute("gecikenSayisi", gecikenSayisi);
        model.addAttribute("odemeler", odemeListesi);

        List<Abonelik> abonelikListesi = abonelikService.tumAbonelikleriGetir();
        model.addAttribute("abonelikler", abonelikListesi);

        return "odemeler";
    }

    @GetMapping("/uyeler")
    public String uyelerSayfasiniGoster(
            @RequestParam(value = "kelime", required = false) String kelime,
            @RequestParam(value = "durum", required = false) String durum,
            HttpSession session,
            Model model) {

        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        var detayliUyeler = uyeService.tumUyeDetaylariniGetir(kelime, durum);
        model.addAttribute("uyelerListesi", detayliUyeler);
        return "uyeler";
    }

    @GetMapping("/kayit")
    public String kayitSayfasiniGoster(HttpSession session, Model model) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        model.addAttribute("paketler", paketService.tumPaketleriGetir());
        model.addAttribute("sonUyeler", abonelikService.getSonEklenenAbonelikler());
        return "kayit";
    }

    @GetMapping("/ayarlar")
    public String ayarlarSayfasiniGoster(HttpSession session, Model model) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        // Veritabanından mevcut salon ayarlarını çekip sayfaya gönderiyoruz
        model.addAttribute("sistemAyarlari", sistemAyarlariService.ayarlariGetir());
        return "ayarlar";
    }

    // --- 4. İŞLEM METOTLARI (KAYIT, GÜNCELLEME, SİLME) ---

    @PostMapping("/uye-kaydet")
    public String yeniUyeKaydet(
            @RequestParam("adSoyad") String adSoyad,
            @RequestParam("telefon") String telefon,
            @RequestParam(value = "tcNo", required = false) String tcNo,
            @RequestParam(value = "cinsiyet", required = false) String cinsiyet,
            @RequestParam(value = "kanGrubu", required = false) String kanGrubu,
            @RequestParam("baslangicTarihi") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate baslangicTarihi,
            @RequestParam("paketId") Integer paketId,
            @RequestParam(value = "antrenorId", defaultValue = "1") Integer antrenorId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Uye yeniUye = new Uye();
            yeniUye.setAdSoyad(adSoyad);

            String temizTelefon = telefon.replaceAll("\\s+", "");
            yeniUye.setTelefon(temizTelefon);

            yeniUye.setTcNo(tcNo);
            yeniUye.setCinsiyet(cinsiyet);
            yeniUye.setKanGrubu(kanGrubu);
            yeniUye.setKayitTarihi(baslangicTarihi);

            Uye kaydedilenUye = uyeService.uyeyiKaydet(yeniUye);

            Abonelik abonelik = new Abonelik();
            abonelik.setUye(kaydedilenUye);

            Paket secilenPaket = paketService.paketBul(paketId);
            abonelik.setPaket(secilenPaket);
            abonelik.setBaslangicTarihi(baslangicTarihi);

            abonelik.setBitisTarihi(baslangicTarihi.plusMonths(1));
            abonelik.setDurum("Aktif");

            abonelikService.abonelikKaydet(abonelik);

            redirectAttributes.addFlashAttribute("basariMesaji", "Kayıt başarıyla oluşturuldu!");
            return "redirect:/kayit";

        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("hataMesaji", "Hata: Bu TC Kimlik Numarası veya Telefon Numarası zaten sistemde kayıtlı!");
            return "redirect:/kayit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("hataMesaji", "Kayıt sırasında beklenmeyen bir hata oluştu.");
            return "redirect:/kayit";
        }
    }

    // --- DÜZENLENEN KISIM (Tarih ve Paket Kaydı Eklendi) ---
    @PostMapping("/uye-guncelle")
    public String uyeGuncelle(
            @RequestParam("uyeId") Integer uyeId,
            @RequestParam("adSoyad") String adSoyad,
            @RequestParam("telefon") String telefon,
            @RequestParam("tcNo") String tcNo,
            @RequestParam("baslangic") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate baslangicTarihi,
            @RequestParam("paketId") Integer paketId
    ) {
        // 1. Üyenin temel bilgilerini (Ad, Telefon, TC) güncelle
        uyeService.uyeGuncelle(uyeId, adSoyad, telefon, tcNo);

        // 2. Üyenin aktif olan abonelik kaydını bul
        Abonelik aktifAbonelik = abonelikService.tumAbonelikleriGetir().stream()
                .filter(a -> a.getUye() != null && a.getUye().getUyeId().equals(uyeId) && "Aktif".equals(a.getDurum()))
                .findFirst()
                .orElse(null);

        // 3. Eğer aktif aboneliği varsa paketini ve tarihlerini güncelle
        if (aktifAbonelik != null) {
            Paket yeniPaket = paketService.paketBul(paketId);
            aktifAbonelik.setPaket(yeniPaket);
            aktifAbonelik.setBaslangicTarihi(baslangicTarihi);

            // Yeni başlangıç tarihine göre bitiş tarihini otomatik 1 ay ileri atıyoruz
            aktifAbonelik.setBitisTarihi(baslangicTarihi.plusMonths(1));

            // Abonelik servisi üzerinden veritabanına kaydet
            abonelikService.abonelikKaydet(aktifAbonelik);
        }

        return "redirect:/uyeler";
    }

    // --- DÜZENLENEN KISIM (Paketlerin GET Metoduna Eklenmesi) ---
    @GetMapping("/uyeler/duzenle/{id}")
    public String uyeDuzenleSayfasiniGoster(@PathVariable("id") Integer id, Model model, HttpSession session) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        // 1. Üye bilgilerini çek ve modele ekle
        Uye guncellenecekUye = uyeService.uyeGetir(id);
        model.addAttribute("uye", guncellenecekUye);

        // 2. Veritabanındaki tüm paketleri çek ve açılır kutu (select) için modele ekle
        model.addAttribute("paketler", paketService.tumPaketleriGetir());

        // 3. Üyenin aktif aboneliğini bulup başlangıç tarihi ve mevcut paketi seçili getirmek için modele ekle
        Abonelik aktifAbonelik = abonelikService.tumAbonelikleriGetir().stream()
                .filter(a -> a.getUye() != null && a.getUye().getUyeId().equals(id) && "Aktif".equals(a.getDurum()))
                .findFirst()
                .orElse(null);
        model.addAttribute("aktifAbonelik", aktifAbonelik);
        model.addAttribute("sonUyeler", abonelikService.getSonEklenenAbonelikler());
        return "duzenle";
    }

    @GetMapping("/uyeler/sil/{id}")
    public String uyeSil(@PathVariable("id") Integer id) {
        uyeService.uyeSil(id);
        return "redirect:/uyeler";
    }

    @GetMapping("/odemeler/rapor")
    public String raporlariGoster(Model model, HttpSession session) {
        if (session.getAttribute("oturumKullanici") == null) return "redirect:/login";

        var muhasebeVerileri = odemeService.muhasebeRaporuAl();
        model.addAttribute("muhasebeRaporu", muhasebeVerileri);
        return "raporlar";
    }

    @PostMapping("/odeme-kaydet")
    public String yeniOdemeKaydet(@ModelAttribute Odeme odeme) {
        odemeService.odemeKaydet(odeme);
        return "redirect:/odemeler";
    }

    @GetMapping("/odemeler/sil/{id}")
    public String odemeSil(@PathVariable("id") Integer id) {
        odemeService.odemeSil(id);
        return "redirect:/odemeler";
    }

    // --- GÜVENLİK: ŞİFRE DEĞİŞTİRME METODU ---
    @PostMapping("/ayarlar/sifre-degistir")
    public String ayarlarSifreDegistir(
            @RequestParam("mevcutSifre") String mevcutSifre,
            @RequestParam("yeniSifre") String yeniSifre,
            @RequestParam("yeniSifreTekrar") String yeniSifreTekrar,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        SistemKullanicisi aktifKullanici = (SistemKullanicisi) session.getAttribute("oturumKullanici");
        if (aktifKullanici == null) return "redirect:/login";

        if (!yeniSifre.equals(yeniSifreTekrar)) {
            redirectAttributes.addFlashAttribute("hataMesaji", "Hata: Yeni şifre ve tekrarı birbiriyle eşleşmiyor!");
            return "redirect:/ayarlar";
        }

        if (!aktifKullanici.getSifreHash().equals(mevcutSifre)) {
            redirectAttributes.addFlashAttribute("hataMesaji", "Hata: Mevcut şifrenizi yanlış girdiniz!");
            return "redirect:/ayarlar";
        }

        boolean basarili = sistemKullanicisiService.sifreGuncelle(aktifKullanici.getKullaniciAdi(), yeniSifre);

        if (basarili) {
            aktifKullanici.setSifreHash(yeniSifre);
            session.setAttribute("oturumKullanici", aktifKullanici);
            redirectAttributes.addFlashAttribute("basariMesaji", "Tebrikler! Yönetici şifreniz başarıyla değiştirildi.");
        } else {
            redirectAttributes.addFlashAttribute("hataMesaji", "Şifre güncellenirken beklenmeyen bir hata oluştu.");
        }

        return "redirect:/ayarlar";
    }

    // --- GENEL AYARLAR: SALON BİLGİLERİNİ GÜNCELLEME METODU ---
    @PostMapping("/ayarlar/genel-bilgiler")
    public String genelBilgileriGuncelle(
            @RequestParam("salonAdi") String salonAdi,
            @RequestParam("telefon") String telefon,
            @RequestParam("adres") String adres,
            RedirectAttributes redirectAttributes) {

        sistemAyarlariService.ayarlariGuncelle(salonAdi, telefon, adres);
        redirectAttributes.addFlashAttribute("basariMesaji", "Genel bilgiler başarıyla güncellendi!");
        return "redirect:/ayarlar";
    }

    // --- SİSTEM TERCİHLERİ GÜNCELLEME METODU ---
    @PostMapping("/ayarlar/sistem-tercihleri")
    public String sistemTercihleriniGuncelle(
            // HTML'de checkbox işaretlenmezse veri gelmez, bu yüzden defaultValue="false" hayat kurtarır
            @RequestParam(value = "uyelikBitisHatirlatici", defaultValue = "false") boolean hatirlatici,
            @RequestParam(value = "otomatikYedekleme", defaultValue = "false") boolean yedekleme,
            RedirectAttributes redirectAttributes) {

        sistemAyarlariService.tercihleriGuncelle(hatirlatici, yedekleme);
        redirectAttributes.addFlashAttribute("basariMesaji", "Sistem tercihleri başarıyla güncellendi!");
        return "redirect:/ayarlar";
    }
}