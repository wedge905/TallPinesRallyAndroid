package barnes.matt.tallpinesrally;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamDetailFragment extends Fragment {

    private Team thisTeam;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamDetailFragment.
     */
    public static TeamDetailFragment newInstance(Team team) {
        TeamDetailFragment fragment = new TeamDetailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public TeamDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_detail, container, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.tp_golton));

        TextView teamName = (TextView)view.findViewById(R.id.teamName);
        teamName.setText("Alien Racing Team");
        TextView driverName = (TextView)view.findViewById(R.id.driver);
        driverName.setText("Matt B");
        TextView coDriverName = (TextView)view.findViewById(R.id.codriver);
        coDriverName.setText("Nat B");
        TextView website = (TextView)view.findViewById(R.id.website);
        website.setText("http://www.directdynamics.ca");
        TextView carNumber = (TextView)view.findViewById(R.id.carnumber);
        carNumber.setText("27");
        TextView bio = (TextView)view.findViewById(R.id.teamBio);
        bio.setText("Just imagine if you took a small car, gave it four-wheel-drive, turbocharged it to over 300 horsepower, and then drove it flat-out sideways down twisty treacherous forest roads – lined with trees, rocks and cheering fans. Kicking up dirt or snow, spitting flames. Sliding on the edge of control, and sometimes over it! This is rally racing at the Rally of the Tall Pines and it is taking the world by storm");

        return view;
        //return inflater.inflate(R.layout.fragment_team_detail, container, false);
    }


}
