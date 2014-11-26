package barnes.matt.tallpinesrally;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Matt on 11/23/2014.
 */
public class TrackerMarker {
    public String CarNumber;
    public Double Longitude;
    public Double Latitude;
    public String Speed;
    public long Nonce;

    public MarkerOptions markerOptions = new MarkerOptions()
            .flat(false)
            .anchor(0.5f,0.5f)
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_blue));

    @Override
    public boolean equals(Object o) {
        return CarNumber.equals(((TrackerMarker)o).CarNumber);
    }
}
