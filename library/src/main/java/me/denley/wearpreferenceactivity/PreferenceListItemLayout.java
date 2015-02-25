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

package me.denley.wearpreferenceactivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import preference.Preference;

@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class PreferenceListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final float ALPHA_NON_CENTER = 0.6f;

    private static final float CIRCLE_RADIUS_CENTER_DP = 24;
    private static final float CIRCLE_RADIUS_NON_CENTER_DP = 16;

    private static final long ANIMATION_DURATION = 100;


    private final SharedPreferences preferences;

    @Nullable private Preference bindedPreference = null;

    @Nullable private final CircledImageView icon;
    @Nullable private final TextView title, summary;

    private final float circleRadiusCenter, circleRadiusNonCenter;
    private float animationStartRadius;

    final Animation circleGrowAnimation = new Animation(){
        @Override protected void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
            assert icon != null;

            float radius = interpolatedTime*circleRadiusCenter
                    + (1-interpolatedTime)*animationStartRadius;
            icon.setCircleRadius(radius);
        }
    };

    final Animation circleShrinkAnimation = new Animation(){
        @Override protected void applyTransformation(float interpolatedTime, @NonNull Transformation t) {
            assert icon != null;

            float radius = interpolatedTime*circleRadiusNonCenter
                    + (1-interpolatedTime)*animationStartRadius;
            icon.setCircleRadius(radius);
        }
    };

    public PreferenceListItemLayout(Context context) {
        super(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LayoutInflater.from(context).inflate(R.layout.wearprefs_preference_item, this);
        icon = (CircledImageView) findViewById(android.R.id.icon);
        title = (TextView) findViewById(android.R.id.title);
        summary = (TextView) findViewById(android.R.id.summary);

        final float density = context.getResources().getDisplayMetrics().density;
        circleRadiusCenter = CIRCLE_RADIUS_CENTER_DP * density;
        circleRadiusNonCenter = CIRCLE_RADIUS_NON_CENTER_DP * density;
        if(icon!=null) {
            icon.setCircleRadiusPressed(circleRadiusCenter);
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
        if(icon !=null) {
            icon.setImageResource(preference.getIcon());
        }
        if(title!=null) {
            title.setText(preference.getTitle());
        }
        if(summary!=null) {
            final String summaryText = preference.getSummary();
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
                if (icon.getAnimation() != circleGrowAnimation) {
                    icon.clearAnimation();
                    animationStartRadius = icon.getCircleRadius();
                    icon.startAnimation(circleGrowAnimation);
                }
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
                if (icon.getAnimation() != circleShrinkAnimation) {
                    icon.clearAnimation();
                    animationStartRadius = icon.getCircleRadius();
                    icon.startAnimation(circleShrinkAnimation);
                }
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