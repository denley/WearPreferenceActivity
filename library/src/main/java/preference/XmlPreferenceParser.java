package preference;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.XmlRes;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import preference.internal.WearPreferenceScreen;

/**
 * Used to parse preferences xml files into an object model.
 *
 * This can be subclassed to provide aliases for preference types (such as for removing package names from tags).
 */
public class XmlPreferenceParser {

    @NonNull
    WearPreferenceScreen parse(@NonNull final Context context, @XmlRes final int prefsResId) {
        try {
            final XmlResourceParser parser = context.getResources().getXml(prefsResId);
            return parseScreen(context, parser);
        } catch (XmlPullParserException | IOException e) {
            throw new RuntimeException("Error parsing preferences file", e);
        }
    }

    @NonNull
    private WearPreferenceScreen parseScreen(@NonNull final Context context, @NonNull final XmlResourceParser parser)
            throws XmlPullParserException, IOException {

        while(parser.getName() == null) {
            parser.next();
        }

        if("PreferenceScreen".equals(parser.getName())) {
            final WearPreferenceScreen screen = new WearPreferenceScreen(context, parser);
            parsePreferences(context, parser, screen);
            return screen;
        } else {
            throw new IllegalArgumentException("Preferences file must start with a PreferenceScreen element");
        }
    }

    private void parsePreferences(@NonNull final Context context, @NonNull final XmlResourceParser parser, @NonNull final WearPreferenceScreen screen)
            throws XmlPullParserException, IOException {

        if(parser.getEventType() == XmlResourceParser.START_TAG) {
            parser.next();
            while(parser.getEventType() != XmlResourceParser.END_TAG) {
                parseItem(context, parser, screen);
                parser.next();
            }
        }
    }

    private void parseItem(final Context context, final XmlResourceParser parser, final WearPreferenceScreen screen)
            throws XmlPullParserException, IOException {

        switch(parser.getName()) {
            case "PreferenceScreen":
                final WearPreferenceScreen childScreen = parseScreen(context, parser);
                screen.addChild(childScreen);
                break;
            default:
                parsePreference(context, parser, screen);
                break;
        }
    }

    private void parsePreference(@NonNull final Context context, @NonNull final XmlResourceParser parser, @NonNull final WearPreferenceScreen screen)
            throws XmlPullParserException, IOException {

        final String name = parser.getName();

        WearPreference preference = parsePreference(context, name, parser);

        if(preference == null) {
            preference = parsePreferenceInternal(context, name, parser);
        }

        screen.addChild(preference);

        while(parser.getEventType() != XmlResourceParser.END_TAG) {
            parser.next();
        }
    }

    @NonNull
    private WearPreference parsePreferenceInternal(@NonNull final Context context, @NonNull final String name, @NonNull final XmlResourceParser parser) {
        switch(parser.getName()) {
            case "ListPreference":
                return new WearListPreference(context, parser);
            case "SwitchPreference":
            case "CheckBoxPreference":
                return new WearTwoStatePreference(context, parser);
            default:
                return parsePreferenceUsingReflection(context, name, parser);
        }
    }

    @NonNull
    private WearPreference parsePreferenceUsingReflection(@NonNull final Context context, @NonNull final String name, @NonNull final XmlResourceParser parser) {
        try {
            final Class<?> cls = Class.forName(name);
            final Constructor<?> constructor = cls.getConstructor(Context.class, AttributeSet.class);
            return (WearPreference) constructor.newInstance(context, parser);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown preference type \"" + name +
                    "\". To use custom preference types you must create a custom XmlPreferenceParser, or use the fully qualified class name");
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Custom preference type \"" + name +
                    "\" must have a (Context, AttributeSet) constructor. Alternatively, use a custom XmlPreferenceParser to instantiate the preferences.");
        }
    }

    /**
     * Override this method when creating custom preference types in order to parse these types from a preferences xml file.
     *
     * Returning null will defer the parsing to internal (pre-made) preference types.
     *
     * @param context           A Context that can be used to load styled attributes
     * @param preferenceType    The name of the preference type to create (this is the xml tag name).
     * @param attrs             The xml attributes contained in the xml tag
     *
     * @return                  A new {@link WearPreference} object that represents this type of preference.
     */
    @Nullable
    protected WearPreference parsePreference(@NonNull final Context context, @NonNull final String preferenceType, @NonNull final AttributeSet attrs) {
        return null;
    }

}
