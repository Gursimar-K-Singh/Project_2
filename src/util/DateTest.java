package util;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTest {

    @Test
    public void testInvalidDate1() {
        util.Date date1 = new util.Date(1, 32, 2025); // Invalid: January has only 31 days
        assertFalse(date1.isValid());
    }

    @Test
    public void testInvalidDate2() {
        util.Date date2 = new util.Date(2, 29, 2025); // Invalid: 2025 is not a leap year
        assertFalse( date2.isValid());
    }

    @Test
    public void testInvalidDate3() {
        util.Date date3 = new util.Date(13, 13, 2024); // Invalid: date is in the future
        assertFalse( date3.isValid());
    }

    @Test
    public void testInvalidDate4() {
        util.Date date4 = new util.Date(9, 13, 1854); // Invalid: year is less than 1900
        assertFalse( date4.isValid());
    }

    @Test
    public void testValidDate1() {
        util.Date date5 = new util.Date(10, 31, 2024); // Valid calendar date
        assertTrue( date5.isValid());
    }

    @Test
    public void testValidDate2() {
        util.Date date6 = new util.Date(12, 13, 2024); // Valid calendar date
        assertTrue( date6.isValid());
    }
}