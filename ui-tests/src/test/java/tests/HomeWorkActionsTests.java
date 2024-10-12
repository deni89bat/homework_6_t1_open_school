package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.junit5.ScreenShooterExtension;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import tests.conditions.CustomElementConditions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.addAttachment;
import static tests.conditions.CustomElementConditions.*;
@TestMethodOrder(OrderAnnotation.class)
public class HomeWorkActionsTests {
    // Регистрация расширения для создания скриншотов
    @RegisterExtension
    static ScreenShooterExtension screenshotEmAll = new ScreenShooterExtension(true).to("target/screenshots");

    @BeforeAll
    public static void setup() {
        Configuration.remote = "http://45.89.65.48:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserVersion = "125.0";
        Configuration.pageLoadStrategy = "eager";
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(true));
    }

    @BeforeEach
    void openBaseUrl() {
        open("https://the-internet.herokuapp.com/");
    }

    //Основные тестовые методы
    @Test
    @Order(1)
    @DisplayName("Drag and Drop")
    @Description("""
            Перейти на страницу Drag and Drop.
            Перетащить элемент A на элемент B.
            Задача на 10 баллов – сделать это, не прибегая к методу DragAndDrop();
            Проверить, что элементы поменялись местами""")
    public void dragAndDropTest() {
        SelenideElement dragAndDropButton = $x("//a[@href='/drag_and_drop']");
        SelenideElement elementA = $x("//div[@id='column-a']");
        SelenideElement elementB = $x("//div[@id='column-b']");

        clickLink(dragAndDropButton, dragAndDropButton.getText());
        moveElementAndCheck(elementA, elementB);

    }

    @Test
    @Order(2)
    @DisplayName("Context menu")
    @Description("""
            Перейти на страницу Context menu.
            Нажать правой кнопкой мыши на отмеченной области и проверить, что JS Alert имеет ожидаемый текст.""")
    public void contextMenuAlertTest() {
        SelenideElement contextMenuButton = $x("//a[@href='/context_menu']");
        SelenideElement boxElement = $x("//div[@id='hot-spot']");

        clickLink(contextMenuButton, contextMenuButton.getText());
        rightClickOnBox(boxElement);
        checkAlertText("You selected a context menu");
    }
    @Disabled
    @Test
    @Order(4)
    @DisplayName("Infinite Scroll")
    @Description("""
            Перейти на страницу Infinite Scroll.
            Проскролить страницу до текста «Eius», проверить, что текст в поле зрения.""")
    public void infiniteScrollTest() {
        SelenideElement infiniteScrollButton = $x("//a[@href='/infinite_scroll']");

        clickLink(infiniteScrollButton, infiniteScrollButton.getText());
        scrollToText("Eius");
    }

    @Test
    @Order(3)
    @DisplayName("Key Presses")
    @Description("""
            Перейти на страницу Key Presses.
            Нажать по 10 латинских символов, клавиши Enter, Ctrl, Alt, Tab.
            Проверить, что после нажатия отображается всплывающий текст снизу, соответствующий конкретной клавише.""")
    public void keyPressesTest() {
        SelenideElement keyPressesButton = $x("//a[@href='/key_presses']");

        clickLink(keyPressesButton, keyPressesButton.getText());
        pressKeysAndCheck("ABCDEFGHIJ",
                Keys.ENTER, Keys.CONTROL, Keys.ALT, Keys.TAB);
    }

    //Шаги для тестов
    //Общий
    @Step("Перейти на страницу {buttonName}")
    private void clickLink(SelenideElement buttonElement, String buttonName) {
        buttonElement.shouldBe(CustomElementConditions.visible()).click();
    }

    //Drag and Drop
    @Step("Перемещаем элемент A на элемент B. Проверяем, что элементы поменялись местами")
    private void moveElementAndCheck(SelenideElement holdElement, SelenideElement targetElement) {
        String holdElementText = holdElement.getText();
        String targetElementText = targetElement.getText();
        Actions actions = new Actions(getWebDriver());

        actions.clickAndHold(holdElement)
                .moveToElement(targetElement)
                .release()
                .perform();

        holdElement.shouldHave(text(targetElementText));
        targetElement.shouldHave(text(holdElementText));
    }

    //Context menu
    @Step("Кликаем правой кнопкой мыши на отмеченной области")
    private void rightClickOnBox(SelenideElement boxElement) {
        boxElement.shouldBe(CustomElementConditions.visible()).contextClick();
    }

    @Step("Проверяем, что JS Alert содержит текст '{expectedText}'")
    private void checkAlertText(String expectedText) {
        Alert activeAlert = switchTo().alert();
        String alertText = activeAlert.getText();
        System.out.println("Текст алерта: " + alertText);

        Assertions.assertEquals(expectedText, alertText);
    }

    //Infinite Scroll
    @Step("Прокрутить страницу до текста '{text}'")
    private void scrollToText(String text) {
        SelenideElement textElement = $x("//*[contains(text(), '" + text + "')]");
        Actions actions = new Actions(getWebDriver());

        while (!textElement.isDisplayed()) {
            actions.scrollByAmount(0, 1000).perform();
            sleep(300);
        }

        textElement.shouldBe(CustomElementConditions.visible());
        System.out.println("Текст '" + text + "' в поле зрения.");

        highlightSpecificText(textElement, text);
    }

    @Step("Выделить элемент")
    private void highlightSpecificText(SelenideElement element, String textToHighlight) {
        executeJavaScript(
                "arguments[0].innerHTML = arguments[0].innerHTML.replace(arguments[1], " +
                        "'<span style=\"background-color: yellow; color: red;\">' + arguments[1] + '</span>');",
                element, textToHighlight
        );
    }

    //Key Presses
    @Step("Нажать клавиши и проверить отображение результата")
    private void pressKeysAndCheck(String letters, Keys... specialKeys) {
        SelenideElement resultText = $x("//p[@id='result']");

        // Обрабатываем латинские символы
        for (char letter : letters.toCharArray()) {
            pressKey(Character.toString(letter), resultText);
        }
        // Обрабатываем специальные клавиши
        for (Keys key : specialKeys) {
            pressKey(key, resultText);
        }

    }

    @Step("Нажать клавишу {key} и проверить отображение результата")
    private void pressKey(CharSequence key, SelenideElement resultText) {
        actions().sendKeys(key).perform();
        resultText.shouldHave(text("You entered: " + keyToReadableString(key)));
    }

    private String keyToReadableString(CharSequence key) {
        if (key.equals(Keys.ENTER)) return "ENTER";
        if (key.equals(Keys.CONTROL)) return "CONTROL";
        if (key.equals(Keys.ALT)) return "ALT";
        if (key.equals(Keys.TAB)) return "TAB";

        return key.toString().toUpperCase();
    }

    // Метод для добавления скриншота в отчет Allure
    public void attachScreenshot() {
        File screenshotFile = Screenshots.takeScreenShotAsFile();
        if (screenshotFile != null) {
            try {
                byte[] screenshotBytes = Files.readAllBytes(screenshotFile.toPath());
                addAttachment("Финальный скриншот", "image/png", new ByteArrayInputStream(screenshotBytes), "png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Скриншот не был создан, так как screenshotFile равен null.");
        }
    }

    @AfterEach
    void teardown() {
        attachScreenshot();
        closeWebDriver();
    }
}
