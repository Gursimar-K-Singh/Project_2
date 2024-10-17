package util;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ListTest {

    @Test
    public void add() {
        List<String> list = new List<>(); // Initialize the list

        // Test adding elements to the list
        list.add("First");
        list.add("Second");
        list.add("Third");

        // Verify the size of the list
        assertTrue(list.size() == 3);

        // Verify the elements in the list
        assertTrue(list.get(0).equals("First"));
        assertTrue(list.get(1).equals("Second"));
        assertTrue(list.get(2).equals("Third"));
    }


    @Test
    public void remove() {
        List<String> list = new List<>(); // Initialize the list

        // Add elements to the list before testing remove
        list.add("First");
        list.add("Second");
        list.add("Third");

        // Test removing an element
        list.remove("Second");

        // Verify the size of the list
        assertTrue(list.size() == 2);

        // Verify that the element was removed
        assertFalse(list.contains("Second"));
        assertTrue(list.get(0).equals("First"));
        assertTrue(list.get(1).equals("Third"));

        // Test removing an element that does not exist
        list.remove("Fourth");
        // Ensure the list size remains unchanged
        assertTrue(list.size() == 2);
    }
}
