package com.omneagate.lip.Activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.omneagate.lip.Utility.MySharedPreference;

import java.util.Locale;

/**
 * Created by user on 7/6/16.
 */
public class BaseActivity extends AppCompatActivity {

    private Locale myLocale;

    public String languageChcek(){
        String language = null;
        language = MySharedPreference.readString(getApplicationContext(), MySharedPreference.LANGUAGE_SELECT, "");
        setLocale(language);
        return language;
    }

    public void setLocale(String lang) {
        try {
            myLocale = new Locale(lang);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
