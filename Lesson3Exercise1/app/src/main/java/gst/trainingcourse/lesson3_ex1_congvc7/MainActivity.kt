package gst.trainingcourse.lesson3_ex1_congvc7

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestSelfPermissions()
    }

    private fun requestSelfPermissions() {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (!isGranted) {
                    val errorMsg = getString(R.string.error_receive_boot_permission_not_granted)
                    showToast(errorMsg, Toast.LENGTH_LONG)
                }
            }
        when {
            ContextCompat.checkSelfPermission(
                this,
                REQUIRED_PERMISSIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // do nothing
            }
            shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS) -> {
                val msg = getString(R.string.msg_receive_boot_permission_required)
                showToast(msg, Toast.LENGTH_LONG)
            }
            else -> {
                requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
            }
        }
    }

    companion object {
        const val REQUIRED_PERMISSIONS = Manifest.permission.RECEIVE_BOOT_COMPLETED
    }
}

fun Context.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}
