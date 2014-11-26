package barnes.matt.tallpinesrally;

import android.os.AsyncTask;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.IOException;

/**
 * Created by Matt on 11/23/2014.
 */
public class TrackerUpdateTask extends AsyncTask<Boolean, Void, Void>
{
    String id;
    @Override
    protected Void doInBackground(Boolean... params) {

        String url;

        if (params[0] == true)
            url = "http://wedge1-001-site1.mywindowshosting.com/api/tallpines/addtracker/";
        else
            url = "http://wedge1-001-site1.mywindowshosting.com/api/tallpines/deletetracker/";

        try {
            HttpPost post = new HttpPost(url);
            //String id = MainActivity.getInstance().getGCMId();
            //if (id.isEmpty() || id.equals("null"))
            // {
            // Thread.sleep(10000); // 10 seconds
            //   // wait for a few seconds and check again
            // id = MainActivity.getInstance().getGCMId();
            // if (id.isEmpty() || id.equals("null"))
            //   return null;
            // }
            CheckId();

            post.setEntity(new StringEntity("{ \"RegId\":\"" + id + "\" }"));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());

            client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
            //  } catch (InterruptedException e) {
            //    e.printStackTrace();
            // }
        }
            return null;

    }

    private boolean CheckId() {
        id = MainActivity.getInstance().getGCMId();

        if (id.isEmpty() || id.equals("null"))
        {
            // wait for a few seconds and check again
            return CheckId(1);
        }
        else
            return true;
    }

    private boolean CheckId(int retry) {
        if (retry > 3)
            return false;
        else
        {
            try {
                Thread.sleep(10000); // 10 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            id = MainActivity.getInstance().getGCMId();
            if (id.isEmpty() || id.equals("null"))
                return CheckId(++retry);
            else
                return true;

        }
    }
}
