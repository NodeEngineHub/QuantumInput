package ca.nodeengine.quantum.api.context;

/**
 * Resolution rules for context
 */
public enum ContextRules {

    /// Highest priority wins
    HIGHEST_PRIORITY,

    /// First match wins
    FIRST_MATCH,

    /// Aggregate values (for axes)
    AGGREGATE
}
