package br.com.elissonsilva.ptzwebcontrol.backend.component;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigPtz;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigShortcut;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties("config")
public class Config {

    private String urlBase;
    private String vmixUrl;
    private String blackMagicIP;
    private ConfigPtz ptz;
    private List<String> startStreaming;
    private List<String> stopStreaming;
    private List<ConfigShortcut> shortcuts;

}
