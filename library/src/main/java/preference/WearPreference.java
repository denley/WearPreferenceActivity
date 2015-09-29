package preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;

public abstract class WearPreference {

    protected static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    protected Context context;

    protected String key;
    @DrawableRes protected int icon;
    protected String title;
    protected String summary;

    protected String defaultValue;

    public WearPreference(Context context, AttributeSet attrs) {
        this.context = context;
        obtainAndroidAttributes(attrs);
        obtainCustomAttributes(attrs);
        checkRequiredAttributes();
    }

    private void obtainAndroidAttributes(final AttributeSet attrs) {
        key = loadAndroidStringAttr(attrs, "key");
        icon = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, "icon", 0);
        title = loadAndroidStringAttr(attrs, "title");
        summary = loadAndroidStringAttr(attrs, "summary");
        defaultValue = loadAndroidStringAttr(attrs, "defaultValue");
    }

    protected String loadAndroidStringAttr(final AttributeSet attrs, final String name) {
        String value = attrs.getAttributeValue(NAMESPACE_ANDROID, name);
        if(value != null && value.startsWith("@")) {
            final int resId = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, name, 0);
            value = context.getString(resId);
        }
        return value;
    }

    private void obtainCustomAttributes(final AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Preference, 0, 0);

        if(array.hasValue(R.styleable.Preference_wear_icon)) {
            icon = array.getResourceId(R.styleable.Preference_wear_icon, 0);
        }

        if(array.hasValue(R.styleable.Preference_wear_title)) {
            title = array.getString(R.styleable.Preference_wear_title);
        }

        if(array.hasValue(R.styleable.Preference_wear_summary)) {
            summary = array.getString(R.styleable.Preference_wear_summary);
        }

        array.recycle();
    }

    private void checkRequiredAttributes() {
        if(key==null || key.isEmpty()) {
            throw new IllegalArgumentException("Missing preference key from preference item");
        }
    }

    /**
     * Returns the key for this preference.
     */
    @NonNull public String getKey(){
        return key;
    }

    /**
     * Returns a resource ID for the icon to display with this preference.
     * Subclasses may override this to display an icon based on the value of the preference.
     */
    @DrawableRes public int getIcon() {
        return icon;
    }

    /**
     * Returns the title text to display to the user for this preference.
     *
     * By default, this will return the string defined in the xml attribute for
     * this preference (app:title).
     *
     * Subclasses will typically not override this method but may do so in some cases.
     */
    public CharSequence getTitle() {
        return title;
    }

    /**
     * Returns the summary text to display to the user.
     * Subclasses may override this to display the preference value to the user.
     */
    public CharSequence getSummary() {
        return summary;
    }

    /**
     * Called when the preference item is clicked by the user.
     */
    public abstract void onPreferenceClick();

}
