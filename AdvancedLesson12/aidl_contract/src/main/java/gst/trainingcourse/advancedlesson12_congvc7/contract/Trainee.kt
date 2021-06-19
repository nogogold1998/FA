package gst.trainingcourse.advancedlesson12_congvc7.contract

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "trainee_tb")
@Parcelize
data class Trainee(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val age: Int,
    val classCode: String,
    @Embedded
    val markEntry: MarkEntry = MarkEntry.DEFAULT
) : Parcelable
