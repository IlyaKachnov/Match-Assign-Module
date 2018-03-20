package app.helpers;

import java.util.Arrays;

public class UrlHelper {
    private static final String[] patterns = {"/stadiums", "/leagues", "/matches", "/users", "/slots",
            "/slot-types", "/sessions"};

    public static boolean isContained(String uri) {
        return Arrays.stream(patterns).anyMatch(str -> str.equals(uri) || uri.contains(str));
    }
}
