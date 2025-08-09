package com.example.webappconverted

object Config {
    // Toggle mock mode. In production, set via BuildConfig or env vars.
    const val MOCK_MODE = true

    // Supabase settings - prefer providing via secure env or local.properties in production
    const val SUPABASE_URL = "https://zckpocoedkulokcpxvqu.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inpja3BvY29lZGt1bG9rY3B4dnF1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTQwNzE4ODMsImV4cCI6MjA2OTY0Nzg4M30.3HdOMxvIqSnkPFxpUAOrCLXIba3G9Wo95VMxSE_BqUs"
}
