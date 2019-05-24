import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private Point[] points;
    private ArrayList<Point[]> usedPairs;
    private ArrayList<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkValidPointSet(points);
        this.points = points;

        lineSegments = new ArrayList<>();
        usedPairs =  new ArrayList<>();
        findSegmentBrute();
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return (LineSegment[]) lineSegments.toArray();
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
                                Point[] line = {points[i], points[j], points[k], points[m]};
                                createSegment(line);
                            }
                        }

                    }
                }
            }
        }
    }

    private void createSegment(Point[] arrayPoints) {
        Arrays.sort(arrayPoints);
        Point[] lineTwoPoint = {arrayPoints[0], arrayPoints[arrayPoints.length - 1]};

        if (!checkDuplicateLine(lineTwoPoint)) {
            LineSegment line = new LineSegment(lineTwoPoint[0], lineTwoPoint[1]);
            usedPairs.add(lineTwoPoint);
            lineSegments.add(line);
        }
    }

    private boolean checkDuplicateLine(Point[] newLine) {
        for (Point[] line: usedPairs) {
            if (line[0].compareTo(newLine[0]) == 0 && line[1].compareTo(newLine[1]) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDifferentIdx(int i, int j, int k, int m) {
        return i != j &&  i != k && i != m && j != k && j != m && k != m;
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
