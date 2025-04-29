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

1. Bu projeyi GitHub üzerinden klonlayın veya ZIP olarak indirin:

2. MySQL veritabanınızı oluşturun:
database.sql dosyasını phpMyAdmin veya MySQL Workbench kullanarak import edin (önerilen phpMyAdmin).

3.DatabaseConnection.java içindeki bağlantı bilgilerini kendi MySQL ayarlarınıza göre güncelleyin: ( önerilenleri yaparsanız gerek yok güncellemeye).

String url = "jdbc:mysql://localhost:3306/ministeam";
String user = "root";
String password = ""; // şifre varsa ekleyin

4. IDE'nizde projeyi açın ve Login_Panel.java dosyasını çalıştırın.

5. iyi kullanımlar dileriz


