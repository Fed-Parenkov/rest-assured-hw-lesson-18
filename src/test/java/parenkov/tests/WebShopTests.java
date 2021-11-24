package parenkov.tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.openqa.selenium.Cookie;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parenkov.config.App;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static io.qameta.allure.Allure.step;

public class WebShopTests {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
    }

    @Test
    void successfulRegistration() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"123\" }")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4),
                        "token", is("QpwL5tke4Pnpja7X4")
                );
    }

    @Test
    void checkTotal20WithResponseAndBadPractice() {
        String response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response().asString();

        System.out.println(response);
    }


    @Test
    @DisplayName("Successful authorization to some demowebshop (API + UI)")
    void loginWithCookieTest() {
        step("Get cookie by api and set it to browser", () -> {
//                    given()
//                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                            .formParam("Email", App.config.userEmail())
//                            .formParam("Password", App.config.userPassword())
//                            when()
            String response =
                    get("/desktops")
                            .then()
                            .statusCode(200)
                            .extract().body().asString();
            System.out.println(response);
        });
    }

//            step("Open minimal content, because cookie can be set when site is opened", () ->
//                    open("/Themes/DefaultClean/Content/images/logo.png"));
//
//            step("Set cookie to to browser", () ->
//                    getWebDriver().manage().addCookie(
//                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
//        });
//
//        step("Open main page", () ->
//                open(""));
//
//        step("Verify successful authorization", () ->
//                $(".account").shouldHave(text(App.config.userEmail())));
//    }


//    @Test
//    @DisplayName("Successful authorization to some demowebshop (API + UI)")
//    void loginWithCookieTest() {
//        step("Get cookie by api and set it to browser", () -> {
//            String authorizationCookie =
//                    given()
//                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
//                            .formParam("Email", App.config.userEmail())
//                            .formParam("Password", App.config.userPassword())
//                            .when()
//                            .post("/login")
//                            .then()
//                            .statusCode(302)
//                            .extract()
//                            .cookie("NOPCOMMERCE.AUTH");
//
//            step("Open minimal content, because cookie can be set when site is opened", () ->
//                    open("/Themes/DefaultClean/Content/images/logo.png"));
//
//            step("Set cookie to to browser", () ->
//                    getWebDriver().manage().addCookie(
//                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
//        });
//
//        step("Open main page", () ->
//                open(""));
//
//        step("Verify successful authorization", () ->
//                $(".account").shouldHave(text(App.config.userEmail())));
//    }




}