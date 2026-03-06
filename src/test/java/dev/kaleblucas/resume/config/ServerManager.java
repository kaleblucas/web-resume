package dev.kaleblucas.resume.config;

import dev.kaleblucas.resume.api.ResumeRoutes;
import io.javalin.Javalin;

public final class ServerManager {

    private static Javalin app;

    private ServerManager() {}

    public static synchronized void start() {
        if (app != null) return;
        app = Javalin.create(config ->
            config.staticFiles.add(staticConfig -> {
                staticConfig.directory = "/static";
                staticConfig.hostedPath = "/";
            })
        );
        ResumeRoutes.register(app);
        app.start(TestConfig.port());
    }

    public static synchronized void stop() {
        if (app != null) {
            app.stop();
            app = null;
        }
    }
}
