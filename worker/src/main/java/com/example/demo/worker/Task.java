package com.example.demo.worker;

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
@JsonInclude(Include.ALWAYS)
public final class Task implements Serializable {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("text")
    private String text;

    public Task(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Task task = objectMapper.readValue(jsonString, Task.class);
            this.id = task.getId();
            this.text = task.getText();

        } catch (IOException exception) {
            System.err.println("Error al deserializar el JSON: " + exception.getMessage());
        }
    }

}
