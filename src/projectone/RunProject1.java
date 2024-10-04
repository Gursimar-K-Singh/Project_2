package projectone;

/**
 * The RunProject1 class serves as the entry point for the application.
 * It initiates the scheduling system by creating a new instance of the Scheduler class
 * and calling its `run()` method to start the program.
 *
 * This class contains only the `main()` method, which is responsible for starting the application.
 *
 * The Scheduler class handles the core functionality of scheduling appointments, managing providers,
 * and handling other related tasks.
 *
 * @see Scheduler
 *
 * @author AparnaSrinivas
 * @author GursimarSingh
 */
public class RunProject1 {

    /**
     * The main method serves as the entry point of the application.
     * It creates an instance of the Scheduler class and starts the scheduling system by calling its `run()` method.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {

        new Scheduler().run();
    }
}
