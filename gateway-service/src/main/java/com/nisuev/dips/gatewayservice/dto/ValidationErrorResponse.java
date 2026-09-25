package com.nisuev.dips.gatewayservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ValidationErrorResponse")
public class ValidationErrorResponse {
    private String message;
    private List<ErrorDescription> errors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "ErrorDescription")
    public static class ErrorDescription {
        private String field;
        private String error;
    }
}
