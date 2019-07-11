package com.revolut.transfer.controller.dto;

import lombok.Data;

@Data
public class TransferDto {
    String source;
    String destination;
    long amount;
}
