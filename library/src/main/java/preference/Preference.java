package preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import me.denley.wearpreferenceactivity.R;

public class Preference extends View {

    protected final String key;
    @DrawableRes protected final int icon;
    protected final String title;
    protected final String summary;

    protected final String defaultValue;

    public Preference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Preference, 0, 0);
        try {
            key = array.getString(R.styleable.Preference_key);
            icon = array.getResourceId(R.styleable.Preference_icon, 0);
            title = array.getString(R.styleable.Preference_title);
            summary = array.getString(R.styleable.Preference_summary);
            defaultValue = array.getString(R.styleable.Preference_defaultValue);

            checkRequiredAttributes();
        } finally {
            array.recycle();
        }
    }

    private void checkRequiredAttributes() {
        if(key==null || key.isEmpty()) {
            throw new IllegalArgumentException("Missing preference key from preference item");
        }
    }

    @Override protected void onAttachedToWindow() {
        throw new IllegalStateException("Preferences should not be attached to a UI. They are for use with WearPrefrenceActivity only.");
    }

    public String getKey(){
        return key;
    }

    @DrawableRes public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public void onPreferenceClick() {
        // Default implementation does nothing. This is a stub for overriding.
    }

}
