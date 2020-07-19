package pl.coas.api;

/**
 * Class representing aspect types;
 * 
 * @author pmaslankowski
 */
public enum AspectType {

    /**
     * Transient aspects use new aspect instance on each advised join point.
     */
    TRANSIENT,

    /**
     * Singleton aspects use the same aspect instance on each advised joinpoint.
     */
    SINGLETON;
}
