package dev.kaleblucas.resume.page.component;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class NavbarComponent {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = ".nav-logo")
    private WebElement logo;

    @FindBy(css = ".hamburger")
    private WebElement hamburger;

    @FindBy(css = ".navbar")
    private WebElement navbar;

    @FindBy(css = ".nav-dropdown-toggle")
    private WebElement toolsDropdownToggle;

    @FindBy(css = ".nav-dropdown-menu")
    private WebElement toolsDropdownMenu;

    @FindBy(css = ".dark-mode-toggle")
    private WebElement darkModeToggle;

    public NavbarComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    public boolean isLogoVisible() {
        return logo.getAttribute("class").contains("logo-visible");
    }

    public boolean isNavbarScrolled() {
        return navbar.getAttribute("class").contains("scrolled");
    }

    public void waitUntilScrolled() {
        wait.until(ignored -> isNavbarScrolled());
    }

    public void waitUntilNotScrolled() {
        wait.until(ignored -> !isNavbarScrolled());
    }

    public void waitUntilLogoVisible() {
        wait.until(ignored -> isLogoVisible());
    }

    public void waitUntilLogoHidden() {
        wait.until(ignored -> !isLogoVisible());
    }

    public boolean isMenuOpen() {
        return navbar.getAttribute("class").contains("nav-open");
    }

    public boolean isDarkMode() {
        return driver.findElement(By.tagName("html"))
                     .getAttribute("class").contains("dark-mode");
    }

    public void clickHamburger() {
        hamburger.click();
    }

    public void openToolsDropdown() {
        toolsDropdownToggle.click();
        wait.until(ExpectedConditions.attributeContains(toolsDropdownMenu, "class", "open"));
    }

    public void toggleDarkMode() {
        openToolsDropdown();
        darkModeToggle.click();
    }

    public void clickNavLink(String section) {
        driver.findElement(By.cssSelector(
            ".nav-menu a[href='#" + section + "'], " +
            ".nav-menu a[href='index.html#" + section + "']"
        )).click();
    }

    public void navigateToJsonFormatter() {
        openToolsDropdown();
        driver.findElement(By.cssSelector(
            ".nav-dropdown-menu a[href='json-formatter.html']"
        )).click();
    }

    public void navigateToXmlFormatter() {
        openToolsDropdown();
        driver.findElement(By.cssSelector(
            ".nav-dropdown-menu a[href='xml-formatter.html']"
        )).click();
    }
}
