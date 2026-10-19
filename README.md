# Your Dictionary

Android sözlük uygulaması. Kelime ekleme, görüntüleme ve ses kaydı ile telaffuz özelliği sunar. Firebase (Realtime Database, Auth) ve Google Ads (AdMob) entegrasyonu içerir.

## Özellikler

- Kelime ekleme ve listeleme
- Ses kaydı ile telaffuz
- Firebase veritabanı ve kimlik doğrulama
- AdMob reklam entegrasyonu

## Gereksinimler

- Android Studio
- JDK 8+
- Android SDK (compileSdkVersion 28)
- Gradle 3.3.0+

## Kurulum

1. Repoyu klonlayın:
   ```bash
   git clone <repo-url>
   cd My-PRECIOUS-master
   ```
2. Android Studio ile projeyi açın.
3. `google-services.json` dosyasını `app/` dizinine ekleyin (Firebase konsolundan).
4. Gradle sync yapın ve çalıştırın.

## Yapı

- `app/src/main/java/com/powerbros/yourdictionary` — kaynak kod
- `app/src/main/res` — kaynaklar
- `app/src/main/AndroidManifest.xml` — manifest

