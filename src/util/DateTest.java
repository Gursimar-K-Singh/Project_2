package util;

import model.Date;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateTest {

    @Test
    public void testInvalidDate1() {
        model.Date date1 = new model.Date(1, 32, 2025); // Invalid: January has only 31 days
        assertFalse(date1.isValid());
    }

    @Test
    public void testInvalidDate2() {
        model.Date date2 = new model.Date(2, 29, 2025); // Invalid: 2025 is not a leap year
        assertFalse( date2.isValid());
    }

    @Test
    public void testInvalidDate3() {
        model.Date date3 = new model.Date(13, 13, 2024); // Invalid: date is in the future
        assertFalse( date3.isValid());
    }

    @Test
    public void testInvalidDate4() {
        model.Date date4 = new model.Date(9, 13, 1854); // Invalid: year is less than 1900
        assertFalse( date4.isValid());
    }

    @Test
    public void testValidDate1() {
        model.Date date5 = new model.Date(10, 31, 2024); // Valid calendar date
        assertTrue( date5.isValid());
    }

    @Test
    public void testValidDate2() {
        model.Date date6 = new Date(12, 13, 2024); // Valid calendar date
        assertTrue( date6.isValid());
    }
}