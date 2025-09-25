package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "StreamConfig")
public class StreamConfig implements Serializable {

    @Id
    @GeneratedValue
    private int streamConfigId;

    private String label;

    private String title;

    private String description;

    private String thumbnail;

    private int active;

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
