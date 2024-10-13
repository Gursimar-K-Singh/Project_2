package model;


public class CircleList<E> extends List<E> {
    private int currentIndex;

    public CircleList() {
        super();
        currentIndex = 0; // Start at the beginning of the list
    }

    // Gets the next element in the list in a circular manner
    public E getNext() {
        if (size() == 0) {
            return null; // Return null if the list is empty
        }
        E nextElement = get(currentIndex);
        currentIndex = (currentIndex + 1) % size(); // Move to the next element in a circular manner
        return nextElement;
    }

    // Resets the index to start over
    public void reset() {
        currentIndex = 0;
    }
}
