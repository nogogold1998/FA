package gst.trainingcourse.advancedlesson9_congvc7.di

import android.content.Context
import androidx.room.Room
import gst.trainingcourse.advancedlesson9_congvc7.data.database.AppDatabase
import gst.trainingcourse.advancedlesson9_congvc7.data.database.TransactionDao

object Injector : InjectorContract() {
    fun getAppDatabase(context: Context = application): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DB_NAME
            )
            .fallbackToDestructiveMigration()
            // VERY IMPORTANT
            .enableMultiInstanceInvalidation()
            .build()

    override val transactionDao: TransactionDao by lazy {
        getAppDatabase().transactionDao
    }
}
