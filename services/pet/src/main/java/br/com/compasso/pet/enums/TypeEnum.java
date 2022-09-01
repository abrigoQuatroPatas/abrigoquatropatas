package br.com.compasso.pet.enums;

import br.com.compasso.pet.exceptions.GenericException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum TypeEnum {
    DOG ("dog"),
    CAT ("cat");

    private String type;

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

