package br.com.elissonsilva.ptzwebcontrol.backend.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import br.com.elissonsilva.ptzwebcontrol.backend.component.SecretsYoutube;
import br.com.elissonsilva.ptzwebcontrol.backend.component.SecretsFacebook;

@Component
@ConfigurationProperties("secrets")
@Data
public class Secrets {

    private SecretsYoutube youtube;
    private SecretsFacebook facebook;

}
