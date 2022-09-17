package ru.droidcat.core_network_impl.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.droidcat.core_network_api.NetworkRepository
import ru.droidcat.core_network_impl.BuildConfig
import ru.droidcat.core_network_impl.NetworkRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseOptions(): FirebaseOptions {
        return FirebaseOptions.Builder()
            .setProjectId(BuildConfig.firebaseProjectId)
            .setApplicationId(BuildConfig.firebaseApplicationId)
            .setApiKey(BuildConfig.firebaseApiKey)
            .setDatabaseUrl(BuildConfig.firebaseDBUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideFirebaseApp(
        @ApplicationContext
        appContext: Context,
        options: FirebaseOptions
    ): FirebaseApp {
        val app = FirebaseApp.initializeApp(appContext, options)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        return app
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(
        firebaseApp: FirebaseApp
    ): FirebaseAuth {
        return Firebase.auth(firebaseApp)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(
        firebaseApp: FirebaseApp
    ): FirebaseDatabase {
        return Firebase.database(firebaseApp)
    }
}

@Module(includes = [NetworkModule.BindsModule::class])
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {

        @Binds
        @Singleton
        fun bindNetworkRepository(impl: NetworkRepositoryImpl): NetworkRepository
    }
}