package barnes.matt.tallpinesrally;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String dbName="TP_db";

    static final String teamTable="team";
    static final String tweetTable="tweet";
    static final String settingsTable="settings";
    static final String tweetID="id";
    static final String tweetText="value";
    static final String tweetCreated="created_at";

    public DatabaseHelper(Context context) {
        //TODO: Implement version 6, add results table
        super(context, dbName, null, 5);  //db version 4 is same as 3, except starts fresh for 2013  //db version 5 is same as 4, but starts fresh for 2014

        //SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + tweetTable);
        //db.execSQL("DROP TABLE IF EXISTS " + teamTable);
        //db.execSQL("CREATE TABLE tweet (id TEXT PRIMARY KEY, value TEXT, created_at TEXT)");
        //db.execSQL("CREATE TABLE team (id TEXT PRIMARY KEY, name TEXT, driver TEXT, codriver TEXT, car TEXT, class TEXT, comment TEXT, photo TEXT, startorder TEXT, carnumber TEXT");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE tweet (id TEXT PRIMARY KEY, value TEXT, created_at TEXT)");
        db.execSQL("CREATE TABLE team (id TEXT PRIMARY KEY, name TEXT, driver TEXT, codriver TEXT, car TEXT, class TEXT, comment TEXT, photo TEXT, startorder INTEGER, carnumber INTEGER, version INTEGER)");

        db.execSQL("CREATE TABLE settings (setting TEXT PRIMARY KEY, value TEXT)");

        ContentValues defaultNewsUpdateInterval = new ContentValues();
        defaultNewsUpdateInterval.put("setting", "newsUpdateInterval");
        defaultNewsUpdateInterval.put("value", "30");

        ContentValues defaultNewsUpdate = new ContentValues();
        defaultNewsUpdate.put("setting", "updateNews");
        defaultNewsUpdate.put("value", "1");

        ContentValues defaultNewsNotify = new ContentValues();
        defaultNewsNotify.put("setting", "newsNotification");
        defaultNewsNotify.put("value", "1");

        db.insert("settings", "setting", defaultNewsUpdate);
        db.insert("settings", "setting", defaultNewsUpdateInterval);
        db.insert("settings", "setting", defaultNewsNotify);

        ContentValues defaultTeamUpdateInterval = new ContentValues();
        defaultTeamUpdateInterval.put("setting", "teamUpdateInterval");
        defaultTeamUpdateInterval.put("value", "24");

        ContentValues defaultTeamUpdate = new ContentValues();
        defaultTeamUpdate.put("setting", "updateTeam");
        defaultTeamUpdate.put("value", "1");

        ContentValues defaultTeamNotify = new ContentValues();
        defaultTeamNotify.put("setting", "teamNotification");
        defaultTeamNotify.put("value", "0");

        ContentValues defaultWifiOnly = new ContentValues();
        defaultWifiOnly.put("setting", "wifiOnly");
        defaultWifiOnly.put("value", "0");

        db.insert("settings", "setting", defaultTeamUpdate);
        db.insert("settings", "setting", defaultTeamUpdateInterval);
        db.insert("settings", "setting", defaultTeamNotify);
        db.insert("settings",  "setting", defaultWifiOnly);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + tweetTable);
        if (oldVersion == 1)
        {
            db.execSQL("DROP TABLE IF EXISTS " + teamTable);
            //db.execSQL("DROP TABLE IF EXISTS " + settingsTable);
            db.execSQL("CREATE TABLE team (id TEXT PRIMARY KEY, name TEXT, driver TEXT, codriver TEXT, car TEXT, class TEXT, comment TEXT, photo TEXT, startorder INTEGER, carnumber INTEGER, version INTEGER)");

            ContentValues defaultWifiOnly = new ContentValues();
            defaultWifiOnly.put("setting", "wifiOnly");
            defaultWifiOnly.put("value", "0");
            db.insert("settings",  "setting", defaultWifiOnly);
        }

        if (oldVersion == 2)
        {
            ContentValues defaultWifiOnly = new ContentValues();
            defaultWifiOnly.put("setting", "wifiOnly");
            defaultWifiOnly.put("value", "0");
            db.insert("settings",  "setting", defaultWifiOnly);
        }

        if (oldVersion < 5) //change for 2013, drop all old tables (except settings) and start fresh
        {
            db.execSQL("DROP TABLE IF EXISTS " + teamTable);
            db.execSQL("DROP TABLE IF EXISTS " + tweetTable);

            db.execSQL("CREATE TABLE team (id TEXT PRIMARY KEY, name TEXT, driver TEXT, codriver TEXT, car TEXT, class TEXT, comment TEXT, photo TEXT, startorder INTEGER, carnumber INTEGER, version INTEGER)");
            db.execSQL("CREATE TABLE tweet (id TEXT PRIMARY KEY, value TEXT, created_at TEXT)");
        }

        //TODO: Implement version 6, add results table
    }

    public int getDelay(String value)
    {
        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = null;
        String temp = "";

        if (value == "news")
        {
            temp = getValue("newsUpdateInterval");
            //cur=db.rawQuery("SELECT value FROM settings WHERE setting = \"newsUpdateInterval\"",new String [] {});
        }
        else if (value == "team")
        {
            temp = getValue("teamUpdateInterval");
            //cur=db.rawQuery("SELECT value FROM settings WHERE setting = \"teamUpdateInterval\"",new String [] {});
        }

        //cur.moveToFirst();
        int result = Integer.parseInt(temp);

        //db.close();
        return result;
    }

    public int getNotify(String value)
    {
        //SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = null;
        String temp = "";

        if (value == "news")
        {
            temp = getValue("newsNotification");
            //cur=db.rawQuery("SELECT value FROM settings WHERE setting = \"newsUpdateInterval\"",new String [] {});
        }
        else if (value == "team")
        {
            temp = getValue("teamNotification");
            //cur=db.rawQuery("SELECT value FROM settings WHERE setting = \"teamUpdateInterval\"",new String [] {});
        }

        //cur.moveToFirst();
        int result = Integer.parseInt(temp);

        //db.close();
        return result;
    }

    public int setValue(String setting, String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", value);

        int i = db.update("settings", values, "setting=\""+setting+"\"", null);
        db.close();

        return i;
    }

    public String getValue(String setting)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur=db.rawQuery("SELECT value FROM settings WHERE setting = \"" + setting + "\"",new String [] {});

        cur.moveToFirst();
        String result = cur.getString(0);

        db.close();

        return result;

    }

    public void insertTeam(Team team) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id", team.TeamId);
        cv.put("name", team.Name);
        cv.put("driver", team.Driver);
        cv.put("codriver", team.Codriver);
        cv.put("car", team.Car);
        cv.put("class", team.CarClass);
        cv.put("comment", team.Bio);
        cv.put("photo", team.Photo);
        cv.put("startorder", team.StartNumber);
        cv.put("carnumber", team.CarNumber);
        cv.put("version", team.Version);

        db.insert(teamTable, "id", cv);
        db.close();
    }

    private void insertTeam(String id, String name, String driver, String codriver, String car, String carclass, String comment, String photo, int startorder, int carnumber, int version) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("id", id);
        cv.put("name", name);
        cv.put("driver", driver);
        cv.put("codriver", codriver);
        cv.put("car", car);
        cv.put("class", carclass);
        cv.put("comment", comment);
        cv.put("photo", photo);
        cv.put("startorder", startorder);
        cv.put("carnumber", carnumber);
        cv.put("version", version);

        db.insert(teamTable, "id", cv);

        db.close();
    }

    private int deleteTeam(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int i = db.delete(teamTable, "id = " + id , null);
        db.close();

        return i;
    }

    public int getTeamCount()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, "team");

        //Cursor cur=db.rawQuery("SELECT id FROM team",new String [] {});

