package gst.trainingcourse.advancedlesson12_congvc7.contract

import android.os.Parcelable
import androidx.annotation.FloatRange
import kotlinx.parcelize.Parcelize

@FloatRange(from = 0.0, to = 10.0)
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
)
annotation class MarkRange

@Parcelize
data class MarkEntry(
    @MarkRange val math: Float,
    @MarkRange val physics: Float,
    @MarkRange val chemistry: Float,
    @MarkRange val english: Float,
    @MarkRange val literary: Float,
) : Parcelable {
    companion object {
        val DEFAULT = MarkEntry(0f, 0f, 0f, 0f, 0f)
    }
}
