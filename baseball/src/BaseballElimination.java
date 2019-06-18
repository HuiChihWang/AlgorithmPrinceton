import edu.princeton.cs.algs4.In;

import java.util.HashMap;

public class BaseballElimination {

    private int numberTeams;
    private int[] winsRecord;
    private int[] lossRecord;
    private int[] remainingsRecord;
    private int[][] againstRecord;
    private HashMap<String, Integer> mapNameIndex;



    public BaseballElimination(String filename) {
        parseTeamsFile(filename);
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
        return  false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    private void parseTeamsFile(String filename) {
        In fileReader = new In(filename);
        String[] allLines = fileReader.readAllLines();

        numberTeams = Integer.parseInt(allLines[0]);

        initializeContainer();
        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            String[] fields = allLines[teamIdx + 1].split("\\s+");

            mapNameIndex.put(fields[0], teamIdx);
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
        winsRecord = new int[numberTeams];
        lossRecord = new int[numberTeams];
        remainingsRecord = new int[numberTeams];
        againstRecord = new int[numberTeams][];

        for (int teamIdx = 0; teamIdx < numberTeams; ++teamIdx) {
            againstRecord[teamIdx] = new int[numberTeams];
        }
    }



}
