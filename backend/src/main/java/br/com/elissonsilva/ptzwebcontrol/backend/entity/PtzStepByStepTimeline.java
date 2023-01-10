package br.com.elissonsilva.ptzwebcontrol.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "PtzSbsTimeline")
public class PtzStepByStepTimeline implements Serializable {

    @Id
    @GeneratedValue
    private Long timelineId;

    private String ptz;
    private String label;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "timelineId")
    @OrderBy("executionOrder ASC")
    @JsonManagedReference
    private List<PtzStepByStepAction> actions;

    /*
    PtzStepByStepTimeline
    - Timeline ID (int) [1]
    - PTZ         (str) [PTZ1]
    - Label       (str) [Pulpito para Igreja]
    - List of actions (PtzStepByStepAction)
      - Action ID      (int) [10]
      - Execution Time (ms)  [100]
      - Order          (int) [1]
      - Label          (str) [Set Zoom Speed]
      - Description    (str) [Define a velocidade do zoom]
      - Function Name  (str) [setZoomSpeed]
      - List of params (PtzStepByStepParam)
        - Param ID     (int) [100]
        - Label        (str) [Speed]
        - Short Label  (str) []
        - Type         (str) [input]
        - Value        (str) [100]
        - List of Options (PtzStepByStepParamOption)
          - Option ID        []
          - Type             []
          - Value            []

     */

}
