package br.com.elissonsilva.ptzwebcontrol.backend.services;

// https://developers.google.com/youtube/v3/guides/auth/server-side-web-apps?hl=pt-br#Obtaining_Access_Tokens
// https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#web_server_applications
// https://github.com/google/google-api-java-client-samples/blob/master/oauth2-cmdline-sample/src/main/java/com/google/api/services/samples/oauth2/cmdline/OAuth2Sample.java
// http://highaltitudedev.blogspot.com/2013/10/google-oauth2-with-jettyservlets.html

// https://developers.google.com/youtube/v3/docs/channels/list?apix=true#common-use-cases

import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

@Service
public class YoutubeService {
    private static final String CLIENT_ID = "";
    private static final String CLIENT_SECRET = "";
    private static final Collection<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/youtube");
    private static final String APPLICATION_NAME = "PTZ Web Control";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static GoogleCredential credential;
    private static YouTube youtubeGoogleService;

    private YouTube getService() throws GeneralSecurityException, IOException {
        if(youtubeGoogleService == null)
        {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            youtubeGoogleService = new YouTube.Builder(httpTransport, JSON_FACTORY, getCredential())
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        }
        return youtubeGoogleService;
    }

    public void setCredential(YoutubeSession session)  {
        credential = new GoogleCredential().setAccessToken(session.getAccessToken());
        credential.setRefreshToken(session.getRefreshToken());
        credential.setExpiresInSeconds(session.getExpiresInSeconds());
    }

    public Credential getCredential() {
        return credential;
    }

    public String getClientRequestUrl(String redirectUrl) {
        return new GoogleBrowserClientRequestUrl(
                CLIENT_ID,
                redirectUrl,
                SCOPES).setResponseTypes(Arrays.asList("code")).build();
    }

    public GoogleTokenResponse getAuthorizationCodeToken(String code, String redirectUrl) throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                JSON_FACTORY,
                CLIENT_ID,
                CLIENT_SECRET,
                code,
                "http://localhost/api/youtube/callback/").execute();
    }

    public ChannelListResponse getChannelList() throws IOException, GeneralSecurityException {
        YouTube.Channels.List request = getService().channels()
                .list("snippet,contentDetails,statistics");
        return request.setMine(true).execute();
    }

}