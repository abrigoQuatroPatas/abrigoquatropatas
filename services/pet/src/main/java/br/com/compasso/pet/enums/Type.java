package br.com.compasso.pet.enums;

import br.com.compasso.pet.exception.GenericException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Type {
    DOG ("dog"),
    CAT ("cat");

    private String type;

    Type(String type) {

        this.type = type;
    }

    public String returnType() {

        return this.type;
    }

    @JsonCreator
    public static Type decode(final String type) throws GenericException {
        return Stream.of(Type.values()).filter(
                targetEnum -> targetEnum.type.equals(type)
        ).findFirst().orElseThrow(
                () -> new GenericException("Invalid value:" + type));
    }

    @JsonValue
    public String getType() {

        return type;
    }
}

