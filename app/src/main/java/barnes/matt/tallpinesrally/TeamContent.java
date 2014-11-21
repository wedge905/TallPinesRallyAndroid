package barnes.matt.tallpinesrally;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matt on 11/16/2014.
 */
public class TeamContent {
    public static List<Team> teamList = new ArrayList<Team>();

    public static List<Team> getTeamList() {

        return teamList;
    }

    public static boolean Contains(Team team)
    {
        for (Team t : teamList) {
            if (t.CarNumber == team.CarNumber)
                    return true;

        }
        return false;
    }

   // public static Team getTeamById(int carNumber) {
//        if (teamList.size() > 0)
//            return teamList.get(carNumber - 1);
//        else
//            return null;
//    }

    //public static Team getTeamByCarNumber(int carNumber) {
//        for (Team t : teamList)
//        {
//            if (t.CarNumber == carNumber) {
//                return t;
//            }
//        }
//        return null;
//    }

    public static void addTeam(Team team) {
        teamList.add(team);
    }

    public static void updateTeam(Team team) {
        teamList.set(teamList.indexOf(team), team);
    }

    static {
        DatabaseHelper feeder = new DatabaseHelper(MainActivity.getInstance());
        Cursor teams = feeder.getAllTeams();

        if (teams.moveToFirst()) {
            do {
                teamList.add(new Team(teams.getString(0), teams.getString(1), teams.getString(2), teams.getString(3), teams.getString(4), teams.getString(5), teams.getString(6), teams.getString(7), teams.getInt(9), teams.getInt(8)));
            } while (teams.moveToNext());
        }
    }
}
