package barnes.matt.tallpinesrally;

/**
 * Created by Matt on 11/24/2014.
 */
public class Result implements Comparable<Result> {
    public String Id;
    public int CarNumber;
    public int Position;
    public int ClassPosition;
    public String TotalTime;
    public String DiffLeader;
    public String DiffPrev;

    @Override
    public boolean equals(Object o) {
        return Id == ((Result)o).Id;
    }

    @Override
    public int compareTo(Result t) {
        return t.Position - Position;
    }
}
