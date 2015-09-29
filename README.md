[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WearPreferenceActivity-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1643)

# WearPreferenceActivity
A preferences framework for Android Wear apps. Equivalent to Android's `PreferenceActivity`, but for Android Wear.

![Preference List](/screenshots/preference_list.png) ![Preference List](/screenshots/language_select.png)

Build Configuration
--------
Add the following line to the gradle dependencies for your wearable app's module.
```groovy
compile 'me.denley.wearpreferenceactivity:wearpreferenceactivity:0.4.0'
```

Basic Use
-------
`WearPreferenceActivity` works much the same way as Android's `PreferenceActivity` framework.

Start by creating a preferences xml file (in the `/res/xml/` folder). You may choose to reuse the same file from your mobile app. An example of such a file is below.

```xml
<PreferenceScreen
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >

    <SwitchPreference
        android:key="use_location"
        android:title="@string/title_location"
        android:summaryOn="@string/location_summary_on"
        android:summaryOff="@string/location_summary_off"
        app:wear_iconOn="@drawable/ic_location_on_white_24dp"
        app:wear_iconOff="@drawable/ic_location_off_white_24dp"
        android:defaultValue="true"
        />

    <SwitchPreference
        android:key="backup_data"
        android:title="@string/title_backup_data"
        android:summaryOn="@string/location_summary_on"
        android:summaryOff="@string/location_summary_off"
        app:wear_summaryOn="@string/location_summary_on"
        app:wear_summaryOff="@string/location_summary_off"
        app:wear_iconOn="@drawable/ic_cloud_queue_white_24dp"
        app:wear_iconOff="@drawable/ic_cloud_off_white_24dp"
        android:defaultValue="true"
        />

    <ListPreference
        android:key="language"
        android:title="@string/title_language"
        android:icon="@drawable/ic_language_white_24dp"
        android:entries="@array/entries_language"
        android:entryValues="@array/values_language"
        android:defaultValue="en"
        />

    <SwitchPreference
        android:key="full_screen"
        android:title="@string/title_full_screen"
        />


</PreferenceScreen>
```

Next, create an `Activity` that extends `WearPreferenceActivity`, and add the preferences from the xml resource using the `addPreferencesFromResource` method. Don't forget to define the `Activity` in your `AndroidManifest.xml` file.

```java
public class MySettingsActivity extends WearPreferenceActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
```

That's it! The preferences page is created. It will load and save the corresponding preference values, and it will automatically listen for external changes to preference values and update the view accordingly.

Preference Types
--------

This library currently supports `ListPreference`, `SwitchPreference`, and `CheckBoxPreference` out of the box.

Other preference types can be created by extending the `WearPreference` class, and defining how it responds to user clicks in its `onPreferenceClick()` method. You may also want to override `getSummary()` to have it show the current value of the preference.

If you create a custom preference type, you need to let the xml parser know how to create it. This can be done in two ways:

#### Option 1 - Use the full class name in the xml file

```xml
<PreferenceScreen
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >

    ...

    <com.example.MyCustomWearPreference
        android:key="a_pref_key"
        android:title="@string/a_pref_title"
        />


</PreferenceScreen>
```

#### Option 2 - Create a Custom Parser

You can specify exactly how you want the preferences to be instantiated from the xml file by creating your own `XmlPreferenceParser`. You could also use this method to give your preference types additional aliases.

```java
public class MyPreferenceParser extends XmlPreferenceParser {

    protected WearPreference parsePreference(Context context, String preferenceType, final AttributeSet attrs) {
        if(preferenceType.equals("MyCustomWearPreference") {
            return new MyCustomWearPreference(context, attrs);
        } else {
            return null;
        }
    }

}
```

If you choose this method, you must supply the parser with the xml resource value:

```java
addPreferencesFromResource(R.xml.preferences, new MyPreferenceParser());
```


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