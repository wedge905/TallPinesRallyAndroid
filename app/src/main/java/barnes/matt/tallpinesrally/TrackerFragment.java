package barnes.matt.tallpinesrally;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Property;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import barnes.matt.tallpinesrally.MapHelpers.LatLngInterpolator;
import barnes.matt.tallpinesrally.MapHelpers.MarkerAnimation;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TrackerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackerFragment extends Fragment {

    private List<TrackerMarker> markers = new ArrayList<TrackerMarker>();
    private List<Marker> MarkerList = new ArrayList<Marker>();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static TrackerFragment instance;
  //  private OnFragmentInteractionListener mListener;

    public static TrackerFragment newInstance() {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static TrackerFragment getInstance() {
        return instance;
    }

    public TrackerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        instance = this;
        setUpMapIfNeeded();
        RegisterForUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        MainActivity.getInstance().unSetReciever();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        IntentFilter filter = new IntentFilter(UpdateReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        MainActivity.getInstance().SetReciever(new UpdateReceiver(), filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        CancelUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();

        instance = this;
        setUpMapIfNeeded();
        RegisterForUpdates();
    }

    private void RegisterForUpdates()
    {
        new TrackerUpdateTask().execute(true);
    }
    private void CancelUpdates()
    {
        new TrackerUpdateTask().execute(false);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment mapFrag = (SupportMapFragment)MainActivity.getInstance().getSupportFragmentManager().findFragmentById(R.id.map);
            mMap = mapFrag.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.clear();
        mMap.addMarker(TrackerContent.HQMarker);

        mMap.addPolyline(TrackerContent.GoltonLine);
        mMap.addMarker(TrackerContent.GoltonStart);
        mMap.addMarker(TrackerContent.GoltonEnd);

        new GetLiveTask().execute();  // Trigger async task to find out if event is Live, and populate remaining map if it is

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.014550,-77.786183), 4));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1500, null);
    }

    public void setUpAllMap()
    {
        mMap.addPolyline(TrackerContent.UpperHastingsLine);
        mMap.addMarker(TrackerContent.UpperHastingsStart);
        mMap.addMarker(TrackerContent.UpperHastingsEnd);

        mMap.addPolyline(TrackerContent.PartialPeanutLine);
        mMap.addMarker(TrackerContent.PartialPeanutStart);
        mMap.addMarker(TrackerContent.PartialPeanutEnd);

        mMap.addPolyline(TrackerContent.OldDetlorLine);
        mMap.addMarker(TrackerContent.OldDetlorStart);
        mMap.addMarker(TrackerContent.OldDetlorEnd);

        mMap.addPolyline(TrackerContent.IronBridgeLine);
        mMap.addMarker(TrackerContent.IronBridgeStart);
        mMap.addMarker(TrackerContent.IronBridgeEnd);

        mMap.addPolyline(TrackerContent.LowerTuriffLine);
        mMap.addMarker(TrackerContent.LowerTuriffStart);
        mMap.addMarker(TrackerContent.LowerTuriffEnd);

        mMap.addPolyline(TrackerContent.MiddleHastingsLine);
        mMap.addMarker(TrackerContent.MiddleHastingsStart);
        mMap.addMarker(TrackerContent.MiddleHastingsEnd);

        mMap.addPolyline(TrackerContent.LowerHastingsLine);
        mMap.addMarker(TrackerContent.LowerHastingsStart);
        mMap.addMarker(TrackerContent.LowerHastingsEnd);

        mMap.addPolyline(TrackerContent.EganCreekLine);
        mMap.addMarker(TrackerContent.EganCreekStart);
        mMap.addMarker(TrackerContent.EganCreekEnd);

        mMap.addPolyline(TrackerContent.LongEganCreekLine);
        mMap.addMarker(TrackerContent.LongEganCreekStart);
        mMap.addMarker(TrackerContent.LongEganCreekEnd);

        mMap.addPolyline(TrackerContent.OldDetlorNightLine);
        mMap.addMarker(TrackerContent.OldDetlorNightStart);
        mMap.addMarker(TrackerContent.OldDetlorNightEnd);
    }

    public List<TrackerMarker> updatedMarkerList = new ArrayList<TrackerMarker>();
    public void updateMarkerList()
    {
        updateMarkerList(updatedMarkerList);
    }

    public void updateMarkerList(List<TrackerMarker> updatedMarkers)
    {
        for (TrackerMarker m : updatedMarkers) {
            if (!markers.contains(m)){
                m.markerOptions.position(new LatLng(m.Latitude, m.Longitude));
                m.markerOptions.title("Car: " + m.CarNumber + " Speed: " + m.Speed + "km/h");
                markers.add(m);  // add to list

                Marker marker = mMap.addMarker(m.markerOptions);
                MarkerList.add(marker);  // draw on map
            }
            else
            {
                if (m.Nonce > markers.get(markers.indexOf(m)).Nonce) {
                    //m.markerOptions.title("Car: " + m.CarNumber + "\n" + "Speed: " + m.Speed);
                    int travelTime = CalculateTravelTime(markers.get(markers.indexOf(m)).Nonce, m.Nonce);  //calculate animation time
                    LatLng newPosition = new LatLng(m.Latitude, m.Longitude);
                    m.markerOptions.position(new LatLng(m.Latitude, m.Longitude));
                    markers.set(markers.indexOf(m), m);  // update marker in list

                    Marker marker = MarkerList.get(markers.indexOf(m));
                    boolean showInfo = marker.isInfoWindowShown();

                    if (showInfo)
                        marker.hideInfoWindow();
                    marker.setTitle("Car: " + m.CarNumber + " Speed: " + m.Speed + "km/h");
                    if (showInfo)
                        marker.showInfoWindow();

                    MarkerAnimation.animateMarkerTo(marker, newPosition, new LatLngInterpolator.Linear(), travelTime);
                }
                else {
                    // do nothing
                }
            }
        }
    }

    //private int CalculateTravelTime(LatLng start, LatLng end, long startTime, long endTime)
    private int CalculateTravelTime(long startTime, long endTime)
    {
        long elapsedTicks = endTime - startTime;
        long ms = elapsedTicks / 10000;

        return (int)ms;
    }

    private float CalculateDistance(LatLng start, LatLng end)
    {
        float[] result = new float[1];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, result);
        return result[0];
    }

    private static View mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (mapView != null) {
            ViewGroup parent = (ViewGroup) mapView.getParent();
            if (parent != null)
                parent.removeView(mapView);
        }
        try {
            mapView = inflater.inflate(R.layout.fragment_tracker, container, false);
        } catch (InflateException e) {

        /* map is already there, just return view as it is */
            //hmmm this might be bad
        }
        return mapView;

    }

    public void onButtonPressed(Uri uri) {
      //  if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //  try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.getInstance().unSetReciever();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
  //  public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

    public class GetLiveTask extends AsyncTask<Void, Boolean, Boolean> {
        @Override
        protected void onPostExecute(Boolean isLive) {
            super.onPostExecute(isLive);

            if (isLive)
                TrackerFragment.getInstance().setUpAllMap();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean isLive = false;

            try {
                HttpGet Get = new HttpGet("http://wedge1-001-site1.mywindowshosting.com/api/tallpines/eventislive");

                DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = client.execute(Get);

                if (EntityUtils.toString(response.getEntity()).equals("true"))
                    isLive = true;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isLive;
        }
    }
}

