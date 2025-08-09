package com.example.webappconverted.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.StandardCharsets

// Interceptor that serves local asset JSON when Config.MOCK_MODE is true and request matches /mock/
class MockInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val path = req.url().encodedPath()
        if (!com.example.webappconverted.Config.MOCK_MODE) {
            return chain.proceed(req)
        }
        // Map simple endpoints
        val assetName = when {
            path.contains("daily_questions") -> "mock/daily_questions.json"
            path.contains("secrets") -> "mock/secrets.json"
            else -> null
        }
        if (assetName != null) {
            val isStream = context.assets.open(assetName)
            val bytes = isStream.readBytes()
            val body = okhttp3.ResponseBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), bytes)
            return Response.Builder()
                .request(req)
                .protocol(okhttp3.Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(body)
                .build()
        }
        return chain.proceed(req)
    }
}
