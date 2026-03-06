package dev.kaleblucas.resume.config;

public final class TestConfig {

    private TestConfig() {}

    public static String baseUrl() {
        String explicit = System.getProperty("test.baseUrl");
        if (explicit != null && !explicit.isBlank()) return explicit;
        return "http://localhost:" + port();
    }

    public static int port() {
        return Integer.parseInt(
            System.getProperty("test.port",
                System.getenv().getOrDefault("PORT", "8080")));
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("test.headless", "true"));
    }

    public static int defaultTimeoutSeconds() {
        return 10;
    }
}
