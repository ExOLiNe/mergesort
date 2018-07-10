import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    Point[] points;
    LineSegment[] segments;
    int numberOfSegments;
    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        validate(points);
        this.points = points;
        this.segments = new LineSegment[1];
        numberOfSegments = 0;
        for(int i = 0; i < points.length; i++) {
            Point iPoint = points[i];
            for(int j = i + 1; j < points.length; j++) {
                for(int k = j + 1; k < points.length; k++) {
                    for(int l = k + 1; l < points.length; l++) {
                        Point jPoint = points[j];
                        Point kPoint = points[k];
                        Point lPoint = points[l];
                        if(iPoint.slopeTo(jPoint) == iPoint.slopeTo(kPoint) && iPoint.slopeTo(kPoint)== iPoint.slopeTo(lPoint)) {
                            Point minPoint = min(iPoint, jPoint, kPoint, lPoint);
                            Point maxPoint = max(iPoint, jPoint, kPoint, lPoint);
                            if(segments.length == numberOfSegments) resizeSegments(segments.length * 2);
                            segments[numberOfSegments++] =  new LineSegment(minPoint, maxPoint);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {        // the number of line segments
        return numberOfSegments;
    }

    public LineSegment[] segments() {                // the line segments
        return segments;
    }

    private void validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (points[j] == null) throw new IllegalArgumentException();
                if (i != j && points[i] == points[j]) throw new IllegalArgumentException();
            }
        }
    }

    private Point min(Point ...a) {
        Point min = a[0];
        for(int i = 1; i < a.length; i++) {
            if(a[i].compareTo(min) < 0 ) min = a[i];
        }
        return min;
    }

    private Point max(Point ...a) {
        Point max = a[0];
        for(int i = 1; i < a.length; i++) {
            if(a[i].compareTo(max) > 0 ) max = a[i];
        }
        return max;
    }

    private void resizeSegments(int newSize) {
        LineSegment[] newSegments = new LineSegment[newSize];
        for(int i = 0; i < segments.length; i++) {
            newSegments[i] = segments[i];
        }
        segments = newSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}