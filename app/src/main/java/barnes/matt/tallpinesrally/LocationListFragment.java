package barnes.matt.tallpinesrally;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import java.util.List;

public class LocationListFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private OnLocationClickListener mListener;
    private LayoutInflater mInflater;

    // TODO: Rename and change types of parameters
    public static LocationListFragment newInstance() {
        LocationListFragment fragment = new LocationListFragment();
        Bundle args = new Bundle();
        args.putInt("SECTIONNUMBER", 2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LocationListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setListAdapter(new LocationListAdapter(getActivity(), R.layout.locationlistitem, R.id.title, LocationContent.Locations));
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnLocationClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onLocationClick(LocationContent.Locations.get(position).Id);
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnLocationClickListener {
        public void onLocationClick(int id);
    }

    private class LocationListAdapter extends ArrayAdapter<LocationContent.LocationItem> {

        public LocationListAdapter(Context context, int resource, int textViewResourceId, List<LocationContent.LocationItem> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LocationContent.LocationItem item = getItem(position);
            if (view == null) {
                view = mInflater.inflate(R.layout.locationlistitem, parent, false);
            }

            TextView title = (TextView)view.findViewById(R.id.title);
            title.setText(item.Name);

            TextView detail = (TextView)view.findViewById(R.id.detail);
            detail.setText(item.Description);

            return view;
        }
    }
}
