import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node sentinalNode;
    private int size;



    public Deque() {
        sentinalNode = new Node();
        sentinalNode.next = sentinalNode;
        sentinalNode.prev = sentinalNode;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        checkValidItem(item);
        Node frontNode = new Node(item, sentinalNode.next, sentinalNode);
        sentinalNode.next.prev = frontNode;
        sentinalNode.next = frontNode;
        size += 1;
    }

    public void addLast(Item item) {
        checkValidItem(item);
        Node backNode = new Node(item, sentinalNode, sentinalNode.prev);
        sentinalNode.prev.next = backNode;
        sentinalNode.prev = backNode;
        size += 1;
    }

    public Item removeFirst() {
        checkRemovable();
        Node oldFrontNode = sentinalNode.next;
        sentinalNode.next = oldFrontNode.next;
        oldFrontNode.next.prev = sentinalNode;
        size -= 1;
        return oldFrontNode.item;
    }

    public Item removeLast() {
        checkRemovable();
        Node oldBackNode = sentinalNode.prev;
        sentinalNode.prev = oldBackNode.prev;
        oldBackNode.prev.next = sentinalNode;
        size -= 1;
        return oldBackNode.item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private void checkValidItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkRemovable() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node() {
            this.item = null;
            this.next = null;
            this.prev = null;
        }

        Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current;

        DequeIterator() {
            current = sentinalNode.next;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public boolean hasNext() {
            return current != sentinalNode;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
