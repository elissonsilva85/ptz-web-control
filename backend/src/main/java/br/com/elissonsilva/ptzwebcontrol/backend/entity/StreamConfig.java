package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "stream_config")
public class StreamConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stream_config_id")
    private int streamConfigId;

    @Column(name = "label")
    private String label;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "active")
    private int active;

    @Column(name = "stream_destination")
    private String streamDestination;

    public void setStreamDestinationFromList(List<String> values) {
        streamDestination = values.stream().reduce(",", (a, b) -> {
            return a + b;
        });
    }

    public List<String> getStreamDestinationToList() {
        return Arrays.asList(streamDestination.split(","));
    }


}
