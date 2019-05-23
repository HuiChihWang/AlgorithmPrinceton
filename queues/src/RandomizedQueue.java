import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_SIZE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private static final double SHRINK_FACTOR = 0.25;

    private Item[] arrayQueue;
    private int size;
    private int capacity;

    public RandomizedQueue() {
        capacity = INITIAL_SIZE;
        size = 0;
        arrayQueue = (Item[]) new Object[capacity];

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        checkValidItem(item);
        checkDoubleSize();

        arrayQueue[size] = item;
        size += 1;
    }

    public Item dequeue() {
        checkAccessible();
        checkHalveSize();

        int randomIdx = StdRandom.uniform(size);
        swap(arrayQueue, size - 1, randomIdx);
        Item item = arrayQueue[size - 1];
        arrayQueue[size - 1] = null;
        size -= 1;
        return item;
    }

    public Item sample() {
        int randomIdx = StdRandom.uniform(size);
        return arrayQueue[randomIdx];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void checkDoubleSize() {
        double ratioElementCapacity = (double) size / (double) capacity;
        if (ratioElementCapacity > LOAD_FACTOR) {
            doubleSize();
        }
    }

    private void doubleSize() {
        capacity = capacity * 2;
        Item[] newQueue = (Item[]) new Object[capacity];
        for (int idx = 0; idx < size; ++idx) {
            newQueue[idx] = arrayQueue[idx];
        }
        arrayQueue = newQueue;
    }

    private void checkHalveSize() {
        if (capacity / 2 < INITIAL_SIZE) {
            return;
        }

        double ratioElementCapacity = (double) size / (double) capacity;
        if (ratioElementCapacity < SHRINK_FACTOR) {
            halveSize();
        }
    }

    private void halveSize() {
        capacity = capacity / 2;
        Item[] newQueue = (Item[]) new Object[capacity];
        for (int idx = 0; idx < size; ++idx) {
            newQueue[idx] = arrayQueue[idx];
        }
        arrayQueue = newQueue;
    }

    private void swap(Object[] items, int i, int j) {
        Item temp = arrayQueue[i];
        arrayQueue[i] = arrayQueue[j];
        arrayQueue[j] = temp;
    }

    private void checkValidItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkAccessible() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private Integer[] randomSeqence;
        private int count;

        RandomizedQueueIterator() {
            count = 0;
            createRandomSequence();
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public Item next() {
            Item item = arrayQueue[randomSeqence[count]];
            count += 1;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void createRandomSequence() {
            randomSeqence = new Integer[size];

            for (int idx = 0; idx < size; ++idx) {
                randomSeqence[idx] = idx;
            }

            for (int idx = 0; idx < size; ++idx) {
                swap(randomSeqence, idx, StdRandom.uniform(size));
            }
        }

    }

}
