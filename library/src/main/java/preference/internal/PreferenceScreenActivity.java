package preference.internal;

import android.os.Bundle;

import preference.WearPreferenceActivity;

public class PreferenceScreenActivity extends WearPreferenceActivity {

    public static final String EXTRA_SCREEN = "preference_screen";

    @Override protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!getIntent().hasExtra(EXTRA_SCREEN)) {
            throw new IllegalStateException("Missing required extra EXTRA_SCREEN");
        }

        final WearPreferenceScreen screen = (WearPreferenceScreen) getIntent().getSerializableExtra(EXTRA_SCREEN);
        setTitle(screen.getTitle(this));
        addPreferencesFromPreferenceScreen(screen);
    }

}
