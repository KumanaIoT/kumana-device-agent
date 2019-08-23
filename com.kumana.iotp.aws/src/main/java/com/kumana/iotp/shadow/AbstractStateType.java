package com.kumana.iotp.shadow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kumana.iotp.AlertModel;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractStateType {

    @JsonIgnore
    private String id;
    private Latest latest;
    private List<AlertModel> alerts;
    private Location location;

}
