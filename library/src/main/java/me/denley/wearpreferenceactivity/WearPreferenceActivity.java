package me.denley.wearpreferenceactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import preference.Preference;
import preference.PreferenceScreen;

public abstract class WearPreferenceActivity extends Activity implements WearableListView.ClickListener {

    LayoutInflater inflater;

    WearableListView list;

    List<Preference> preferences = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);

        setContentView(R.layout.wearprefs_activity_preferences);
        list = (WearableListView) findViewById(android.R.id.list);
    }

    protected void addPreferencesFromResource(@LayoutRes int prefsResId) {
        final View prefsRoot = inflater.inflate(prefsResId, null);

        if(!(prefsRoot instanceof PreferenceScreen)) {
            throw new IllegalArgumentException("Preferences resource must use preference.PreferenceScreen as its root element");
        }

        addPreferencesFromPreferenceScreen((PreferenceScreen)prefsRoot);
    }

    private void addPreferencesFromPreferenceScreen(PreferenceScreen preferenceScreen){
        final List<Preference> loadedPreferences = new ArrayList<>();
        for(int i=0;i<preferenceScreen.getChildCount();i++) {
            loadedPreferences.add(parsePreference(preferenceScreen.getChildAt(i)));
        }
        addPreferences(loadedPreferences);
    }

    private void addPreferences(List<Preference> newPreferences){
        preferences = newPreferences;
        list.setAdapter(new SettingsAdapter());
        list.setClickListener(this);
    }

    private Preference parsePreference(View preferenceView) {
        if(preferenceView instanceof Preference) {
            return (Preference)preferenceView;
        }

        throw new IllegalArgumentException("Preferences layout resource may only contain Views extending preference.Preference");
    }

    @Override public void onClick(WearableListView.ViewHolder viewHolder) {
        final Preference clickedPreference = preferences.get(viewHolder.getPosition());
        clickedPreference.onPreferenceClick();
    }

    @Override public void onTopEmptyRegionClick() {}


    private class SettingsAdapter extends WearableListView.Adapter {
        @Override public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final PreferenceListItemLayout itemView = new PreferenceListItemLayout(WearPreferenceActivity.this);
            return new WearableListView.ViewHolder(itemView);
        }

        @Override public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            final Preference preference = preferences.get(position);
            final PreferenceListItemLayout itemView = (PreferenceListItemLayout)holder.itemView;
            itemView.bindPreference(preference);
            itemView.onNonCenterPosition(false);
        }

        @Override public int getItemCount() {
            return preferences.size();
        }

        @Override public int getItemViewType(int position) {
            return 0;
        }

        @Override public void onViewRecycled(WearableListView.ViewHolder holder) {
            final PreferenceListItemLayout itemView = (PreferenceListItemLayout)holder.itemView;
            itemView.releaseBinding();
        }
    }

}
