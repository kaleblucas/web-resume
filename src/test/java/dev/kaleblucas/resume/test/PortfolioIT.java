package dev.kaleblucas.resume.test;

import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.page.PortfolioPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PortfolioIT extends BaseIT {

    private PortfolioPage page;

    @BeforeMethod
    public void openPage() {
        page = new PortfolioPage(driver, baseUrl());
    }

    @Test
    public void heroIsVisible() {
        assertThat(page.isHeroVisible()).isTrue();
    }

    @Test
    public void heroDisplaysName() {
        assertThat(page.getHeroTitle()).containsIgnoringCase("Kaleb");
    }

    @Test
    public void heroDisplaysRole() {
        assertThat(page.getHeroSubtitle()).containsIgnoringCase("QA Engineering");
    }

    @Test
    public void discChartIsPresent() {
        assertThat(page.isDiscChartPresent()).isTrue();
    }

    @Test
    public void skillsGridHasFourCategories() {
        assertThat(page.getSkillCategories()).hasSize(4);
    }

    @Test
    public void skillCategoryNamesMatchExpected() {
        assertThat(page.getSkillCategoryNames())
            .containsExactly("Test Automation", "Development", "Infrastructure", "AI");
    }

    @Test
    public void eachSkillCategoryHasSkills() {
        for (String name : page.getSkillCategoryNames()) {
            assertThat(page.getSkillsForCategory(name))
                .as("Skills for category: " + name)
                .isNotEmpty();
        }
    }

    @Test
    public void experienceGridHasThreeCards() {
        assertThat(page.getExperienceCount()).isEqualTo(3);
    }

    @Test
    public void firstExperienceCardShowsCorrectCompany() {
        assertThat(page.getExperienceCompany(0)).isEqualTo("Quavo Fraud & Disputes");
    }

    @Test
    public void firstExperienceCardShowsCorrectRole() {
        assertThat(page.getExperienceRole(0)).isEqualTo("Lead, Automation Engineer");
    }

    @Test
    public void experienceCardsHaveTechTags() {
        for (int i = 0; i < page.getExperienceCount(); i++) {
            assertThat(page.getExperienceTechTags(i))
                .as("Tech tags for card " + i)
                .isNotEmpty();
        }
    }

    @Test
    public void linkedInLinkPointsToCorrectUrl() {
        assertThat(page.getLinkedInUrl()).contains("linkedin.com/in/kaleblucas");
    }

    @Test
    public void footerShowsCurrentYear() {
        assertThat(page.getFooterYear()).contains("2026");
    }
}
