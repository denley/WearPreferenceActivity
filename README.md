# WearPreferenceActivity
A preferences framework for Android Wear apps. Equivalent to Android's `PreferenceActivity`, but for Android Wear.

Basic Use
-------
`WearPreferenceActivity` works much the same way as Android's `PreferenceActivity` framework.

Start by defining which preference to display. This is done in a layout xml file, as shown below. This is much like creating a preferences xml file for Android's `PreferenceActivity`, but here we must are create a layout resource file instead (in `/res/layout` not `/res/xml`). This layout is never actually added to the window. It is just being used as a familiar way to define the structure of the preferences page.
```xml
<preference.PreferenceScreen
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    >

    <preference.BooleanPreference
        app:key="use_location"
        app:title="Location"
        app:iconOn="@drawable/ic_location_on"
        app:iconOff="@drawable/ic_location_off"
        app:summaryOn="@string/location_summary_on"
        app:summaryOff="@string/location_summary_off"
        app:defaultValue="true"
        app:icon="@drawable/ic_launcher"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

    <preference.BooleanPreference
        app:key="backup_data"
        app:title="Data Backup"
        app:iconOn="@drawable/ic_cloud_queue_white_24dp"
        app:iconOff="@drawable/ic_cloud_off_white_24dp"
        app:defaultValue="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

    <preference.ListPreference
        app:key="language"
        app:title="Language"
        app:icon="@drawable/ic_language_white_24dp"
        app:entries="@array/entries_language"
        app:entryValues="@array/values_language"
        app:defaultValue="en"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

    <preference.BooleanPreference
        app:key="full_screen"
        app:title="Full Screen"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

</preference.PreferenceScreen >
```

Next, create an `Activity` that extends `WearPreferenceActivity`, and add the preferences from the xml resource using the `addPreferencesFromResource` method. Don't forget to define the `Activity` in your `AndroidManifest.xml` file.

```java
public class MySettingsActivity extends WearPreferenceActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.layout.preferences);
    }

}
```

That's it! The preferences page is created. It will load and save the corresponding preference values, and it will automatically listen for external changes to preference values and update the view accordingly.

Build Configuration
--------
Add the following line to the gradle dependencies for your module.
```groovy
compile 'me.denley.wearpreferenceactivity:wearpreferenceactivity:0.1.0'
```

Preference Types
--------

This library includes two pre-build preference types: `BooleanPreference` and `ListPreferenece`, which behave in a similar fashion to their Android counterparts (`android.preference.CheckBoxPreference` and `android.preference.ListPreference`).

Other custom types can be added by extending the `Preference` class, and defining its behaviour in its implementation of the `onPreferenceClick()` method. You may also want to override `getSummary()` to have it show the current value of the preference.

If you would like to see any additional preference types included in this library, don't hesitate to submit an issue or a pull request.

License
-------

    Copyright 2015 Denley Bihari

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.