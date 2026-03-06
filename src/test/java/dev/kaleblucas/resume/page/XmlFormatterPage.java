package dev.kaleblucas.resume.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class XmlFormatterPage extends BaseFormatterPage {

    @FindBy(id = "jf-node-count")
    private WebElement nodeCount;

    public XmlFormatterPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl + "/xml-formatter.html");
        PageFactory.initElements(driver, this);
    }

    public void enterXml(String xml) {
        enterInput(xml);
    }

    public String getNodeCount() {
        return nodeCount.getText();
    }

    public boolean isNodeCountVisible() {
        return nodeCount.isDisplayed();
    }
}
