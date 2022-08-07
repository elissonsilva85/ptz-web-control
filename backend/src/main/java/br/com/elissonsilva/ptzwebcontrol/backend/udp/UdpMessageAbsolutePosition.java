package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import br.com.elissonsilva.ptzwebcontrol.backend.ptz.PtzSessionAbstract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpMessageAbsolutePosition extends UdpMessageBase {

    protected Logger logger = LoggerFactory.getLogger(UdpMessageAbsolutePosition.class);

    public static int pan;
    public static int tilt;

    public UdpMessageAbsolutePosition() {
        String FILTER = "81010602";

        this.setName("Absolute Position");
        this.setFilterBase(FILTER);
    }

    @Override
    public void doAction(PtzSessionAbstract session) {
        //
        // ex: 81 01 06 02 18 14 00 00 00 00 00 00 00 00 FF
        // 8x 01 06 02
        // vv ww
        // 0p 0p 0p 0p
        // 0t 0t 0t 0t FF
        //
        // vv: Pan Speed 01 (Slow) - 18 (Fast)
        // ww: Tilt Speed
        //
        // For ppppp and tttt, refer to the section of the PanTilt position of VISCA command set value
        // (reference value)
        //

        try {
            int panSpeed = Integer.valueOf(getData().substring(8, 10), 16); // 01 - 18
            int tiltSpeed = Integer.valueOf(getData().substring(10, 12), 16); // 01 - 14

            // Ângulo Horizontal(0~3600)
            pan = Integer.valueOf(getData().substring(13, 14), 16) * 1000 +
                    Integer.valueOf(getData().substring(15, 16), 16) * 100 +
                    Integer.valueOf(getData().substring(17, 18), 16) * 10 +
                    Integer.valueOf(getData().substring(19, 20), 16);

            // Ângulo Vertical(-350~900)
            int sinal = ( Integer.valueOf(getData().substring(21, 22), 16) == 0 ? 1 : -1 ); // 0 positivo / 1 - negativo
            tilt = (Integer.valueOf(getData().substring(23, 24), 16) * 100 +
                     Integer.valueOf(getData().substring(25, 26), 16) * 10 +
                     Integer.valueOf(getData().substring(27, 28), 16)) * sinal;

            if(panSpeed + tiltSpeed == 0)
            {
                this.logger.info("Moving Absolute Position -> " + "Pan: " + pan + " (speed " + panSpeed + ") | Tilt: " + tilt + " (speed " + tiltSpeed + ")");
                if(session.isConnected()) {
                    session.specificPosition(pan, tilt, 0);
                }
            }
            else {
                this.logger.info("Saving Absolute Position -> " + "Pan: " + pan + " (speed " + panSpeed + ") | Tilt: " + tilt + " (speed " + tiltSpeed + ")");
            }

        } catch (Exception e) {
            this.logger.warn("doAction exception : " + e.getMessage(), e);
        }
    }

}
