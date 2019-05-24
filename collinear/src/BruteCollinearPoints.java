import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private int numSegments;
    private Point[] points;
    private ArrayList<Point[]> usedPairs;
    private ArrayList<Integer[]> visitedSegmentList;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkValidPointSet(points);
        this.points = points;
        numSegments = 0;
        visitedSegmentList = new ArrayList<>();
        usedPairs =  new ArrayList<>();

        findSegmentBrute();

        lineSegments = new LineSegment[numSegments];
        for (int i = 0; i < numSegments; ++i) {
            lineSegments[i] = createSegment(visitedSegmentList.get(i));
        }

    }

    public int numberOfSegments() {
        return numSegments;
    }

    public LineSegment[] segments() {
        return lineSegments;
    }

    private void findSegmentBrute() {
        for (int i = 0; i < points.length; ++i) {
            for (int j = 0; j < points.length; ++j) {
                for (int k = 0; k < points.length; ++k) {
                    for (int m = 0; m < points.length; ++m) {
                        if (checkDifferentIdx(i, j, k, m)) {

                            int colinear1 = points[i].slopeOrder().compare(points[j], points[k]);
                            int colinear2 = points[i].slopeOrder().compare(points[k], points[m]);

                            if (colinear1 == 0 && colinear2 == 0) {
                                Point[]
                                if (!checkDuplicateLine(i, j, k,  m)) {
                                    Integer[] segmentPoint = { i, j, k, m };
                                    visitedSegmentList.add(segmentPoint);
                                    numSegments += 1;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private void createSegment(ArrayList<Point> linePoint) {

        Point[] arrayPoints = (Point[]) linePoint.toArray();
        Arrays.sort(arrayPoints);
        Point[] lineTwoPoint = {arrayPoints[0], arrayPoints[arrayPoints.length - 1]};

        if (!checkDuplicateLine(lineTwoPoint)) {
            LineSegment line = new LineSegment(lineTwoPoint[0], lineTwoPoint[1]);
            usedPairs.add(lineTwoPoint);
            numSegments += 1;
        }
    }

    private boolean checkDifferentIdx(int i, int j, int k, int m) {
        return i != j &&  i != k && i != m && j != k && j != m && k != m;
    }

    private boolean checkDuplicateLine(Point[] newLine) {
        for (Point[] line: usedPairs) {
            if (line[0].compareTo(newLine[0]) == 0 && line[1].compareTo(newLine[1]) == 0) {
                return true;
            }
        }
        return false;
    }

    private void checkValidPointSet(Point[] pointsSet) {
        if (pointsSet == null) {
            throw new IllegalArgumentException();
        }

        for (Point point: pointsSet) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < pointsSet.length; ++i) {
            for (int j = i + 1; j < pointsSet.length; ++j) {
                if (pointsSet[i].compareTo(pointsSet[j]) == 0) {
                    throw  new IllegalArgumentException();
                }
            }
        }
    }

}
