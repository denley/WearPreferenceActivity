package preference.internal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WearPreferenceScreen extends WearPreferenceItem {

    private final List<WearPreferenceItem> childItems = new ArrayList<>();

    public WearPreferenceScreen(@NonNull final Context context, @NonNull final AttributeSet attrs) {
        super(context, attrs);
    }

    public void addChild(WearPreferenceItem preference) {
        childItems.add(preference);
    }

    public List<WearPreferenceItem> getChildren() {
        return childItems;
    }

    @Override public void onPreferenceClick(@NonNull final Context context) {
        final Intent intent = new Intent(context, PreferenceScreenActivity.class);
        intent.putExtra(PreferenceScreenActivity.EXTRA_SCREEN, this);

        try {
            context.startActivity(intent);
        }catch(Exception e) {
            Log.d("Cause", "", e.getCause());
            throw e;
        }
    }

}
