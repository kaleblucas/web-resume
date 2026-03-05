package dev.kaleblucas.resume.page;

import dev.kaleblucas.resume.page.component.NavbarComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PortfolioPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final NavbarComponent navbar;

    @FindBy(css = ".hero")
    private WebElement heroSection;

    @FindBy(css = ".hero-title")
    private WebElement heroTitle;

    @FindBy(css = ".hero-subtitle")
    private WebElement heroSubtitle;

    @FindBy(css = ".hero-cta")
    private WebElement heroCta;

    @FindBy(id = "about")
    private WebElement aboutSection;

    @FindBy(css = ".disc-svg")
    private WebElement discChart;

    @FindBy(id = "skills-grid")
    private WebElement skillsGrid;

    @FindBy(id = "projects-grid")
    private WebElement projectsGrid;

    @FindBy(id = "contact")
    private WebElement contactSection;

    @FindBy(css = ".contact-email")
    private WebElement linkedInLink;

    @FindBy(css = ".footer-year")
    private WebElement footerYear;

    public PortfolioPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.navbar = new NavbarComponent(driver);
        driver.get(baseUrl + "/");
        PageFactory.initElements(driver, this);
        waitForDynamicContent();
    }

    private void waitForDynamicContent() {
        wait.until(d -> !skillsGrid.findElements(By.cssSelector(".skill-category")).isEmpty());
        wait.until(d -> !projectsGrid.findElements(By.cssSelector(".project-card")).isEmpty());
    }

    public NavbarComponent navbar() {
        return navbar;
    }

    public String getHeroTitle() {
        return heroTitle.getText();
    }

    public String getHeroSubtitle() {
        return heroSubtitle.getText();
    }

    public boolean isHeroVisible() {
        return heroSection.isDisplayed();
    }

    public void clickViewWorkCta() {
        heroCta.click();
    }

    public boolean isDiscChartPresent() {
        new Actions(driver).scrollToElement(discChart).perform();
        return discChart.isDisplayed();
    }

    public List<WebElement> getSkillCategories() {
        return skillsGrid.findElements(By.cssSelector(".skill-category"));
    }

    public List<String> getSkillCategoryNames() {
        return getSkillCategories().stream()
            .map(card -> {
                new Actions(driver).scrollToElement(card).perform();
                return card.findElement(By.tagName("h3")).getText();
            })
            .toList();
    }

    public List<String> getSkillsForCategory(String categoryName) {
        return getSkillCategories().stream()
            .filter(card -> {
                new Actions(driver).scrollToElement(card).perform();
                return card.findElement(By.tagName("h3")).getText().equals(categoryName);
            })
            .findFirst()
            .map(card -> card.findElements(By.tagName("li")).stream()
                .map(WebElement::getText).toList())
            .orElse(List.of());
    }

    public List<WebElement> getExperienceCards() {
        return projectsGrid.findElements(By.cssSelector(".project-card"));
    }

    public int getExperienceCount() {
        return getExperienceCards().size();
    }

    public String getExperienceCompany(int index) {
        WebElement card = getExperienceCards().get(index);
        new Actions(driver).scrollToElement(card).perform();
        String role = card.findElement(By.cssSelector(".project-role")).getText();
        return card.findElement(By.tagName("h3")).getText().replace(role, "").trim();
    }

    public String getExperienceRole(int index) {
        WebElement role = getExperienceCards().get(index)
            .findElement(By.cssSelector(".project-role"));
        new Actions(driver).scrollToElement(role).perform();
        return role.getText();
    }

    public String getExperienceTenure(int index) {
        return getExperienceCards().get(index)
            .findElement(By.cssSelector(".project-tenure")).getText();
    }

    public String getExperienceLocation(int index) {
        return getExperienceCards().get(index)
            .findElement(By.cssSelector(".project-location")).getText();
    }

    public List<String> getExperienceTechTags(int index) {
        return getExperienceCards().get(index)
            .findElements(By.cssSelector(".project-tech span")).stream()
            .map(WebElement::getText).toList();
    }

    public String getLinkedInUrl() {
        return linkedInLink.getAttribute("href");
    }

    public String getFooterYear() {
        return footerYear.getText();
    }

    public void scrollToSection(String sectionId) {
        ((JavascriptExecutor) driver).executeScript(
            "document.getElementById(arguments[0]).scrollIntoView(true);", sectionId);
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }
}
