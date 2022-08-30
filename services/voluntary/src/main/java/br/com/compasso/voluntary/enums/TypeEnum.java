package br.com.compasso.voluntary.enums;

import br.com.compasso.voluntary.exceptions.GenericException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum TypeEnum {

    VETERINARIAN ("veterinarian"),
    HELPER ("helper");

    private final String type;

    TypeEnum(String type) {

        this.type = type;
    }

    public String returnType() {
        return this.type;
    }

    @JsonCreator
    public static TypeEnum decode(final String type) throws GenericException {
        return Stream.of(TypeEnum.values()).filter(
                targetEnum -> targetEnum.type.equals(type)
        ).findFirst().orElseThrow(
                () -> new GenericException("Invalid value:" + type));
    }

    @JsonValue
    public String getType() {

        return type;
    }
}
