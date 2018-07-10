package Percolation;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Date: 29.05.2018
 *
 * @author Tolstikov Alexaxndr
 */
public final class Percolation {
    private boolean[] opened;
    private final int size;
    private final WeightedQuickUnionUF quickUnion;
    private final WeightedQuickUnionUF backwashQU;

    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = n;
        final int nn = n * n;
        quickUnion = new WeightedQuickUnionUF(nn + 2);
        backwashQU = new WeightedQuickUnionUF(nn + 1);
        opened = new boolean[nn + 2];
        opened[0] = true;
        opened[nn + 1] = true;
    }

    public void open(final int row, final int col) {
        validateIndices(row, col);
        final int index = xyToFlat(row, col);
        opened[index] = true;

        if (row == size) { // bottom
            quickUnion.union(index, size * size + 1);
        }
        if (row == 1) { // top
            quickUnion.union(index, 0);
            backwashQU.union(index, 0);
        }
        if (row > 1 && isOpen(row - 1, col)) { // above
            quickUnion.union(index, xyToFlat(row - 1, col));
            backwashQU.union(index, xyToFlat(row - 1, col));
        }
        if (row < size && isOpen(row + 1, col)) { // below
            quickUnion.union(index, xyToFlat(row + 1, col));
            backwashQU.union(index, xyToFlat(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) { // left
            quickUnion.union(index, xyToFlat(row, col - 1));
            backwashQU.union(index, xyToFlat(row, col - 1));
        }
        if (col < size && isOpen(row, col + 1)) { // right
            quickUnion.union(index, xyToFlat(row, col + 1));
            backwashQU.union(index, xyToFlat(row, col + 1));
        }
    }

    public boolean isOpen(final int row, final int col) {
        validateIndices(row, col);
        return opened[xyToFlat(row, col)];
    }

    public boolean isFull(final int row, final int col) {
        validateIndices(row, col);
        return backwashQU.connected(xyToFlat(row, col), 0);
    }

    public int numberOfOpenSites() {
        int number = 0;
        for(int i = 0; i < opened.length; i++) {
            if(opened[i]) number++;
        }
        return number - 2;
    }

    public boolean percolates() {
        return quickUnion.connected(0, size * size + 1);
    }

    /**
     * Convert xy-coordinates to array index
     *
     * @param row - row of grid [1, n]
     * @param col - column of grid [1, n]
     * @return index value
     */
    private int xyToFlat(final int row, final int col) {
        return (row - 1) * size + col;
    }

    /**
     * Checks that indices are in valid range
     *
     * @param row - row of grid [1, n]
     * @param col - col of grid [1, n]
     * @throws IllegalArgumentException if some not in valid range
     */
    private void validateIndices(final int row, final int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) {
            throw new IllegalArgumentException("Indices are out of range");
        }
    }

    public static void main(final String[] args) {
        In in = new In(args[0]);      // input file
        final int n = in.readInt();         // n-by-n percolation system

        Percolation percolation = new Percolation(n);
        while (!in.isEmpty()) {
            final int i = in.readInt();
            final int j = in.readInt();
            percolation.open(i, j);
        }
    }
}
