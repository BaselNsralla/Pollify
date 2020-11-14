package com.projekt.tdp028.activities;

import android.Manifest;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.projekt.tdp028.utility.LocalStore;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDarkModeIfNeeded(savedInstanceState);
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1
        );
        setLocale(LocalStore.getSavedLocale(this));
    }


    protected void setLocale(String localeStr) {

        Locale locale = new Locale(localeStr);
        Locale.setDefault(locale);
        Context context = getBaseContext();
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        context.createConfigurationContext(config);
        config.locale = locale;
        LocalStore.saveLocale(localeStr, this);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    private void setDarkModeIfNeeded(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            setDarkMode();
        } else {
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if((Configuration.UI_MODE_NIGHT_NO == nightModeFlags || Configuration.UI_MODE_NIGHT_UNDEFINED == nightModeFlags) && LocalStore.getDarkMode(this)) {
                setDarkMode();
            }
        }
    }


    private void setDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        getDelegate().applyDayNight();
        LocalStore.saveDarkMode(true, this);
        recreate();
    }
}
