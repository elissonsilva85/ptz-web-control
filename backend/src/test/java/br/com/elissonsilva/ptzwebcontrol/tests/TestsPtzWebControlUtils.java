package br.com.elissonsilva.ptzwebcontrol.tests;


import br.com.elissonsilva.ptzwebcontrol.backend.utils.PtzWebControlUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestsPtzWebControlUtils {

    @Test
    public void whenSpeedConvertedReceiveWrongReceiveInterval() {

        Exception exception = assertThrows(Exception.class, () ->
                PtzWebControlUtils.speedConverter(0,0,0,0,0)
        );

        String expectedMessage = "receivedMax <= receivedMin";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenSpeedConvertedReceiveWrongOutputInterval() {

        Exception exception = assertThrows(Exception.class, () ->
                PtzWebControlUtils.speedConverter(0,0,10,0,0)
        );

        String expectedMessage = "outputMax <= outputMin";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenSpeedConvertedReceiveNegativeReceiveInterval() throws Exception {

        int expectedOutput = 25;
        int actualOutput = PtzWebControlUtils.speedConverter(-5,-10,10,0,100);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceiveNegativeOutputInterval() throws Exception {

        int expectedOutput = -5;
        int actualOutput = PtzWebControlUtils.speedConverter(5,0,20,-10,10);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceivePositiveIntervals() throws Exception {

        int expectedOutput = 3;
        int actualOutput = PtzWebControlUtils.speedConverter(5,0,20,0,10);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceiveLowerLimit() throws Exception {

        int expectedOutput = 10;
        int actualOutput = PtzWebControlUtils.speedConverter(1,0,100,10,20);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceiveUpperLimit() throws Exception {

        int expectedOutput = 20;
        int actualOutput = PtzWebControlUtils.speedConverter(99,0,100,10,20);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceiveReceivedMinGTZero10() throws Exception {

        int expectedOutput = 1;
        int actualOutput = PtzWebControlUtils.speedConverter(10,10,300,1,128);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void whenSpeedConvertedReceiveReceivedMinGTZero40() throws Exception {

        int expectedOutput = 15;
        int actualOutput = PtzWebControlUtils.speedConverter(40,10,300,1,128);

        assertEquals(expectedOutput, actualOutput);
    }

}