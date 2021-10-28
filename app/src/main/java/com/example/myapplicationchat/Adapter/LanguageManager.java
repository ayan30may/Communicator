package com.example.myapplicationchat.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    private SharedPreferences sharedPreferences;
    public Context context;
    public LanguageManager(Context context1){
        context = context1;
        sharedPreferences = context.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }
    public void updateResources(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
        setLang(lang);
    }

    public String getLang(){
        return sharedPreferences.getString("language","en");
    }

    public void setLang(String lang){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language",lang);
        editor.commit();
    }
}
