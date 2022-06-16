package br.com.elissonsilva.ptzwebcontrol.backend.udp;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Data
public abstract class UdpMessageBase {

    Logger logger = LoggerFactory.getLogger(UdpMessageBase.class);

    private String name;

    private String data;

    private String filterBase;

    /*
    // Help
    // https://www.sony.net/Products/CameraSystem/CA/BRC_X1000_BRC_H800/Technical_Document/C456100121.pdf
    // https://www.epiphan.com/userguides/LUMiO12x/Content/UserGuides/PTZ/3-operation/VISCAcommands.htm

    // Connect
    // Buffer 81 09 06 12 ff
    // x81 - 1000 0001 - Header
    // x09 - 0000 1001 - Message
    // x06 - 0000 0110 - Message
    // x12 - 0001 0010 - Message
    // xFF - 1111 1111 - Terminator
     */

    public boolean doFilter() {
        this.logger.debug(this.getName() + " :: doFilter :: filterBase [" + getFilterBase() + "]");
        if(getData() == null || getData().length() == 0) return false;
        return getData().startsWith(getFilterBase());
    }

    public void doAction() {
        //
        this.logger.info("Running " + this.getName());
        //
    }

    public String getResponse() {
        return null;
    }
}
