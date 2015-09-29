package preference;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

class WearPreferenceScreen {

    private final List<WearPreference> preferences = new ArrayList<>();

    public WearPreferenceScreen(@NonNull final Context context, @NonNull final AttributeSet attrs) {

    }

    void addPreference(WearPreference preference) {
        preferences.add(preference);
    }

    List<WearPreference> getPreferences() {
        return preferences;
    }

}
