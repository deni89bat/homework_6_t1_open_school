package tests.examples;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tests.conditions.CustomElementConditions;
import tests.conditions.CustomElementsCollectionConditions;

import static com.codeborne.selenide.Selenide.*;

@Disabled
public class CustomElementConditionTests {
    @BeforeEach
    void setup() {
        open("https://the-internet.herokuapp.com/");
    }

    @Test
    void test() {
        SelenideElement checkBoxesButton = $x("//a[@href='/checkboxes']");
        checkBoxesButton.should(CustomElementConditions.visible());

        ElementsCollection elementsControls = $$x("//a");
        elementsControls.should(CustomElementsCollectionConditions.allElementsIsVisible())
                .should(CustomElementsCollectionConditions.elementsIsSortedAsc());
    }

    @AfterEach
    void tearDown() {
        closeWebDriver();
    }
}
