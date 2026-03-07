package dev.kaleblucas.resume.data;

import dev.kaleblucas.resume.model.Experience;
import dev.kaleblucas.resume.model.SkillCategory;

import java.util.List;

public class ResumeData {

    public static List<SkillCategory> skills() {
        return List.of(
            new SkillCategory("Test Automation", List.of(
                "Selenium", "Playwright", "Page Object Model",
                "API Testing", "Cross-browser Testing", "Data-driven Testing"
            )),
            new SkillCategory("Development", List.of(
                "Java", "SQL", "Git Version Control",
                "Jenkins CI/CD", "Code Review", "Agile Methodologies"
            )),
            new SkillCategory("Infrastructure", List.of(
                "Docker", "System Deployment", "Linux Administration",
                "Proxmox VE", "System Monitoring", "Backup & Recovery"
            )),
            new SkillCategory("AI", List.of(
                "Prompt Engineering", "Agentic AI Workflows", "AI-assisted Development",
                "LLM Tool Integration", "Workflow Automation", "Context Engineering"
            ))
        );
    }

    public static List<Experience> experience() {
        return List.of(
            new Experience(
                "Quavo Fraud & Disputes",
                "Lead, Automation Engineer",
                "Oct. 2023 \u2014 Present",
                "Remote",
                List.of(
                    "Fintech platform processing nearly 1 million disputed transactions monthly, built on PEGA.",
                    "Designed a risk-based regression strategy pairing broad coverage with reduced manual execution overhead.",
                    "Developed tactics to migrate assertions from Pega to a Java-based test framework.",
                    "Refined the vision and roadmap for the test automation development team."
                ),
                List.of("Risk-based Testing", "Planning", "Java", "AI", "System Building", "PEGA")
            ),
            new Experience(
                "VML",
                "Technology Specialist, Automation of Quality",
                "Feb. 2022 \u2014 Sept. 2023",
                "Kansas City, MO",
                List.of(
                    "Digital transformation agency delivering CMS and API solutions across tourism, finance, and retail clients.",
                    "Reduced automated test execution times from 30 min to 3 min via a migration to Playwright.",
                    "Administrated and stabilized Jenkins CI infrastructure across QA teams."
                ),
                List.of("Playwright", "Selenium", "Jenkins", "API Testing", "Java", "Drupal")
            ),
            new Experience(
                "Retail Success",
                "Quality Assurance Engineer",
                "Sept. 2020 \u2014 Feb. 2022",
                "Overland Park, KS",
                List.of(
                    "Mobile and web commerce platform development organization.",
                    "Designed test automation framework and critical coverage for e-commerce mobile app.",
                    "Validated API contracts, queried records with SQL, and intercepted network traffic for review."
                ),
                List.of("Appium", "C#", "SQL", "AWS")
            )
        );
    }
}
