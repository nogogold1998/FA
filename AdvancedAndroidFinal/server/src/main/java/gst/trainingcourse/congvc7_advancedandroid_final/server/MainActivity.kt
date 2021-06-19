package gst.trainingcourse.congvc7_advancedandroid_final.server

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

interface LoginContract {
    interface View {
        fun showDialogSuccess()
    }

    interface Presenter {
        fun login(us: String, pw: String)
    }
}

interface RegisterContract {
    interface View {
        fun showDialogSuccess()
    }

    interface Presenter {

    }
}

class P : LoginContract.Presenter {
    override fun login(us: String, pw: String) {

    }
}

class MainActivity : AppCompatActivity(), LoginContract.View {
    private val loginView = object : LoginContract.View {
        override fun showDialogSuccess() {
            TODO("Not yet implemented")
        }
    }

    private val registerView = object : RegisterContract.View {
        override fun showDialogSuccess() {
            TODO("Not yet implemented")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun showDialogSuccess() {
        TODO("Not yet implemented")
    }
}
