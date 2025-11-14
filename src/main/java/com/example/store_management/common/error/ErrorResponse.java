package com.example.store_management.common.error;

import java.time.OffsetDateTime;

/**
 * Standard error response model used by the global exception handler.
 */
public record ErrorResponse(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
                OffsetDateTime.now(),
                status,
                error,
                message,
                path
        );
    }
}