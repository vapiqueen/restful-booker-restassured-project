package com.restful.booker.crudtest;

import com.restful.booker.model.AuthorisationPojo;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.model.PatchBookingPojo;
import com.restful.booker.model.UpdateBookingPojo;
import io.restassured.response.Response;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CRUDTest {
    static int id;
    static String token;

    @Test
    public void test01() {
        AuthorisationPojo authorisationPojo = new AuthorisationPojo();
        authorisationPojo.setUsername("admin");
        authorisationPojo.setPassword("password123");
        token = given()
                .header("Content-Type", "application/json")
                .when()
                .body(authorisationPojo)
                .post("https://restful-booker.herokuapp.com/auth")
                .then().statusCode(200).extract().path("token");
    }

    @Test
    public void test02() {
        BookingPojo.BookingDates date = new BookingPojo.BookingDates();
        date.setCheckin("2023-07-01");
        date.setCheckout("2023-07-10");
        BookingPojo bookingPojo = new BookingPojo();
        bookingPojo.setFirstname("Jimesh");
        bookingPojo.setLastname("Patel");
        bookingPojo.setTotalprice(1000);
        bookingPojo.setDepositpaid(true);
        bookingPojo.setBookingdates(date);
        bookingPojo.setAdditionalneeds("Breakfast");
        id = given()
                .header("Content-Type", "application/json")
                .when()
                .body(bookingPojo)
                .post("https://restful-booker.herokuapp.com/booking")
                .then().statusCode(200).extract().path("bookingid");
    }

    @Test
    public void test03() {
        PatchBookingPojo patchBookingPojo = new PatchBookingPojo();
        patchBookingPojo.setAdditionalneeds("Lunch");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(patchBookingPojo)
                .when().patch("https://restful-booker.herokuapp.com/booking/" + id);
        response.then().statusCode(200);
    }

    @Test
    public void test04() {
        UpdateBookingPojo.BookingDates date = new UpdateBookingPojo.BookingDates();
        date.setCheckin("2023-06-01");
        date.setCheckout("2023-06-05");
        UpdateBookingPojo updateBookingPojo = new UpdateBookingPojo();
        updateBookingPojo.setFirstname("Jimesh");
        updateBookingPojo.setLastname("Patel");
        updateBookingPojo.setTotalprice(200);
        updateBookingPojo.setDepositpaid(true);
        updateBookingPojo.setBookingdates(date);
        updateBookingPojo.setAdditionalneeds("Breakfast and Lunch");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(updateBookingPojo)
                .when().put("https://restful-booker.herokuapp.com/booking/" + id);
        response.then().statusCode(200);
    }

    @Test
    public void test05() {
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "token=" + token)
                .when().delete("https://restful-booker.herokuapp.com/booking/" + id);
        response.then().statusCode(201);
    }
}
