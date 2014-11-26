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
    @Override
    protected Void doInBackground(Boolean... params) {

        String url;

        if (params[0] == true)
            url = "http://wedge1-001-site1.mywindowshosting.com/api/tallpines/addtracker/";
        else
            url = "http://wedge1-001-site1.mywindowshosting.com/api/tallpines/deletetracker/";

        try {
            HttpPost post = new HttpPost(url);
            post.setEntity(new StringEntity("{ \"RegId\":\"" + MainActivity.getInstance().getGCMId() + "\" }"));
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");

            DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());

            client.execute(post);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
