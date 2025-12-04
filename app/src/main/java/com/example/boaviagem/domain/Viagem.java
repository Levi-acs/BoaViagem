package com.example.boaviagem.domain;

import java.util.Date;

public class Viagem {

    private long id;
    private String destino;
    private Integer tipoViagem;
    private Date dataChegada;
    private Date dataSaida;
    private Double orcamento;
    private Integer quantidadePessoas;

    public Viagem(){}

    public Viagem(Long id, String destino, Integer tipoViagem, Date dataChegada, Date dataSaida, Double orcamento, Integer quantidadePessoas){
        this.id = id;
        this.destino = destino;
        this.tipoViagem = tipoViagem;
        this.dataChegada = dataChegada;
        this.dataSaida = dataSaida;
        this.orcamento = orcamento;
        this.quantidadePessoas = quantidadePessoas;
    }


}
