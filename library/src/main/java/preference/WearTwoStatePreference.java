package preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;

public class WearTwoStatePreference extends WearPreference {

    protected String summaryOn, summaryOff;
    @DrawableRes protected int iconOn, iconOff;

    protected boolean defaultValue;

    public WearTwoStatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        summaryOn = loadAndroidStringAttr(attrs, "summaryOn");
        summaryOff = loadAndroidStringAttr(attrs, "summaryOff");

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TwoStatePreference, 0, 0);

        if(array.hasValue(R.styleable.TwoStatePreference_wear_summaryOn)) {
            summaryOn = array.getString(R.styleable.TwoStatePreference_wear_summaryOn);
        }

        if(array.hasValue(R.styleable.TwoStatePreference_wear_summaryOff)) {
            summaryOff = array.getString(R.styleable.TwoStatePreference_wear_summaryOff);
        }

        iconOn = array.getResourceId(R.styleable.TwoStatePreference_wear_iconOn, 0);
        iconOff = array.getResourceId(R.styleable.TwoStatePreference_wear_iconOff, 0);

        array.recycle();

        defaultValue = Boolean.parseBoolean(super.defaultValue);

        if(summaryOn==null){
            summaryOn = context.getString(R.string.default_summary_on);
        }

        if(summaryOff==null) {
            summaryOff = context.getString(R.string.default_summary_off);
        }
    }

    @Override public int getIcon() {
        // Delegate to super if no specific icons are set
        if(iconOn==0 && iconOff==0) {
            return super.getIcon();
        }

        if(getPreferenceValue()){
            return iconOn;
        } else {
            return iconOff;
        }
    }

    @Override public CharSequence getSummary() {
        if(getPreferenceValue()){
            return summaryOn;
        } else {
            return summaryOff;
        }
    }

    @Override public void onPreferenceClick() {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        final boolean currentState = preferences.getBoolean(key, defaultValue);
        preferences.edit().putBoolean(key, !currentState).apply();
    }

    private boolean getPreferenceValue(){
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }

}
