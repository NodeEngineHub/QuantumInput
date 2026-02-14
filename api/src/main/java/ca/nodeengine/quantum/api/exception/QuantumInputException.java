package ca.nodeengine.quantum.api.exception;

/**
 * Explicit Quantum Input Exceptions will use this exception.
 *
 * @author FX
 */
public class QuantumInputException extends RuntimeException {
    public QuantumInputException(String message) {
        super(message);
    }
}
