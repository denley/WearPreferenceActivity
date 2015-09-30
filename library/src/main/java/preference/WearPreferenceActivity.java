package preference;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.denley.wearpreferenceactivity.R;
import preference.internal.ListItemLayout;
import preference.internal.TitledWearActivity;
import preference.internal.WearPreferenceItem;
import preference.internal.WearPreferenceScreen;

/**
 * An Activity that will show preferences items in a list.
 *
 * Users should subclass this class and call {@link #addPreferencesFromResource(int)} to populate the list of preference items.
 */
public abstract class WearPreferenceActivity extends TitledWearActivity {

    private WearableListView list;
    private List<WearPreferenceItem> preferences = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.preference_list);
        list = (WearableListView) findViewById(android.R.id.list);
    }

    /**
     * Inflates the preferences from the given resource and displays them on this page.
     * @param prefsResId    The resource ID of the preferences xml file.
     */
    protected void addPreferencesFromResource(@XmlRes int prefsResId) {
        addPreferencesFromResource(prefsResId, new XmlPreferenceParser());
    }

    /**
     * Inflates the preferences from the given resource and displays them on this page.
     * @param prefsResId    The resource ID of the preferences xml file.
     * @param parser        A parser used to parse custom preference types
     */
    protected void addPreferencesFromResource(@XmlRes int prefsResId, @NonNull XmlPreferenceParser parser) {
        final WearPreferenceScreen prefsRoot = parser.parse(this, prefsResId);
        addPreferencesFromPreferenceScreen(prefsRoot);
    }

    /** DO NOT USE - For internal use only */
    protected void addPreferencesFromPreferenceScreen(WearPreferenceScreen preferenceScreen){
        addPreferences(preferenceScreen.getChildren());
    }

    private void addPreferences(List<WearPreferenceItem> newPreferences){
        preferences = newPreferences;
        list.setAdapter(new SettingsAdapter());

        list.setClickListener(new WearableListView.ClickListener() {
            @Override public void onClick(final WearableListView.ViewHolder viewHolder) {
                final WearPreferenceItem clickedItem = preferences.get(viewHolder.getPosition());
                clickedItem.onPreferenceClick(WearPreferenceActivity.this);
            }
            @Override public void onTopEmptyRegionClick() {}
        });
    }

    private class SettingsAdapter extends WearableListView.Adapter {
        @Override public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ListItemLayout itemView = new ListItemLayout(WearPreferenceActivity.this);
            return new WearableListView.ViewHolder(itemView);
        }

        @Override public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            final WearPreferenceItem preference = preferences.get(position);
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.bindPreference(preference);
            itemView.onNonCenterPosition(false);
        }

        @Override public int getItemCount() {
            return preferences.size();
        }

        @Override public void onViewRecycled(WearableListView.ViewHolder holder) {
            final ListItemLayout itemView = (ListItemLayout)holder.itemView;
            itemView.releaseBinding();
        }
    }

}
