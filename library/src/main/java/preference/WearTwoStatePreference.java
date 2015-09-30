package preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;

/**
 * A simple two-state preference type.
 *
 * This is the default parsed preference type for SwitchPreference and CheckBoxPreference.
 *
 * Both the summary and icon values can be specified differently for each state (on/off).
 */
public class WearTwoStatePreference extends WearPreference {

    @Nullable private CharSequence summaryOn, summaryOff;
    @DrawableRes private int iconOn, iconOff;

    private boolean defaultValue;

    public WearTwoStatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        summaryOn = loadAndroidStringAttr(context, attrs, "summaryOn");
        summaryOff = loadAndroidStringAttr(context, attrs, "summaryOff");

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoStatePreference, 0, 0);

        if(array.hasValue(R.styleable.TwoStatePreference_wear_summaryOn)) {
            summaryOn = array.getText(R.styleable.TwoStatePreference_wear_summaryOn);
        }

        if(array.hasValue(R.styleable.TwoStatePreference_wear_summaryOff)) {
            summaryOff = array.getText(R.styleable.TwoStatePreference_wear_summaryOff);
        }

        iconOn = array.getResourceId(R.styleable.TwoStatePreference_wear_iconOn, 0);
        iconOff = array.getResourceId(R.styleable.TwoStatePreference_wear_iconOff, 0);

        array.recycle();

        defaultValue = Boolean.parseBoolean(getDefaultValue());

        if(summaryOn==null){
            summaryOn = context.getString(R.string.default_summary_on);
        }

        if(summaryOff==null) {
            summaryOff = context.getString(R.string.default_summary_off);
        }
    }

    @Override public int getIcon(@NonNull final Context context) {
        // Delegate to super if no specific icons are set
        if(iconOn==0 && iconOff==0) {
            return super.getIcon(context);
        }

        if(getPreferenceValue(context)){
            return iconOn;
        } else {
            return iconOff;
        }
    }

    @Override public CharSequence getSummary(@NonNull final Context context) {
        if(getPreferenceValue(context)){
            return summaryOn;
        } else {
            return summaryOff;
        }
    }

    @Override public void onPreferenceClick(@NonNull final Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        final boolean currentState = preferences.getBoolean(getKey(), defaultValue);
        preferences.edit().putBoolean(getKey(), !currentState).apply();
    }

    private boolean getPreferenceValue(@NonNull final Context context){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(getKey(), defaultValue);
    }

}
