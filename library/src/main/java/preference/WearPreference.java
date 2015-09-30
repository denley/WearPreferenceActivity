package preference;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

public abstract class WearPreference extends WearPreferenceItem {

    protected String key;
    protected String defaultValue;

    public WearPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        key = loadAndroidStringAttr(context, attrs, "key");
        defaultValue = loadAndroidStringAttr(context, attrs, "defaultValue");
        checkRequiredAttributes();
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

}
