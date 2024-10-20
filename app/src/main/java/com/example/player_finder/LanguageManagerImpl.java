package com.example.player_finder;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Locale;

public class LanguageManagerImpl implements LanguageManager {

    private static final String PREFS_NAME = "LanguagePrefs";
    private static final String LANGUAGE_KEY = "app_language";
    private final Context context;

    public LanguageManagerImpl(Context context) {
        this.context = context;
    }

    @Override
    public void toggleLanguage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String currentLang = prefs.getString(LANGUAGE_KEY, "en");

        // Switch language
        if ("en".equals(currentLang)) {
            setLocale("he");
        } else {
            setLocale("en");
        }
    }

    @Override
    public void loadLanguage() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String language = prefs.getString(LANGUAGE_KEY, "en");
        setLocale(language);
    }

    @Override
    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LANGUAGE_KEY, lang);
        editor.apply();
    }
}
