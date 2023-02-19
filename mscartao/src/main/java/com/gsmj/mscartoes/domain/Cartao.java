package com.gsmj.mscartoes.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@NoArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(STRING)
    private BandeiraCartao bandeira;

    private BigDecimal renda;

    private BigDecimal limitebasico;

    public Cartao(String nome,
                  BandeiraCartao bandeira,
                  BigDecimal renda,
                  BigDecimal limitebasico) {
        this.nome = nome;
        this.bandeira = bandeira;
        this.renda = renda;
        this.limitebasico = limitebasico;
    }


}
