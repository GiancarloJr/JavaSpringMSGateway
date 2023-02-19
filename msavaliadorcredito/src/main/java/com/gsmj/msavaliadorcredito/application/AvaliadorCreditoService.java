package com.gsmj.msavaliadorcredito.application;


import com.gsmj.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import com.gsmj.msavaliadorcredito.application.exceptions.ErroComunicacaoMicroServicesException;
import com.gsmj.msavaliadorcredito.domain.model.*;
import com.gsmj.msavaliadorcredito.infra.clients.CartoesResourceClient;
import com.gsmj.msavaliadorcredito.infra.clients.ClienteResourceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clienteResourceClient;
    private final CartoesResourceClient cartoesResourceClient;

    public SituacaoCliente obterSituacaoCliente(String cpf)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroServicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> cartoesByCliente = cartoesResourceClient.listCartoesByCliente(cpf);

            return SituacaoCliente.builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesByCliente.getBody())
                    .build();
        } catch (FeignException.FeignClientException e){
            if(HttpStatus.NOT_FOUND.value() == e.status()) {
                throw new DadosClienteNotFoundException();
            }
            throw  new ErroComunicacaoMicroServicesException(e.getMessage(), e.status());
        }

    }

    public RetornoAvaliacaoCliente realizarAvaliacaoCliente(String cpf, Long renda)
            throws DadosClienteNotFoundException,ErroComunicacaoMicroServicesException {
        try {
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteResourceClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesReponse = cartoesResourceClient.getCartoesRendaAte(renda);

            var listaCartoesAprovados = cartoesReponse.getBody().stream().map(cartao -> {

                BigDecimal limitebasico = cartao.getLimitebasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosClienteResponse.getBody().getIdade());

                var fator =idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limitebasico);


                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch (FeignException.FeignClientException e){
            if(HttpStatus.NOT_FOUND.value() == e.status()) {
                throw new DadosClienteNotFoundException();
            }
            throw  new ErroComunicacaoMicroServicesException(e.getMessage(), e.status());
        }
    }

}
