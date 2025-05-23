# GameManagementSystem
 Eclipse ile geliştirildi ve XAMPP kullanılarak veri tabanına bağlantı sağlandı

# SeaOfGames - Java Swing Uygulaması

Bu proje, kullanıcıların giriş yapabildiği, kayıt olabildiği ve bir market paneline erişebildiği bir Java Swing arayüz uygulamasıdır. Veritabanı bağlantısı MySQL ile gerçekleştirilmiştir.

## Özellikler

- Kullanıcı girişi (Login)
- Kayıt olma (Register)
- Satın Alma Sistemi
- Kütüphaneye oyun ekleme(Library)
- Geliştirici olup oyun yayınlanabilir(publish game)
- Giriş yapan kullanıcıya özel kütüphane ekranı
- Oyunlar herkesde aynı ama kütüphane kişiye özel
- MySQL veritabanı bağlantısı

## Gereksinimler

- Java JDK 8 veya üzeri 
- MySQL Server (önerilen ben XAMPP kullandım)
- JDBC MySQL Connector (class path'e eklenmeli)
- IDE (Eclipse)

## Kurulum

1. 📥 Projeyi Bilgisayarınıza İndirin
GitHub sayfasına gidin.
Sağ üstte bulunan "Code" butonuna tıklayın.
Açılan pencerede:
"Download ZIP" seçeneğine tıklayıp dosyaları bilgisayarınıza indirin.

2.🖥️ XAMPP Kurulumu ve Başlatılması
https://www.apachefriends.org/index.html adresinden XAMPP programını indirin ve bilgisayarınıza kurun.
Kurulumdan sonra XAMPP’yi açın.
Ana ekranda Apache ve MySQL servislerini bulun.
Apache butonunun sağındaki “Start” butonuna tıklayın.
Ardından MySQL butonunun sağındaki “Start” butonuna tıklayın.
İkisi de “Yeşil” renkte görünüyorsa başarıyla çalışıyor demektir.

3. 🌐 phpMyAdmin’e Giriş
Tarayıcınızı açın ve adres çubuğuna şu adresi yazın:
127.0.0.1
Açılan XAMPP ana sayfasında üst menüden phpMyAdmin seçeneğine tıklayın.

4. 🗄️ Veritabanını İçe Aktarma (Import)
phpMyAdmin açıldığında üst menüden “Yeni” seçeneğine tıklayarak boş bir veritabanı oluşturun.
Örneğin adı: seaOfGames olabilir.
Daha sonra üst menüden “İçe Aktar” (Import) seçeneğine tıklayın.
Açılan sayfada “Dosya Seç” butonuna tıklayıp projedeki database.sql dosyasını seçin.
Sayfanın en altındaki “Git” butonuna tıklayın.
Veritabanı başarıyla yüklendiğinde “İçe aktarma başarılı” mesajı göreceksiniz.

5.🔌 Java Projesinde Veritabanı Bağlantısını Ayarlama
Projenizdeki DatabaseConnection.java dosyasını açın.
Aşağıdaki satırda kendi veritabanı bilgilerinizi girin:
String url = "jdbc:mysql://localhost:3306/seaOfGames(adını ne girdiyseniz size bağlı hocam)";
String username = "root";
String password = ""; // Eğer şifre koyduysanız buraya yazın

Eğer XAMPP’daki MySQL’e özel bir kullanıcı adı veya şifre koyduysanız, username ve password kısmını ona göre değiştirin.

6. 🧪 Projeyi Çalıştırma
Java Eclpse IDE’nizde  projeyi açın.
Login_Panel.java dosyasını sağ tıklayıp “Run” (Çalıştır) seçeneğine tıklayın.
Uygulama açılacak ve giriş ekranı görünecektir.

✅ Son Uyarılar!
Giriş bilgilerini kullanarak test edebilirsiniz.
Eğer veritabanı içeriğinde kullanıcı bilgisi yoksa phpMyAdmin üzerinden veya register butonuna basarak yeni kullanıcı ekleyebilirsiniz.
Steam Store kısmında oyun olmazsa siz publish game butonuna basarak ordan oyun ekleyebilirsiniz hiç bir şekilde phpMyAdmin üstünden eklemek zorunda değilsiniz,
proje kendi kendinin ihtiyaçlarını giderebiliyor.




