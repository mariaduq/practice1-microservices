package com.example.demo.server;

import java.io.IOException;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public final class TaskMessageServer implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("completed")
    private Boolean completed;

    @JsonProperty("progress")
    private Integer progress;

    @JsonProperty("result")
    private String result;

    public TaskMessageServer(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            TaskMessageServer taskMessageServer = objectMapper.readValue(jsonString, TaskMessageServer.class);
            this.id = taskMessageServer.getId();
            this.completed = taskMessageServer.getCompleted();
            this.progress = taskMessageServer.getProgress();
            this.result = taskMessageServer.getResult();

        } catch (IOException exception) {
            System.err.println("Error al deserializar el JSON: " + exception.getMessage());
        }
    }
}
