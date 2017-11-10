import java.util.HashMap;
import java.util.Map;
import edu.princeton.cs.algs4.In;

public class BaseballElimination {
    private Map<String, Integer> _teamNames;
    private int _numberOfTeams;
    private int[] _wins;
    private int[] _looses;
    private int[] _remain;
    private int[][] _games;
    private String[] _nameList;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        _teamNames = new HashMap<String, Integer>();

        In input = new In(filename);
        _numberOfTeams = input.readInt();
        _wins = new int[_numberOfTeams];
        _looses = new int[_numberOfTeams];
        _remain = new int[_numberOfTeams];
        _games = new int[_numberOfTeams][_numberOfTeams];
        _nameList = new String[_numberOfTeams];
        for (int n = 0; n < _numberOfTeams; n++) {
            String name = input.readString();
            _teamNames.put(name, n);
            _wins[n] = input.readInt();
            _looses[n] = input.readInt();
            _remain[n] = input.readInt();
            for (int m = 0; m < _numberOfTeams; m++) {
                _games[n][m] = input.readInt();
            }
            _nameList[n] = name;
        }

        input.close();
    }

    // number of teams
    public int numberOfTeams() {
        return _numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return _teamNames.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!_teamNames.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return _wins[_teamNames.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!_teamNames.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return _looses[_teamNames.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!_teamNames.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return _remain[_teamNames.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!_teamNames.containsKey(team1) || !_teamNames.containsKey(team2)) {
            throw new java.lang.IllegalArgumentException();
        }
        int a = _teamNames.get(team1);
        int b = _teamNames.get(team2);
        return _games[a][b];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        throw new UnsupportedOperationException();
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}