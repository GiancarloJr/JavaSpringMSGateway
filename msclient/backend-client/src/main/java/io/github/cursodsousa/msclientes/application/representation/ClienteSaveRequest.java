package io.github.cursodsousa.msclientes.application.representation;

import io.github.cursodsousa.msclientes.domain.Cliente;
import lombok.Data;

@Data
public class ClienteSaveRequest {

    private String cpf;

    private String nome;

    private Integer idade;

    public Cliente toEntity(){
        return new Cliente(cpf,nome,idade);
    }

}
