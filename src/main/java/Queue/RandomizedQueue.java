package Queue;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private final int head;
    private int tail;

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] q;
        int current;

        public RandomizedQueueIterator(Item[] array, int head, int tail) {
            int size = tail - head;
            int[] shuffledIndices = StdRandom.permutation(size);
            q = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                q[i] = array[head + shuffledIndices[i]];
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return current != q.length;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return q[current++];
        }
    }

    public RandomizedQueue() {                 // construct an empty randomized queue
        array = (Item[]) new Object[1];
        head = 0;
        tail = 0;
    }

    public boolean isEmpty() {                 // is the randomized queue empty?
        return tail - head == 0;
    }

    public int size() {                        // return the number of items on the randomized queue
        return tail - head;
    }

    public void enqueue(Item item) {           // add the item
        if (item == null) throw new IllegalArgumentException();
        if (tail == array.length) resize(size() * 2);
        array[tail] = item;
        tail++;
    }

    public Item dequeue() {                    // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException();
        int index = getRandomIndex();
        Item item = array[index];
        array[index] = array[tail - 1];
        array[tail - 1] = null;
        tail--;
        if (2 * size() < array.length && array.length > 1) resize(array.length / 2);
        return item;
    }

    public Item sample() {                     // return a random item (but do not remove it)
        if (isEmpty()) throw new NoSuchElementException();
        return array[getRandomIndex()];
    }

    public Iterator<Item> iterator() {         // return an independent iterator over items in random order
        return new RandomizedQueueIterator(array, head, tail);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int limit;
        if (capacity < array.length) limit = capacity;
        else limit = array.length;
        for (int i = 0; i < limit; i++) {
            copy[i] = array[i];
        }
        array = copy;
    }

    private int getRandomIndex() {
        return StdRandom.uniform(size());
    }

    public static void main(String[] args) {   // unit testing (optional)
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 100; i++) {
            if (queue.size() > 0) {
                if (StdRandom.uniform(2) == 1) {
                    queue.enqueue(i);
                } else {
                    StdOut.println(queue.dequeue());
                }
            } else {
                queue.enqueue(i);
            }
        }
    }
}