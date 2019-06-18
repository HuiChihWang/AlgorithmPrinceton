import org.junit.Test;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;



public class ReadTeamsFileTest {
    private static final String testFolder = "data/";
    private static final String[] testFileNames = {"teams1.txt", "teams4.txt"};
    private static final int[] numAns = {1, 4};

    private ArrayList<BaseballElimination> baseballTesters;


    @Before
    public void init() {
        baseballTesters = new ArrayList<>();

        for (String filename: testFileNames){
            baseballTesters.add(new BaseballElimination(testFolder + filename));
        }
    }

    @Test
    public void testCorrectTeamNumber(){

        int testerCount = 0;
        for (BaseballElimination tester: baseballTesters) {
            assertEquals(numAns[testerCount], tester.numberOfTeams());
            testerCount += 1;
        }
    }

    @Test
    public void testWins(){
        BaseballElimination tester = baseballTesters.get(1);
        HashMap<String, Integer> winsAns = new HashMap<>();
        winsAns.put("Atlanta", 83);
        winsAns.put("Philadelphia", 80);
        winsAns.put("New_York", 78);
        winsAns.put("Montreal", 77);

        int count = 0;
        for (String team: tester.teams()) {
            assertEquals((int) winsAns.get(team), tester.wins(team));
            count += 1;
        }
    }

    @Test
    public void testLosses() {
        BaseballElimination tester = baseballTesters.get(1);
        HashMap<String, Integer> lossesAns = new HashMap<>();
        lossesAns.put("Atlanta", 71);
        lossesAns.put("Philadelphia", 79);
        lossesAns.put("New_York", 78);
        lossesAns.put("Montreal", 82);

        int count = 0;
        for (String team: tester.teams()) {
            assertEquals((int) lossesAns.get(team), tester.losses(team));
            count += 1;
        }
    }

    @Test
    public void testRemainings() {
        BaseballElimination tester = baseballTesters.get(1);
        HashMap<String, Integer> remainingsAns = new HashMap<>();
        remainingsAns.put("Atlanta", 8);
        remainingsAns.put("Philadelphia", 3);
        remainingsAns.put("New_York", 6);
        remainingsAns.put("Montreal", 3);

        int count = 0;
        for (String team: tester.teams()) {
            assertEquals((int) remainingsAns.get(team), tester.remaining(team));
            count += 1;
        }
    }

    @Test
    public void testAgainsts() {
        BaseballElimination tester = baseballTesters.get(1);

        HashMap<String, Integer> mapNameIndex = new HashMap<>();
        mapNameIndex.put("Atlanta", 0);
        mapNameIndex.put("Philadelphia", 1);
        mapNameIndex.put("New_York", 2);
        mapNameIndex.put("Montreal", 3);

        int[][] againstAns = {
                new int[]{0, 1, 6, 1},
                new int[]{1, 0 ,0, 2},
                new int[]{6, 0, 0, 0},
                new int[]{1, 2, 0, 0},
        };

        for (String team: tester.teams()) {
            for (String other: tester.teams()) {
                int against = tester.against(team, other);
                int agianExpect = againstAns[mapNameIndex.get(team)][mapNameIndex.get(other)];
                assertEquals(agianExpect, against);
            }
        }
    }



}
