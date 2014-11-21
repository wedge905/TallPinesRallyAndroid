package barnes.matt.tallpinesrally;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.IntentService;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.JsonReader;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;

public class TwitterFeedV3 extends IntentService{

    private Context context;
    private DatabaseHelper tweetDB;

    private int delay = 30;
    public static final String NEW_UPDATES = "barnes.matt.NEW_UPDATES";

    //private String twitterId = "directdynamics";
    private String twitterId = "tallpinesrally";
    //public String timelineURL = "https://api.twitter.com/1/statuses/user_timeline.xml?id=" + twitterId + "&since_id=";

    private String notifyText = "";
    private int notifyNumber = 0;

    public TwitterFeedV3()
    {
        super(TwitterFeedV3.class.getSimpleName());
    }

    public TwitterFeedV3(String name) {
        super(name);
    }

    public TwitterFeedV3(Context _context)
    {
        super(TwitterFeedV3.class.getSimpleName());

        context = _context;
        tweetDB = new DatabaseHelper(context);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        context = this.getApplicationContext();
        tweetDB = new DatabaseHelper(context);

        this.getNewTweets();

        if (intent.hasExtra("interval"))
        {
            delay = intent.getExtras().getInt("interval");

            this.scheduleNextUpdate();

        }

        tweetDB.close();
    }

    public void getNewTweets()
    {
        new DownloadTwitterTask().execute(twitterId, tweetDB.getSinceID());
    }

    public class Authenticated {
        String token_type;
        String access_token;
    }

    private void scheduleNextUpdate()
    {
        Intent intent = new Intent(this, this.getClass());
        intent.putExtra("interval", delay); //long interval = delay * 60000;
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, delay);

        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    // Uses an AsyncTask to download a Twitter user's timeline
    private class DownloadTwitterTask extends AsyncTask<String, NewsItem, Twitter> {
        final static String CONSUMER_KEY = "NLKJ5K6CM5v1blyLJMsmTA";
        final static String CONSUMER_SECRET = "Se0jYf1DA2sE9U0xUtDM6JCdOgL9iN4wtt7fCsSryg";
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

        @Override
        protected Twitter doInBackground(String... params) {
            String result = null;

            if (params.length > 0) {
                result = getTwitterStream(params[0], params[1]);
            }

            Twitter twits = jsonToTwitter(result);

            //int number = 0;
            for (Tweet tweet : twits) {
                String date = tweet.getDateCreated();
                String id = tweet.getId();
                String text = tweet.getText();
                notifyText = text;
                //number++;

                tweetDB.insertTweet(id, text, date);


                NewsItem item = new NewsItem(text, date, id);
               // NewsContent.News.add(item);
               // NewsListFragment.getInstance().getAdapter().add(item);
                publishProgress(item);

            }

            return twits;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(Twitter twits) {
           // Twitter twits = jsonToTwitter(result);

//            int number = 0;
//            for (Tweet tweet : twits) {
//                String date = tweet.getDateCreated();
//                String id = tweet.getId();
//                String text = tweet.getText();
//                notifyText = text;
//                number++;
//
//                tweetDB.insertTweet(id, text, date);
//
//                NewsContent.NewsItem item = new NewsContent.NewsItem(0, tweet.getText(), tweet.getDateCreated(), tweet.getId());
//                NewsContent.News.add(item);
//                NewsListFragment.getInstance().getAdapter().add(item);
//            }
//
//            if (tweetDB.getNotify("news") == 1)
//            {
//                //notifyNumber = nl.getLength();
//                notify(number, notifyText);
//            }
        }

        @Override
        protected void onProgressUpdate(NewsItem... items) {
            //NewsContent.NewsItem item = new NewsContent.NewsItem(0, tweet.getText(), tweet.getDateCreated(), tweet.getId());
                //NewsContent.addNews(items[0]);
                NewsListFragment.getInstance().getAdapter().add(items[0]);
        }

        private void notify(int number, String text)
        {
            //String ns = Context.NOTIFICATION_SERVICE;
            //NotificationManager nm =(NotificationManager)context.getSystemService(ns);

            //Intent noteIntent = new Intent(context, TweetList.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, noteIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            //NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            //builder.setContentIntent(pendingIntent)
              //      .setWhen(System.currentTimeMillis())
                //    .setTicker(text)
                    //.setSmallIcon(R.drawable.notify_icon)
                  //  .setContentTitle("Official Tall Pines Rally Update")
                   // .setContentText(text)
                   // .setNumber(number);

            //nm.notify(1, builder.getNotification());

            //broadcast();
        }

        // converts a string of JSON data into a Twitter object
        private Twitter jsonToTwitter(String result) {
            Twitter twits = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    twits = gson.fromJson(result, Twitter.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return twits;
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getTwitterStream(String screenName, String sinceId) {
            String results = null;

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
                String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

                // Concatenate the encoded consumer key, a colon character, and the
                // encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);
                Authenticated auth = jsonToAuthenticated(rawAuthorization);

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.token_type.equals("bearer")) {

                    // Step 3: Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName + "&since_id=" + sinceId + "&include_rts=true&exclude_replies=true");

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                    httpGet.setHeader("Content-Type", "application/json");
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }
    }
}
