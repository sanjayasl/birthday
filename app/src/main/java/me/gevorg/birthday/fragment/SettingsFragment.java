package me.gevorg.birthday.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import me.gevorg.birthday.R;

/**
 * Fragment for settings.
 *
 * @author Gevorg Harutyunyan.
 */
public class SettingsFragment extends PreferenceFragment {
    /**
     * Fragment created.
     *
     * @param savedInstanceState state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }
}