//		return cur.getCount();

    }

    public void updateTeam(Team team)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        //cv.put("id", team.id);
        cv.put("name", team.Name);
        cv.put("driver", team.Driver);
        cv.put("codriver", team.Codriver);
        cv.put("car", team.Car);
        cv.put("class", team.CarClass);
        cv.put("comment", team.Bio);
        cv.put("photo", team.Photo);
        cv.put("startorder", team.StartNumber);
        cv.put("carnumber", team.CarNumber);
        cv.put("version", team.Version);

        db.update(teamTable, cv, "id=" + team.TeamId, null);
    }

    public int updateTeam(String id, String name, String driver, String codriver, String car, String carclass, String comment, String photo, int startorder, int carnumber, int version)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        if (version == -1)
        {
            deleteTeam(id);

            return -1; //withdrew
        }
        else
        {
            Cursor cur=db.rawQuery("SELECT id, version FROM team WHERE id = " + id,new String [] {});

            if (cur.getCount() > 0)
            {
                //update
                cur.moveToFirst();
                int oldVersion = cur.getInt(1);

                if (version > oldVersion)
                {
                    Team old = getTeam(id);

                    deleteTeam(id);
                    insertTeam(id, name, driver, codriver, car, carclass, comment, photo, startorder, carnumber, version);

                    if (old.Driver == driver && old.Codriver == codriver && old.Car == car && old.Bio == comment && old.Photo == photo)
                    {
                        return 0; //no change worth notifying for.
                    }
                    else
                    {
                        return 2; //modified
                    }
                }
            }
            else
            {
                //insert
                insertTeam(id, name, driver, codriver, car, carclass, comment, photo, startorder, carnumber, version);

                return 1;
            }
        }
        return 0;  //no update
    }

    public Cursor getAllTeamsByStart()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT id, name, driver, codriver, car, class, comment, photo, startorder, carnumber FROM team ORDER BY startorder DESC",new String [] {});

        return cur;
    }

    public Cursor getAllTeams()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT id, name, driver, codriver, car, class, comment, photo, startorder, carnumber FROM team ORDER BY carnumber",new String [] {});

        return cur;
    }

    public Team getTeam(String id)
    {
        Team team = null;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT id, name, driver, codriver, car, class, comment, photo, startorder, carnumber FROM team WHERE id = '" + id +"'",new String [] {});

        if (cur.moveToFirst())
        {
            team = new Team();

            team.TeamId = cur.getString(0);
            team.Name = cur.getString(1);
            team.Driver = cur.getString(2);
            team.Codriver = cur.getString(3);
            team.Car = cur.getString(4);
            team.CarClass = cur.getString(5);
            team.Bio = cur.getString(6);
            team.Photo = cur.getString(7);
            team.StartNumber = cur.getInt(8);
            team.CarNumber = cur.getInt(9);
        }

        db.close();

        return team;
    }


  //  public Team getTeamByStartPos(int startPos)
