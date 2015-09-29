package preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.denley.wearpreferenceactivity.R;

public class HeadingListView extends RelativeLayout implements WearableListView.OnScrollListener {

    TextView heading;
    WearableListView list;

    public HeadingListView(final Context context) {
        super(context);
    }

    public HeadingListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadingListView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeadingListView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();

        heading = (TextView) findViewById(R.id.heading);
        list = (WearableListView) findViewById(android.R.id.list);

        list.addOnScrollListener(this);

    }

    @Override public WindowInsets onApplyWindowInsets(final WindowInsets insets) {
        heading.setGravity(insets.isRound() ? Gravity.CENTER_HORIZONTAL : Gravity.START);
        return super.onApplyWindowInsets(insets);
    }

    @Override public void onAbsoluteScrollChange(final int i) {
        heading.setY(Math.min(-i, 0));
    }

    @Override public void onScroll(final int i) {}
    @Override public void onScrollStateChanged(final int i) {}
    @Override public void onCentralPositionChanged(final int i) {}
}
