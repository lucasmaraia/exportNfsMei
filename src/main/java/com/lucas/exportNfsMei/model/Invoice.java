package com.lucas.exportNfsMei.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {
    private String date;
    private String issuedTo;
    private String city;
    private String value;
    private String situation;
    private String municipality;
    private String tax;

}