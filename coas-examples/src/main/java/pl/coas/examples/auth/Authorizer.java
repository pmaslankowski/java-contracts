package pl.coas.examples.auth;

public class Authorizer {

    private static boolean authorized;

    public static boolean isAuthorized() {
        return authorized;
    }

    public static void toggleAuthorized() {
        authorized = !authorized;
    }
}
