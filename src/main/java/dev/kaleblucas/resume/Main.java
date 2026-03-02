package dev.kaleblucas.resume;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Embedded Javalin server for the web-resume portfolio.
 *
 * Serves all static HTML/CSS/JS from classpath:/static
 * No backend API endpoints — all formatting/tools run client-side.
 */
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticConfig -> {
                staticConfig.directory = "/static";
                staticConfig.hostedPath = "/";
            });
        })
        .start(port);

        log.info("✓ Server running on http://localhost:{}", port);
    }
}
