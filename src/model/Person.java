package model;

public class Person {
    protected Profile profile; // Instance variable for profile

    /**
     * Constructor to create a Person object.
     *
     * @param profile The profile of the person.
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    // Getter for profile
    public Profile getProfile() {
        return profile;
    }

    /**
            * Checks if this Person object is equal to another object.
     * Two Person objects are considered equal if they have the same profile.
     *
             * @param obj The object to compare to.
            * @return true if the persons are equal, false otherwise.
     */


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Person)) {
            return false;
        }

        Person other = (Person) obj;
        return this.profile.equals(other.profile); // Compare based on profile
    }



    /**
     * Returns a string representation of the Person.
     *
     * @return A string representing the Person object.
     */
    @Override
    public String toString() {
        return "Person Profile: " + profile.toString(); // Uses Profile's toString method
    }


    }
