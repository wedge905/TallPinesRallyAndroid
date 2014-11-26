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

public class ScheduleListFragment extends ListFragment {

    private OnScheduleItemClickListener mListener;
    private LayoutInflater mInflater;
    private int currentPosition = -1;

    // TODO: Rename and change types of parameters
    public static ScheduleListFragment newInstance() {
        ScheduleListFragment fragment = new ScheduleListFragment();
        Bundle args = new Bundle();
        args.putInt("SECTIONNUMBER", 1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        setListAdapter(new ScheduleListAdapter(getActivity(), R.layout.schedulelistitem, R.id.title, ScheduleContent.Schedule));
    }

    @Override
        public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putInt("currentPos", currentPosition);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        if (state != null) {
            currentPosition = state.getInt("currentPos", -1);
            if (currentPosition >= 0) {
                mListener.onScheduleItemClick(ScheduleContent.Schedule.get(currentPosition).Id);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnScheduleItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        currentPosition = -1;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        currentPosition = position;

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onScheduleItemClick(ScheduleContent.Schedule.get(position).Id);
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
    public interface OnScheduleItemClickListener {
        public void onScheduleItemClick(int id);
    }

    private class ScheduleListAdapter extends ArrayAdapter<ScheduleContent.ScheduleItem> {

        public ScheduleListAdapter(Context context, int resource, int textViewResourceId, List<ScheduleContent.ScheduleItem> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            ScheduleContent.ScheduleItem item = getItem(position);
            if (view == null) {
                view = mInflater.inflate(R.layout.schedulelistitem, parent, false);
            }

            TextView title = (TextView)view.findViewById(R.id.title);
            title.setText(item.Name);

            TextView detail = (TextView)view.findViewById(R.id.detail);
            detail.setText(item.When + "\n" + item.Description);

            return view;
        }
    }
}
