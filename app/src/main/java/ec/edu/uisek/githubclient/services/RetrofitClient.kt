package ec.edu.uisek.githubclient.services

import android.content.Context
import ec.edu.uisek.githubclient.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ec.edu.uisek.githubclient.services.ApiService
import ec.edu.uisek.githubclient.services.AuthService

object RetrofitClient {
    private const val BASE_URL = "https://api.github.com"
    private var authService: AuthService? = null

    fun init(context: Context) {
        if (authService == null) {
            authService = AuthService(context.applicationContext)
        }
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                // Prioridad: 1. Token de AuthService (login manual), 2. Token de BuildConfig (local.properties)
                val token = authService?.getToken()?.takeIf { it.isNotBlank() } ?: BuildConfig.GITHUB_TOKEN

                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Accept", "application/vnd.github+json")
                    .addHeader("X-GitHub-Api-Version", "2022-11-28")
                    .addHeader("Cache-Control", "no-cache, no-store, must-revalidate")

                if (token.isNotBlank()) {
                    // GitHub requiere "Bearer " para PATs modernos o "token " para antiguos.
                    // "Bearer" es el estándar actual.
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}