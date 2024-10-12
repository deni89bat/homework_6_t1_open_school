package tests.examples;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

@Disabled
public class JSAlertTests {
    @BeforeEach
    void setup() {
        open("https://the-internet.herokuapp.com/");
    }

    @Test
    void alertTest() {
        SelenideElement jsAlertsButton = $x("//a[@href='/javascript_alerts']");
        jsAlertsButton.should(visible).click();

        SelenideElement simpleAlertButton = $x("//button[@onclick='jsAlert()']");
        simpleAlertButton.should(visible).click();
        Alert activeAlert = switchTo().alert();
        sleep(1000);
        activeAlert.accept();
        sleep(1000);
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
