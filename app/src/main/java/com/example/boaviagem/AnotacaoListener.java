package com.example.boaviagem;

import android.os.Bundle;

import com.example.boaviagem.domain.Anotacao;

public interface AnotacaoListener {
    void viagemSelecionada(Bundle bundle);
    void anotacaoSelecionada(Anotacao anotacao);
    void novaAnotacao();
}
