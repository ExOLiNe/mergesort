package Percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Date: 29.05.2018
 *
 * @author Tolstikov Alexaxndr
 */
public final class PercolationStats {
    private static final double MULTIPLIER = 1.96;

    private final int trials;
    private final boolean[][] set;
    private final double[] thresholds;

    private class Point {
        private final int row, col;

        Point(final int r, final int c) {
            this.row = r;
            this.col = c;
        }

        int getRow() {
            return row;
        }

        int getCol() {
            return col;
        }
    }

    public PercolationStats(final int n, final int trls) {
        if (n <= 0 || trls <= 0) {
            throw new IllegalArgumentException();
        }
        this.trials = trls;
        set = new boolean[n][n];
        thresholds = new double[trls];
        for (int trial = 0; trial < trls; trial++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    set[i][j] = false;
                }
            }
            Percolation percolation = new Percolation(n);
            boolean percolates = false;
            for (int i = 1; i <= n; i++) {
                if (percolates) {
                    break;
                }
                for (int j = 1; j <= n; j++) {
                    if (percolates) {
                        break;
                    }
                    Point point = randomPoint();
                    percolation.open(point.getRow(), point.getCol());
                    percolates = percolation.percolates();
                }
            }
            int openSites = 0;
            for (boolean[] subSet : set) {
                for (boolean site : subSet) {
                    if (site) {
                        openSites++;
                    }
                }
            }
            thresholds[trial] = (double) openSites / (set.length * set.length);
        }
    }

    public double mean() {
        return StdStats.mean(thresholds);
    }

    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo() {
        return mean() - (MULTIPLIER * stddev()) / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + (MULTIPLIER * stddev()) / Math.sqrt(trials);
    }

    public static void main(final String[] args) {
        final int n = Integer.parseInt(args[0]);
        final int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        System.out.println(String.format("mean = %f", stats.mean()));
        System.out.println(String.format("stddev = %f", stats.stddev()));
        System.out.println(
                String.format("95%% confidence interval = [%f, %f]",
                        stats.confidenceLo(), stats.confidenceHi())
        );
    }

    private Point randomPoint() {
        final int n = set.length;
        int closed = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean opened = set[i][j];
                if (!opened) {
                    closed++;
                }
            }
        }
        final int[] blocked = new int[closed];
        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                boolean opened = set[i][j];
                if (!opened) {
                    blocked[index++] = i * n + j;
                }
            }
        }
        Point point = flatToPoint(
                blocked[StdRandom.uniform(blocked.length)], n);
        set[point.getRow() - 1][point.getCol() - 1] = true;
        return point;
    }

    private Point flatToPoint(final int index, final int n) {
        if (index == 0) {
            return new Point(1, 1);
        }
        final int y = index % n;
        final int x;
        if (y == 0) {
            x = index / n;
        } else {
            x = index / n + 1;
        }
        return new Point(x, y + 1);
    }
}
