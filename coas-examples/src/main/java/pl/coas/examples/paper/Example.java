package pl.coas.examples.paper;

public class Example {

    @Secured
    @Logged
    public int exampleMethod() {
        return 42;
    }
}
