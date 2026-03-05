package dev.kaleblucas.resume.page;

import dev.kaleblucas.resume.page.component.NavbarComponent;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseFormatterPage {

    protected final WebDriver driver;
    private final WebDriverWait wait;
    private final NavbarComponent navbar;

    @FindBy(id = "jf-input")
    private WebElement inputTextarea;

    @FindBy(id = "jf-output")
    private WebElement outputDiv;

    @FindBy(id = "jf-error-panel")
    private WebElement errorPanel;

    @FindBy(id = "jf-error-msg")
    private WebElement errorMessage;

    @FindBy(id = "jf-status-indicator")
    private WebElement statusDot;

    @FindBy(id = "jf-status-text")
    private WebElement statusText;

    @FindBy(id = "jf-char-count")
    private WebElement charCount;

    @FindBy(id = "jf-depth")
    private WebElement depth;

    @FindBy(id = "jf-clear-btn")
    private WebElement clearBtn;

    @FindBy(id = "jf-indent-2")
    private WebElement indent2Btn;

    @FindBy(id = "jf-indent-4")
    private WebElement indent4Btn;

    @FindBy(id = "jf-minify-btn")
    private WebElement minifyBtn;

    @FindBy(id = "jf-tree-wrap")
    private WebElement treeWrap;

    @FindBy(id = "jf-expand-all")
    private WebElement expandAllBtn;

    @FindBy(id = "jf-collapse-all")
    private WebElement collapseAllBtn;

    protected BaseFormatterPage(WebDriver driver, String url) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(2));
        this.navbar = new NavbarComponent(driver);
        driver.get(url);
        PageFactory.initElements(driver, this);
    }

    public NavbarComponent navbar() {
        return navbar;
    }

    protected void enterInput(String text) {
        inputTextarea.clear();
        inputTextarea.sendKeys(text);
        wait.until(ExpectedConditions.textToBePresentInElement(charCount, text.length() + " chars"));
    }

    public void clickClear() {
        clearBtn.click();
    }

    public void clickIndent2() {
        indent2Btn.click();
    }

    public void clickIndent4() {
        indent4Btn.click();
    }

    public void clickMinify() {
        minifyBtn.click();
    }

    public void clickExpandAll() {
        expandAllBtn.click();
    }

    public void clickCollapseAll() {
        collapseAllBtn.click();
    }

    public String getOutputText() {
        return outputDiv.getText();
    }

    public String getStatusText() {
        new Actions(driver).scrollToElement(statusText).perform();
        return statusText.getText();
    }

    public String getStatusDotClass() {
        return statusDot.getAttribute("class");
    }

    public String getCharCount() {
        return charCount.getText();
    }

    public String getDepth() {
        return depth.getText();
    }

    public boolean isErrorPanelVisible() {
        if (errorPanel.getAttribute("class").contains("hidden")) return false;
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", errorPanel);
        return wait.until(d -> errorPanel.getAttribute("class").contains("visible"));
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public boolean isTreeVisible() {
        if (treeWrap.getAttribute("class").contains("hidden")) return false;
        new Actions(driver).scrollToElement(treeWrap).perform();
        return treeWrap.isDisplayed();
    }

    public boolean isIndent2Active() {
        return indent2Btn.getAttribute("class").contains("active");
    }

    public boolean isIndent4Active() {
        return indent4Btn.getAttribute("class").contains("active");
    }

    public boolean isMinifyActive() {
        return minifyBtn.getAttribute("class").contains("active");
    }

}
