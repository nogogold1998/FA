package gst.trainingcourse.lesson3_ex2_congvc7

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class ActivityB : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val extra = intent.getStringArrayListExtra(ActivityA.GREETINGS_KEY) ?: return
        Log.d("ActivityB", "onCreate: extra=$extra")
    }
}
