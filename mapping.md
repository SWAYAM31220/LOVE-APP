# Mapping of web files to Android components

index.html -> LoginFragment (fragment_login.xml)
home.html -> HomeFragment (fragment_home.xml)
dailyquestions.html -> DailyQuestionsFragment (fragment_dailyquestions.xml)
truthdare.html -> TruthDareFragment (fragment_truthdare.xml)
countdown.html -> CountdownFragment (fragment_countdown.xml)
secret.html -> SecretFragment (fragment_secret.xml)
chat.html -> ChatFragment (fragment_chat.xml)

chat.js -> ChatFragment.kt / FirebaseChatService (comments in code)
script.js -> LoginFragment.kt + DailyQuestionsFragment.kt + SecretFragment.kt
supabaseClient.js -> network/ApiClient.kt + network/api/SupabaseService.kt

Assets: CSS styles mapped to res/values/colors.xml and layouts (approximate)
