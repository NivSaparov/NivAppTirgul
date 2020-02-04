package com.example.nivapptirgul.di

import android.app.Application
import android.content.Context
import com.example.nivapptirgul.data.Repository.DataRepository
import com.example.nivapptirgul.data.Repository.DataRepositoryImpl
import com.example.nivapptirgul.data.db.RemindersDatabase
import com.example.nivapptirgul.data.db.entity.ReminderDao
import com.example.nivapptirgul.data.db.network.RemindersNetworkDataSource
import com.example.nivapptirgul.data.db.network.RemindersNetworkDataSourceImpl
import com.example.nivapptirgul.data.provider.DataPreferenceProvider
import com.example.nivapptirgul.data.provider.DataPreferenceProviderImpl
import com.example.nivapptirgul.ui.fragments.ListFragment.ListFragment
import com.example.nivapptirgul.ui.fragments.addReminder.AddEditReminderFragment
import com.example.nivapptirgul.ui.fragments.login.LoginFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Component(modules = [DataRepositoryModule::class])
@Singleton
interface AppComponent {
    fun inject(fragment: ListFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: AddEditReminderFragment)

    // Factory to create instances of the AppComponent
    @Component.Factory
    interface Factory {
        // With @BindsInstance, the context passed in will be available in the graph
        fun create(@BindsInstance context: Context): AppComponent
    }

}

@Module
class DataRepositoryModule {

    @Provides
    fun provideDataRepository(
        dataPreferenceProvider: DataPreferenceProvider,
        reminderDao: ReminderDao,
        remindersNetworkDataSource:
        RemindersNetworkDataSource
    ): DataRepository =
        DataRepositoryImpl(dataPreferenceProvider, reminderDao, remindersNetworkDataSource)

    @Provides
    fun provideReminderNetworkDataSource(): RemindersNetworkDataSource {
        return RemindersNetworkDataSourceImpl()
    }

    @Provides
    fun provideDataPreference(context: Context): DataPreferenceProvider {
        return DataPreferenceProviderImpl(context)
    }


    @Singleton
    @Provides
    fun provideLocalDatabase(context: Context): RemindersDatabase {
        return RemindersDatabase.invoke(context)
    }

    @Provides
    fun provideDatabaseDao(database: RemindersDatabase):ReminderDao{
        return database.getReminderDao()
    }



}
