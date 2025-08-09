package com.example.webappconverted.repository

import android.content.Context
import com.example.webappconverted.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.InputStream

// SupabaseRepository: handles Supabase REST operations including file uploads to Storage.
// Original web logic: secret.html upload -> supabase.storage.from('secrets').upload(fileName, imageFile)
// Translated to Android using OkHttp multipart upload to Supabase Storage REST endpoint.
//
// Note: For production, move SUPABASE_ANON_KEY out of code and into secure storage or environment vars.
class SupabaseRepository(private val context: Context) {
    private val client = OkHttpClient()

    // Uploads an image InputStream to Supabase storage 'secrets' bucket with the given fileName.
    // Returns the public URL on success or throws an exception.
    suspend fun uploadSecretImage(fileName: String, inputStream: InputStream, contentType: String = "image/jpeg"): String =
        withContext(Dispatchers.IO) {
            if (Config.MOCK_MODE) {
                // In mock mode, we won't upload. Instead, pretend and return a local asset path
                return@withContext "file:///android_asset/mock/$fileName"
            }

            // Supabase Storage upload endpoint:
            // POST https://<SUPABASE_URL>/storage/v1/object/sign/<bucket>/<path>  (for signed uploads) OR
            // PUT https://<SUPABASE_URL>/storage/v1/object/<bucket>/<path> with api key header
            val url = "${Config.SUPABASE_URL}/storage/v1/object/secrets/$fileName"

            val bytes = inputStream.readBytes()
            val body = RequestBody.create(MediaType.parse(contentType), bytes)

            val request = Request.Builder()
                .url(url)
                .put(body)
                .addHeader("apikey", Config.SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer ${Config.SUPABASE_ANON_KEY}")
                .addHeader("Content-Type", contentType)
                .build()

            client.newCall(request).execute().use { resp ->
                if (!resp.isSuccessful) {
                    throw Exception("Upload failed: ${resp.code()} ${resp.message()}")
                }
                // Construct public URL (Supabase public url format)
                return@withContext "${Config.SUPABASE_URL}/storage/v1/object/public/secrets/$fileName"
            }
        }
}
