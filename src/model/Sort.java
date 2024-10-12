package model;

public class Sort {
    public static <E extends Comparable<E>> void sort(List<E> list) {
        int n = list.size();

        // Create a temporary list to hold sorted elements
        List<E> sortedList = new List<>();

        while (!list.isEmpty()) {
            E smallest = list.get(0); // Assume the first element is the smallest

            // Find the smallest element in the current list
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).compareTo(smallest) < 0) {
                    smallest = list.get(i);
                }
            }

            // Remove the smallest element from the original list
            list.remove(smallest);

            // Add it to the sorted list
            sortedList.add(smallest);
        }

        // Transfer sorted elements back to the original list
        while (!sortedList.isEmpty()) {
            E element = sortedList.get(0); // Get the first element
            sortedList.remove(element); // Remove from sorted list
            list.add(element); // Add to the original list
        }
    }

    public static void main(String[] args) {
        // Example with Integers
        List<Integer> intList = new List<>();
        intList.add(5);
        intList.add(3);
        intList.add(8);
        intList.add(1);

        Sort.sort(intList);
        for (Integer i : intList) {
            System.out.println(i); // Outputs: 1, 3, 5, 8
        }

        // Example with Strings
        List<String> stringList = new List<>();
        stringList.add("Bob");
        stringList.add("Alice");
        stringList.add("Charlie");

        Sort.sort(stringList);
        for (String s : stringList) {
            System.out.println(s); // Outputs: Alice, Bob, Charlie
        }


    }
}
