package gst.trainingcourse.lesson3_ex2_congvc7

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivityA : AppCompatActivity() {
    private val list = arrayListOf(
        "Hello!", "Hi!", "Salut!", "Hallo!", "Ciao!", "Ahoj!", "YAH sahs!", "Bog!",
        "Hej!", "Czesc!", "Ní hảo!", "Kon'nichiwa!", "Annyeonghaseyo!", "Shalom!",
        "Sah-wahd-dee-kah!", "Merhaba!", "Huujambo!", "Olá!"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)

        val sendIntent = Intent(this, ActivityB::class.java).apply {
            putStringArrayListExtra(GREETINGS_KEY, list)
        }
        startActivity(sendIntent)
    }

    companion object {
        const val GREETINGS_KEY = "greeting"
    }
}
