# SurveyFlow
# 📝 SurveyFlow — Dynamic Survey App (Jetpack Compose + Clean Architecture)

A dynamic survey app built using **Jetpack Compose**, following **Clean Architecture** and **MVVM** pattern. The app loads a JSON-based form dynamically and guides the user through a step-by-step question flow based on JSON logic (referTo / skip). Final answers are saved using **Room Database** and displayed on a separate result screen.

## 📱 Features

- ✅ Dynamic survey UI based on JSON-driven flow
- ✅ Supports multiple question types:
  - Multiple Choice
  - Text Input
  - Number Input
  - Dropdown
  - Checkbox
  - Camera
- ✅ Navigation handled via `referTo.id` and `skip.id`
- ✅ Answer validation via **regex**
- ✅ Offline persistence via **Room Database**
- ✅ Final result screen displays all saved answers
- ✅ Modern **Jetpack Compose** UI
- ✅ **Hilt** for dependency injection
- ✅ Lifecycle-safe architecture with **ViewModel + LiveData**
- ✅ Built with **Kotlin**

---

## 🧱 Tech Stack

| Category               | Library / Framework                |
|------------------------|------------------------------------|
| UI                     | Jetpack Compose, Material 3         |
| Architecture           | MVVM + Clean Architecture          |
| Dependency Injection   | Hilt                               |
| Local DB               | Room                               |
| Network                | Retrofit, Gson                     |
| Serialization          | Kotlinx Serialization              |
| Async / Lifecycle      | Coroutines, LiveData               |
| Navigation             | Navigation-Compose                 |

---

## 🔗 JSON API Used


- Only the `record` array is used.
- Navigation logic is driven by:
  - `referTo.id`: Next question ID
  - `skip.id`: Optional skip target
- Final step ID: `"submit"`

---

## 🚀 Setup Instructions

### 1. Clone the repository
Apk Link : https://drive.google.com/drive/folders/1LNvqsu7c60QBCd22ttYecboYr22aldHE

```bash
git clone https://github.com/your-username/SurveyFlow.git
cd SurveyFlow
com.example.surveyflow
│
├── data
│   ├── model         # DTOs and data classes
│   ├── remote        # Retrofit API service
│   ├── local         # Room entities, DAOs
│   └── repository    # SurveyRepository implementation
│
├── domain
│   ├── usecase       # Business logic
│   └── repository    # Repository interface
│
├── presentation
│   ├── home          # Survey screen UI + ViewModel
│   ├── result        # Result screen UI + ViewModel
│   └── navigation    # Navigation routes
│
├── di                # Hilt modules
└── utils             # Extensions, Response wrappers
