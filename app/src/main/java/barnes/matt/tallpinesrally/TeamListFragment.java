package barnes.matt.tallpinesrally;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTeamClickListener}
 * interface.
 */
public class TeamListFragment extends ListFragment {

    private LayoutInflater mInflater;
    private TeamListAdapter mAdapter;
    private OnTeamClickListener mListener;

    private static TeamListFragment Instance;
    public static TeamListFragment getInstance() {
        return Instance;
    }

    public TeamListAdapter getAdapter() {
        return mAdapter;
    }

    public static TeamListFragment newInstance() {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeamListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflater = (LayoutInflater) getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mAdapter = new TeamListAdapter(getActivity(), R.layout.teamitem, R.id.title, TeamContent.getTeamList());
        Instance = this;

        setListAdapter(mAdapter);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            TeamFeed feed = new TeamFeed(MainActivity.getInstance());
            feed.getTeamUpdates(true);

            mListener = (OnTeamClickListener) activity;
        }
        catch (ClassCastException e) {
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
            mListener.onTeamClick(TeamContent.teamList.get(position).CarNumber);
        }
    }

    public class TeamListAdapter extends ArrayAdapter<Team> {

        public TeamListAdapter(Context context, int resource, int terViewResourceId, List<Team> teamList) {
            super(context, resource, terViewResourceId, teamList);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            Team team = getItem(position);

            if (view == null) {
                view = mInflater.inflate(R.layout.teamitem, parent, false);
            }

            ImageView imageView = (ImageView)view.findViewById(R.id.icon);

            if (team.Photo.length() > 2) {
                ImageManager im = new ImageManager(MainActivity.getInstance());
                im.fetchDrawableOnThread(team.Photo, imageView);
                //imageView.setImageDrawable(getResources().getIdentifier(Image, "drawable", getActivity().getPackageName()));
                //ImageAsyncLoader imageLoader = new ImageAsyncLoader(imageView);
                //DownloadedDrawable downloadedDrawable = new DownloadedDrawable(imageLoader);
                //imageView.setImageDrawable(downloadedDrawable);
            }
            else {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.tp_golton));
            }

            if (team.Name.length() > 0)
                ((TextView)view.findViewById(R.id.teamName)).setText(team.Name);
            else
                ((TextView)view.findViewById(R.id.teamName)).setText(team.Driver);
            ((TextView)view.findViewById(R.id.driver)).setText("Driver: " + team.Driver);
            ((TextView)view.findViewById(R.id.codriver)).setText("Co-Driver: " + team.Codriver);
            ((TextView)view.findViewById(R.id.carnumber)).setText("Car #: " + Integer.toString(team.CarNumber));

            return view;
        }

        @Override
        public void add(Team team) {

            if (TeamContent.Contains(team))
                TeamContent.updateTeam(team);
            else
                super.add(team);

            super.notifyDataSetChanged();
        }

        @Override
        public void addAll(Team ... teams) {
            for (Team team : teams) {
                if (TeamContent.teamList.contains(team)) {
                    TeamContent.updateTeam(team);

                } else {
                    super.add(team);
                }
            }
            super.notifyDataSetChanged();
        }
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
    public interface OnTeamClickListener {
        public void onTeamClick(int id);
    }

}
