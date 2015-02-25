package preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class PreferenceScreen extends LinearLayout {

    public PreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PreferenceScreen(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
