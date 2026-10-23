# Battery Disney Overlay App

Un'app Android che visualizza un personaggio Disney (Topolino) sopra il livello della batteria come overlay permanente sullo schermo.

## Funzionalità
- Mostra un'icona di Topolino con il livello della batteria in percentuale
- L'overlay rimane visibile sopra tutte le altre app
- Il personaggio cambia espressione in base al livello della batteria:
  - **Felice**: Batteria > 50%
  - **Neutro**: Batteria tra 20% e 50%
  - **Triste**: Batteria < 20%

## Permessi Richiesti
- `SYSTEM_ALERT_WINDOW`: Necessario per mostrare l'overlay sopra le altre app

## Come Usare

### 1. Compilare l'APK

```bash
cd BatteryDisneyOverlay
./gradlew assembleDebug
```

L'APK verrà generato in:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 2. Installare l'APK sul dispositivo Android

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 3. Abilitare il permesso SYSTEM_ALERT_WINDOW

1. Aprire l'app "Battery Disney Overlay"
2. Premere "Start Overlay"
3. Verrà richiesto di abilitare il permesso per mostrare sopra le altre app
4. Andare su Impostazioni > App > Battery Disney Overlay > Permessi speciali > Mostra sopra le altre app
5. Abilitare il permesso

### 4. Avviare l'Overlay

- Premere "Start Overlay" per avviare il servizio
- Premere "Stop Overlay" per fermare il servizio

## Struttura del Progetto

```
BatteryDisneyOverlay/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/batterydisneyoverlay/
│   │       │   ├── MainActivity.java
│   │       │   └── BatteryOverlayService.java
│   │       └── res/
│   │           ├── layout/
│   │           │   ├── activity_main.xml
│   │           │   └── overlay_battery.xml
│   │           ├── drawable/
│   │           │   ├── mickey_mouse.xml
│   │           │   ├── mickey_mouse_happy.xml
│   │           │   └── mickey_mouse_sad.xml
│   │           ├── values/
│   │           │   ├── colors.xml
│   │           │   ├── strings.xml
│   │           │   └── styles.xml
│   │           └── mipmap-*/
│   │               └── (icone app)
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── gradle/
    └── wrapper/
        └── gradle-wrapper.properties
```

## Personalizzazione

### Cambiare il Personaggio Disney
Modificare i file XML in `app/src/main/res/drawable/`:
- `mickey_mouse.xml` - Versione neutra
- `mickey_mouse_happy.xml` - Versione felice (batteria > 50%)
- `mickey_mouse_sad.xml` - Versione triste (batteria < 20%)

### Cambiare Posizione dell'Overlay
Modificare i parametri in `BatteryOverlayService.java`:
```java
params.gravity = Gravity.TOP | Gravity.END;
params.x = 100;  // Posizione orizzontale
params.y = 100;  // Posizione verticale
```

### Cambiare Stile del Testo
Modificare `overlay_battery.xml`:
```xml
<TextView
    android:id="@+id/batteryLevel"
    android:textColor="#FFFFFF"
    android:textSize="16sp"
    android:textStyle="bold" />
```

## Requisiti
- Android SDK 23+ (Android 6.0 Marshmallow o superiore)
- Java JDK 8+
- Android Studio o Gradle

## Note
- L'app richiede il permesso `SYSTEM_ALERT_WINDOW` che deve essere concesso manualmente dall'utente
- Su alcuni dispositivi, potrebbe essere necessario abilitare l'opzione "Sviluppatore" e "Debug USB"
- L'overlay non interferisce con il tocco, quindi è possibile interagire normalmente con il dispositivo
