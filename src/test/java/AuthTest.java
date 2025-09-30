package task2;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task2.data.RegistrationDto;



import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;

class AuthTest {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static RegistrationDto activeUser;
    private static RegistrationDto blockedUser;

    @BeforeAll
    static void setUpAll() {
        activeUser = new RegistrationDto("vasyaActive", "password1", "active");
        blockedUser = new RegistrationDto("vasyaBlocked", "password2", "blocked");

        given().spec(requestSpec).body(activeUser).when().post("/api/system/users").then().statusCode(200);
        given().spec(requestSpec).body(blockedUser).when().post("/api/system/users").then().statusCode(200);
    }

    @Test
    void shouldLoginWithActiveUser() {
        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("button.button").click();
        $("#root h2").shouldHave(text("Личный кабинет"));


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
        $("[data-test-id='login'] input").setValue("nonexistent"); // несуществующий логин
        $("[data-test-id='password'] input").setValue(activeUser.getPassword());
        $("button.button").click();

        $("[data-test-id='error-notification']").shouldBe(visible);
    }

    @Test
    void shouldNotLoginWithInvalidPassword() {
        open("http://localhost:9999");
        $("[data-test-id='login'] input").setValue(activeUser.getLogin());
        $("[data-test-id='password'] input").setValue("wrongPassword"); // неправильный пароль
        $("button.button").click();

        $("[data-test-id='error-notification']").shouldBe(visible);
    }
}