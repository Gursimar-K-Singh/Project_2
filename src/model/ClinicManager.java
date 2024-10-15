package model;


import util.Date;
import util.sort;

import java.util.Scanner;
import util.List; // Import the new List class
import util.CircleList;
import java.io.File;
import java.io.FileNotFoundException;

public class ClinicManager {

    private List<Provider> providerList;
    private List<Appointment> appointmentList;
    private CircleList<Technician> technicianList;

    // Constants for calculating a date within 6 months
    private static final int TOTAL_MONTHS_IN_YEAR = 12;
    private static final int HALF_YEAR_IN_MONTHS = 6;

    public ClinicManager() {
        providerList = new List<>();
        appointmentList = new List<>();
        this.technicianList = new CircleList<>();

        //loadProviders(); //load + sort providers + instantiate technician rotation
        //initializeTechnicianRotation(); //load technician rotation circular list (before sorting providers)
        sort.provider(providerList); //Sort providers
        //displayProviders(); //print providers
        //printTechnicianRotation(); //print technician rotation circular list
    }

}
