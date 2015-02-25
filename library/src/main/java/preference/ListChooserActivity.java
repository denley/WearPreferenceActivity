package preference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.wearable.view.WearableListView;
import android.view.ViewGroup;

import me.denley.wearpreferenceactivity.R;

public class ListChooserActivity extends Activity implements WearableListView.ClickListener {

    public static Intent createIntent(Context context, String key, int icon,
                                      CharSequence[] entries, CharSequence[] entryValues,
                                      int currentValue){

        final Intent launcherIntent = new Intent(context, ListChooserActivity.class);
        launcherIntent.putExtra(EXTRA_PREF_KEY, key);
        launcherIntent.putExtra(EXTRA_ICON, icon);
        launcherIntent.putExtra(EXTRA_ENTRIES, entries);
        launcherIntent.putExtra(EXTRA_ENTRY_VALUES, entryValues);
        launcherIntent.putExtra(EXTRA_CURRENT_VALUE, currentValue);
        return launcherIntent;
    }

    public static final String EXTRA_PREF_KEY = "key";
    public static final String EXTRA_ICON = "icon";
    public static final String EXTRA_ENTRIES = "entries";
    public static final String EXTRA_ENTRY_VALUES = "values";
    public static final String EXTRA_CURRENT_VALUE = "current_value";



    String key;
    @DrawableRes private int icon;
    CharSequence[] entries;
    CharSequence[] values;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_list);

        loadIntentExtras();
        checkRequiredExtras();

        final WearableListView list = (WearableListView) findViewById(android.R.id.list);
        list.setAdapter(new PreferenceEntriesAdapter());
        list.scrollToPosition(getIntent().getIntExtra(EXTRA_CURRENT_VALUE, 0));
        list.setClickListener(this);
    }

    private void loadIntentExtras(){
        key = getIntent().getStringExtra(EXTRA_PREF_KEY);
        icon = getIntent().getIntExtra(EXTRA_ICON, 0);
        entries = getIntent().getCharSequenceArrayExtra(EXTRA_ENTRIES);
        values = getIntent().getCharSequenceArrayExtra(EXTRA_ENTRY_VALUES);
    }

    private void checkRequiredExtras(){
        if(key==null || key.isEmpty()){
            throw new IllegalArgumentException("Missing required extra EXTRA_PREF_KEY (preference key)");
        }
        if(entries==null){
            throw new IllegalArgumentException("Missing required extra EXTRA_ENTRIES (entry names)");
        }
        if(values==null){
            throw new IllegalArgumentException("Missing required extra EXTRA_ENTRY_VALUES (preference entry values)");
        }
    }

    @Override public void onClick(WearableListView.ViewHolder viewHolder) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(key, values[viewHolder.getPosition()].toString()).apply();

        finish();
    }

    @Override public void onTopEmptyRegionClick() { }


    private class PreferenceEntriesAdapter extends WearableListView.Adapter {

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final PreferenceListItemLayout itemView = new PreferenceListItemLayout(ListChooserActivity.this);
            return new WearableListView.ViewHolder(itemView);
        }

        @Override public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            final PreferenceListItemLayout itemView = (PreferenceListItemLayout)holder.itemView;
            itemView.bindView(icon, entries[position], null);
            itemView.onNonCenterPosition(false);
        }

        @Override public int getItemCount() {
            return entries.length;
        }
    }

}
