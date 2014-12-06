package co.publicscience.argos;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class ArgosApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Set the default font for the application.
        CalligraphyConfig.initDefault("fonts/GraphikApp-Regular.ttf", R.attr.fontPath);
    }
}
