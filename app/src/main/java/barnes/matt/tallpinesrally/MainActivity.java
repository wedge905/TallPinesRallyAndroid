package barnes.matt.tallpinesrally;

import android.app.Activity;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ScheduleListFragment.OnScheduleItemClickListener, LocationListFragment.OnLocationClickListener, TeamListFragment.OnTeamClickListener{

    private GoogleCloudMessaging gcm;
    private String regid;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private String SENDER_ID = "314127831723";

    private UpdateReceiver updateReceiver;// = new TrackerFragment().new UpdateReceiver();

    private static MainActivity Instance;
    public static MainActivity getInstance() {
        return Instance;
    }


    public void SetReciever(UpdateReceiver receiver, IntentFilter filter)
    {
        if (updateReceiver == null) {
            updateReceiver = receiver;

            registerReceiver(updateReceiver, filter);
        }
    }

    public void unSetReciever()
    {
        try {
            unregisterReceiver(updateReceiver);
        }
        catch (IllegalArgumentException e)
        { }
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(updateReceiver);
        }
        catch (IllegalArgumentException e)
        { }
        super.onStop();
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;

        setContentView(R.layout.activity_main);

       if (checkPlayServices())
       {
           gcm = GoogleCloudMessaging.getInstance(this);
           regid = getRegistrationId(this);

           if (regid.isEmpty()) {
               registerInBackground();
           }
       }

        mNavigationDrawerFragment = (NavigationDrawerFragment)getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        if (savedInstanceState != null ) {
            currentfrag = savedInstanceState.getString("currentFrag");

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragmentManager.findFragmentByTag(currentfrag), currentfrag).addToBackStack(null).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentFrag", currentfrag);
    }

    public String currentfrag = "";

    @Override
    public void onLocationClick(int id) {
        FragmentManager fragmentManager = getFragmentManager();
        LocationContent.LocationItem location = LocationContent.Locations.get(id);
        currentfrag = "LOCATIONDETAIL";
        fragmentManager.beginTransaction().replace(R.id.container, LocationDetailFragment.newInstance(location.Name, location.LongDescription, location.ImageName, location.Direction), currentfrag).addToBackStack(null).commit();
    }

    @Override
    public void onScheduleItemClick(int id) {
        FragmentManager fragmentManager = getFragmentManager();
        ScheduleContent.ScheduleItem scheduleItem = ScheduleContent.Schedule.get(id);
        LocationContent.LocationItem location = LocationContent.Locations.get(scheduleItem.locationId);
        currentfrag = "LOCATIONDETAIL";
        fragmentManager.beginTransaction().replace(R.id.container, LocationDetailFragment.newInstance(location.Name, location.LongDescription, location.ImageName, location.Direction), currentfrag).addToBackStack(null).commit();
    }

    @Override
    public void onTeamClick(int id) {
        FragmentManager fragmentManager = getFragmentManager();
        currentfrag = "TEAMDETAIL";
        fragmentManager.beginTransaction().replace(R.id.container, TeamDetailFragment.newInstance(TeamContent.getTeamById(id)), currentfrag).addToBackStack(null).commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();

        switch (position) {
            case 0 :
                currentfrag = "INFORMATION";
                fragmentManager.beginTransaction().replace(R.id.container, InformationFragment.newInstance(), currentfrag).commit();
                break;
            case 1 :
                currentfrag = "SCHEDULELIST";
                fragmentManager.beginTransaction().replace(R.id.container, ScheduleListFragment.newInstance(), currentfrag).commit();
                break;
            case 2 :
                currentfrag = "LOCATIONLIST";
                fragmentManager.beginTransaction().replace(R.id.container, LocationListFragment.newInstance(), currentfrag).commit();
                break;
            case 3 :
                currentfrag = "NEWSLIST";
                fragmentManager.beginTransaction().replace(R.id.container, NewsListFragment.newInstance(), currentfrag).commit();
                break;
            case 4 :
                currentfrag = "TEAMLIST";
                fragmentManager.beginTransaction().replace(R.id.container, TeamListFragment.newInstance(), currentfrag).commit();
                break;
            case 5 :
                currentfrag = "RESULTS";
                fragmentManager.beginTransaction().replace(R.id.container, ResultListFragment.newInstance(), currentfrag).commit();
                break;
            case 6 :
                currentfrag = "TRACKER";
                fragmentManager.beginTransaction().replace(R.id.container, TrackerFragment.newInstance(), currentfrag).commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.section_info_title);
                break;
            case 2:
                mTitle = getString(R.string.section_schedule_title);
                break;
            case 3:
                mTitle = getString(R.string.section_locations_title);
                break;
            case 4:
                mTitle = getString(R.string.section_news_title);
                break;
            case 5:
                mTitle = getString(R.string.section_teams_title);
                break;
            case 6:
                mTitle = getString(R.string.section_results_title);
                break;
            case 7:
                mTitle = getString(R.string.section_tracking_title);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getInstance());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                //Log.i(TAG, "This device is not supported.");
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
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            //Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            //Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    public String getGCMId()
    {
        return regid;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        AsyncTask bgTask = new AsyncTask() {
           // @Override
            //protected String doInBackground(Object[] params) {
//                return null;
//            }

            @Override
            protected String doInBackground(Object[] params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getInstance());
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(getInstance(), regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            //@Override
            //protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            //}
        };

        bgTask.execute(null, null, null);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP
     * or CCS to send messages to your app. Not needed for this demo since the
     * device sends upstream messages to a server that echoes back the message
     * using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() throws IOException {
        try {
            HttpPost httpPost = new HttpPost("http://wedge1-001-site1.mywindowshosting.com/api/tallpines/registerdevice");
            httpPost.setEntity(new StringEntity("{ \"RegId\":\"" + regid + "\" }"));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        //Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /**
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
    */
}
