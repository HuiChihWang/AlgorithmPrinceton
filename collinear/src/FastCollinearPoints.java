import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private Point[] points;
    private ArrayList<Point[]> usedPairs;
    private ArrayList<LineSegment> lineList;

    public FastCollinearPoints(Point[] points) {
        checkValidPointSet(points);
        this.points = points;
        usedPairs = new ArrayList<>();
        lineList = new ArrayList<>();
        findSegments();

    }

    public int numberOfSegments() {
        return lineList.size();
    }

    public LineSegment[] segments() {
        return (LineSegment[]) lineList.toArray();
    }

    private void findSegments() {
        for (Point point: points) {
            PointSlope[] sortedSlopes = getSortedSlope(point);
            findLineSegment(sortedSlopes, point);
        }
    }


    private PointSlope[] getSortedSlope(Point point) {
        PointSlope[] slopes = new PointSlope[points.length - 1];

        int count = 0;
        for (int idx = 0; idx < points.length; ++idx) {
            if (!point.equals(points[idx])) {
                slopes[count] = new PointSlope(point, idx);
                count += 1;
            }
        }

        Arrays.sort(slopes);
        return slopes;
    }

    private void findLineSegment(PointSlope[] sortedSlopes, Point point) {

        ArrayList<Point> linePoint = new ArrayList<>();
        for (int pIdx = 0; pIdx < sortedSlopes.length; ++pIdx) {

            if (pIdx < sortedSlopes.length - 1 && sortedSlopes[pIdx].slope == sortedSlopes[pIdx + 1].slope) {
                linePoint.add(points[sortedSlopes[pIdx].idx]);
            } else {
                if (linePoint.size() >= 2) {
                    linePoint.add(points[sortedSlopes[pIdx].idx]);
                    linePoint.add(point);
                    createSegment(linePoint);
                }

                linePoint.clear();
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
            lineList.add(line);
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

    private class PointSlope implements Comparable<PointSlope> {
        double slope;
        int idx;

        PointSlope(Point current, int idx) {
            this.idx = idx;
            this.slope = current.slopeTo(points[idx]);
        }

        @Override
        public int compareTo(PointSlope other) {
            return Double.compare(slope, other.slope);
        }
    }

}
