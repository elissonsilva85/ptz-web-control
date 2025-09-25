package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.google.api.client.auth.oauth2.TokenResponse;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

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

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTokenRequest;

    public void setTokenResponse(TokenResponse token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresInSeconds = token.getExpiresInSeconds();
        this.refreshToken = token.getRefreshToken();
    }
}
