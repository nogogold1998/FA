package gst.trainingcourse.lesson3_ex3_congvc7a

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.buttonStartWithIntent).setOnClickListener {
            val appBPackageName = packageManager.getLaunchIntentForPackage(APP_B_PACKAGE_NAME)
            if (appBPackageName != null) {
                startActivity(appBPackageName)
            }
        }

        findViewById<Button>(R.id.buttonStartWithBroadcast).setOnClickListener {
            val intent = Intent().apply {
                addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                component = ComponentName(APP_B_PACKAGE_NAME,"$APP_B_PACKAGE_NAME.MyReceiver")
            }
            sendBroadcast(intent)
        }
    }

    companion object {
        const val APP_B_PACKAGE_NAME = "gst.trainingcourse.lesson3_ex3_congvc7b"
    }
}
