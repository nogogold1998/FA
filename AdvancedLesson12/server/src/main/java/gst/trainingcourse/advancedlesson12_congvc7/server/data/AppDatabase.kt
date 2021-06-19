package gst.trainingcourse.advancedlesson12_congvc7.server.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee

@Database(entities = [Trainee::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val traineeDao: TraineeDao

    companion object {
        private const val DB_NAME = "app_db"

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .build()
                .also { instance = it }
        }
    }
}
