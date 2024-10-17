package model;

/**
 * Represents different types of radiology imaging services.
 *
 * This enum defines the available imaging services, including CT scans,
 * ultrasounds, and X-rays. Each service has a name that can be retrieved
 * for display or processing purposes.
 *
 * @author Gursimar Singh
 */

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
     * Returns a string representation of the imaging service.
     *
     * @return the name of the imaging service.
     */    @Override
    public String toString() {
        switch (serviceName.toLowerCase()) {
            case "x-ray":
                return "XRAY";
            case "ct scan":
                return "CATSCAN";
            case "ultrasound":
                return "ULTRASOUND";
            default:
                return serviceName.toUpperCase(); // Fallback to return the service name in uppercase if not matched
        }
    }
}
