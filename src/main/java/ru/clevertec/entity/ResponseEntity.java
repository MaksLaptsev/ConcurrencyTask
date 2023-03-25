package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ResponseEntity implements RequestResponse{
    private int responseValue;

    @Override
    public int getValue() {
        return responseValue;
    }
}
