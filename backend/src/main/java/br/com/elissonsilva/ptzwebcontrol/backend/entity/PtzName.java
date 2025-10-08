package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "ptz_name")
@IdClass(PtzNameId.class)
public class PtzName implements Serializable {

    @Id
    @Column(name = "ptz")
    private String ptz;

    @Id
    @Column(name = "position")
    private int position;

    @Column(name = "name")
    private String name;

}
