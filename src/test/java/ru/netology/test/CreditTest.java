package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pay.ByuCredit;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreditTest {
    String validCardNumber = DataHelper.getApprovedCard().getCardNumber();
    String declinedCardNumber = DataHelper.getDeclinedCard().getCardNumber();
    String unknownCardNumber = DataHelper.getUnknownCard().getCardNumber();
    String validMonth = DataHelper.getValidMonth();
    String validYear = DataHelper.getValidYear(1);
    String validName = DataHelper.getValidName();
    String validCode = DataHelper.getNumber(3);

    @BeforeAll
    static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanBase() {
        SQLHelper.cleanBase();
    }

    @Test
    @DisplayName("Валидные данные ")
    void shouldHappyPath() {
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifySuccessVisibility();
        assertEquals("APPROVED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Данные неактивной карты")
    void shouldSadPath() {
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(declinedCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyErrorVisibility();
        assertEquals("DECLINED", SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Данные неизвестной карты")
    void shouldReturnFailWithUnknownCard() {
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(unknownCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyErrorVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Отправка пустой формы")
    void shouldEmptyPath() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var emptyMonth = DataHelper.getEmptyValue();
        var emptyYear = DataHelper.getEmptyValue();
        var emptyName = DataHelper.getEmptyValue();
        var emptyCode = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(emptyCardNumber, emptyMonth, emptyYear, emptyName, emptyCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        creditpage.verifyInvalidFormatMonthVisibility();
        creditpage.verifyInvalidFormatYearVisibility();
        creditpage.verifyEmptyNameVisibility();
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Заполнить все поля кроме номера карты")
    void shouldReturnErrorWithEmptyCardNumber() {
        var emptyCardNumber = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(emptyCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Заполнить все поля кроме месяца")
    void shouldReturnErrorWithEmptyMonth() {
        var emptyMonth = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, emptyMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Заполнить все поля кроме года")
    void shouldReturnErrorWithEmptyYear() {
        var emptyYear = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, emptyYear, validName, validCode);
        creditpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Заполнить все поля кроме имя владельца")
    void shouldReturnErrorWithEmptyName() {
        var emptyName = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, emptyName, validCode);
        creditpage.verifyEmptyNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Заполнить все поля кроме кодом CVC")
    void shouldReturnErrorWithEmptyCode() {
        var emptyCode = DataHelper.getEmptyValue();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, emptyCode);
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в номере карты, имя заполнить кириллицей")
    void shouldReturnErrorWithCyrillicCardNumber() {
        var cyrillicCardNumber = DataHelper.getNameOfCyrillic();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(cyrillicCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в номере карты, имя заполнить латиницей")
    void shouldReturnErrorWithLatinCardNumber() {
        var latinCardNumber = DataHelper.getValidName();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(latinCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в номере карты, имя заполнить спецсимволами")
    void shouldReturnErrorWithSymbolsCardNumber() {
        var symbolCardNumber = DataHelper.getSymbols();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(symbolCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в номере карты, имя заполнить не полностью")
    void shouldReturnErrorWithShortCardNumber() {
        var shortCardNumber = DataHelper.getNumber(15);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(shortCardNumber, validMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatCardNumberVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле месяца, заполнить кириллицей")
    void shouldReturnErrorWithCyrillicMonth() {
        var cyrillicMonth = DataHelper.getNameOfCyrillic();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, cyrillicMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле месяца, заполнить латиницей")
    void shouldReturnErrorWithLatinMonth() {
        var latinMonth = DataHelper.getValidName();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, latinMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле месяца, заполнить спецсимволами")
    void shouldReturnErrorWithSymbolsMonth() {
        var symbolMonth = DataHelper.getSymbols();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, symbolMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле месяца, заполнить не полностью")
    void shouldReturnErrorWithShortMonth() {
        var shortMonth = DataHelper.getNumber(1);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, shortMonth, validYear, validName, validCode);
        creditpage.verifyInvalidFormatMonthVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле месяца, заполнить двузначным значением более 12")
    void shouldReturnErrorWithErrorMonth() {
        var errorMonth = "13";
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, errorMonth, validYear, validName, validCode);
        creditpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить кириллицей")
    void shouldReturnErrorWithCyrillicYear() {
        var cyrillicYear = DataHelper.getNameOfCyrillic();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, cyrillicYear, validName, validCode);
        creditpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить латиницей")
    void shouldReturnErrorWithLatinYear() {
        var latinYear = DataHelper.getValidName();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, latinYear, validName, validCode);
        creditpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить спецсимволами")
    void shouldReturnErrorWithSymbolsYear() {
        var symbolYear = DataHelper.getSymbols();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, symbolYear, validName, validCode);
        creditpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить не полностью")
    void shouldReturnErrorWithShortYear() {
        var shortYear = DataHelper.getNumber(1);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, shortYear, validName, validCode);
        creditpage.verifyInvalidFormatYearVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить более чем на 5 лет вперед")
    void shouldReturnErrorWithOverFiveYear() {
        var overFiveYear = DataHelper.getValidYear(6);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, overFiveYear, validName, validCode);
        creditpage.verifyInvalidValidityPeriodVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле года, заполнить в прошлом")
    void shouldReturnErrorWithLastYear() {
        var lastYear = DataHelper.getValidYear(-1);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, lastYear, validName, validCode);
        creditpage.verifyExpiredValidityPeriodVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле Владелец, заполнить кириллицей")
    void shouldReturnErrorWithCyrillicName() {
        var cyrillicName = DataHelper.getNameOfCyrillic();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, cyrillicName, validCode);
        creditpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле Владелец, заполнить цифрами")
    void shouldReturnErrorWithNumberName() {
        var numberName = DataHelper.getNumber(5);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, numberName, validCode);
        creditpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле Владелец, заполнить спецсимволами")
    void shouldReturnErrorWithSymbolName() {
        var symbolName = DataHelper.getSymbols();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, symbolName, validCode);
        creditpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле Владелец, заполнить только фамилией")
    void shouldReturnErrorWithSurname() {
        var surname = DataHelper.getValidSurname();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, surname, validCode);
        creditpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле Владелец, заполнить одной буквой")
    void shouldReturnErrorWithOneLetterName() {
        var oneLetterName = "r";
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, oneLetterName, validCode);
        creditpage.verifyInvalidFormatNameVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле кода(CVC), заполнить кириллицей")
    void shouldReturnErrorWithCyrillicCode() {
        var cyrillicCode = DataHelper.getNameOfCyrillic();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, cyrillicCode);
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле кода(CVC), заполнить латиницей")
    void shouldReturnErrorWithLatinCode() {
        var latinCode = DataHelper.getValidName();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, latinCode);
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле кода(CVC), заполнить спецсимволами")
    void shouldReturnErrorWithSymbolsCode() {
        var symbolCode = DataHelper.getSymbols();
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, symbolCode);
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }

    @Test
    @DisplayName("Ошибка в поле кода(CVC), заполнить не полностью")
    void shouldReturnErrorWithShortCode() {
        var shortCode = DataHelper.getNumber(2);
        var creditpage = new ByuCredit();
        creditpage.cleanCreditForm();
        creditpage.enterCreditInputs(validCardNumber, validMonth, validYear, validName, shortCode);
        creditpage.verifyInvalidFormatCodeVisibility();
        assertNull(SQLHelper.getCreditStatus());
    }
}