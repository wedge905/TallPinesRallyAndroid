package barnes.matt.tallpinesrally;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationDetailFragment extends Fragment {
    private static final String TITLE = "Title";
    private static final String DESCRIPTION = "Description";
    private static final String IMAGE = "Image";
    private static final String DIRECTION = "Direction";

    private String Title;
    private String Description;
    private String Image;
    private String Direction;

    public static LocationDetailFragment newInstance(String title, String description, String image, String dir) {
        LocationDetailFragment fragment = new LocationDetailFragment();

        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(DESCRIPTION, description);
        args.putString(IMAGE, image);
        args.putString(DIRECTION, dir);
        fragment.setArguments(args);

        return fragment;
    }
    public LocationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        MainActivity.getInstance().currentfrag = "LOCATIONLIST";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Title = getArguments().getString(TITLE);
            Description = getArguments().getString(DESCRIPTION);
            Image = getArguments().getString(IMAGE);
            Direction = getArguments().getString(DIRECTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_detail, container, false);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        titleView.setText(Title);

        ImageView imageView = (ImageView)view.findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(Image, "drawable", getActivity().getPackageName())));

        TextView map = (TextView)view.findViewById(R.id.directions);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(Direction));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }
        });

        TextView descView = (TextView)view.findViewById(R.id.description);
        descView.setText(Description);

        // Inflate the layout for this fragment
        return view;
    }


}
