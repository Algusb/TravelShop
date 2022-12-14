package test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import data.DataHelper;
import data.SQLHelper;
import page.PageMain;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PaymentCardTest {
    PageMain pageMain = new PageMain();

    public void setUp() {
        Configuration.timeout = 6000;
    }

    @BeforeEach
    void openForTests() {
        open("http://localhost:8080");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("AllureSelenide");
    }





    @Test
    @DisplayName("Тест загрузки вкладки Купить")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCheckTheDownloadOfThePaymentByCard() {
        pageMain.payByDebitCard();
    }

    @Test
    @DisplayName("Тест с APPROVED картой и валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    void shouldCheckWithAnApprovedCardAndValidData() {

        var payForm = pageMain.payByDebitCard();
        var approvedInfo = DataHelper.getApprovedCardInfo();
        payForm.fillingForm(approvedInfo);
        payForm.checkOperationIsApproved();
        String dataSQLPayment = SQLHelper.getPaymentStatus();
        assertEquals("APPROVED", dataSQLPayment);
    }

    @Test
    @DisplayName("Тест с валидными данными")
    @Severity(SeverityLevel.CRITICAL)
    void shouldBeCheckedWithValidData() {

        var payForm = pageMain.payByDebitCard();
        var approvedInfo = DataHelper.getApprovedCardInfo();
        payForm.fillingForm(approvedInfo);
        payForm.checkOperationIsApproved();
        String dataSQLPayAmount = SQLHelper.getPaymentAmount();
        assertEquals("45000", dataSQLPayAmount);
    }

    @Test
    @DisplayName("Тест DECLINED карты с валидными данными")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckTheDeclinedCardAndTheValidData() {

        var payForm = pageMain.payByDebitCard();
        var declinedInfo = DataHelper.getDeclinedCardInfo();
        payForm.fillingForm(declinedInfo);
        payForm.checkOperationIsApproved();
        String dataSQLPayAmount = SQLHelper.getPaymentAmount();
        payForm.checkErrorNotification();
        String dataSQLPayment = SQLHelper.getPaymentStatus();
        assertEquals("DECLINED", dataSQLPayment);
    }

    @Test
    @DisplayName("Тест невалидной карты")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckTheInvalidCard() {

        var payForm = pageMain.payByDebitCard();
        var invalidCardNumber = DataHelper.getInvalidCardNumberInfo();
        payForm.fillingForm(invalidCardNumber);
        payForm.checkErrorNotification();
    }

    @Test
    @DisplayName("Тест невалидного месяца")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckTheInvalidMonth() {

        var payForm = pageMain.payByDebitCard();
        var invalidMonth = DataHelper.getInvalidMonthInfo();
        payForm.fillFormNoSendRequest(invalidMonth);
        payForm.checkInvalidExpirationDate();
    }

    @Test
    @DisplayName("Тест невалидного месяца со значением 00")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckTheInvalidMonthZero() {

        var payForm = pageMain.payByDebitCard();
        var invalidMonth = DataHelper.getInvalidMonthZeroInfo();
        payForm.fillFormNoSendRequest(invalidMonth);
        payForm.checkInvalidExpirationDate();
    }

    @Test
    @DisplayName("Тест с истекшим сроком действия карты")
    @Severity(SeverityLevel.NORMAL)
    void shouldBeCheckedWithAnExpiredExpirationDate() {

        var payForm = pageMain.payByDebitCard();
        var expiredYear = DataHelper.getExpiredYearInfo();
        payForm.fillFormNoSendRequest(expiredYear);
        payForm.checkCardExpired();
    }

    @Test
    @DisplayName("Тест с неверно указаным сроком действия карты")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckWithTheIncorrectlySpecifiedCardExpirationDate() {
        var payForm = pageMain.payByDebitCard();
        var invalidYear = DataHelper.getInvalidYearInfo();
        payForm.fillFormNoSendRequest(invalidYear);
        payForm.checkInvalidExpirationDate();
    }


    @Test
    @DisplayName("Тест данные владельца карты на киррилице")
    @Severity(SeverityLevel.NORMAL)
    void shouldCheckTheOwnersDataInCyrillic() {
        var payForm = pageMain.payByDebitCard();
        var invalidOwner = DataHelper.getInvalidOwnerInfo();
        payForm.fillFormNoSendRequest(invalidOwner);
        payForm.checkOperationIsApproved();
        String dataSQLPayAmount = SQLHelper.getPaymentAmount();
        assertEquals("45000", dataSQLPayAmount);
    }

    @Test
    @DisplayName("Тест отправка пустой формы")
    @Severity(SeverityLevel.NORMAL)
    void shouldSendAnEmptyForm() {

        var payForm = pageMain.payByDebitCard();
        var emptyFields = DataHelper.getEmptyFields();
        payForm.fillFormNoSendRequest(emptyFields);
        payForm.checkWrongFormat();
        payForm.checkRequiredField();
    }

    @Test
    @DisplayName("Тест отправить пустую форму заявки, затем заполнить валидными данными и отправить повторно")
    @Severity(SeverityLevel.MINOR)
    void shouldSendTheFormEmptyAndThenWithTheOwnersData() {
        var payForm = pageMain.payByDebitCard();
        var emptyFields = DataHelper.getEmptyFields();
        var approvedInfo = DataHelper.getApprovedCardInfo();
        payForm.fillFormNoSendRequest(emptyFields);
        payForm.checkWrongFormat();
        payForm.checkRequiredField();
        payForm.fillingForm(approvedInfo);
        payForm.checkOperationIsApproved();
    }

    @Test
    @DisplayName("Тест с невалидными данными всех полей")
    @Severity(SeverityLevel.NORMAL)
    void shouldBeCheckedWithInvalidDataOfAllFields() {
        var payForm = pageMain.payByDebitCard();
        var invalidValue = DataHelper.getInvalidCardForm();
        payForm.fillFormNoSendRequest(invalidValue);
        payForm.checkNotCorrectData();

    }


}