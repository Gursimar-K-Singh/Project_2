package projectone;

//@author AparnaSrinivas @author GursimarSingh
public class Patient implements Comparable<Patient> {
    private Profile profile;
    private Visit visits;

    public Patient(Profile profile) {
        this.profile = profile;
        this.visits = null;//empty list
    }

    public Profile getProfile() {
        return profile;
    }

    //compare patients by profiles
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof Patient){
            Patient patient = (Patient) obj;
            return profile.equals(patient.profile);
        }
        //Patient patient = (Patient) obj;
        return false;
    }

    public void addVisit(Visit visit) {
        if (visits == null) {
            visits = visit; // If there are no visits yet, set it as the head
        } else {
            Visit current = visits;
            // Traverse to the end of the list
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(visit); // Add the new visit at the end
        }
    }

    public int charge() {
        int total = 0;
        Visit current = visits;//first visit - traversing start point
        while (current != null){
            total += current.getAppointment().getProvider().getSpecialty().getCharge();
            current = current.getNext();
        }
        return total;
    }

    @Override
    public String toString() {
        return profile.toString();//profile class's toString()
    }

    @Override
    public int compareTo(Patient other) {
        return this.profile.compareTo(other.profile);//profile class's compareTo()
    }

    //method calculates total charge due depending on patient's visits

}

