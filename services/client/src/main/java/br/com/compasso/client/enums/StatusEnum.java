package br.com.compasso.client.enums;

import br.com.compasso.client.advices.GenericException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum StatusEnum {
    IN_PROGRESS ("in progress"),
    APPROVED ("approved"),
    DISAPPROVED ("disapproved");
    private String progress;

    StatusEnum(String progress) {

        this.progress = progress;
    }

    public String returnProgress() {

        return this.progress;
    }

    @JsonCreator
    public static StatusEnum decode(final String progress) throws GenericException {
        return Stream.of(StatusEnum.values()).filter(
                targetEnum -> targetEnum.progress.equals(progress
                )
        ).findFirst().orElseThrow(
                () -> new GenericException("Invalid value:" + progress));
    }

    @JsonValue
    public String getProgress() {
        return progress;
    }
}
