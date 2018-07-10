package Queue;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node node;

        public DequeIterator(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = node.item;
            node = node.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Deque() {                          // construct an empty deque
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {                // is the deque empty?
        return size == 0;
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {          // add the item to the front
        if (item == null) throw new IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (oldFirst != null) {
            oldFirst.previous = first;
            if (last == null) {
                last = oldFirst;
            }
        }
        size++;
    }

    public void addLast(Item item) {          // add the item to the end
        if (item == null) throw new IllegalArgumentException();
        if (isEmpty()) {
            addFirst(item);
            return;
        }
        Node oldLast;
        if (last == null) {
            oldLast = first;
        } else {
            oldLast = last;
        }
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (oldLast != null) {
            oldLast.next = last;
        }
        size++;
    }

    public Item removeFirst() {                // remove and return the item from the front
        if (isEmpty()) throw new NoSuchElementException();
        Node oldFirst = first;
        first = oldFirst.next;
        size--;
        if (size <= 1) {
            last = null;
        } else {
            first.previous = null;
        }
        return oldFirst.item;
    }

    public Item removeLast() {                // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException();
        if (last == null) return removeFirst();

        Node oldLast = last;
        last = last.previous;
        last.next = null;
        size--;
        return oldLast.item;
    }

    public Iterator<Item> iterator() {         // return an iterator over items in order from front to end
        return new DequeIterator(first);
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(4);
        deque.addLast(5);
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeFirst());
        deque.addLast(9);
        StdOut.println(deque.removeFirst());
    }
}