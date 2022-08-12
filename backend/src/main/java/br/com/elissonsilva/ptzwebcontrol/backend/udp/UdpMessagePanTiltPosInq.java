package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.exception.PtzSessionException;
import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessagePanTiltPosInq extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessagePanTiltPosInq.class);

    private int pan;
    private int tilt;

    public UdpMessagePanTiltPosInq() {
        String FILTER = "81090612FF";

        this.setName("Pan-tiltPosInq");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doBefore(PtzSessionAbstract session) {
        //
        try {
            pan = 0;
            tilt = 0;
            //
            if(session.isConnected()) {
                int[] angles = session.getViewAngles();
                pan = angles[0];
                tilt = angles[1];
            }
            //
        } catch (PtzSessionException e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
        //
    }

    @Override
    public String getResponse() {
        //
        // y0 50 0w 0w 0w 0w 0z 0z 0z 0z FF
        // wwww: Pan Position
        // zzzz: Tilt Position
        //

        // https://s3.amazonaws.com/amcrest-files/Amcrest+HTTP+API+3.2017.pdf

        // Ângulo Horizontal(0~3600)
        int[] p = new int[]{
                pan/1000, // milhar
                (pan%1000)/100, // centena
                (pan%100)/10, // dezena
                (pan%10) // unidade
        };

        // Ângulo Vertical(-350~900)
        int[] t = new int[]{
                ( tilt >= 0 ? 0 : 1 ), // 0 positivo / 1 - negativo
                Math.abs(tilt/100), // centena
                Math.abs((tilt%100)/10), // dezena
                Math.abs((tilt%10)) // unidade
        };

        return "9050" +
                "0"+ p[0] + "0" + p[1] + "0" + p[2] + "0" + p[3] +
                "0"+ t[0] + "0" + t[1] + "0" + t[2] + "0" + t[3] +
                "FF";
    }

}
