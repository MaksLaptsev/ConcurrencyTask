package ru.clevertec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RequestEntity implements RequestResponse{
    private int requestValue;

    @Override
    public int getValue() {
        return requestValue;
    }
}
