package model;

public enum Radiology {
    CATSCAN("CT Scan"),
    ULTRASOUND("Ultrasound"),
    XRAY("X-Ray");

    private final String serviceName;

    /**
     * Constructor for the enum.
     *
     * @param serviceName the name of the imaging service.
     */
    Radiology(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * Retrieves the name of the imaging service.
     *
     * @return the name of the imaging service.
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Returns a string representation of the imaging service.
     *
     * @return the name of the imaging service.
     */    @Override
    public String toString() {
        return serviceName;
    }
}
