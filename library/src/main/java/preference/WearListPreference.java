package preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;

public class WearListPreference extends WearPreference {

    protected CharSequence[] entries, entryValues;
    protected boolean useEntryAsSummary = true;

    public WearListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int entriesResId = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, "entries", -1);
        if(entriesResId != -1) {
            entries = context.getResources().getStringArray(entriesResId);
        }

        final int entryValuesResId = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, "entryValues", -1);
        if(entryValuesResId != -1) {
            entryValues = context.getResources().getStringArray(entryValuesResId);
        }

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ListPreference, 0, 0);
        try {
            useEntryAsSummary = array.getBoolean(R.styleable.ListPreference_wear_entryAsSummary, true);
        } finally {
            array.recycle();
        }

        checkRequiredAttributes();
    }

    private void checkRequiredAttributes(){
        if(entries==null || entryValues==null){
            throw new IllegalArgumentException("ListPreference requires 'entries' and 'entryValues' attributes");
        }
    }

    private String getCurrentValue(){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }

    private int getEntryPositionFor(CharSequence value){
        for(int i=0;i<entries.length;i++) {
            if(entryValues[i].toString().equals(value)){
                return i;
            }
        }

        return -1;
    }

    private CharSequence getCurrentEntry(){
        final String currentValue = getCurrentValue();
        final int currentValuePos = getEntryPositionFor(currentValue);

        if(currentValuePos==-1){
            return currentValue;
        }else {
            return entries[currentValuePos];
        }
    }

    @Override public CharSequence getSummary() {
        if(useEntryAsSummary){
            return getCurrentEntry();
        } else {
            return super.getSummary();
        }
    }

    @Override public void onPreferenceClick() {
        final Intent chooseEntryIntent = ListChooserActivity.createIntent(
                context,
                key,
                icon,
                entries,
                entryValues,
                getEntryPositionFor(getCurrentValue())
        );
        context.startActivity(chooseEntryIntent);
    }

}
