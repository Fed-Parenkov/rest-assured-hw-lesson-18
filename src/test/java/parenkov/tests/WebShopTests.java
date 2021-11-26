package parenkov.tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import parenkov.config.App;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

public class WebShopTests {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
    }

    String authorizationCookie;
    @BeforeEach
    void gettingCookies() {
        step("Get cookie by api and set it to browser", () -> {
            authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", App.config.userEmail())
                            .formParam("Password", App.config.userPassword())
                            .when()
                            .post("/login")
                            .then()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");
        });

            step("Set cookie to browser", () -> {
                    open("/Themes/DefaultClean/Content/images/logo.png");
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie));
        });
    }

    @Test
    @DisplayName("Регистрация")
    void registration() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("__RequestVerificationToken=yharMk0UzLqakit" +
                        "-MPnBjOJc6oyYNnsn8u_CWOwbVk9djHYwKWUFoxe" +
                        "0_2nndWrMgtJaual3dxIZ6SkidpixmRpzv6NucA-" +
                        "EDDWc0k5Wmrk1&Gender=M&FirstName=Alex&La" +
                        "stName=Qwerty&Email=qwerty%40www.co&Pass" +
                        "word=123456&ConfirmPassword=123456&regis" +
                        "ter-button=Register")
                .when()
                .post("/register")
                .then()
                .statusCode(302);
        get("/registerresult/1")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Добавление товара в Shopping Cart")
    void addItemToShoppingCart() {
        step("Добавить товар в Shopping cart", () -> {
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_74_5_26=82" +
                                    "&product_attribute_74_6_27=85" +
                                    "&product_attribute_74_3_28=87" +
                                    "&product_attribute_74_8_29=88" +
                                    "&product_attribute_74_8_29=89" +
                                    "&product_attribute_74_8_29=90" +
                                    "&addtocart_74.EnteredQuantity=2")
                            .cookie(authorizationCookie)
                            .when()
                            .post("/addproducttocart/details/74/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("message", is("The product has been added to your " +
                                    "<a href=\"/cart\">shopping cart</a>"));
        });
    }

    @Test
    @DisplayName("Написание отзыва на товар")
    void leaveItemReview() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("AddProductReview.Title=Nice+item&AddProduct" +
                        "Review.ReviewText=Nice+item&AddProductRev" +
                        "iew.Rating=3&add-review=Submit+review")
                .cookie(authorizationCookie)
                .when()
                .post("/productreviews/72")
                .then()
                .statusCode(200);
    }
}