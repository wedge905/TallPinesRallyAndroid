package barnes.matt.tallpinesrally;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class TeamFeed extends IntentService {

    private Context context;
    private DatabaseHelper db;

    public TeamFeed() { super("TeamFeed"); }

    public TeamFeed(Context _context) {
        super("TeamFeed");
        context = _context;
        db = new DatabaseHelper(context);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
        }
    }

    public void getTeamUpdates(Boolean liveUpdate)
    {
        SharedPreferences settings = MainActivity.getInstance().getSharedPreferences("TallPines", Context.MODE_PRIVATE);
        int teamVersion = settings.getInt("teamversion", 0);

        DownloadTeamsTask task = new DownloadTeamsTask(liveUpdate);
        task.execute(teamVersion);
    }

    public void getTeamUpdates(int ver) {
        SharedPreferences settings = MainActivity.getInstance().getSharedPreferences("TallPines", Context.MODE_PRIVATE);
        int teamVersion = settings.getInt("teamversion", 0);

        if (ver > teamVersion) {
            DownloadTeamsTask task = new DownloadTeamsTask();
            task.execute(teamVersion);
        }
    }

    private class DownloadTeamsTask extends AsyncTask<Integer, Team, Team[]> {

        private Boolean liveUpdate = false;
        public DownloadTeamsTask() {

        }

        public DownloadTeamsTask(Boolean update) {
            liveUpdate = update;
        }

        @Override
        protected Team[] doInBackground(Integer... params) {

            List<Team> TeamList = getUpdatedTeams(params[0]);

            for (Team team : TeamList) {
                if (team != null) {
                    if (db.getTeam(team.TeamId) == null) {
                        db.insertTeam(team);
                    } else {
                        db.updateTeam(team);
                    }

                        if (team.Photo.length() > 2) {
                            ImageManager im = new ImageManager(MainActivity.getInstance());
                            im.DownloadFromUrl(team.Photo, team.Photo);
                        }


                    // if (liveUpdate) {
                    publishProgress(team);
                    //}
                }
            }

            return TeamList.toArray(new Team[TeamList.size()]);
        }

        @Override
        protected void onPostExecute(Team... teams) {

            if (!liveUpdate) {
                TeamListFragment.getInstance().getAdapter().addAll(teams);
            }
        }

        @Override
        protected void onProgressUpdate(Team... teams) {
            TeamListFragment.getInstance().getAdapter().add(teams[0]);
        }

        private List<Team> getUpdatedTeams(int teamVersion) {
            String TeamUrl = "http://wedge1-001-site1.mywindowshosting.com/api/tallpines/teamsince/";

            List<Team> t = new ArrayList<Team>();
            DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());

            HttpResponse response = null;
            try {
                response = client.execute(new HttpGet(TeamUrl + teamVersion));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();

                    InputStreamReader reader = null;
                    try {
                        reader = new InputStreamReader(entity.getContent(), "UTF-8");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Gson gson = new Gson();
                    t = gson.fromJson(reader, new TypeToken<List<Team>>() {}.getType());
                }
                else {
                 //   return null;
                }
            int newVersion = teamVersion;
            for (Team team : t)
            {
                if (team.Version > newVersion)
                    newVersion = team.Version;
            }
            if (t.size() > 0)
                MainActivity.getInstance().getSharedPreferences("TallPines", Context.MODE_PRIVATE).edit().putInt("teamversion", newVersion).commit();

            return t;
        }
    }
}
