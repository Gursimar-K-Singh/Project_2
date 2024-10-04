package projectone;

/**
 * The Profile class represents an individual's profile with a first name, last name, and date of birth.
 * It provides methods to compare profiles, check equality, and return a string representation of the profile.
 *
 * This class implements the Comparable interface, allowing profiles to be compared based on
 * their last name, first name, and date of birth. Profiles can be used for sorting or checking equality.
 *
 * The equals() method ensures that two profiles are equal if their first name, last name, and
 * date of birth are the same.
 *
 * The toString() method returns a string representation of the profile in the format:
 * "FirstName LastName DateOfBirth".
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructs a Profile object with the specified first name, last name, and date of birth.
     *
     * @param fname The first name of the individual.
     * @param lname The last name of the individual.
     * @param dob The date of birth of the individual.
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Returns the first name of the individual.
     *
     * @return The first name of the individual.
     */
    public String getFname() {
        return fname;
    }

    /**
     * Returns the last name of the individual.
     *
     * @return The last name of the individual.
     */
    public String getLname() {
        return lname;
    }

    /**
     * Returns the date of birth of the individual.
     *
     * @return The date of birth of the individual.
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Checks if this profile is equal to another object.
     * Two profiles are considered equal if they have the same first name, last name, and date of birth.
     *
     * @param obj The object to compare with this profile.
     * @return true if the profiles are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Profile)){
            return false;
        }



        Profile other = (Profile) obj;
        return this.fname.equals(other.fname) && this.lname.equals(other.lname) && this.dob.equals(other.dob);
    }

    /**
     * Returns a string representation of the profile in the format "FirstName LastName DateOfBirth".
     *
     * @return A string representing the profile.
     */
    //John Doe 12/13/1989
    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob;
    }
    //call apt class to print appointment

    /**
     * Compares this profile with another profile based on last name, first name, and date of birth.
     *
     * The comparison is performed in the following order:
     * 1. Last name (lexicographically)
     * 2. First name (lexicographically)
     * 3. Date of birth (chronologically)
     *
     * @param other The profile to compare to.
     * @return A negative integer, zero, or a positive integer as this profile is less than, equal to,
     *         or greater than the specified profile.
     */
    @Override
    public int compareTo(Profile other) {

        int lastNameComparison = this.lname.compareTo(other.lname);
        if (lastNameComparison != 0) {
            return lastNameComparison > 0 ? 1 : -1;
        }

        int firstNameComparison = this.fname.compareTo(other.fname);
        if (firstNameComparison != 0) {
            return firstNameComparison > 0 ? 1 : -1;
        }

        int dobComparison = this.dob.compareTo(other.dob);
        if (dobComparison != 0) {
            return dobComparison > 0 ? 1 : -1;
        }

        return 0;
    }

    /**
     * Testbed main method to test the Profile class.
     * Tests various comparisons and equality checks between Profile objects.
     *
     * @param args The command-line arguments (not used).
     */
    public static void main(String[] args) {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p2 = new Profile("Jane", "Doe", new Date(1990, 1, 5));  // p2 < p1 (First Name)
        Profile p3 = new Profile("John", "Smith", new Date(1989, 11, 13)); // p1 < p3 (Last Name)
        Profile p4 = new Profile("John", "Doe", new Date(1995, 6, 23));  // p1 < p4 (DOB)

        System.out.println(p1.compareTo(p2));
        System.out.println(p1.compareTo(p3));
        System.out.println(p1.compareTo(p4));

        // Three test cases that return 1:
        Profile p5 = new Profile("Jane", "Doe", new Date(1985, 11, 13));  // p1 > p5 (First Name)
        Profile p6 = new Profile("John", "Doe", new Date(1985, 11, 13));  // p1 > p6 (DOB)
        Profile p7 = new Profile("John", "Adams", new Date(1989, 11, 13));  // p1 > p7 (Last Name)

        System.out.println(p1.compareTo(p5));
        System.out.println(p1.compareTo(p6));
        System.out.println(p1.compareTo(p7));

        // One test case that returns 0:
        Profile p8 = new Profile("John", "Doe", new Date(1989, 11, 13));  // p1 == p8 (All Equal)
        System.out.println(p1.compareTo(p8));


    }
}
