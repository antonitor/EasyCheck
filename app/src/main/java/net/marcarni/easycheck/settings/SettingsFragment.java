package net.marcarni.easycheck.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.marcarni.easycheck.R;

/**
 * Classe que hereta de PreferenceFragment. Serà l'encarregat de gestionar les preferencies
 * generals de l'aplicació
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.xml.pref_general);
    }

}
