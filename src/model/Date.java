//@author AparnaSrinivas @author GursimarSingh
package model;
import java.util.Calendar;
/**
 * The Date class represents a specific date consisting of year, month, and day.
 * It provides various methods to validate the date, check for leap years, compare dates,
 * and determine if the date falls on a weekend, is in the past, or is within six months from today.
 * Implements the Comparable interface to compare two Date objects.
 * Constants are defined for the leap year calculation and the number of days in specific months.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public class Date implements Comparable<Date> {
    private int year, month, day;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int month_with_31_days = 31;
    public static final int month_with_30_days = 30;
    public static final int feb_leap = 29;
    public static final int feb = 28;
    public static final int year1900 = 1900;


    /**
     * Constructor method to create a Date object.
     * The month is stored as 0-indexed (i.e., January is 0, December is 11).
     *
     * @param month The month of the date (1-12).
     * @param day The day of the date (1-31).
     * @param year The year of the date.
     */
    public Date(int month, int day, int year) {
        this.year = year;
        this.month = month -1;
        this.day = day;
    }
    // Getters

    //checks if date it valid on calendar
    /**
     * Checks if the date is a valid calendar date.
     * Ensures that the year is greater than or equal to 1900,
     * the month is within valid bounds, and the day is within the valid range for the month.
     *
     * @return true if the date is valid, false otherwise.
     */
    public boolean isValid() {

        if(year < year1900){
            return false;
        }


        if (month < Calendar.JANUARY|| month > Calendar.DECEMBER) {
            return false;
        }

        int maxDaysInMonth = getDaysInMonth(year, month);

        return day >= 1 && day <= maxDaysInMonth;

    }

    /**
     * checks if valid date is present in a leap year - helper method
     *
     * @param year to check.
     * @return true if the year is a leap year, false otherwise.
     */
    private boolean isLeapYear(int year){
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUATERCENTENNIAL == 0;
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the maximum number of days in the given month for the given year.
     * Handles February based on whether the year is a leap year.
     *
     * @param year to check.
     * @param month to check.
     * @return number of days in the month.
     */
    private int getDaysInMonth(int year, int month) {
        // Handle the days for February (leap year consideration)
        if (month == Calendar.FEBRUARY) {
            return isLeapYear(year) ? feb_leap :feb;
        }

        // Handle other months using Calendar constants
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return month_with_31_days;

            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return month_with_30_days;

            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }


    /**
     * Checks if current date is equal to another date.
     * Two Date objects are considered equal if they have the same year, month, and day.
     *
     * @param obj The object to compare to.
     * @return true if the dates are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(!(obj instanceof Date)){
            return false;
        }

        Date other = (Date) obj;
        return this.year == other.year && this.month == other.month && this.day == other.day;
    }

    /**
     * Compares current Date object to another Date object.
     * Dates are compared first by year, then by month, then by day.
     *
     * @param other other Date object to compare to.
     * @return a negative integer, zero, or a positive integer as this date is
     *         less than, equal to, or greater than the specified date.
     */
    @Override
    public int compareTo(Date other) {
        if (this.year < other.year) {
            return -1;
        } else if (this.year > other.year) {
            return 1;
        }

        if (this.month < other.month) {
            return -1;
        } else if (this.month > other.month) {
            return 1;
        }

        if (this.day < other.day) {
            return -1;
        } else if (this.day > other.day) {
            return 1;
        }

        return 0;
    }


    /**
     * Returns a string representation of the date in the format: mm/dd/yyyy.
     *
     * @return A string representing the date.
     */
    @Override
    public String toString() {
        return (this.month +1 ) + "/" + this.day + "/" + this.year;
    }

    /**
     * Checks if the date is today's date.
     *
     * @return true if the date is today, false otherwise.
     */
    public boolean isToday() {
        Calendar today = Calendar.getInstance();
        return this.year == today.get(Calendar.YEAR) &&
                this.month == today.get(Calendar.MONTH) &&
                this.day == today.get(Calendar.DAY_OF_MONTH);
    }

    public boolean isInThePast() {
        Calendar today = Calendar.getInstance();
        int currentYear = today.get(Calendar.YEAR);
        int currentMonth = today.get(Calendar.MONTH);  // January is 0
        int currentDay = today.get(Calendar.DAY_OF_MONTH);

        if (this.year < currentYear) {
            return true;
        }
        if (this.year == currentYear) {
            if (this.month < currentMonth) {
                return true;
            }
            if (this.month == currentMonth) {
                return this.day < currentDay;
            }
        }
        return false;
    }

    /**
     * Checks if the date falls on a weekend (Saturday or Sunday).
     *
     * @return true if the date is a weekend, false otherwise.
     */
    public boolean isWeekend() {
        Calendar cal = Calendar.getInstance();
        cal.set(this.year, this.month, this.day);

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);
    }

    /**
     * Checks if the date is within six months from today.
     *
     * @return true if the date is within six months, false otherwise.
     */
    public boolean isWithinSixMonths() {
        Calendar today = Calendar.getInstance(); // Get the current date
        Calendar sixMonthsLater = Calendar.getInstance(); // Create a calendar for six months later
        sixMonthsLater.add(Calendar.MONTH, 6); // Move the calendar 6 months ahead

        Calendar dateToCheck = Calendar.getInstance(); // Create a calendar for the date to check
        dateToCheck.set(this.year, this.month - 1, this.day); // Set the date (adjust month for 0-indexed)

        return dateToCheck.before(sixMonthsLater) || dateToCheck.equals(sixMonthsLater);
    }

    /**
     * Test cases for the Date class.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Test cases
        Date date1 = new Date(1, 32, 2025);// Invalid as Jan has only 31 days
        Date date2 = new Date(2, 29, 2025);//Invalid as 2025 is not a leap yr
        Date date3 = new Date(13, 13, 2024);//Invalid as the date is present in the past
        Date date4 = new Date(9, 13, 1854);//Invalid as year is less than 1900
        Date date5 = new Date(10, 31, 2024);//Valid calendar date
        Date date6 = new Date(12, 13, 2024);

        System.out.println(date1.isValid());
        System.out.println(date2.isValid());
        System.out.println(date3.isValid());
        System.out.println(date4.isValid());
        System.out.println(date5.isValid());
        System.out.println(date6.isValid());

    }
}

