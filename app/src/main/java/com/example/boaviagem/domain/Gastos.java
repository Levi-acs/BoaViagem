package com.example.boaviagem.domain;

import java.util.Date;

public class Gastos {
    private Long id;
    private Date data;
    private String categoria;
    private String descricao;
    private Double valor;
    private String local;
    private Integer viagemId;

    public Gastos() {
    }

    public Gastos(Long id, Date data, String categoria,
                  String descricao, Double valor, String local,
                  Integer viagemId) {
        this.id = id;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.local = local;
        this.viagemId = viagemId;
    }
    //getters	e	setters
}



