package com.example.typinganalyze

import android.app.Application
import androidx.room.Room
import com.example.typinganalyze.typing.TypingViewModel
import com.example.typinganalyze.typing.data.AppDatabase
import com.example.typinganalyze.typing.usecase.FindUnreleasedEventUseCase
import com.example.typinganalyze.typing.usecase.GetTextToTypeUseCase
import com.example.typinganalyze.typing.usecase.SaveKeyEventUseCase
import com.example.typinganalyze.typing.usecase.WPMCalculator
import com.example.typinganalyze.typing.usecase.WPMCalculatorIml
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private val appModule = module {

        single<AppDatabase> {
            Room.databaseBuilder(
                get(),
                AppDatabase::class.java, "app_db"
            ).build()
        }
        single {
            get<AppDatabase>().keyEventAnalyticsDao()
        }

        factory<WPMCalculator> { WPMCalculatorIml(get(), Dispatchers.IO) }
        factory<GetTextToTypeUseCase> { GetTextToTypeUseCase() }
        factory { SaveKeyEventUseCase(get(), Dispatchers.IO) }
        factory { FindUnreleasedEventUseCase(get(), Dispatchers.IO) }

        viewModel { MainViewModel() }
        viewModel { (username: String) ->
            TypingViewModel(
                username = username,
                getTextToTypeUseCase = get(),
                saveKeyEventUseCase = get(),
                findUnreleasedEventUseCase = get(),
                wpmCalculator = get()
            )
        }
    }
}