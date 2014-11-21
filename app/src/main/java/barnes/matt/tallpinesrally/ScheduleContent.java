package barnes.matt.tallpinesrally;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class ScheduleContent {

    public static List<ScheduleItem> Schedule = new ArrayList<ScheduleItem>();

    static {
        Resources res = MainActivity.getInstance().getResources();
        String[] ScheduleArray = res.getStringArray(R.array.scheduleArray);
        String[] ScheduleDetail = res.getStringArray(R.array.scheduleDetailsArray);
        String[] ScheduleWhen = res.getStringArray(R.array.scheduleWhenArray);
        int[] ScheduleLocations = res.getIntArray(R.array.scheduleLocationsArray);

        for (int i = 0; i < ScheduleArray.length; i++ ) {
            Schedule.add(new ScheduleItem(i, ScheduleArray[i], ScheduleDetail[i], ScheduleWhen[i], ScheduleLocations[i]));
        }
    }

    public static class ScheduleItem {
        public int Id;
        public String Name;
        public String Description;
        public String When;
        public int locationId;

        public  ScheduleItem(int id, String name, String description, String when, int locId) {
            this.Id = id;
            this.Name = name;
            this.Description = description;
            this.When = when;
            this.locationId = locId;
        }
    }
}
