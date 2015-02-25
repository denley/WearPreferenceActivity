package preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import me.denley.wearpreferenceactivity.R;

public class ListPreference extends Preference {

    protected final CharSequence[] entries, entryValues;

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ListPreference, 0, 0);
        try {
            entries = array.getTextArray(R.styleable.ListPreference_entries);
            entryValues = array.getTextArray(R.styleable.ListPreference_entryValues);
            checkRequiredAttributes();
        } finally {
            array.recycle();
        }
    }

    private void checkRequiredAttributes(){
        if(entries==null || entryValues==null){
            throw new IllegalArgumentException("ListPreference requires 'entries' and 'entryValues' attributes");
        }
    }

}
