public class BaseballElimination {
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
    }

    // number of teams
    public int numberOfTeams() {
        throw new UnsupportedOperationException();
    }

    // all teams
    public Iterable<String> teams() {
        throw new UnsupportedOperationException();
    }

    // number of wins for given team
    public int wins(String team) {
        throw new UnsupportedOperationException();
    }

    // number of losses for given team
    public int losses(String team) {
        throw new UnsupportedOperationException();
    }

    // number of remaining games for given team
    public int remaining(String team) {
        throw new UnsupportedOperationException();
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        throw new UnsupportedOperationException();
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