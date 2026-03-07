package dev.kaleblucas.resume.api;

import dev.kaleblucas.resume.data.ResumeData;
import io.javalin.Javalin;

public class ResumeRoutes {

    public static void register(Javalin app) {
        app.get("/api/skills",     ctx -> ctx.json(ResumeData.skills()));
        app.get("/api/experience", ctx -> ctx.json(ResumeData.experience()));
    }
}
