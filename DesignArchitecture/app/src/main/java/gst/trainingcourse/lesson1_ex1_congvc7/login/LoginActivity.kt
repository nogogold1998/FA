package gst.trainingcourse.lesson1_ex1_congvc7.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson1_ex1_congvc7.HomeActivity
import gst.trainingcourse.lesson1_ex1_congvc7.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val view = object : LoginContract.View {
        override fun toggleProgressbarVisibility(isShow: Boolean) {
            binding.progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
        }

        override fun notify(request: LoginContract.View.NotifyRequest) {
            when (request) {
                is LoginContract.View.NotifyRequest.ShowErrDialog -> {
                    AlertDialog.Builder(this@LoginActivity)
                        .setMessage(request.id)
                        .setNegativeButton(android.R.string.ok, null)
                        .setCancelable(true)
                        .show()
                }
                is LoginContract.View.NotifyRequest.ToastStringRes -> {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(request.id),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        override fun navigate(request: LoginContract.View.NavigateRequest) {
            startActivity(HomeActivity.provideDefaultIntent(this@LoginActivity))
        }

        override fun updateUsername(username: String) {
            val editable = binding.usernameEditText.text
            if (editable.toString() == username) return
            editable.run {
                clear()
                append(username)
            }
        }

        override fun updateRemember(remember: Boolean) {
            if (binding.checkBox.isChecked == remember) return
            binding.checkBox.isChecked = remember
        }

        override fun updatePassword(password: String) {
            val editable = binding.passwordEditText.text
            if (editable.toString() == password) return
            editable.run {
                clear()
                append(password)
            }
        }
    }

    private val presenter: LoginContract.Presenter = LoginPresenterImpl(view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.toggleRemember(isChecked)
        }
        binding.usernameEditText.addTextChangedListener {
            val str = it?.toString() ?: ""
            presenter.changeUsername(str)
        }
        binding.passwordEditText.addTextChangedListener {
            val str = it?.toString() ?: ""
            presenter.changePassword(str)
        }
        binding.button.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                presenter.login()
            }
        }
    }
}
