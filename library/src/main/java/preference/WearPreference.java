package preference;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import preference.internal.WearPreferenceItem;

/**
 * This is the base class for all other preference types.
 *
 * To create a custom preference type, this class can be subclassed. When doing so,
 * you should override the {@link #onPreferenceClick(Context)} method to provide your own
 * custom click behavior.
 */
public abstract class WearPreference extends WearPreferenceItem {

    private String key;
    private String defaultValue;

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

    /**
     * Returns the default value assigned to this preference item.
     */
    @Nullable public String getDefaultValue() {
        return defaultValue;
    }

}
