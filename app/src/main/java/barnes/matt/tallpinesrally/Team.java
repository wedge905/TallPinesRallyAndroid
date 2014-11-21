package barnes.matt.tallpinesrally;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import org.w3c.dom.Element;

//import android.database.Cursor;

class Team implements Comparable<Team> {

    public String TeamId;
    public String Name;
    public String Driver;
    public String Codriver;
    public String Car;
    public String CarClass;
    public String Bio;
    public String Photo;
    public int CarNumber;
    public int StartNumber;
    public boolean Withdrawn;
    public int Version;
   // public Date LastModified;
    public String Website;
   // public String tpBestResult;

    @Override
    public boolean equals(Object o) {
        Team t = (Team)o;
        return t.CarNumber == this.CarNumber;
    }

    @Override
    public int compareTo(Team t) {
        return t.CarNumber - CarNumber;
    }

    private DateFormat format = DateFormat.getInstance();

    public Team()
    {

    }

    public Team(String _id, String _name, String _driver, String _codriver, String _car, String _carclass, String _bio, String _photo, int _carnumber, int _startnumber)
    {
        TeamId = _id;
        Name = _name;
        Driver = _driver;
        Codriver = _codriver;
        Car = _car;
        CarClass = _carclass;
        Bio = _bio;
        Photo = _photo;
        CarNumber = _carnumber;
        StartNumber = _startnumber;
    }

//    public Team(Element e) throws ParseException
//    {
//        XMLParser parser = new XMLParser();
//
//        id = parser.getValue(e, "id");
//        name = parser.getValue(e, "name");
//        driver = parser.getValue(e, "driver");
//        codriver = parser.getValue(e, "codriver");
//        car = parser.getValue(e, "car");
//        carclass = parser.getValue(e, "carclass");
//        bio = parser.getValue(e, "bio");
//        photo = parser.getValue(e, "photo");
//        carnumber = parser.getIntValue(e, "carnumber");
//        startnumber = parser.getIntValue(e, "startnumber");
//        withdrawn = parser.getValue(e, "withdrawn");
//        Website = parser.getValue(e, "website");
//        tpBestResult = parser.getValue(e, "bestresult");
//
//        lastmodified = format.parse(parser.getValue(e, "lastmodified"));
//
//        String tempVersion = parser.getValue(e, "version");
//        if (tempVersion.length() == 0)
//        {
//            version = 1;
//        }
//        else
//        {
//            version = Integer.parseInt(tempVersion);
//        }
//        if (withdrawn.equals(true))
  //      {
//            version = -1;
//        }
//    }

  //  public Team newVersion()
//    {
//        if (this.withdrawn.equals(false))
//            this.version += 1;
//
//        return this;
//    }

    //public String version()
    //{
//        return Integer.toString(version);
//    }
}