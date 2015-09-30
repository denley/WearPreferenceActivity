package preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;
import preference.internal.ListChooserActivity;

/**
 * A simple list preference type.
 *
 * This is the default parsed preference type for ListPreference.
 *
 * Both the summary and icon values can be specified differently for each item in the set of entries
 */
public class WearListPreference extends WearPreference {

    private CharSequence[] entries, entryValues;
    private int[] icons;
    private boolean useEntryAsSummary = true;

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

            if(array.hasValue(R.styleable.ListPreference_wear_entryIcons)) {
                final int resId = array.getResourceId(R.styleable.ListPreference_wear_entryIcons, 0);
                final TypedArray typed = context.getResources().obtainTypedArray(resId);
                icons = new int[typed.length()];
                for(int i = 0; i < typed.length(); i++) {
                    icons[i] = typed.getResourceId(i, 0);
                }
                typed.recycle();
            }
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

    private String getCurrentValue(@NonNull final Context context){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(getKey(), getDefaultValue());
    }

    private int getEntryPositionFor(CharSequence value){
        for(int i=0;i<entries.length;i++) {
            if(entryValues[i].toString().equals(value)){
                return i;
            }
        }

        return -1;
    }

    private CharSequence getCurrentEntry(@NonNull final Context context) {
        final String currentValue = getCurrentValue(context);
        final int currentValuePos = getEntryPositionFor(currentValue);

        if(currentValuePos==-1){
            return currentValue;
        }else {
            return entries[currentValuePos];
        }
    }

    @Override public CharSequence getSummary(@NonNull final Context context) {
        if(useEntryAsSummary){
            return getCurrentEntry(context);
        } else {
            return super.getSummary(context);
        }
    }

    @Override public int getIcon(@NonNull final Context context) {
        if(icons != null) {
            return icons[getEntryPositionFor(getCurrentValue(context))];
        } else {
            return super.getIcon(context);
        }
    }

    @Override public void onPreferenceClick(@NonNull final Context context) {
        final Intent chooseEntryIntent = ListChooserActivity.createIntent(
                context,
                getKey(),
                super.getTitle(context).toString(),
                super.getIcon(context),
                entries,
                entryValues,
                icons,
                getEntryPositionFor(getCurrentValue(context))
        );
        context.startActivity(chooseEntryIntent);
    }

}
