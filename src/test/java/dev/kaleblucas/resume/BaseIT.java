package dev.kaleblucas.resume;

import dev.kaleblucas.resume.config.ServerManager;
import dev.kaleblucas.resume.config.TestConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public abstract class BaseIT {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void startServer() {
        ServerManager.start();
    }

    @AfterSuite(alwaysRun = true)
    public void stopServer() {
        ServerManager.stop();
    }

    @BeforeMethod(alwaysRun = true)
    public void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        if (TestConfig.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.defaultTimeoutSeconds()));
        driver.manage().window().setSize(new Dimension(1440, 900));
    }

    @AfterMethod(alwaysRun = true)
    public void teardownDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected String baseUrl() {
        return TestConfig.baseUrl();
    }
}
