package com.template.android.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import java.util.Locale;

public class LocaleUtil {

    private static final String SELECTED_LANGUAGE = "Locale.Selected.Language";


    public static Context onAttach(@NonNull Context context) {
        String lang = getPersistedData(context, getSystemLanguage(context));
        return setLocale(context, lang);
    }

    public static Context onAttach(@NonNull Context context, @NonNull String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static Context setLocale(@NonNull Context context, @NonNull String language) {

        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }

        return updateResourcesLegacy(context, language);
    }

    private static String getPersistedData(@NonNull Context context, @NonNull String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(@NonNull Context context, @NonNull String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(@NonNull Context context, @NonNull String language) {
        Locale locale = createLocale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        LocaleList.setDefault(configuration.getLocales());

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(@NonNull Context context, @NonNull String language) {
        Locale locale = createLocale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            configuration.setLayoutDirection(locale);
            context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }


    public static Locale createLocale(@NonNull String language) {
        if ("in".equals(language)) {
//            return new Locale(language, "ID");
            return new Locale(language);
        }

//        if ("en".equals(language)) {
//            return new Locale(language, "US");
//        }

        return new Locale("en", "US");
    }

    /**
     * 获取app 当前语言环境
     *
     * @param context
     * @return
     */
    public static String getLanguage(@NonNull Context context) {
        return getPersistedData(context, getSystemLanguage(context));
    }

    public static String getSystemLanguage(@NonNull Context context) {
        return getSystemLocale(context).getLanguage();
    }

    public static String getDisplayLanguage(@NonNull Context context) {
//        if ("in".equals(getPersistedData(context, getSystemLanguage(context))))
//            return context.getString(R.string.indonesia);
//        return context.getString(R.string.english);

        return "";
    }

    /**
     * 从系统配置获取locale
     *
     * @return
     */
    public static Locale getSystemLocale(@NonNull Context context) {
        Resources resources = context.getApplicationContext() == null ? context.getResources() : context.getApplicationContext().getResources();
        return getLocaleFromConfiguration(resources.getConfiguration());
    }

    /**
     * 从给定配置获取locale
     *
     * @param newConfig
     * @return
     */
    public static Locale getLocaleFromConfiguration(@NonNull Configuration newConfig) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList ls = newConfig.getLocales();
            if (ls.isEmpty())
                return Locale.ENGLISH;

            return ls.get(0);
        }
        return newConfig.locale;
    }

    /**
     * 获取系统默认语言环境
     *
     * @return
     * @deprecated 这是从 Java 来的方法，可以通过 Locale.setDefault(Locale) 来修改。
     * 不过这个方法有时候会出现莫名其妙的问题，比如一会儿返回系统语言 “zn_CN”，
     * 一会儿就返回 “en_US”，并不稳定。
     * See {@link  LocaleUtil#getSystemLanguage(Context)}
     */
    public static String getSystemLanguage() {
        return getSystemLocale().getLanguage();
    }

    /**
     * 获取系统默认 locale
     *
     * @return
     * @deprecated 这是从 Java 来的方法，可以通过 Locale.setDefault(Locale) 来修改。
     * 不过这个方法有时候会出现莫名其妙的问题，比如一会儿返回系统语言 “zh_CN”，
     * 一会儿就返回 “en_US”，并不稳定。
     * See {@link  LocaleUtil#getSystemLocale(Context)}
     */
    public static Locale getSystemLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (LocaleList.getDefault().isEmpty())
                return Locale.ENGLISH;

            return LocaleList.getDefault().get(0);
        }
        return Locale.getDefault();
    }
}
