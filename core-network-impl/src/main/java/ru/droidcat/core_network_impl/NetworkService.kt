package ru.droidcat.core_network_impl

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkService {
    
    suspend fun getApolloClientWithAuthToken(): ApolloClient{
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()

                val builder: Request.Builder = original
                    .newBuilder()
                    .method(original.method, original.body)

                builder.header("Authorization", "Token ${BuildConfig.suggesticApi}")
                return@Interceptor chain.proceed(builder.build())
            }).build()

        return ApolloClient.Builder()
            .serverUrl(BuildConfig.suggesticUrl)
            .okHttpClient(httpClient)
            .build()
    }

    suspend fun getApolloClientWithUserID(sg_user: String): ApolloClient{
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()

                val builder: Request.Builder = original
                    .newBuilder()
                    .method(original.method, original.body)

                builder.header("Authorization", "Token ${BuildConfig.suggesticApi}")
                builder.header("sg-user", sg_user)
                return@Interceptor chain.proceed(builder.build())
            })
            .build()

        return ApolloClient.Builder()
            .serverUrl(BuildConfig.suggesticUrl)
            .okHttpClient(httpClient)
            .build()
    }

    companion object {
        private var mInstance: NetworkService? = null

        fun getInstance(): NetworkService? {
            if (mInstance == null) {
                mInstance = NetworkService()
            }
            return mInstance
        }
    }
    
}