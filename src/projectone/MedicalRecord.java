package projectone;

/**
 * The MedicalRecord class represents a collection of Patient objects. It provides functionality
 * to add patients, grow the internal array when needed, retrieve patients by index,
 * and swap patients at specified indices.
 *
 * This class is designed to manage a dynamic list of patients, automatically resizing the
 * internal array when it reaches capacity.
 *
 * The toString() method returns a formatted string representation of the medical record,
 * including details of all patients in the collection.
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public class MedicalRecord {
    private Patient[] patients;
    private int size;
    //private int initialCapacity = 4;

    /**
     * Constructs a MedicalRecord with an initial capacity for patients.
     *
     * @param initialCapacity The initial capacity of the medical record (number of patients it can store initially).
     */
    public MedicalRecord(int initialCapacity) {
        this.patients = new Patient[initialCapacity];
        this.size = 0;//sent to empty medRec initially
    }

    /**
     * Returns the current number of patients in the medical record.
     *
     * @return The number of patients in the record.
     */
    public int getSize() {
        return size;
    }

    /**
     * Adds a new patient to the medical record.
     * If the internal array is full, it grows the array to accommodate more patients.
     *
     * @param patient The patient to add to the medical record.
     */
    public void add(Patient patient) {
        if (this.size == patients.length) {
            grow();
        }
        patients[size++] = patient;
    }

    /**
     * Grows the internal array by increasing its capacity by 4.
     * This method is called when the array reaches its current capacity.
     */
    private void grow() {
        //new array with higher capacity
        Patient[] patientsNew = new Patient[patients.length + 4];
        for (int i = 0; i < size; i++) {
            patientsNew[i] = patients[i];
        }
        patients = patientsNew;
    }

    /**
     * Returns the patient at the specified index in the medical record.
     *
     * @param index The index of the patient to retrieve.
     * @return The patient at the specified index.
     */
    public Patient getPatientAtIndex(int index) {
        return patients[index];
    }

    /**
     * Swaps two patients in the medical record at the specified indices.
     *
     * @param index1 The index of the first patient to swap.
     * @param index2 The index of the second patient to swap.
     * @throws IndexOutOfBoundsException if either index is out of bounds.
     */
    public void swap(int index1, int index2) {
        if (index1 < 0 || index1 >= patients.length || index2 < 0 || index2 >= patients.length) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Patient temp = patients[index1];
        patients[index1] = patients[index2];
        patients[index2] = temp;
    }

    /**
     * Returns a string representation of the medical record, including all patients.
     * Each patient's details are printed on a new line.
     *
     * @return A string representation of the medical record.
     */
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