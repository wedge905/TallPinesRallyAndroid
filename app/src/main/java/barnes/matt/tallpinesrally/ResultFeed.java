package barnes.matt.tallpinesrally;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Matt on 11/24/2014.
 */
public class ResultFeed extends AsyncTask<Void, Result, Void> {

    private String url ="http://wedge1-001-site1.mywindowshosting.com/api/tallpines/";

    public ResultFeed() {

    }

    public static void Execute() {
        new ResultFeed().execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Empty the current adapter
        ResultListFragment.GetInstance().GetAdapter().clear();
    }

    @Override
    protected Void doInBackground(Void... params) {

        boolean isLive = false;

        try {
            HttpGet Get = new HttpGet(url +"eventislive");

            DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
            HttpResponse response = client.execute(Get);

            if (EntityUtils.toString(response.getEntity()).equals("true"))
                isLive = true;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isLive)
        {
            try {
                HttpGet Get = new HttpGet(url +"results");

                DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = client.execute(Get);

                //proccessing
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();

                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(entity.getContent(), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    List<Result> results = gson.fromJson(reader, new TypeToken<List<Result>>() {}.getType());

                    for (Result r : results)
                    {
                        publishProgress(r);
                    }
                    // Now write those results to the db
                    // And print them on the screen
                }
                else {
                    //   return null;
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Result... result) {
        //super.onProgressUpdate(result);
        if (ResultListFragment.GetInstance() != null)
            if (ResultListFragment.GetInstance().GetAdapter() != null)
                ResultListFragment.GetInstance().GetAdapter().add(result[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}
