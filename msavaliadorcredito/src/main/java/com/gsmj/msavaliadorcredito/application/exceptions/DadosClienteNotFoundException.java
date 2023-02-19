package com.gsmj.msavaliadorcredito.application.exceptions;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException() {
        super("Dados do cliente nao encontrados para o CPF  informado");
    }
}
