# Spor Salonu Yönetim Sistemi 

Java Spring Boot ve MS SQL Server kullanılarak geliştirilmiş, modern ve dinamik bir **Spor Salonu Yönetim Sistemi** web uygulamasıdır. Bu proje; spor salonundaki üyelerin, üyelik paketlerinin, ödemelerin ve antrenörlerin takibini dijitalleştirmek ve kolaylaştırmak amacıyla tasarlanmıştır.

---

## 🚀 Özellikler

* **Üye Yönetimi:** Yeni üye kaydı oluşturma, üye bilgilerini güncelleme, listeleme ve profil takibi.
* **Abonelik & Paket Yönetimi:** Farklı üyelik paketleri (aylık, 3 aylık, yıllık vb.) tanımlama ve üyelere esnek abonelik atama.
* **Gelişmiş Ödeme Takibi:** Üyelerin ödeme durumlarını, geçmişe dönük ödeme kayıtlarını ve finansal raporları izleme.
* **Antrenör Yönetimi:** Salondaki eğitmenlerin sisteme kaydı ve üye-antrenör eşleştirmelerinin yönetilmesi.
* **Sistem Ayarları & Akıllı Hatırlatıcılar:**
  * **Üyelik Bitiş Hatırlatıcı:** Üyeliği bitmek üzere olan kullanıcıları tespit eden dinamik sistem.
  * **Otomatik Yedekleme:** Sistem verilerinin güvenliği için yedekleme tercihlerinin yönetimi.
* **Dinamik Yönetim Paneli (Dashboard):** Salonun anlık aktif üye sayısını, popüler paketleri ve genel durumunu gösteren grafiksel arayüz.

---

## 🛠 Kullanılan Teknolojiler

* **Backend:** Java 17+, Spring Boot (Spring MVC, Spring Data JPA)
* **Frontend:** Thymeleaf, HTML5, CSS3, Bootstrap
* **Veritabanı:** Microsoft SQL Server (MS SQL)
* **Bağımlılık Yönetimi:** Maven

---

## 📂 Proje Mimarisi

Proje, sürdürülebilir ve temiz kod prensiplerine uygun olarak **Katmanlı Mimari (Layered Architecture)** ile geliştirilmiştir:

* 📦 **`model/`** -> Veritabanı tablolarını temsil eden Entity sınıfları (`Uye`, `Abonelik`, `Odeme`, `Antrenor`, `Paket`, `SistemAyarlari`).
* 📦 **`repository/`** -> Spring Data JPA kullanarak veritabanı CRUD operasyonlarını yürüten katman.
* 📦 **`service/`** -> İş mantığının (Business Logic) yazıldığı ve yönetildiği katman.
* 📦 **`controller/`** -> Web arayüzü (Thymeleaf) ve API isteklerini karşılayan yönlendirici katman.

---

## 📦 Kurulum ve Çalıştırma

Uygulamayı yerel bilgisayarınızda ayağa kaldırmak için aşağıdaki adımları izleyebilirsiniz:

### 1. Projeyi Klonlayın
```bash
git clone [https://github.com/alpaslanertan0-ux/sporsalonu.git](https://github.com/alpaslanertan0-ux/sporsalonu.git)
cd sporsalonu
