package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.google.api.client.auth.oauth2.TokenResponse;
import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "youtube_session")
public class YoutubeSession implements Serializable {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "scope")
    private String scope;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "expires_in_seconds")
    private Long expiresInSeconds;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_token_request")
    private Date lastTokenRequest;

    public void setTokenResponse(TokenResponse token) {
        this.accessToken = token.getAccessToken();
        this.tokenType = token.getTokenType();
        this.expiresInSeconds = token.getExpiresInSeconds();
        this.refreshToken = token.getRefreshToken();
    }
}
