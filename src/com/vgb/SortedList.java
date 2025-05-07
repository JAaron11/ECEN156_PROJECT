package com.vgb;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A sorted list backed by a doubly‑linked list.
 */
public class SortedList<T> implements Iterable<T> {
    private final Comparator<? super T> comp;
    private Node<T> head;   // first element
    private Node<T> tail;   // last element
    private int size = 0;   // element count

    // Doubly‑linked node
    private static class Node<E> {
        E value;
        Node<E> prev, next;
        Node(E value) { this.value = value; }
    }

    public SortedList(Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("Comparator cannot be null");
        }
        this.comp = comparator;
    }

    /** Inserts element into its sorted position. */
    public void add(T element) {
        if (element == null) {
            throw new NullPointerException("Null elements not allowed");
        }

        Node<T> newNode = new Node<>(element);

        // 1) Empty list → new node becomes head & tail
        if (head == null) {
            head = tail = newNode;
            size = 1;
            return;
        }

        // 2) If element goes before head
        if (comp.compare(element, head.value) <= 0) {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
            size++;
            return;
        }

        // 3) If element goes after tail
        if (comp.compare(element, tail.value) >= 0) {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
            size++;
            return;
        }

        // 4) Otherwise walk to find insertion point:
        //    stop at first current where element <= current.value
        Node<T> curr = head;
        while (curr != null && comp.compare(element, curr.value) > 0) {
            curr = curr.next;
        }
        // Now curr is the node we should insert before
        Node<T> prev = curr.prev;
        prev.next = newNode;
        newNode.prev = prev;
        newNode.next = curr;
        curr.prev = newNode;
        size++;
    }

    /** Removes the first matching element; returns true if removed. */
    public boolean remove(T element) {
        if (head == null) return false;

        Node<T> curr = head;
        while (curr != null) {
            if (curr.value.equals(element)) {
                // relink prev and next
                if (curr.prev != null) curr.prev.next = curr.next;
                else head = curr.next;

                if (curr.next != null) curr.next.prev = curr.prev;
                else tail = curr.prev;

                size--;
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    /** @return element at given index (0 ≤ index < size). */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        Node<T> curr = head;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.value;
    }

    /** @return number of elements stored. */
    public int size() {
        return size;
    }

    /** @return true if no elements are stored. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** @return true if element is present. */
    public boolean contains(T element) {
        Node<T> curr = head;
        while (curr != null) {
            if (curr.value.equals(element)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    /** Clears all elements. */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    /** Bulk add—all elements inserted one by one. */
    public void addAll(Iterable<? extends T> items) {
        for (T item : items) {
            add(item);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> curr = head;

            @Override
            public boolean hasNext() {
                return curr != null;
            }

            @Override
            public T next() {
                if (curr == null) {
                    throw new NoSuchElementException();
                }
                T val = curr.value;
                curr = curr.next;
                return val;
            }
        };
    }
}