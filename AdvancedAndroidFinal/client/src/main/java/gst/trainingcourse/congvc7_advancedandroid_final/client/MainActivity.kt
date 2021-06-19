package gst.trainingcourse.congvc7_advancedandroid_final.client

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.congvc7_advancedandroid_final.api.IServer
import gst.trainingcourse.congvc7_advancedandroid_final.client.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var server: IServer? = null

    private val serverConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.d(TAG, "onServiceConnected: ")
            server = IServer.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            server = null
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()

        bindServer()
    }

    private fun bindServer() {
        val intent = Intent().apply {
            component = ComponentName(
                "gst.trainingcourse.congvc7_advancedandroid_final.server",
                "gst.trainingcourse.congvc7_advancedandroid_final.server.service.ServerService"
            )
        }
        bindService(intent, serverConnection, Service.BIND_AUTO_CREATE)
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonCallAidl.setOnClickListener {
            callAidl()
        }
        binding.btnS.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun callAidl() {
        val server = server
        if (server == null) {
            Toast.makeText(
                this,
                getString(R.string.server_not_connected),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        lifecycleScope.launch {
            val result = withContext(Dispatchers.Default) { server.run() }
            binding.textAidlResult.text = getString(R.string.format_aidl_result, result)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // TODO
        // unbindService(serverConnection)
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
