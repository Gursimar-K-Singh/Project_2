package model;
//@author AparnaSrinivas @author GursimarSingh
public enum Timeslot {
    SLOT1(9, 0),
    SLOT2(10, 45),
    SLOT3(11, 15),
    SLOT4(13, 30),
    SLOT5(15, 0),
    SLOT6(16, 15);

    private final int hour;
    private final int minute;

    Timeslot(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String toString(){
        int displayHour = hour % 12;
        if(displayHour == 0) {
            displayHour = 12;
        }
        String displayMinute = "";

        if(minute == 0){
            displayMinute = "00";
        }else{
            displayMinute = String.valueOf(minute);
        }

        if(hour <= 12){
            return displayHour + ":" + displayMinute + " AM";
        }else{
            return displayHour + ":" + displayMinute + " PM";
        }

    }

    public static void main(String[] args) {
        System.out.println("Available Time Slots:");

        // Iterate through each Timeslot and print its string representation
        for (Timeslot slot : Timeslot.values()) {
            System.out.println(slot.toString());
        }
    }

}
