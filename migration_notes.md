# Migration Notes & Manual Steps

1. **Credentials**: This project includes the Firebase `google-services.json` you uploaded and Supabase keys as network call headers. For security, move keys into Codemagic env vars or `local.properties`.
2. **Supabase Storage uploads**: The Android upload flow is stubbed. Implement `SecretsRepository` to perform multipart upload to Supabase Storage REST API (or use Supabase Java library).
3. **OneSignal REST key**: Web code used a REST key to send notifications from client JS. Server-side sending should use secure storage (Codemagic env vars). A mock server is provided.
4. **Chat logic**: The Firebase Realtime DB chat logic (presence, typing, statuses) is conceptually ported; concrete listeners and updates must be implemented in `ChatFragment`/`FirebaseChatService`.
5. **Fonts & styling**: The CSS look-and-feel was approximated using basic layouts and theme values. Add `res/font` and update styles as needed.
6. **Build & Tests**: Project is a functional skeleton. Run `./gradlew assembleDebug` in CI. UI tests are stubs and need device/emulator connected.
