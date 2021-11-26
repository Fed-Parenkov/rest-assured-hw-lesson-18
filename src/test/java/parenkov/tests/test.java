package parenkov.tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import parenkov.config.App;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class test {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
    }

    String authorizationCookie;
    @Test
    void gettingCookies() {
        step("Get cookie by api and set it to browser", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                            .formParam("Email", App.config.userEmail())
//                            .formParam("Password", App.config.userPassword())
                            .body("Email=qwerty%40www.co&Password=123456&RememberMe=true")
//                            .body("Email=" + App.config.userEmail() + "&Password=" + App.config.userPassword() + "&RememberMe=false")
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("Nop.customer");
        });

        step("Set cookie to browser", () -> {
            open("/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(
                    new Cookie("Nop.customer", authorizationCookie));
            System.out.println(authorizationCookie);
        });
    }
}
