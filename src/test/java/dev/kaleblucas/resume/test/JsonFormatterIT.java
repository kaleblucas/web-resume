package dev.kaleblucas.resume.test;

import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.page.JsonFormatterPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonFormatterIT extends BaseIT {

    private JsonFormatterPage page;

    @BeforeMethod
    public void openPage() {
        page = new JsonFormatterPage(driver, baseUrl());
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
    public void validJsonShowsFormattedOutput() {
        page.enterJson("{\"name\":\"test\",\"value\":42}");
        assertThat(page.getOutputText()).contains("name").contains("test");
    }

    @Test
    public void validJsonUpdatesStatusToValid() {
        page.enterJson("{\"name\":\"test\"}");
        assertThat(page.getStatusText()).isEqualTo("valid json");
    }

    @Test
    public void validJsonShowsKeyCount() {
        page.enterJson("{\"a\":1,\"b\":2}");
        assertThat(page.isKeyCountVisible()).isTrue();
        assertThat(page.getKeyCount()).contains("keys");
    }

    @Test
    public void validJsonShowsDepth() {
        page.enterJson("{\"a\":{\"b\":1}}");
        assertThat(page.getDepth()).contains("depth");
    }

    @Test
    public void validJsonShowsTreeExplorer() {
        page.enterJson("{\"key\":\"value\"}");
        assertThat(page.isTreeVisible()).isTrue();
    }

    @Test
    public void charCountUpdatesOnInput() {
        page.enterJson("{}");
        assertThat(page.getCharCount()).isEqualTo("2 chars");
    }

    @Test
    public void invalidJsonShowsErrorPanel() {
        page.enterJson("{invalid}");
        assertThat(page.isErrorPanelVisible()).isTrue();
    }

    @Test
    public void invalidJsonUpdatesStatusToInvalid() {
        page.enterJson("{invalid}");
        assertThat(page.getStatusText()).isEqualTo("invalid json");
    }

    @Test
    public void invalidJsonStatusDotIsError() {
        page.enterJson("{invalid}");
        assertThat(page.getStatusDotClass()).contains("jf-status-error");
    }

    @Test
    public void invalidJsonHidesTreeExplorer() {
        page.enterJson("{invalid}");
        assertThat(page.isTreeVisible()).isFalse();
    }

    @Test
    public void indent4ActivatesOnClick() {
        page.enterJson("{\"a\":1}");
        page.clickIndent4();
        assertThat(page.isIndent4Active()).isTrue();
        assertThat(page.isIndent2Active()).isFalse();
    }

    @Test
    public void minifyActivatesOnClick() {
        page.enterJson("{\"a\":1}");
        page.clickMinify();
        assertThat(page.isMinifyActive()).isTrue();
        assertThat(page.isIndent2Active()).isFalse();
        assertThat(page.isIndent4Active()).isFalse();
    }

    @Test
    public void minifyRemovesWhitespaceFromOutput() {
        page.enterJson("{\"a\": 1, \"b\": 2}");
        page.clickMinify();
        assertThat(page.getOutputText()).doesNotContain("\n");
    }

    @Test
    public void clearResetsStatusToReady() {
        page.enterJson("{\"a\":1}");
        page.clickClear();
        assertThat(page.getStatusText()).isEqualTo("ready");
    }

    @Test
    public void clearHidesTreeExplorer() {
        page.enterJson("{\"a\":1}");
        page.clickClear();
        assertThat(page.isTreeVisible()).isFalse();
    }

    @Test
    public void clearHidesErrorPanel() {
        page.enterJson("{invalid}");
        page.clickClear();
        assertThat(page.isErrorPanelVisible()).isFalse();
    }

    @Test
    public void clearResetsOutput() {
        page.enterJson("{\"a\":1}");
        page.clickClear();
        assertThat(page.getOutputText()).isEmpty();
    }
}
