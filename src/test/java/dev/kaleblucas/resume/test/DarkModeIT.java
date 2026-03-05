package dev.kaleblucas.resume.test;

import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.page.JsonFormatterPage;
import dev.kaleblucas.resume.page.PortfolioPage;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DarkModeIT extends BaseIT {

    @Test
    public void darkModeTogglesClassOnHtmlElement() {
        PortfolioPage page = new PortfolioPage(driver, baseUrl());
        assertThat(page.navbar().isDarkMode()).isFalse();
        page.navbar().toggleDarkMode();
        assertThat(page.navbar().isDarkMode()).isTrue();
    }

    @Test
    public void darkModeCanBeToggledOff() {
        PortfolioPage page = new PortfolioPage(driver, baseUrl());
        page.navbar().toggleDarkMode();
        page.navbar().toggleDarkMode();
        assertThat(page.navbar().isDarkMode()).isFalse();
    }

    @Test
    public void darkModePersistsViaLocalStorageAfterReload() {
        PortfolioPage page = new PortfolioPage(driver, baseUrl());
        page.navbar().toggleDarkMode();

        String stored = (String) ((JavascriptExecutor) driver)
            .executeScript("return localStorage.getItem('portfolio-dark-mode');");
        assertThat(stored).isEqualTo("true");

        driver.navigate().refresh();
        page = new PortfolioPage(driver, baseUrl());
        assertThat(page.navbar().isDarkMode()).isTrue();
    }

    @Test
    public void darkModeWorksOnJsonFormatterPage() {
        JsonFormatterPage page = new JsonFormatterPage(driver, baseUrl());
        assertThat(page.navbar().isDarkMode()).isFalse();
        page.navbar().toggleDarkMode();
        assertThat(page.navbar().isDarkMode()).isTrue();
    }
}
