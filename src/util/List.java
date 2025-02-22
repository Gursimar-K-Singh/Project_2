package util;
import java.util.Iterator;

/**
 * The List class represents a dynamic collection of elements of any type.
 * It provides methods to add, remove, and manipulate elements, as well as sort them
 * by various criteria. This class automatically grows the internal array when it reaches capacity.
 *
 * @param <E> The type of elements stored in the list (e.g., Appointment, Integer, String, etc.).
 *
 * @author GursimarSingh
 */

public class List<E> implements Iterable<E>{
    private static final int INITIAL_CAPACITY = 4;
    private E[] objects;
    private int size;

    /**
     * Default constructor that initializes the list with an initial capacity.
     */
    public List() {
        objects = (E[]) new Object[INITIAL_CAPACITY]; // Create an array of generic type
        this.size = 0;
    }

    /**
     * Returns the size of the list.
     *
     * @return The number of elements in the list.
     */
    public int size() {
        return size;
    }


    /**
     * Finds the index of the specified element in the list.
     *
     * @param e The element to find.
     * @return The index of the element, or -1 if not found.
     */
    private int find( E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return -1; // Element not found
    }


    /**
     * Grows the internal array by increasing its capacity.
     */
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length * 2]; // Double the array size
        for (int i = 0; i < size; i++) {
            newObjects[i] = objects[i];
        }
        objects = newObjects;
    }

    /**
     * Checks if the list contains the specified element.
     *
     * @param e The element to check.
     * @return true if the element is in the list, false otherwise.
     */
    public boolean contains(E e) {
        return find(e) != -1;
    }

    /**
     * Adds an element to the list.
     *
     * @param e The element to add.
     */
    public void add(E e) {
        if (size == objects.length) {
            grow();
        }
        objects[size] = e;
        size++;
    }

    /**
     * Removes the specified element from the list.
     *
     * @param e The element to remove.
     */
    public void remove(E e) {
        int index = find(e);
        if (index == -1) return; // Element not found
        for (int i = index; i < size - 1; i++) {
            objects[i] = objects[i + 1]; // Shift elements to the left
        }
        objects[size - 1] = null; // make last element null
        size--;
    }

    /**
     * Sets the element at the specified index to the given value.
     *
     * @param index The index of the element to set.
     * @param e     The new element to be set at the specified index.
     */
    public void set(int index, E e) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        objects[index] = e; // Set the new element at the specified index
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false if not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator to iterate over the elements in the list.
     *
     * @return An iterator for the list.
     */
    @Override
    public Iterator<E> iterator() {
        return new util.List.ListIterator();
    }

    /**
     * Returns the element at the specified index in the list.
     *
     * @param index The index of the element to return.
     * @return The element at the specified index, or null if the index is out of bounds.
     */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return objects[index];
    }

    /**
     * Returns the index of the specified element, or -1 if not found.
     *
     * @param e The element to find.
     * @return The index of the element, or -1 if not found.
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * A private inner class that implements the Iterator interface for the List class.
     */
    private class ListIterator implements Iterator<E> {
        private int currentIndex = 0;

        /**
         * Returns true if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }


        /**
         * Returns the next element in the iteration.
         *
         * @return The next element in the list, or null if there are no more elements.
         */
        @Override
        public E next() {
            if (!hasNext()) {
                return null; // No more elements,
            }
            return objects[currentIndex++];
        }
    }
}
