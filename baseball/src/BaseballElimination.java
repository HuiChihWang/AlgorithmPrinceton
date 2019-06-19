import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;

public class BaseballElimination {

    private int numberTeams;
    private int[] winsRecord;
    private int[] lossRecord;
    private int[] remainingsRecord;
    private int[][] againstRecord;
    private HashMap<String, Integer> mapNameIndex;
    private HashMap<Integer, String> mapIndexName;
    private HashMap<Integer, HashSet<String>> mapEliminateCertificates;

    public BaseballElimination(String filename) {
        parseTeamsFile(filename);
        eliminateTrivialCase();
        eliminateNontrivial();
    }

    public int numberOfTeams() {
        return numberTeams;
    }

    public Iterable<String> teams() {
        return mapNameIndex.keySet();
    }

    public int wins(String team) {
        return winsRecord[mapNameIndex.get(team)];
    }

    public int losses(String team) {
        return lossRecord[mapNameIndex.get(team)];
    }

    public int remaining(String team) {
        return remainingsRecord[mapNameIndex.get(team)];
    }

    public int against(String team1, String team2) {
        int team1Idx = mapNameIndex.get(team1);
        int tema2Idx = mapNameIndex.get(team2);
        return againstRecord[team1Idx][tema2Idx];
    }

    public boolean isEliminated(String team) {
        return  mapEliminateCertificates.containsKey(mapNameIndex.get(team));
    }

    public Iterable<String> certificateOfElimination(String team) {
        return mapEliminateCertificates.get(mapNameIndex.get(team));
    }

    private void parseTeamsFile(String filename) {
        In fileReader = new In(filename);
        String[] allLines = fileReader.readAllLines();

        numberTeams = Integer.parseInt(allLines[0]);

        initializeContainer();
        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            String[] fields = allLines[teamIdx + 1].split("\\s+");

            mapNameIndex.put(fields[0], teamIdx);
            mapIndexName.put(teamIdx, fields[0]);
            winsRecord[teamIdx] = Integer.parseInt(fields[1]);
            lossRecord[teamIdx] = Integer.parseInt(fields[2]);
            remainingsRecord[teamIdx] = Integer.parseInt(fields[3]);

            for (int otherIdx = 0; otherIdx < numberTeams; ++otherIdx) {
                againstRecord[teamIdx][otherIdx] = Integer.parseInt(fields[4 + otherIdx]);
            }
        }
    }

    private void initializeContainer() {
        mapNameIndex = new HashMap<>();
        mapIndexName = new HashMap<>();
        mapEliminateCertificates = new HashMap<>();

        winsRecord = new int[numberTeams];
        lossRecord = new int[numberTeams];
        remainingsRecord = new int[numberTeams];
        againstRecord = new int[numberTeams][];

        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            againstRecord[teamIdx] = new int[numberTeams];
        }
    }

    private void eliminateTrivialCase(){
        for (int iTeamIdx = 0; iTeamIdx < numberTeams; ++iTeamIdx) {

            HashSet<String> setCertificates = new HashSet<>();

            for (int otherTeamIdx = 0; otherTeamIdx < numberTeams; ++otherTeamIdx) {
                if (winsRecord[iTeamIdx] + remainingsRecord[iTeamIdx] < winsRecord[otherTeamIdx]) {
                    setCertificates.add(mapIndexName.get(otherTeamIdx));
                }
            }

            if (!setCertificates.isEmpty()) {
                mapEliminateCertificates.put(iTeamIdx, setCertificates);
            }
        }
    }

    private void eliminateNontrivial() {

        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            if (!mapEliminateCertificates.containsKey(teamIdx)) {
                eliminateGivenTeam(teamIdx);
            }
        }
    }

    private void eliminateGivenTeam(int teamIdx) {
        FlowNetwork flowNet = constructFlowNetwork(teamIdx);
        FordFulkerson maxFlowSolution = new FordFulkerson(flowNet, 0, flowNet.V() -1);

        if (isTeamEliminate(maxFlowSolution, teamIdx)) {
        }



    }

    private FlowNetwork constructFlowNetwork(int team) {
        int gameVertex = (numberTeams - 1) * (numberTeams - 2) / 2;
        int vertexNumber = gameVertex + 2 + numberTeams - 1;

        FlowNetwork flowNet = new FlowNetwork(vertexNumber);

        int teamVertexStart = gameVertex + 1;
        HashMap<Integer, Integer> mapTeamVertex = new HashMap<>();
        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            if (teamIdx != team) {
                mapTeamVertex.put(teamIdx, teamVertexStart);
                teamVertexStart += 1;
            }
        }

        int gameVertexCount = 1;
        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            for (int otherIdx = teamIdx + 1; otherIdx < numberTeams; ++otherIdx) {
                if (teamIdx != team && otherIdx != team) {
                    double capacity = (double) againstRecord[teamIdx][otherIdx];
                    FlowEdge edgeToGame = new FlowEdge(0, gameVertexCount, capacity);
                    flowNet.addEdge(edgeToGame);

                    FlowEdge edgeToTeam = new FlowEdge(gameVertexCount, mapTeamVertex.get(teamIdx), Double.POSITIVE_INFINITY);
                    FlowEdge edgeToOther = new FlowEdge(gameVertexCount, mapTeamVertex.get(otherIdx), Double.POSITIVE_INFINITY);

                    flowNet.addEdge(edgeToTeam);
                    flowNet.addEdge(edgeToOther);

                    gameVertexCount += 1;
                }
            }
        }

        int targetVertex = vertexNumber - 1;
        for (Integer teamVertex: mapTeamVertex.keySet()) {
            double capacity = (double) (winsRecord[team] + remainingsRecord[team] - winsRecord[teamVertex]);
            FlowEdge edgeTotarget = new FlowEdge(mapTeamVertex.get(teamVertex), targetVertex, capacity);
            flowNet.addEdge(edgeTotarget);
        }

        return flowNet;
    }

    private boolean isTeamEliminate(FordFulkerson maxFlowSolution, int team) {
        int targetFlow = 0;
        for (int teamIdx = 0; teamIdx < numberTeams; teamIdx++) {
            for (int otherIdx = teamIdx; otherIdx < numberTeams; otherIdx++) {
                if (teamIdx != team && otherIdx != team) {
                    targetFlow += againstRecord[teamIdx][otherIdx];
                }
            }
        }

        return (double) targetFlow < maxFlowSolution.value();
    }


}
