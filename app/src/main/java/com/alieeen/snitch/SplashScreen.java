package com.alieeen.snitch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.alieeen.snitch.rest.SnitchHttpClient;
import com.alieeen.snitch.util.SharedPrefs;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;

import java.io.IOException;

@EActivity
public class SplashScreen extends ActionBarActivity implements Runnable{

    private int sleep_time = 1500;


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private enum STATE {
        NOT_REGISTRED,
        REGISTRED,
        LOGGED
    }

    // Project number you got from the API Console
    String SENDER_ID = "303751871001";

    private ProgressBar spinner;
    private Context context;
    private GoogleCloudMessaging gcm;
    private String regid;

    public static String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler h = new Handler();
        h.postDelayed(this, sleep_time);

        checkGCMstatus();





        /*
        //Check Play services to register notifications
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(this);



            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                String msg = "Device registered, registration ID=" + regid;
                Log.i("GCM", msg);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }*/

    }

    private void setUpActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
/**
        boolean isLoggedIn = SharedPrefs.isLoggedIn(this);

        Intent i;

        if (isLoggedIn) {
            i = new Intent(this, MainActivity.class);
        } else {
            i = new Intent(this, WalkthroughActivity_.class);
        }
        startActivity(i);
        finish();*/
    }



    @Background
    public void checkGCMstatus() {
        STATE ret = STATE.NOT_REGISTRED;

        boolean result;
        try
        {
            //Get the current thread's token
            synchronized (this)
            {
                context = getApplicationContext();

                // Check device for Play Services APK. If check succeeds, proceed with
                // GCM registration.
                if (checkPlayServices())
                {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    //regid = getRegistrationId(context);
                    regid = SharedPrefs.getRegistrationId(context);

                    if (regid.isEmpty())
                    {
                        for (int i=0; i<5; i++)
                        {
                            result = registerDevice();
                            if (result)
                            {
                                ret = STATE.REGISTRED;
                                Log.i(TAG,"already registered");
                                break;
                            }
                            else
                            {
                                Thread.sleep(5000);		// Wait to try again
                            }
                        }
                    }
                    else
                    {
                        ret = STATE.REGISTRED;
                        Log.i(TAG,"already registered");
                    }

                } else {
                    Log.i(TAG, "No valid Google Play Services APK found.");
                }

                if (ret == STATE.REGISTRED)
                {
                    // Verify login
                    result = checkLogin();

                    if (result)
                    {
                        SnitchHttpClient snitchHttp = new SnitchHttpClient();
                        snitchHttp.doLogin(context,
                                snitchHttp.getUsername(context),
                                snitchHttp.getPassword(context),
                                snitchHttp.getMobileNumber(context));

                        ret = STATE.LOGGED;
                    }
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        STATE status = STATE.values()[ret.ordinal()];

        if (status == STATE.LOGGED)
        {

            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), MainActivity_.class);
            Log.i(TAG,"already logged in");
            startActivity(intent);
            finish();
        }
        else if (status == STATE.REGISTRED)
        {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), WalkthroughActivity_.class);
            Log.i(TAG,"just registered, going to login");
            startActivity(intent);
            finish();
        }
        else
        {
            /// not registred
        }



    }

    private boolean registerDevice ()
    {
        Log.i(TAG,"registering device");
        boolean result;

        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regid = gcm.register(SENDER_ID);
            Log.i(TAG, "ID" + SENDER_ID);
            result = true;

            // Persist the regID
            //storeRegistrationId(context, regid);
            SharedPrefs.setRegistrationId(context, regid);
        } catch (IOException ex) {
            result = false;
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }
        return result;
    }

    private boolean checkLogin()
    {
        //SharedPreferences settings = getSharedPreferences(SNITCH_LOGIN_FILE, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        //boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

        SnitchHttpClient snitchHttp = new SnitchHttpClient();
        boolean hasLoggedIn = snitchHttp.checkLogin(getApplicationContext());
        return hasLoggedIn;

    }

    @Background
    public void registerInBackground() {
        String msg = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this);
            }
            regid = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regid;
            Log.i("GCM", msg);

            // You should send the registration ID to your server over
            // HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your
            // app.
            // The request to your server should be authenticated if
            // your app
            // is using accounts.
            //sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the
            // device
            // will send upstream messages to a server that echo back
            // the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            //storeRegistrationId(this, regid);
            SharedPrefs.setRegistrationId(context, regid);
        } catch (IOException ex) {
            msg = "Error :" + ex.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
            Log.i("GCM", msg);
        }


    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context
     *            application's context.
     * @param regId
     *            registration ID
     */
   /* private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it
     * doesn't, display a dialog that allows users to download the APK from the
     * Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    /*private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }

        Log.i(TAG, "registrationId = "+ registrationId);
        return registrationId;
    }*/

    // ...
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

}
