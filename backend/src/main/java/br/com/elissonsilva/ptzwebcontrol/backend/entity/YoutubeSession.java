package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.google.api.client.auth.oauth2.TokenResponse;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data
@Table(name = "YoutubeSession")
public class YoutubeSession implements Serializable {

    @Id
    private String code;

    private String scope;

    private String accessToken;

    private String tokenType;

    private Long expiresInSeconds;

    private String refreshToken;

    public void setTokenResponse(TokenResponse token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresInSeconds = token.getExpiresInSeconds();
        this.refreshToken = token.getRefreshToken();
    }
}
