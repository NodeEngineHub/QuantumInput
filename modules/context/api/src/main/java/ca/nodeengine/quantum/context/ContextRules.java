package ca.nodeengine.quantum.context;

/**
 * Resolution rules for context
 *
 * @author FX
 */
public enum ContextRules {

    /// Highest priority wins
    HIGHEST_PRIORITY,

    /// First match wins
    FIRST_MATCH,

    /// Aggregate values (for axes)
    AGGREGATE
}
