package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import lombok.Data;

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
