package com.example.surveyflow.di

import android.content.Context
import androidx.room.Room
import com.example.surveyflow.data.repository.SurveyRepositoryImpl
import com.example.surveyflow.data.source.local.SurveyDao
import com.example.surveyflow.data.source.local.SurveyDatabase
import com.example.surveyflow.data.source.remote.ApiService
import com.example.surveyflow.domain.repository.SurveyRepository
import com.example.surveyflow.domain.usecase.GetSurveyAnswerUseCase
import com.example.surveyflow.domain.usecase.SaveSurveyAnswerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.jsonbin.io/") // your real base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSurveyRepository(apiService: ApiService, dao: SurveyDao): SurveyRepository {
        return SurveyRepositoryImpl(
            apiService,
            dao = dao
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): SurveyDatabase {
        return Room.databaseBuilder(
            appContext,
            SurveyDatabase::class.java,
            "survey_db"
        ).build()
    }

    @Provides
    fun provideSurveyDao(db: SurveyDatabase): SurveyDao = db.surveyDao()

    @Provides
    @Singleton
    fun provideSurveyUseCase(repository: SurveyRepository): SaveSurveyAnswerUseCase =
        SaveSurveyAnswerUseCase(repository)

    @Provides
    @Singleton
    fun provideGetSurveyAnswerUseCase(repository: SurveyRepository): GetSurveyAnswerUseCase =
        GetSurveyAnswerUseCase(repository)


}
