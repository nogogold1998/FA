package gst.trainingcourse.lesson6_congvc7

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView: TextView = findViewById(R.id.textView)
        findViewById<Button>(R.id.button).setOnClickListener {
            AlertDialog.Builder(this)
                .setMessage(R.string.choose_yes_no)
                .setPositiveButton(R.string.yes) { _, _ ->
                    textView.setText(R.string.chose_yes)
                }
                .setNegativeButton(R.string.no) { _, _ ->
                    textView.setText(R.string.chose_no)
                }
                .create()
                .show()
        }

        lifecycleScope.launchWhenStarted {
            findViewById<Toolbar>(R.id.toolbarSettings).setNavigationOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    R.string.up_button_clicked,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        listenToSharedPreferences()
    }

    private fun listenToSharedPreferences() = lifecycleScope.launchWhenStarted {
        val settingsFragment = run {
            val string = getString(R.string.settings_fragment)
            supportFragmentManager.findFragmentByTag(string) as? SettingsFragment
                ?: return@launchWhenStarted
        }
        settingsFragment.run {
            val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
                Log.d("MainActivity", "listenToSharedPreferences: $preference, $newValue")
                true
            }

            setOf(
                R.string.preference_checkbox_key,
                R.string.preference_switch_preference,
                R.string.preference_dropdown_preference,
                R.string.preference_list_preference,
            ).forEach {
                findPreference<Preference>(getString(it))?.onPreferenceChangeListener = listener
            }
        }
    }
}
