package parenkov.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parenkov.config.App;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class WebShopTests {

    @BeforeAll
    static void configureBaseUrl() {
        RestAssured.baseURI = App.config.apiUrl();
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
    @DisplayName("Добавление товара с пользовательскими параметрами в Shopping Cart")
    void addItemToShoppingCart() {
        step("Авторизоваться на сайте", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded")
                    .body("Email=qwerty%40www.co&Password=123456&RememberMe=false")
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302);
        });

        step("Перейти в раздел Computers -> Desktops", () -> {
            get("/desktops")
                    .then()
                    .statusCode(200)
                    .body("html.head.title", equalTo("Demo Web Shop. Desktops"));
        });

        step("Выбрать значение фильтра цены 'Over 1200.00'", () -> {
            get("/desktops?price=1200-")
                    .then()
                    .statusCode(200);
        });

        step("Перейти на карточку товара с ценой 1800", () -> {
            get("/build-your-own-expensive-computer-2")
                    .then()
                    .statusCode(200)
                    .body("html.head.title", equalTo("Demo Web Shop. " +
                            "Build your own expensive computer"));
        });

        step("Добавить в Shopping cart 5 компьютеров с максимальной комплектацией", () -> {
            Response response =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .body("product_attribute_74_5_26=82" +
                                    "&product_attribute_74_6_27=85" +
                                    "&product_attribute_74_3_28=87" +
                                    "&product_attribute_74_8_29=88" +
                                    "&product_attribute_74_8_29=89" +
                                    "&product_attribute_74_8_29=90" +
                                    "&addtocart_74.EnteredQuantity=5")
                            .when()
                            .post("/addproducttocart/details/74/1")
                            .then()
                            .statusCode(200)
                            .body("success", is(true))
                            .body("message", is("The product has been added to your " +
                                    "<a href=\"/cart\">shopping cart</a>"))
                            .body("updatetopcartsectionhtml", is("(5)"))
                            .extract().
                            response();
            System.out.println("Quantity: " + response.path("updatetopcartsectionhtml"));
        });
    }

    @Test
    @DisplayName("Отправка сообщения через контактную форму")
    void sendContactForm() {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("FullName=Alex&Email=qwerty%40www.co&Enquiry=TEST&send-email=Submit")
                .when()
                .post("/contactus")
                .then()
                .statusCode(200);
    }
}