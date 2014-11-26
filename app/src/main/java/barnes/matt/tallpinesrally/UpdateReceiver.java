package barnes.matt.tallpinesrally;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Matt on 11/23/2014.
 */
public class UpdateReceiver extends BroadcastReceiver {

    public static final String ACTION_RESP = "barnes.matt.tallpinesrally.UPDATE_TRACKER";

    public UpdateReceiver()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TrackerFragment.getInstance().updateMarkerList();
    }
}