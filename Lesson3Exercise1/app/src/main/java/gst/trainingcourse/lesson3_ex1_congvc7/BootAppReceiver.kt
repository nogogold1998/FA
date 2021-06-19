package gst.trainingcourse.lesson3_ex1_congvc7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 *  Here's a catch, there's no ways to start app with a [BroadcastReceiver] declared in manifests
 *  stand alone to receive android.intent.action.SCREEN_ON. It must be a registered
 *  [BroadcastReceiver].
 *  So my solution here is that we start a foreground service first and then register a
 *  [BroadcastReceiver] listens to android.intent.action.SCREEN_ON. But using service will be out
 *  of range in this exercise so i didn't implement the solution.
 *  For more details see https://d.android.com/reference/android/content/Intent#ACTION_SCREEN_ON
 */
class BootAppReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BootAppReceiver", "onReceive: ")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
