package com.gsmj.msavaliadorcredito.application.exceptions;

import lombok.Getter;

public class ErroComunicacaoMicroServicesException extends Exception{

    @Getter
    private Integer status;

    public ErroComunicacaoMicroServicesException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
