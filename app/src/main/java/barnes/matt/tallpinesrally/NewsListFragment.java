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

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class NewsListFragment extends ListFragment {

//    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    private NewsListAdapter mAdapter;

    private static NewsListFragment Instance;
    public static NewsListFragment getInstance() {
        return Instance;
    }

    public static NewsListFragment newInstance() {
        NewsListFragment fragment = new NewsListFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mAdapter = new NewsListAdapter(getActivity(), R.layout.newsitem, R.id.title, NewsContent.getNews());
        Instance = this;

        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setListAdapter(mAdapter);
    }

    public NewsListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            TwitterFeedV3 feed = new TwitterFeedV3(MainActivity.getInstance());
            feed.getNewTweets();
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
  //      mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    //    if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
      //  }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    public class NewsListAdapter extends ArrayAdapter<NewsItem> {

        public NewsListAdapter(Context context, int resource, int textViewResourceId, List<NewsItem> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            NewsItem item = getItem(position);
            if (view == null) {
                view = mInflater.inflate(R.layout.newsitem, null);
            }

            TextView title = (TextView)view.findViewById(R.id.tweet_text);
            title.setText(item.Text);

            TextView detail = (TextView)view.findViewById(R.id.tweet_date);
            detail.setText(item.Date);

            return view;
        }
    }
}
