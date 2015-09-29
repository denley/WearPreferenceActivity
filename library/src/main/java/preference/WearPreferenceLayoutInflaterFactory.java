package preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

class WearPreferenceLayoutInflaterFactory implements LayoutInflater.Factory {

    @Override
    public View onCreateView(final String name, final Context context, final AttributeSet attrs) {
        if(TextUtils.equals(name, BooleanPreference.class.getSimpleName())) {
            return new BooleanPreference(context, attrs);
        } else if(TextUtils.equals(name, ListPreference.class.getSimpleName())) {
            return new ListPreference(context, attrs);
        } else if(TextUtils.equals(name, PreferenceScreen.class.getSimpleName())) {
            return new PreferenceScreen(context, attrs);
        }
        return null;
    }

}
