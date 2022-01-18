package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "PtzName")
@IdClass(PtzNameId.class)
public class PtzName implements Serializable {

    @Id
    private String ptz;

    @Id
    private int position;

    private String name;

}
