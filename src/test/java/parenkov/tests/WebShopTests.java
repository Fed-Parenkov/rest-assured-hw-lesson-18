package parenkov.tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import parenkov.config.App;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

public class WebShopTests {

    @BeforeAll
    static void testConfiguration() {
        RestAssured.baseURI = App.config.apiUrl();
        Configuration.baseUrl = App.config.webUrl();
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

        open("/registerresult/1");
        $(".result").shouldHave(Condition.text("Your registration completed"));
    }

    @Test
    @DisplayName("Добавление товара в Shopping Cart")
    void addItemToShoppingCart() {
        step("Добавить товар в Shopping cart", () -> {
            Response response =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_74_5_26=82" +
                                    "&product_attribute_74_6_27=85" +
                                    "&product_attribute_74_3_28=87" +
                                    "&product_attribute_74_8_29=88" +
                                    "&product_attribute_74_8_29=89" +
                                    "&product_attribute_74_8_29=90" +
                                    "&addtocart_74.EnteredQuantity=2")
                            .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                            .when()
                            .post("/addproducttocart/details/74/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("message", is("The product has been added to your " +
                                    "<a href=\"/cart\">shopping cart</a>"))
                            .extract().
                            response();
            System.out.println("Quantity: " + response.path("updatetopcartsectionhtml"));

            open("/Themes/DefaultClean/Content/images/logo.png");
            getWebDriver().manage().addCookie(
                    new Cookie("Nop.customer", "69589107-6373-41bd-891d-47fb44277adc"));

            open("/cart");
            $(".cart-item-row").shouldBe(Condition.visible);
            $(".product-name").shouldHave(Condition.text("Build your own expensive computer"));
        });
    }

    @Test
    @DisplayName("Отправка сообщения через форму Contact Us")
    void leaveFeedback() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("FullName=Alex+Qwerty&Email=qwerty%40www.co" +
                        "&Enquiry=Test+enquiry&send-email=Submit")
                .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                .when()
                .post("/contactus")
                .then()
                .statusCode(200);

        open("/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("Nop.customer", "69589107-6373-41bd-891d-47fb44277adc"));

        open("/contactus");
        $(".result").shouldHave(Condition.text("Your enquiry has been successfully " +
                "sent to the store owner."));
    }
}