# QiitaClient 📱

An Android application for searching and viewing Qiita articles using Qiita API v2.  
This project was built to learn and practice modern Android development, asynchronous programming, dependency injection (DI), and automated UI testing.

---

## 🛠️ Features

- **Article Search**: Search and view Qiita articles by keywords.
- **Article Detail View**: Open and read selected articles seamlessly via WebView.
- **Automated UI Testing**: Espresso-based UI tests verifying component visibility and layout integrity.

---

## 🏗️ Tech Stack & Libraries

| Category | Technology / Library |
| :--- | :--- |
| **Language** | Kotlin |
| **Dependency Injection** | Dagger 2 / KSP |
| **Networking** | Retrofit 2 + Gson Converter |
| **Reactive / Asynchronous** | RxJava 3 + RxAndroid + RxLifecycle |
| **Image Loading** | Glide |
| **Testing** | JUnit 4 / AndroidX Test / Espresso |

---

## 📂 Package Structure

```text
com.example.qiitaclient/
├── client/          # API Client (Retrofit / ArticleClient)
├── model/           # Data models (Article, User)
├── view/            # Custom Views (ArticleView)
├── MainActivity.kt  # Article Search & List Screen
├── ArticleActivity.kt # Article Detail Screen (WebView)
└── ArticleListAdapter.kt # ListView Adapter
```

---

## 🧪 Running Tests

Includes automated UI tests using Espresso to verify screen elements (search button, input text, list view, progress bar).

```bash
# Run Instrumented UI Tests (Espresso)
./gradlew connectedAndroidTest
```

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
