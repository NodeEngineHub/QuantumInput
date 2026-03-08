package ca.nodeengine.quantum.exception;

/**
 * Base class for all exceptions thrown by the QuantumInput system.
 *
 * @author FX
 */
public class QuantumInputException extends RuntimeException {

    /**
     * Constructs a new QuantumInputException with the specified detail message.
     *
     * @param message The detail message.
     */
    public QuantumInputException(String message) {
        super(message);
    }
}
