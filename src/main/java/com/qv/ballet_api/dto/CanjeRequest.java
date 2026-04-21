package com.qv.ballet_api.dto;


import lombok.Data;

import java.util.List;

@Data
public class CanjeRequest {
    private String codigoCanje;
    private List<Long> asientosIds;
}
