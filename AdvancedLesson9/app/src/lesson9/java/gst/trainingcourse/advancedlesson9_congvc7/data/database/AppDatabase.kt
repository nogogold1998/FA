package gst.trainingcourse.advancedlesson9_congvc7.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: RoomTransactionDao

    companion object {
        const val DB_NAME = "database-name"
    }
}
