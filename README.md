Here’s an updated `README.md` reflecting the current state of your StudyMate app:

---

# StudyMate App

StudyMate is an e-learning mobile app designed specifically for Grade 12 (Matric) students in South Africa. The app provides access to study materials such as textbooks, past question papers, and study guides. Additional features are planned for future updates to enhance the learning experience.

---

## Features

### 1. **eStudy Materials**
   - Access PDFs of textbooks, past exam papers, and study guides.
   - Organized by subject for easy navigation.

### Upcoming Features
   - **Progress Tracking**: Set study goals, track progress, and mark materials as complete.
   - **Discussion Forums**: Engage in academic discussions, ask questions, and share study tips.
   - **Study Timetable Generator**: Create personalized study timetables based on subjects and available study time.
   - **Career Guidance**: Access information on university applications, bursaries, and career paths.

---

## Tech Stack

- **Java**: Main programming language for Android app development.
- **Firebase**: Used for authentication and real-time database management.
- **OneSignal**: Integrated for push notifications.
- **AdMob**: Integrated for showing ads and generating revenue.
- **Remote Config**: Uses a remote JSON configuration to dynamically control app behavior.

---

## Installation

To run this project locally, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/your-repo/studymate.git
    cd studymate
    ```

2. **Open in Android Studio**:
    - Open **Android Studio** and import the project.
    - Sync Gradle files if prompted.

3. **Configure Firebase**:
    - Go to [Firebase Console](https://console.firebase.google.com/), create a new project, and add your Android app.
    - Download the `google-services.json` file and place it in the `app/` directory.
    - Ensure Firebase Authentication and Firestore are enabled in your Firebase project.

4. **OneSignal Configuration**:
    - Sign up on [OneSignal](https://onesignal.com/) and create an app for push notifications.
    - Get your `OneSignal App ID` and replace the value in the `Config.java` file:
      ```java
      public static String ONE_SIGNAL_ID = "YOUR_ONESIGNAL_APP_ID";
      ```

5. **AdMob Configuration**:
    - Create an [AdMob](https://admob.google.com/) account, and get your AdMob ad unit IDs.
    - Replace the default AdMob IDs in the `Config.java` file with your own:
      ```java
      public static final String ADMOB_BANNER_ID = "your-admob-banner-id";
      public static final String ADMOB_INTERSTITIAL_ID = "your-admob-interstitial-id";
      ```

6. **Run the App**:
    - Connect an Android device or start an Android emulator.
    - Click **Run** in Android Studio.

---

## Remote Configuration (Optional)

The app is configured to fetch a remote JSON file that allows dynamic content changes without updating the app. To use this feature, update the remote JSON URL in `Config.java`:

```java
public static final String REMOTE_JSON_URL = "https://yourserver.com/config.json";
```

Ensure the JSON file is properly formatted and hosted at a publicly accessible URL.

---

## Permissions

The app requires the following permissions for proper functioning:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

---

## Contributions

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Open a pull request.



