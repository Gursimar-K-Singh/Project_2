package projectone;

//@author AparnaSrinivas @author GursimarSingh
public class Profile implements Comparable<Profile> {
    private String fname;
    private String lname;
    private Date dob;

    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public Date getDob() {
        return dob;
    }

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

    //John Doe 12/13/1989
    @Override
    public String toString() {
        return this.fname + " " + this.lname + " " + this.dob;
    }
    //call apt class to print appointment

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

    // Testbed main
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
