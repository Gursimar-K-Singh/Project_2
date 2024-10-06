package model;

public enum Radiology {
    CATSCAN("CT Scan"),
    ULTRASOUND("Ultrasound"),
    XRAY("X-Ray");

    private final String serviceName;

    // Constructor for the enum
    Radiology(String serviceName) {
        this.serviceName = serviceName;
    }

    // Getter for the service name
    public String getServiceName() {
        return serviceName;
    }

    // Overriding toString to return the service name
    @Override
    public String toString() {
        return serviceName;
    }
}
