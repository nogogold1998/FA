package gst.trainingcourse.advancedlesson12_congvc7.server.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee
import kotlinx.coroutines.flow.Flow

@Dao
interface TraineeDao {
    @Insert
    suspend fun add(vararg trainee: Trainee): LongArray

    @Update
    suspend fun update(vararg trainee: Trainee): Int

    @Query("SELECT * FROM trainee_tb WHERE name LIKE ('%' || :name || '%') ORDER BY id LIMIT 1")
    suspend fun findName(name: String): Trainee?

    @Query("SELECT * FROM trainee_tb ORDER BY (chemistry + physics + math + literary + english) DESC LIMIT 1")
    fun queryBestTrainee(): Flow<Trainee?>
}
