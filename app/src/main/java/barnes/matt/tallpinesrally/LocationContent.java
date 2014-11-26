package barnes.matt.tallpinesrally;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class LocationContent {

    public static List<LocationItem> Locations = new ArrayList<LocationItem>();

    static {
        Resources res = MainActivity.getInstance().getResources();
        String[] locationArray = res.getStringArray(R.array.locationsArray);
        String[] locationDetail = res.getStringArray(R.array.locationDetailsArray);
        String[] locationDetailLong = res.getStringArray(R.array.locationDetailsLongArray);
        String[] locationImages = res.getStringArray(R.array.locationImagesArray);
        String[] locationDirection = res.getStringArray(R.array.locationGeoArray);

        for (int i = 0; i < locationArray.length; i++) {
            Locations.add(new LocationItem(i, locationArray[i], locationDetail[i], locationDetailLong[i], locationImages[i], locationDirection[i]));
        }
    }

    public static class LocationItem {
        public int Id;
        public String Name;
        public String Description;
        public String LongDescription;
        public String ImageName;
        public String Direction;

        public  LocationItem(int id, String name, String description, String LongDesc, String image, String dir) {
            this.Id = id;
            this.Name = name;
            this.Description = description;
            this.LongDescription = LongDesc;
            this.ImageName = image;
            this.Direction = dir;
        }
    }
}
