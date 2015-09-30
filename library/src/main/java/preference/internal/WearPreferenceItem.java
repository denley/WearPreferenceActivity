package preference.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import java.io.Serializable;

import me.denley.wearpreferenceactivity.R;

public abstract class WearPreferenceItem implements Serializable {

    /** The Android namespace. This can be used when loading android attributes from an AttributeSet */
    protected static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    @DrawableRes private int icon;
    private String title;
    private String summary;

    public WearPreferenceItem(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        obtainAndroidAttributes(context, attrs);
        obtainCustomAttributes(context, attrs);
    }

    private void obtainAndroidAttributes(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        icon = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, "icon", 0);
        title = loadAndroidStringAttr(context, attrs, "title");
        summary = loadAndroidStringAttr(context, attrs, "summary");
    }

    private void obtainCustomAttributes(@NonNull final Context context, @NonNull final AttributeSet attrs) {
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

    protected String loadAndroidStringAttr(@NonNull final Context context, @NonNull final AttributeSet attrs, @NonNull final String name) {
        String value = attrs.getAttributeValue(NAMESPACE_ANDROID, name);
        if(value != null && value.startsWith("@")) {
            final int resId = attrs.getAttributeResourceValue(NAMESPACE_ANDROID, name, 0);
            value = context.getString(resId);
        }
        return value;
    }

    /**
     * Called when the preference item is clicked by the user.
     */
    public abstract void onPreferenceClick(@NonNull final Context context);

    /**
     * Returns a resource ID for the icon to display with this preference.
     * Subclasses may override this to display an icon based on the value of the preference.
     */
    @DrawableRes public int getIcon(@NonNull final Context context) {
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
    public CharSequence getTitle(@NonNull final Context context) {
        return title;
    }

    /**
     * Returns the summary text to display to the user.
     * Subclasses may override this to display the preference value to the user.
     */
    public CharSequence getSummary(@NonNull final Context context) {
        return summary;
    }


}
