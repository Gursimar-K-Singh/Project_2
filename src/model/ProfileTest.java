package model;
import util.Date;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileTest {

    @Test
    public void testCompareTo_FirstNameLessThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p2 = new Profile("Jane", "Doe", new Date(1990, 1, 5)); // p2 < p1 (First Name)
        assertTrue( p1.compareTo(p2) > 0);
    }

    @Test
    public void testCompareTo_LastNameLessThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p3 = new Profile("John", "Smith", new Date(1989, 11, 13)); // p1 < p3 (Last Name)
        assertTrue( p1.compareTo(p3) < 0);
    }

    @Test
    public void testCompareTo_DOBLessThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p4 = new Profile("John", "Doe", new Date(1995, 6, 23)); // p1 < p4 (DOB)
        assertTrue( p1.compareTo(p4) < 0);
    }

    @Test
    public void testCompareTo_FirstNameGreaterThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p5 = new Profile("Jane", "Doe", new Date(1985, 11, 13)); // p1 < p5 (First Name)
        assertTrue(p5.compareTo(p1) < 0);
    }

    @Test
    public void testCompareTo_DOBGreaterThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p6 = new Profile("John", "Doe", new Date(1985, 11, 13)); // p1 > p6 (DOB)
        assertTrue( p1.compareTo(p6) > 0);
    }

    @Test
    public void testCompareTo_LastNameGreaterThan() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p7 = new Profile("John", "Adams", new Date(1989, 11, 13)); // p1 > p7 (Last Name)
        assertTrue( p1.compareTo(p7) > 0);
    }

    @Test
    public void testCompareTo_EqualProfiles() {
        Profile p1 = new Profile("John", "Doe", new Date(1989, 11, 13));
        Profile p8 = new Profile("John", "Doe", new Date(1989, 11, 13)); // p1 == p8 (All Equal)
        assertEquals(0, p1.compareTo(p8));
    }
}