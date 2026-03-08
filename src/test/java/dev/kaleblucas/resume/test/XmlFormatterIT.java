package dev.kaleblucas.resume.test;

import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.page.XmlFormatterPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlFormatterIT extends BaseIT {

    private XmlFormatterPage page;

    @BeforeMethod
    public void openPage() {
        page = new XmlFormatterPage(driver, baseUrl());
    }

    @Test
    public void initialStatusIsReady() {
        assertThat(page.getStatusText()).isEqualTo("ready");
    }

    @Test
    public void initialStatusDotIsIdle() {
        assertThat(page.getStatusDotClass()).contains("jf-status-idle");
    }

    @Test
    public void indent2IsActiveByDefault() {
        assertThat(page.isIndent2Active()).isTrue();
    }

    @Test
    public void validXmlShowsFormattedOutput() {
        page.enterXml("<root><child>text</child></root>");
        assertThat(page.getOutputText()).contains("root").contains("child");
    }

    @Test
    public void validXmlUpdatesStatusToValid() {
        page.enterXml("<root><child>text</child></root>");
        assertThat(page.getStatusText()).isEqualTo("valid xml");
    }

    @Test
    public void validXmlShowsElementCount() {
        page.enterXml("<root><a/><b/></root>");
        assertThat(page.isNodeCountVisible()).isTrue();
        assertThat(page.getNodeCount()).contains("elements");
    }

    @Test
    public void validXmlShowsDepth() {
        page.enterXml("<root><child><leaf/></child></root>");
        assertThat(page.getDepth()).contains("depth");
    }

    @Test
    public void validXmlShowsTreeExplorer() {
        page.enterXml("<root><child/></root>");
        assertThat(page.isTreeVisible()).isTrue();
    }

    @Test
    public void charCountUpdatesOnInput() {
        page.enterXml("<a/>");
        assertThat(page.getCharCount()).isEqualTo("4 chars");
    }

    @Test
    public void invalidXmlShowsErrorPanel() {
        page.enterXml("<unclosed>");
        assertThat(page.isErrorPanelVisible()).isTrue();
    }

    @Test
    public void invalidXmlUpdatesStatusToInvalid() {
        page.enterXml("<unclosed>");
        assertThat(page.getStatusText()).isEqualTo("invalid xml");
    }

    @Test
    public void invalidXmlHidesTreeExplorer() {
        page.enterXml("<unclosed>");
        assertThat(page.isTreeVisible()).isFalse();
    }

    @Test
    public void indent4ActivatesOnClick() {
        page.enterXml("<root><child/></root>");
        page.clickIndent4();
        assertThat(page.isIndent4Active()).isTrue();
        assertThat(page.isIndent2Active()).isFalse();
    }

    @Test
    public void minifyActivatesOnClick() {
        page.enterXml("<root><child/></root>");
        page.clickMinify();
        assertThat(page.isMinifyActive()).isTrue();
        assertThat(page.isIndent2Active()).isFalse();
    }

    @Test
    public void clearResetsStatusToReady() {
        page.enterXml("<root/>");
        page.clickClear();
        assertThat(page.getStatusText()).isEqualTo("ready");
    }

    @Test
    public void clearHidesTreeExplorer() {
        page.enterXml("<root/>");
        page.clickClear();
        assertThat(page.isTreeVisible()).isFalse();
    }

    @Test
    public void clearResetsOutput() {
        page.enterXml("<root/>");
        page.clickClear();
        assertThat(page.getOutputText()).isEmpty();
    }
}
