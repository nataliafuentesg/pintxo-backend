package com.pintxo.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tapas5DTO {
    private String title;
    private boolean enabled;
    private String daysCsv;   // "MON,TUE,WED"
    private String timezone;  // "America/New_York"
    private String startTime; // "HH:mm"
    private String endTime;   // "HH:mm"
    private double price;
}
