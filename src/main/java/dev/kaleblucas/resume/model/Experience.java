package dev.kaleblucas.resume.model;

import java.util.List;

public record Experience(
    String company, String role, String tenure, String location,
    List<String> bullets, List<String> tech
) {}
