
# WebAppConverted - Android (Generated)

This Android Studio project was generated from a web app (HTML/CSS/JS) and uses:
- Single-Activity + Navigation Component + Fragments
- ViewBinding, Retrofit, OkHttp, Gson, Coroutines, Glide, Room (optional), Firebase Realtime DB, OneSignal
- Package: com.example.webappconverted
- App name: WebAppConverted

## How to open
1. Download and unzip the project.
2. Open in Android Studio (Electric/Earlier) -> Open project at root folder.
3. Provide Firebase `google-services.json` (already included) or replace as needed.
4. To build locally: `./gradlew assembleDebug`

## Mock mode
Mock JSON files are in `app/src/main/assets/mock/`. A simple Node.js mock server is in `mock_server/server.js`.

## Codemagic
A `codemagic.yaml` is included. Replace encrypted vars with Codemagic environment variables.



## Changes applied
- Implemented SupabaseRepository with OkHttp PUT upload
- Implemented FirebaseChatService to mirror chat.js behavior
- Added OneSignal proxy server in mock_server/send_notification.js
- Added MockInterceptor to serve mock JSON from assets when Config.MOCK_MODE=true
- Polished basic UI theme (colors, shapes). Fonts referenced as system fallbacks; please add Poppins/Quicksand .ttf in res/font/ for exact match.
