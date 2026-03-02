package dev.kaleblucas.resume;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        Javalin app = Javalin.create(config -> {

            // ── Static files ──────────────────────────────────────────────
            // Anything inside src/main/resources/static/ is served from "/"
            config.staticFiles.add("/static", Location.CLASSPATH);

            // ── CORS (permissive for local dev) ───────────────────────────
            config.bundledPlugins.enableCors(cors ->
                cors.addRule(rule -> rule.anyHost())
            );

        }).start(port);

        // ── API routes ────────────────────────────────────────────────────
        ApiRoutes.register(app);

        log.info("web-resume listening on port {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }
}
