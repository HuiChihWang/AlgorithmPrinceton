import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private int numSegments;
    private Point[] points;
    private ArrayList<Integer[]> visitedSegmentList;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        checkValidPointSet(points);
        this.points = points;
        numSegments = 0;
        visitedSegmentList = new ArrayList<>();
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
                                if (!checkVisitedSegment(i, j, k,  m)) {
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

    private LineSegment createSegment(Integer[] segment) {
        Point[] pointsLine = new Point[4];

        int count = 0;
        for (int idx: segment) {
            pointsLine[count] = points[idx];
        }

        Arrays.sort(pointsLine);
        return new LineSegment(pointsLine[0], pointsLine[3]);
    }

    private boolean checkDifferentIdx(int i, int j, int k, int m) {
        return i != j &&  i != k && i != m && j != k && j != m && k != m;
    }

    private boolean checkVisitedSegment(int i, int j, int k, int m) {
        Integer[] idxList = {i, j, k, m};
        Arrays.sort(idxList);

        for (Integer[] segment: visitedSegmentList) {
            if (Arrays.equals(segment, idxList)) {
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

        for (Point point1: pointsSet) {
            for (Point point2: pointsSet) {
                if (point1.slopeTo(point2) == Double.NEGATIVE_INFINITY) {
                    throw  new IllegalArgumentException();
                }
            }
        }
    }

}
