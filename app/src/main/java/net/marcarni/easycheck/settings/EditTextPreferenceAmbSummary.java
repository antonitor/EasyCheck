package net.marcarni.easycheck.settings;

import android.content.Context;
import android.preference.EditTextPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Created by Toni on 11/12/2017.
 */

public class EditTextPreferenceAmbSummary extends EditTextPreference{


    public EditTextPreferenceAmbSummary(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EditTextPreferenceAmbSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextPreferenceAmbSummary(Context context) {
        super(context);
    }



    @Override
    public CharSequence getSummary() {
        String text = getText();
        if (TextUtils.isEmpty(text)) {
            return super.getSummary();
        } else {
            CharSequence summary = super.getSummary();
            if (!TextUtils.isEmpty(summary)) {
                return String.format(summary.toString(), text);
            } else {
                return summary;
            }
        }
    }
}
