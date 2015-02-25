package preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;

import me.denley.wearpreferenceactivity.R;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class Preference extends View {

    private String key;
    private int icon;
    private String title;
    private String summary;

    public Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes);

        try {
            loadAttributes(a);
            checkRequiredAttributes();
        } finally {
            a.recycle();
        }
    }

    private void loadAttributes(TypedArray array){
        key = array.getString(R.styleable.Preference_key);
        icon = array.getResourceId(R.styleable.Preference_icon, 0);
        title = array.getString(R.styleable.Preference_title);
        summary = array.getString(R.styleable.Preference_summary);
    }

    private void checkRequiredAttributes() {
        if(key==null || key.isEmpty()) {
            throw new IllegalArgumentException("Missing preference key from preference item");
        }
    }

    /**
     * Returns the resource ID for the layout used to display this preference.
     * The layout should have placeholders for the following elements:
     *
     * icon     android.R.id.icon    ImageView
     * title    android.R.id.text1   TextView
     * summary  android.R.id.text2   TextView
     */
    @LayoutRes public int getLayout() {
        return R.layout.wearprefs_preference_item;
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

}
