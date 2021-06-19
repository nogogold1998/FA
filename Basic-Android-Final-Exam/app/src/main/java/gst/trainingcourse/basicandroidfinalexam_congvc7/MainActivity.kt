package gst.trainingcourse.basicandroidfinalexam_congvc7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.basicandroidfinalexam_congvc7.MainActivityContract.ACTION_DISPLAY_SUM_RESULT
import gst.trainingcourse.basicandroidfinalexam_congvc7.MainActivityContract.EXTRA_SUM_RESULT

object MainActivityContract {
    private const val REF = "gst.trainingcourse.basicandroidfinalexam_congvc7.MainActivity"

    const val ACTION_DISPLAY_SUM_RESULT = "$REF.ACTION_DISPLAY_SUM_RESULT"

    const val EXTRA_SUM_RESULT = "$REF.EXTRA_SUM_RESULT"
}

class MainActivity : AppCompatActivity() {
    private lateinit var sumResultListener: (Long) -> Unit

    private val receiver = object : BroadcastReceiver() {
        val filter = IntentFilter(ACTION_DISPLAY_SUM_RESULT)

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getSumResultFromIntent()?.let(sumResultListener)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        registerReceiver(receiver, receiver.filter)
    }

    private fun setupViews() {
        val startSumServiceButton: Button = findViewById(R.id.main_btn_start_sum_service)
        startSumServiceButton.setOnClickListener {
            val intent = SumService.provideSumIntent(this, 1, 1_000_000)
            startService(intent)
        }
        val builder = AlertDialog.Builder(this)
        sumResultListener = { sum: Long ->
            builder
                .setMessage("Result: $sum")
                .show()
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun Intent.getSumResultFromIntent(): Long? {
        if (action != ACTION_DISPLAY_SUM_RESULT) return null
        return if (hasExtra(EXTRA_SUM_RESULT)) getLongExtra(EXTRA_SUM_RESULT, -1) else null
    }

    companion object {
        fun provideDisplaySumResultIntent(sum: Long): Intent =
            Intent(ACTION_DISPLAY_SUM_RESULT).apply {
                putExtra(EXTRA_SUM_RESULT, sum)
            }
    }
}
