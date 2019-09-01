package ru.muchnik.yota.mobileservices.model.entity.traffic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrafficDetailsTest {

    private TrafficDetails trafficDetails;

    @Before
    public void setUp() throws Exception {
        trafficDetails = new TrafficDetails();
    }

    @Test
    public void getAmountLeft() {
        trafficDetails = new TrafficDetails(null, 5, 3);
        int amountLeft = trafficDetails.getAmountLeft();
        Assert.assertEquals(5, amountLeft);
    }

    @Test
    public void setAmountLeft() {
        trafficDetails.setAmountLeft(2);
        Assert.assertEquals(2, trafficDetails.getAmountLeft());
    }
}