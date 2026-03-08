package dev.kaleblucas.resume;

import dev.kaleblucas.resume.api.ResumeRoutes;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Embedded Javalin server for the web-resume portfolio.
 *
 * Serves static HTML/CSS/JS from classpath:/static and exposes
 * REST endpoints under /api for resume content.
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
        });

        ResumeRoutes.register(app);
        app.start(port);

        log.info("✓ Server running on http://localhost:{}", port);
    }
}
