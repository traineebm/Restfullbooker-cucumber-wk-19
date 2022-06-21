package com.herokuapp.bookinginfo;

import com.herokuapp.allbookinginfo.BookingSteps;
import com.herokuapp.testbase.TestBase;
import com.herokuapp.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasKey;

public class BookingCRUDTest extends TestBase {
    static String username = "admin";
    static String password = "password123";
    static String firstname = "Monica" + TestUtils.getRandomValue();
    static String lastname = "Singh";
    static int totalprice = 50;
    static boolean depositpaid = true;
    static String additionalneeds = "Breakfast";
    static int bookingId;
    static String token;


    @Steps
    BookingSteps bookingSteps;

    @Title("This will Auth user")
    @Test
    public void test001() {
    ValidatableResponse response = bookingSteps.authUser(username, password);
    response.log().all().statusCode(200);
        HashMap<?,?> tokenMap = response.log().all().extract().path("");
        Assert.assertThat(tokenMap,hasKey("token"));
        System.out.println(token);
    }

    @Title("This will Create a booking user")
    @Test
    public void test002() {
        HashMap<String, String> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2018-01-01");
        bookingsDatesData.put("checkout", "2019-01-01");
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname,totalprice,depositpaid,bookingsDatesData,additionalneeds);
        response.log().all().statusCode(200);
        bookingId = response.log().all().extract().path("bookingid"); //1684
        HashMap<?,?>bookingMap = response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }

    @Title("This will Update a booking")
    @Test
    public void test003() {
        HashMap<String,String> bookingsDatesData = new HashMap<>();
        bookingsDatesData.put("checkin", "2018-01-01");
        bookingsDatesData.put("checkout", "2019-01-01");
        firstname = firstname + "_updated";
        lastname = lastname + "_updated";
        additionalneeds = "Wi-fi";
        ValidatableResponse response = bookingSteps.updateBooking(bookingId,firstname, lastname,totalprice,depositpaid,bookingsDatesData,additionalneeds);
        response.log().all().statusCode(200);
        HashMap<?,?>bookingMap= response.log().all().extract().path("");
        Assert.assertThat(bookingMap,anything(firstname));
        System.out.println(token);
    }

    @Title("This will Deleted a user")
    @Test
    public void test004() {
        ValidatableResponse response = bookingSteps.deleteBooking(bookingId);
        response.log().all().statusCode(201);
        ValidatableResponse response1 = bookingSteps.getBookingByID(bookingId);
        response1.log().all().statusCode(404);
    }
}
