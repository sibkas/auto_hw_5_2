import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;
import com.github.javafaker.Faker;

public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static RegistrationDto activeUser() {
        RegistrationDto user = new RegistrationDto("vasyaActive", "password1", "active");
        postUser(user);
        return user;
    }

    public static RegistrationDto blockedUser() {
        RegistrationDto user = new RegistrationDto("vasyaBlocked", "password2", "blocked");
        postUser(user);
        return user;
    }

    private static void postUser(RegistrationDto user) {
        given().spec(requestSpec)
                .body(user)
                .when().post("/api/system/users")
                .then().statusCode(200);
    }

    public static RequestSpecification getRequestSpec() {
        return requestSpec;
    }

    private static final Faker faker = new Faker();

    public static RegistrationDto randomUser() {
        String login = faker.name().username();            // случайный логин
        String password = faker.internet().password(8, 16); // пароль длиной от 8 до 16 символов
        String status = "active";
        RegistrationDto user = new RegistrationDto(login, password, status);
        postUser(user);
        return user;
    }
}