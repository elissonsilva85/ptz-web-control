package br.com.elissonsilva.ptzwebcontrol.backend.services;

// https://developers.google.com/youtube/v3/guides/auth/server-side-web-apps?hl=pt-br#Obtaining_Access_Tokens
// https://developers.google.com/api-client-library/java/google-api-java-client/oauth2#web_server_applications
// https://github.com/google/google-api-java-client-samples/blob/master/oauth2-cmdline-sample/src/main/java/com/google/api/services/samples/oauth2/cmdline/OAuth2Sample.java
// http://highaltitudedev.blogspot.com/2013/10/google-oauth2-with-jettyservlets.html

// https://developers.google.com/youtube/v3/docs/channels/list?apix=true#common-use-cases

import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeLiveBroadcast;
import br.com.elissonsilva.ptzwebcontrol.backend.entity.YoutubeSession;
import br.com.elissonsilva.ptzwebcontrol.backend.component.Secrets;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

@Service
public class YoutubeService {

    @Autowired
    private Secrets secrets;

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
                secrets.getYoutube().getClientId(),
                redirectUrl,
                SCOPES).setResponseTypes(Arrays.asList("code")).build();
    }

    public GoogleTokenResponse getAuthorizationCodeToken(String code, String redirectUrl) throws IOException, GeneralSecurityException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new GoogleAuthorizationCodeTokenRequest(
                httpTransport,
                JSON_FACTORY,
                secrets.getYoutube().getClientId(),
                secrets.getYoutube().getClientSecret(),
                code,
                "http://localhost/api/youtube/callback/").execute();
    }

    public ChannelListResponse getChannelInfo() throws IOException, GeneralSecurityException {
        //
        // https://developers.google.com/youtube/v3/docs/channels/list
        //
        return getService().channels()
                .list("snippet,contentDetails,statistics")
                .setMine(true)
                .execute();
    }

    public LiveBroadcastListResponse getLiveBroadcastsList() throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts/list
        //
        return getService().liveBroadcasts()
                .list("snippet,contentDetails,status")
                .setBroadcastType("event")
                .setBroadcastStatus("upcoming")
                .execute();
    }

    public LiveBroadcastListResponse getLiveBroadcastsList(String broadcastId) throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts/list
        //
        return getService().liveBroadcasts()
                .list("snippet,contentDetails,status")
                .setBroadcastType("event")
                .setId(broadcastId)
                .execute();
    }

    public LiveStreamListResponse getLiveStreamsList() throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/live/docs/liveStreams/list
        //
        return getService().liveStreams()
                .list("snippet,cdn,contentDetails,status")
                .setMine(true)
                .execute();
    }

    public ThumbnailSetResponse setThumbnail(String videoId, File mediaFile) throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/docs/thumbnails/set
        //
        InputStreamContent mediaContent =
                new InputStreamContent("application/octet-stream",
                        new BufferedInputStream(new FileInputStream(mediaFile)));
        mediaContent.setLength(mediaFile.length());
        //mediaContent.setType(); // image/${extensionName.split('.').pop()}

        return getService().thumbnails()
                .set(videoId, mediaContent)
                .execute();
    }

    public PlaylistItem addPlaylistItem(String videoId, String playlistId) throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/docs/playlistItems/insert
        //
        return getService().playlistItems()
                .insert("snippet", new PlaylistItem()
                        .setSnippet(new PlaylistItemSnippet()
                                .setPlaylistId(playlistId)
                                .setResourceId(new ResourceId()
                                        .setKind("youtube#video")
                                        .setVideoId(videoId))))
                .execute();
    }

    public LiveBroadcast addLiveBroadcast(String title, String description, DateTime scheduledStartTime, String privacy) throws GeneralSecurityException, IOException {
        //
        // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts/insert
        //
        return getService().liveBroadcasts()
                .insert("snippet,contentDetails,status", new LiveBroadcast()
                        .setSnippet(new LiveBroadcastSnippet()
                                .setTitle(title)
                                .setDescription(description)
                                .setScheduledStartTime(scheduledStartTime))
                        .setContentDetails(new LiveBroadcastContentDetails()
                                .setEnableClosedCaptions(false)
                                .setEnableContentEncryption(true)
                                .setEnableAutoStart(true)
                                .setEnableDvr(true)
                                .setEnableEmbed(true)
                                .setRecordFromStart(true)
                                .setStartWithSlate(true))
                        .setStatus(new LiveBroadcastStatus()
                                .setPrivacyStatus(privacy)
                                .setSelfDeclaredMadeForKids(false)))
                .execute();
    }

    public YoutubeLiveBroadcast addLiveBroadcast(YoutubeLiveBroadcast liveData) throws GeneralSecurityException, IOException {

        liveData.setLiveBroadcastResponse(addLiveBroadcast(
                liveData.getTitle(),
                liveData.getDescription(),
                new DateTime(liveData.getScheduledStartTime()),
                liveData.getPrivacy()));

        if(liveData.getThumbnailFilename() != null)
            liveData.setThumbnailSetResponse(setThumbnail(
                    liveData.getVideoId(),
                    new File(liveData.getThumbnailFilename())
            ));

        if(liveData.getPlaylistId() != null)
            liveData.setPlaylistItemResponse(addPlaylistItem(
                    liveData.getVideoId(),
                    liveData.getPlaylistId()
            ));

        return liveData;
    }
}