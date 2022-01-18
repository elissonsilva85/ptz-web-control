package br.com.elissonsilva.ptzwebcontrol.backend.component;

import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigConnection;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.ConfigShortcut;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties("config")
@Data
public class Config {

    private String urlBase;
    private String ptzBrand;
    private Map<String,ConfigConnection> ptzConnection;
    private List<String> startStreaming;
    private List<String> stopStreaming;
    private List<ConfigShortcut> shortcuts;

}
