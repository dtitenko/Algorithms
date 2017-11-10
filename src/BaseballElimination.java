import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

public class BaseballElimination {
    private Map<String, Integer> _teamNames;
    private int _numberOfTeams;
    private int[] _wins;
    private int[] _looses;
    private int[] _remain;
    private int[][] _games;
    private String[] _teams;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        _teamNames = new HashMap<String, Integer>();

        In input = new In(filename);
        _numberOfTeams = input.readInt();
        _wins = new int[_numberOfTeams];
        _looses = new int[_numberOfTeams];
        _remain = new int[_numberOfTeams];
        _games = new int[_numberOfTeams][_numberOfTeams];
        _teams = new String[_numberOfTeams];
        for (int n = 0; n < _numberOfTeams; n++) {
            String name = input.readString();
            _teamNames.put(name, n);
            _wins[n] = input.readInt();
            _looses[n] = input.readInt();
            _remain[n] = input.readInt();
            for (int m = 0; m < _numberOfTeams; m++) {
                _games[n][m] = input.readInt();
            }
            _teams[n] = name;
        }

        input.close();
    }

    // number of teams
    public int numberOfTeams() {
        return _numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(_teams);
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
        if (!_teamNames.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        int x = _teamNames.get(team);

        // Trivial elimination
        for (int i = 0; i < _numberOfTeams; i++) {
            if (_wins[x] + _remain[x] - _wins[i] < 0) {
                return true;
            }
        }

        // Nontrivial elimination
        FlowNetwork flowNetwork = buildFlowNetwork(team);
        int s = flowNetwork.V() - 2;
        int t = flowNetwork.V() - 1;
        new FordFulkerson(flowNetwork, s, t);

        for (FlowEdge e : flowNetwork.adj(s)) {
            if (e.flow() != e.capacity()) {
                return true;
            }
        }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!_teamNames.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        int x = _teamNames.get(team);

        List<String> nList = new ArrayList<String>();
        for (int i = 0; i < _numberOfTeams; i++) {
            if (_wins[x] + _remain[x] - _wins[i] < 0)
                nList.add(_teams[i]);
        }
        if (nList.size() > 0) {
            return nList;
        }

        FlowNetwork flowNetwork = buildFlowNetwork(team);
        int s = flowNetwork.V() - 2;
        int t = flowNetwork.V() - 1;
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, s, t);

        boolean flag = false;
        for (FlowEdge e : flowNetwork.adj(s)) {
            if (e.flow() != e.capacity()) {
                flag = true;
                break;
            }
        }

        if (!flag) {
            // team is not eliminated
            return null;
        } else {
            List<Integer> rPathNodes = residualPath(flowNetwork, s);
            List<String> nodeList = new ArrayList<String>();
            int teamMatches = _numberOfTeams * (_numberOfTeams - 1) / 2;
            for (Integer v : rPathNodes) {
                if (fordFulkerson.inCut(v) && v >= teamMatches) {
                    nodeList.add(_teams[v - teamMatches]);
                }
            }
            return nodeList;
        }
    }

    private FlowNetwork buildFlowNetwork(String team) {
        int x = _teamNames.get(team);
        int teamMatches = _numberOfTeams * (_numberOfTeams - 1) / 2;
        int nodeID = 0;
        FlowNetwork flowNetwork = new FlowNetwork(teamMatches + _numberOfTeams + 2);
        int s = teamMatches + _numberOfTeams;
        int t = s + 1;

        for (int i = 0; i < _numberOfTeams; i++) {
            for (int j = i + 1; j < _numberOfTeams; j++) {
                if (i == j) {
                    continue;
                }
                flowNetwork.addEdge(new FlowEdge(s, nodeID, _games[i][j]));
                flowNetwork.addEdge(new FlowEdge(nodeID, teamMatches + i, Integer.MAX_VALUE));
                flowNetwork.addEdge(new FlowEdge(nodeID, teamMatches + j, Integer.MAX_VALUE));
                nodeID += 1;
            }
            flowNetwork.addEdge(new FlowEdge(teamMatches + i, t, Math.max(0, _wins[x] + _remain[x] - _wins[i])));
        }

        return flowNetwork;
    }

    private List<Integer> residualPath(FlowNetwork graph, int node) {
        Queue<Integer> queue = new Queue<Integer>();
        boolean[] visited = new boolean[graph.V()];
        queue.enqueue(node);
        visited[node] = true;
        List<Integer> nodeList = new ArrayList<Integer>();
        while (!queue.isEmpty()) {
            int cn = queue.dequeue();
            for (FlowEdge e : graph.adj(cn)) {
                int t = e.from() == cn ? e.to() : e.from();
                if (e.residualCapacityTo(t) > 0) {
                    if (!visited[t]) {
                        queue.enqueue(t);
                        nodeList.add(t);
                        visited[t] = true;
                    }
                }
            }
        }
        return nodeList;
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