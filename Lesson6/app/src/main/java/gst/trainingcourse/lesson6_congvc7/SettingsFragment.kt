package gst.trainingcourse.lesson6_congvc7

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

@Suppress("unused")
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings_preferences, rootKey)
    }
}
