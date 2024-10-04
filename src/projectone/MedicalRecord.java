package projectone;

//author @AparnaSrinivas @author GursimarSingh
public class MedicalRecord {
    private Patient[] patients;
    private int size;
    //private int initialCapacity = 4;

    public MedicalRecord(int initialCapacity) {
        this.patients = new Patient[initialCapacity];
        this.size = 0;//sent to empty medRec initially
    }

    public int getSize() {
        return size;
    }

    //adds new patient and increments array size when needed
    public void add(Patient patient) {
        if (this.size == patients.length) {
            grow();
        }
        patients[size++] = patient;
    }

    private void grow() {
        //new array with higher capacity
        Patient[] patientsNew = new Patient[patients.length + 4];
        for (int i = 0; i < size; i++) {
            patientsNew[i] = patients[i];
        }
        patients = patientsNew;
    }

    //getPatientIndex
    public Patient getPatientAtIndex(int index) {
        return patients[index];
    }

    public void swap(int index1, int index2) {
        if (index1 < 0 || index1 >= patients.length || index2 < 0 || index2 >= patients.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Patient temp = patients[index1];
        patients[index1] = patients[index2];
        patients[index2] = temp;
    }



    @Override
    public String toString() {

        StringBuilder medRec = new StringBuilder();
        medRec.append("Medical Record:\n");

        for (int i = 0; i < size; i++) {
            medRec.append(patients[i].toString()).append("\n");
        }
        return medRec.toString();
    }

}