package barnes.matt.tallpinesrally;

import android.graphics.drawable.Drawable;
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

    private String Title;
    private String Description;
    private String Image;

    public static LocationDetailFragment newInstance(String title, String description, String image) {
        LocationDetailFragment fragment = new LocationDetailFragment();

        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(DESCRIPTION, description);
        args.putString(IMAGE, image);
        fragment.setArguments(args);

        return fragment;
    }
    public LocationDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Title = getArguments().getString(TITLE);
            Description = getArguments().getString(DESCRIPTION);
            Image = getArguments().getString(IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_detail, container, false);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        titleView.setText(Title);

        ImageView imageView = (ImageView)view.findViewById(R.id.image);
        imageView.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(Image, "drawable", getActivity().getPackageName())));

        TextView descView = (TextView)view.findViewById(R.id.description);
        descView.setText(Description);

        // Inflate the layout for this fragment
        return view;
    }


}
