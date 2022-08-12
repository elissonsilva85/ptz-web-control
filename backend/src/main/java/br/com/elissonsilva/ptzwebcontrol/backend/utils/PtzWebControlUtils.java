package br.com.elissonsilva.ptzwebcontrol.backend.utils;

public class PtzWebControlUtils {

    public static int speedConverter(int received, int receivedMin, int receivedMax, int outputMin, int outputMax) throws Exception {

        if(receivedMax <= receivedMin)
            throw new Exception("receivedMax <= receivedMin");

        if(outputMax <= outputMin)
            throw new Exception("outputMax <= outputMin");

        if(receivedMin < 0) {
            int diff = receivedMin * (-1);
            received += diff;
            receivedMin += diff;
            receivedMax += diff;
        }
        else if(receivedMin > 0) {
            received -= receivedMin;
            receivedMax -= receivedMin;
        }

        int percentage = Math.round((float) received / (receivedMax - receivedMin) * 100);

        int outputInterval = outputMax - outputMin;
        int outputAmount = Math.round((float) outputInterval * percentage / 100);
        return outputMin + outputAmount;

    }

}
