package com.enigmacamp.barbershop.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationalHourResponse {
    private String operating_hours_id;
    private String barbershop_id;
    private String day;
    private String opening_time;
    private String closing_time;

}