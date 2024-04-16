package com.inspiron.labs.app.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressDto {
    private String city;
    private Integer pinCode;
}
