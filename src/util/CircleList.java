package util;
/**
 * Represents a circular linked list that allows traversal of elements in a circular manner.
 *
 * This class extends the generic List class and maintains an index to track the current
 * position in the list. It provides methods to retrieve the next element in a circular
 * fashion and to reset the index back to the start of the list.
 *
 * @param <E> the type of elements in the list
 *
 * @author Gursimar Singh
 */

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


}
