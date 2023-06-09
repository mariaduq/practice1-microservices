package com.example.demo.worker;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.ALWAYS)
public final class TaskMessage implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("completed")
    private Boolean completed;

    @JsonProperty("progress")
    private Integer progress;

    @JsonProperty("result")
    private String result;
}
