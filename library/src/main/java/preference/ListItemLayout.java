/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package preference;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.denley.wearpreferenceactivity.R;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class ListItemLayout extends FrameLayout implements WearableListView.OnCenterProximityListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final float ALPHA_NON_CENTER = 0.6f;

    private static final float CIRCLE_RADIUS_CENTER_DP = 24;
    private static final float CIRCLE_RADIUS_NON_CENTER_DP = 16;

    private static final long ANIMATION_DURATION = 100;


    private SharedPreferences preferences;

    @Nullable private Preference bindedPreference = null;

    @Nullable private CircledImageView icon;
    @Nullable private TextView title, summary;

    private float circleRadiusCenter, circleRadiusNonCenter;
    private int circleColorCenter = Color.TRANSPARENT;
    private int circleColorNonCenter = Color.TRANSPARENT;

    private CircleSizeAnimation circleGrowAnimation = null;
    private CircleSizeAnimation circleShrinkAnimation = null;

    public ListItemLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ListItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ListItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(@NonNull Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        initLayout(context);
        initCircle(context, attrs, defStyleAttr, defStyleRes);
        initAnimations();
    }

    private void initLayout(@NonNull Context context) {
        LayoutInflater.from(context).inflate(R.layout.preference_item, this);
        icon = (CircledImageView) findViewById(android.R.id.icon);
        title = (TextView) findViewById(android.R.id.title);
        summary = (TextView) findViewById(android.R.id.summary);
    }

    private void initCircle(@NonNull Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if(icon!=null) {
            final float density = context.getResources().getDisplayMetrics().density;
            circleRadiusCenter = density * CIRCLE_RADIUS_CENTER_DP;
            circleRadiusNonCenter = density * CIRCLE_RADIUS_NON_CENTER_DP;

            if(attrs!=null) {
                final TypedArray array = context.obtainStyledAttributes(
                        attrs, R.styleable.ListItemLayout,
                        defStyleAttr, defStyleRes);

                circleRadiusCenter = array.getDimension(R.styleable.ListItemLayout_icon_circle_radius_focused,
                        density * CIRCLE_RADIUS_CENTER_DP);
                circleRadiusNonCenter = array.getDimension(R.styleable.ListItemLayout_icon_circle_radius,
                        density * CIRCLE_RADIUS_NON_CENTER_DP);
                final float borderWidth = array.getDimension(R.styleable.ListItemLayout_icon_circle_border_width, 0);
                circleColorCenter = array.getColor(R.styleable.ListItemLayout_icon_circle_color, Color.TRANSPARENT);
                circleColorNonCenter = array.getColor(R.styleable.ListItemLayout_icon_circle_color, Color.TRANSPARENT);
                final int borderColor = array.getColor(R.styleable.ListItemLayout_icon_circle_border_color, Color.TRANSPARENT);

                array.recycle();

                icon.setCircleBorderColor(borderColor);
                icon.setCircleBorderWidth(borderWidth);
            }

            icon.setCircleColor(circleColorCenter);
            icon.setCircleRadiusPressed(circleRadiusCenter);
            circleGrowAnimation = new CircleSizeAnimation(icon, circleRadiusCenter);
            circleShrinkAnimation = new CircleSizeAnimation(icon, circleRadiusNonCenter);
        }
    }

    private void initAnimations() {
        circleGrowAnimation.setDuration(ANIMATION_DURATION);
        circleShrinkAnimation.setDuration(ANIMATION_DURATION);
    }

    public void bindPreference(@NonNull final Preference preference){
        bindedPreference = preference;
        bindPreferenceView(preference);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void releaseBinding(){
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        bindedPreference = null;
    }

    private void bindPreferenceView(@NonNull final Preference preference){
        bindView(preference.getIcon(), preference.getTitle(), preference.getSummary());
    }

    public void bindView(@DrawableRes final int iconId,
                         @Nullable final CharSequence titleText,
                         @Nullable final CharSequence summaryText) {
        if(icon !=null) {
            icon.setImageResource(iconId);
        }
        if(title!=null) {
            title.setText(titleText);
        }
        if(summary!=null) {
            if(summaryText==null) {
                summary.setVisibility(View.GONE);
            } else {
                summary.setText(summaryText);
                summary.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override public void onSharedPreferenceChanged(@NonNull SharedPreferences sharedPreferences, @NonNull String key) {
        if(bindedPreference!=null && key.equals(bindedPreference.getKey())){
            bindPreferenceView(bindedPreference);
        }
    }

    @Override public void onCenterPosition(boolean animate) {
        if(icon!=null) {
            icon.setAlpha(1);
            icon.setCircleColor(circleColorCenter);

            if (animate) {
                circleGrowAnimation.animate();
            } else {
                icon.setCircleRadius(circleRadiusCenter);
            }
        }

        if(title!=null) {
            title.setAlpha(1);
        }
        if(summary!=null) {
            summary.setAlpha(1);
        }
    }

    @Override public void onNonCenterPosition(boolean animate) {
        if(icon!=null) {
            icon.setAlpha(ALPHA_NON_CENTER);
            icon.setCircleColor(circleColorNonCenter);

            if (animate) {
                circleShrinkAnimation.animate();
            } else {
                icon.setCircleRadius(circleRadiusNonCenter);
            }
        }

        if(title!=null) {
            title.setAlpha(ALPHA_NON_CENTER);
        }
        if(summary!=null) {
            summary.setAlpha(ALPHA_NON_CENTER);
        }
    }

}