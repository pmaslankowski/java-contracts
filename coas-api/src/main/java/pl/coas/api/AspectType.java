package pl.coas.api;

/**
 * Class representing aspect types;
 * 
 * @author pmaslankowski
 */
public enum AspectType {

    /**
     * Prototype aspects use new aspect instance on each advised join point.
     */
    PROTOTYPE,

    /**
     * Singleton aspects use the same aspect instance on each advised joinpoint.
     */
    SINGLETON;
}
