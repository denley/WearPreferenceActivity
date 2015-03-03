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
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class ListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final float ALPHA_NON_CENTER = 0.6f;

    private static final float CIRCLE_RADIUS_CENTER_DP = 24;
    private static final float CIRCLE_RADIUS_NON_CENTER_DP = 16;

    private static final long ANIMATION_DURATION = 100;


    private final SharedPreferences preferences;

    @Nullable private Preference bindedPreference = null;

    @Nullable private final CircledImageView icon;
    @Nullable private final TextView title, summary;

    private final float circleRadiusCenter, circleRadiusNonCenter;

    private CircleSizeAnimation circleGrowAnimation = null;
    private CircleSizeAnimation circleShrinkAnimation = null;

    public ListItemLayout(@NonNull Context context, @LayoutRes int layout) {
        super(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LayoutInflater.from(context).inflate(layout, this);
        icon = (CircledImageView) findViewById(android.R.id.icon);
        title = (TextView) findViewById(android.R.id.title);
        summary = (TextView) findViewById(android.R.id.summary);

        final float density = context.getResources().getDisplayMetrics().density;
        circleRadiusCenter = CIRCLE_RADIUS_CENTER_DP * density;
        circleRadiusNonCenter = CIRCLE_RADIUS_NON_CENTER_DP * density;
        if(icon!=null) {
            icon.setCircleRadiusPressed(circleRadiusCenter);
            circleGrowAnimation = new CircleSizeAnimation(icon, circleRadiusCenter);
            circleShrinkAnimation = new CircleSizeAnimation(icon, circleRadiusNonCenter);
        }

        circleGrowAnimation.setDuration(ANIMATION_DURATION);
        circleShrinkAnimation.setDuration(ANIMATION_DURATION);
    }

    public void bindPreference(final Preference preference){
        bindedPreference = preference;
        bindPreferenceView(preference);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public void releaseBinding(){
        preferences.unregisterOnSharedPreferenceChangeListener(this);
        bindedPreference = null;
    }

    private void bindPreferenceView(final Preference preference){
        bindView(preference.getIcon(), preference.getTitle(), preference.getSummary());
    }

    public void bindView(final int iconId, final CharSequence titleText, final CharSequence summaryText) {
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

    @Override public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @NonNull String key) {
        if(bindedPreference!=null && key.equals(bindedPreference.getKey())){
            bindPreferenceView(bindedPreference);
        }
    }

    @Override public void onCenterPosition(boolean animate) {
        if(icon!=null) {
            icon.setAlpha(1);

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