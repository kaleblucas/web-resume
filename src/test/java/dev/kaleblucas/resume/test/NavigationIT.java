package dev.kaleblucas.resume.test;

import dev.kaleblucas.resume.BaseIT;
import dev.kaleblucas.resume.page.PortfolioPage;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NavigationIT extends BaseIT {

    private PortfolioPage page;

    @BeforeMethod
    public void openPage() {
        page = new PortfolioPage(driver, baseUrl());
    }

    @Test
    public void navbarGetsScrolledClassAfterScrollingDown() {
        page.scrollToSection("skills");
        page.navbar().waitUntilScrolled();
        assertThat(page.navbar().isNavbarScrolled()).isTrue();
    }

    @Test
    public void navbarLosesScrolledClassWhenBackAtTop() {
        page.scrollToSection("skills");
        page.navbar().waitUntilScrolled();
        page.scrollToTop();
        page.navbar().waitUntilNotScrolled();
        assertThat(page.navbar().isNavbarScrolled()).isFalse();
    }

    @Test
    public void logoHiddenWhenHeroIsVisible() {
        page.scrollToTop();
        page.navbar().waitUntilLogoHidden();
        assertThat(page.navbar().isLogoVisible()).isFalse();
    }

    @Test
    public void logoAppearsAfterScrollingPastHero() {
        page.scrollToSection("skills");
        page.navbar().waitUntilLogoVisible();
        assertThat(page.navbar().isLogoVisible()).isTrue();
    }

    @Test
    public void hamburgerOpensMenuOnMobile() {
        driver.manage().window().setSize(new Dimension(375, 812));
        page.navbar().clickHamburger();
        assertThat(page.navbar().isMenuOpen()).isTrue();
    }

    @Test
    public void hamburgerClosesMenuOnSecondClick() {
        driver.manage().window().setSize(new Dimension(375, 812));
        page.navbar().clickHamburger();
        page.navbar().clickHamburger();
        assertThat(page.navbar().isMenuOpen()).isFalse();
    }

    @Test
    public void toolsDropdownNavigatesToJsonFormatter() {
        page.navbar().navigateToJsonFormatter();
        assertThat(driver.getCurrentUrl()).contains("json-formatter.html");
    }

    @Test
    public void toolsDropdownNavigatesToXmlFormatter() {
        page.navbar().navigateToXmlFormatter();
        assertThat(driver.getCurrentUrl()).contains("xml-formatter.html");
    }
}
