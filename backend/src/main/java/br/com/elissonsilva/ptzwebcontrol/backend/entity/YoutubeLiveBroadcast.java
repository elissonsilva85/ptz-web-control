package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import lombok.Data;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.Date;

@Data
public class YoutubeLiveBroadcast {

    private String title;
    private String description;
    private String scheduledStartTime;
    private String privacy;
    private String videoId;
    private String playlistId;
    private String thumbnailFilename;

    private LiveBroadcast liveBroadcastResponse;
    private ThumbnailSetResponse thumbnailSetResponse;
    private PlaylistItem playlistItemResponse;

    public void setLiveBroadcastResponse(LiveBroadcast liveBroadcast) {
        liveBroadcastResponse = liveBroadcast;
        videoId = liveBroadcast.getId();
    }

}
