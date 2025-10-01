import com.github.javafaker.Faker;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


class AuthTest {
    private static RequestSpecification requestSpec;
    private static RegistrationDto activeUser;
    private static RegistrationDto blockedUser;

    @BeforeAll
    static void setUpAll() {
        requestSpec = DataGenerator.getRequestSpec();
        activeUser = DataGenerator.activeUser();
        blockedUser = DataGenerator.blockedUser();
    }

    @Test
    void shouldLoginWithActiveUser() {
        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldBe(visible)          // проверяем видимость
                .shouldHave(text("Личный кабинет")); // проверяем текст


    }

    @Test
    void shouldNotLoginWithBlockedUser() {
        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("button.button").click();

        $("[data-test-id='error-notification']").shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithInvalidLogin() {
        open("http://localhost:9999");
        String randomLogin = DataGenerator.randomUser().getLogin();
        $("[data-test-id='login'] input").setValue(randomLogin);// несуществующий логин
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("button.button").click();

        $("[data-test-id='error-notification']").shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        String wrongPassword = DataGenerator.randomUser().getPassword();
        $("[data-test-id='password'] input").setValue(wrongPassword);// неправильный пароль
        $("button.button").click();

        $("[data-test-id='error-notification']").shouldBe(visible);
    }
}