package br.com.compasso.voluntary.enums;

import br.com.compasso.voluntary.exceptions.GenericException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum StatusEnum {

    AVAILABLE("available"),
    UNAVAILABLE("unavailable");

    private String status;

    StatusEnum(String status) {

        this.status = status;
    }

    public String returnType() {

        return this.status;
    }

    @JsonCreator
    public static StatusEnum decode(final String status) throws GenericException {
        return Stream.of(StatusEnum.values()).filter(
                targetEnum -> targetEnum.status.equals(status)
        ).findFirst().orElseThrow(
                () -> new GenericException("Invalid value:" + status));
    }

    @JsonValue
    public String getStatus() {

        return status;
    }

}
