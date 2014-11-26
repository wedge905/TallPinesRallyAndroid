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


public class ResultListFragment extends ListFragment {

//    private OnFragmentInteractionListener mListener;
    private static ResultListFragment instance;
    private LayoutInflater mInflater;
    private static ResultListAdapter mAdapter;

    public static ResultListFragment newInstance() {
        ResultListFragment fragment = new ResultListFragment();
        Bundle args = new Bundle();
        args.putInt("SECTIONNUMBER", 5);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResultListFragment() {
    }

    public static ResultListFragment GetInstance()
    {
        return instance;
    }

    public static ResultListAdapter GetAdapter()
    {
        return mAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mAdapter = new ResultListAdapter(getActivity(), R.layout.resultitem, R.id.resultposition, ResultContent.resultList);
        instance = this;
        // TODO: Change Adapter to display your content
        //setListAdapter(new ArrayAdapter< DummyContent.DummyItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));
        setListAdapter(mAdapter);

        if (mAdapter.isEmpty())
            ResultFeed.Execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    //    try {
  //          mListener = (OnFragmentInteractionListener) activity;
      //  } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

      //  if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
           // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
       // }
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
    //public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(String id);
//    }

    public class ResultListAdapter extends ArrayAdapter<Result> {
        public ResultListAdapter(Context context, int resource, int textViewResourceId, List<Result> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public void clear() {
            super.clear();
        }

        @Override
        public void add(Result r) {
            super.add(r);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = mInflater.inflate(R.layout.resultitem, parent, false);

            Result r = ResultContent.resultList.get(position);
            Team t = TeamContent.getTeamById(r.CarNumber);

            ((TextView)view.findViewById(R.id.resultposition)).setText(String.valueOf(r.Position));
            ((TextView)view.findViewById(R.id.carnumber)).setText("#" + r.CarNumber);
            ((TextView)view.findViewById(R.id.overalltime)).setText(r.TotalTime);
            if (r.Position > 1) {
                ((TextView) view.findViewById(R.id.diffleader)).setText(r.DiffLeader);
                ((TextView) view.findViewById(R.id.diffprev)).setText(r.DiffPrev);
            }

            try {
                ((TextView) view.findViewById(R.id.driver)).setText(t.Driver);
                ((TextView) view.findViewById(R.id.codriver)).setText(t.Codriver);
                ((TextView) view.findViewById(R.id.car)).setText(t.Car);
            }
            catch (Exception e) {
                ((TextView) view.findViewById(R.id.driver)).setText("?");
                ((TextView) view.findViewById(R.id.codriver)).setText("?");
                ((TextView) view.findViewById(R.id.car)).setText("?");
            }

            return view;
        }
    }
}
