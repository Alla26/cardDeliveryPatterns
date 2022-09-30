package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

class DeliveryTest {

    @BeforeAll                                               //
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll                                                 //
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id='city'] [placeholder=\"Город\"]").sendKeys(validUser.getCity());
        $("[data-test-id=\"date\"]").click();
        $x("//*[@placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL + "a"), Keys.BACK_SPACE);
        $x("//*[@placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] .input__control").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] .input__control").setValue(validUser.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $(".button__text").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $(".icon-button__text").click();
        $("[data-test-id=\"date\"]").click();
        $x("//*[@placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.CONTROL + "a"), Keys.BACK_SPACE);
        $x("//*[@placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $(".button__text").click();
        $("[data-test-id=\"replan-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id=\"replan-notification\"] .notification__content").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//span[text()='Перепланировать']").click();
        $("[data-test-id=success-notification]").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondMeetingDate));

    }


}