//    {
//        Team team = new Team();
//
//        SQLiteDatabase db=this.getReadableDatabase();
//        Cursor cur=db.rawQuery("SELECT id, name, driver, codriver, car, class, comment, photo, startorder, carnumber FROM team WHERE startorder = " + Integer.toString(++startPos),new String [] {});
//
//        if (cur.moveToFirst())
//        {
//            team = new Team();
//
//            team.id = cur.getString(0);
//            team.name = cur.getString(1);
//            team.driver = cur.getString(2);
//            team.codriver = cur.getString(3);
//            team.car = cur.getString(4);
//            team.carclass = cur.getString(5);
//            team.bio = cur.getString(6);
//            team.photo = cur.getString(7);
//            team.startnumber = cur.getInt(8);
//            team.carnumber = cur.getInt(9);
//        }
//
//        db.close();
//
//        return team;
//    }

    public void insertTweet(String id, String text, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tweetID, id);
        cv.put(tweetText, text);
        cv.put(tweetCreated, date);

        db.insert(tweetTable, tweetID, cv);

        db.close();
    }

    public String getSinceID()
    {
        //String id = "519994250172592129"; //default starting id is the first tweet of the 2014 Tall Pines
        String id = "401415756945956864"; //default starting id is the first tweet of the 2013 Tall Pines
        //String id = "141901385259155456";  //default starting id is the first tweet of the 2012 Tall Pines
        //String id = "248163827118653440";  //test starting id for directdynamics

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur=db.rawQuery("SELECT id FROM tweet ORDER BY id DESC", new String [] {});

        if (cur.moveToFirst())
        {
            //do
            //{
            id = cur.getString(0);
//			} while(cur.moveToNext());
        }

        return id;
    }

    public Cursor getAllTweets()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cur=db.rawQuery("SELECT id, value, created_at FROM tweet ORDER BY id DESC",new String [] {});

        return cur;
    }

}