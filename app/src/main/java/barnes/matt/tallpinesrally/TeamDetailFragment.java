package barnes.matt.tallpinesrally;


import android.app.Activity;
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
        //Bundle args = new Bundle();

        //fragment.setArguments(args);
        fragment.thisTeam = team;
        return fragment;
    }

    public TeamDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (thisTeam == null) {
            int num = savedInstanceState.getInt("CarNumber", 0);
            if (num > 0)
                thisTeam = TeamContent.getTeamById(num);

           // getFragmentManager().beginTransaction().replace(R.id.container, this, "TEAMDETAIL").addToBackStack(null).commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CarNumber", thisTeam.CarNumber);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.getInstance().currentfrag = "TEAMLIST";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //7
        View view = inflater.inflate(R.layout.fragment_team_detail, container, false);

        if (savedInstanceState != null) {
            thisTeam = TeamContent.getTeamById(savedInstanceState.getInt("CarNumber"));
        }

        ImageView imageView = (ImageView)view.findViewById(R.id.icon);
        if (thisTeam.Photo.length() > 2) {
            ImageManager im = new ImageManager(MainActivity.getInstance());
            im.fetchDrawableOnThread(thisTeam.Photo, imageView);
        }
        else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.tp_golton));
        }

        TextView teamName = (TextView)view.findViewById(R.id.teamName);
        if (thisTeam.Name.length() >= 1) {
            teamName.setText(thisTeam.Name);
        }
        else {
            teamName.setText(thisTeam.Driver);
        }

        //teamName.setText("Alien Racing Team");
        TextView driverName = (TextView)view.findViewById(R.id.driver);
        TextView coDriverName = (TextView)view.findViewById(R.id.codriver);
        TextView carName = (TextView)view.findViewById(R.id.car);

        int currentOrientation = getActivity().getResources().getConfiguration().orientation;

        if (currentOrientation == 2) {
            driverName.setText("Driver:\r\n" + thisTeam.Driver);
            //driverName.setText("Matt B");

            coDriverName.setText("CoDriver:\r\n" + thisTeam.Codriver);
            carName.setText("Car:\r\n" + thisTeam.Car);
        }
        else {
            driverName.setText("Driver: " + thisTeam.Driver);
            //driverName.setText("Matt B");

            coDriverName.setText("CoDriver: " + thisTeam.Codriver);
            carName.setText("Car: " + thisTeam.Car);
        }
        //coDriverName.setText("Nat B");
        //TextView website = (TextView)view.findViewById(R.id.website);
        //website.setText("http://www.directdynamics.ca");



        TextView carNumber = (TextView)view.findViewById(R.id.carnumber);
        carNumber.setText("Car # " + thisTeam.CarNumber);

        TextView carClass = (TextView)view.findViewById(R.id.carClass);
        carClass.setText("Class: " + thisTeam.CarClass);
        //carNumber.setText("27");
        TextView bio = (TextView)view.findViewById(R.id.teamBio);
        bio.setText(thisTeam.Bio);
        //bio.setText("Just imagine if you took a small car, gave it four-wheel-drive, turbocharged it to over 300 horsepower, and then drove it flat-out sideways down twisty treacherous forest roads – lined with trees, rocks and cheering fans. Kicking up dirt or snow, spitting flames. Sliding on the edge of control, and sometimes over it! This is rally racing at the Rally of the Tall Pines and it is taking the world by storm");

        return view;
        //return inflater.inflate(R.layout.fragment_team_detail, container, false);
    }


}
