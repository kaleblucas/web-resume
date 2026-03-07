package dev.kaleblucas.resume.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class JsonFormatterPage extends BaseFormatterPage {

    @FindBy(id = "jf-key-count")
    private WebElement keyCount;

    public JsonFormatterPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl + "/json-formatter.html");
        PageFactory.initElements(driver, this);
    }

    public void enterJson(String json) {
        enterInput(json);
    }

    public String getKeyCount() {
        return keyCount.getText();
    }

    public boolean isKeyCountVisible() {
        return keyCount.isDisplayed();
    }
}
