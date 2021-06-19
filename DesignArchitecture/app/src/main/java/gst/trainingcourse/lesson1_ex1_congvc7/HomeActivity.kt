package gst.trainingcourse.lesson1_ex1_congvc7

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    companion object {
        fun provideDefaultIntent(context: Context): Intent =
            Intent(context, HomeActivity::class.java)
    }
}
