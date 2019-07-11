package com.revolut.transfer.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ErrorResponse {
    String message;
}
