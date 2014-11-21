package barnes.matt.tallpinesrally;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 11/8/2014.
 */
public class NewsContent {

    private static List<NewsItem> news = new ArrayList<NewsItem>();
    private static int length = 0;

    public static List<NewsItem> getNews() {
        return news;
    }

    public static void addNews(NewsItem item) {
        news.add(item);
    }

    static {
        DatabaseHelper feed = new DatabaseHelper(MainActivity.getInstance());
        Cursor tweets = feed.getAllTweets();

        if (tweets.moveToFirst())
        {
            //int i = 0;
            do
            {
                news.add(new NewsItem(tweets.getString(1), tweets.getString(2), tweets.getString(0)));
               // rd = new RowData(i++, tweets.getString(1), tweets.getString(2));//, R.drawable.tp_golton);
                //data.add(rd);
            } while(tweets.moveToNext());
        }
    }

    public static int getNextId() {
        return length++;
    }
}


class NewsItem {
    public int Id;
    public String Text;
    public String Date;
    public String TweetId;

    public NewsItem(int id, String text, String date, String tweetId) {
        this.Id = id;
        this.Text = text;
        this.Date = date;
        this.TweetId = tweetId;
    }

    public NewsItem(String text, String date, String tweetId) {
        this.Id = NewsContent.getNextId();
        this.Text = text;
        this.Date = date;
        this.TweetId = tweetId;
    }
}
