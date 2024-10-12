package model;

import org.junit.Test;

import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void testInvalidDate1() {
        Date date1 = new Date(1, 32, 2025); // Invalid: January has only 31 days
        assertFalse(date1.isValid());
    }

    @Test
    public void testInvalidDate2() {
        Date date2 = new Date(2, 29, 2025); // Invalid: 2025 is not a leap year
        assertFalse( date2.isValid());
    }

    @Test
    public void testInvalidDate3() {
        Date date3 = new Date(13, 13, 2024); // Invalid: date is in the future
        assertFalse( date3.isValid());
    }

    @Test
    public void testInvalidDate4() {
        Date date4 = new Date(9, 13, 1854); // Invalid: year is less than 1900
        assertFalse( date4.isValid());
    }

    @Test
    public void testValidDate1() {
        Date date5 = new Date(10, 31, 2024); // Valid calendar date
        assertTrue( date5.isValid());
    }

    @Test
    public void testValidDate2() {
        Date date6 = new Date(12, 13, 2024); // Valid calendar date
        assertTrue( date6.isValid());
    }
}