package com.example.boaviagem;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.boaviagem.domain.Anotacao;
import com.example.boaviagem.fragment.Anotacaofragment;
import com.example.boaviagem.fragment.AnotacoesListFragment;
import com.example.boaviagem.fragment.ViagemListFragment;

public class AnotacaoActivity extends FragmentActivity implements AnotacaoListener {

    private boolean tablet = true;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.anotacoes);

        // Se existir fragment_unico → é celular
        View fragmentUnico = findViewById(R.id.fragment_unico);
        if (fragmentUnico != null) {
            tablet = false;
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        ViagemListFragment listaFragment = new ViagemListFragment();

        // Decide onde inserir o fragment
        if (tablet) {
            transaction.replace(R.id.fragment_viagens, listaFragment);
        } else {
            transaction.replace(R.id.fragment_unico, listaFragment);
        }

        transaction.commit();
    }
    @Override
    public void viagemSelecionada(Bundle bundle){

        FragmentManager manager = getSupportFragmentManager();
        AnotacoesListFragment fragment;

        if(tablet){
            fragment = (AnotacoesListFragment) manager.findFragmentById(R.id.fragment_anotacoes);
            fragment.listarAnotacoesPorViagem(bundle);
        }else {
            fragment = new AnotacoesListFragment();
            fragment.setArguments(bundle);

            manager.beginTransaction().replace(R.id.fragment_unico,fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void anotacaoSelecionada(Anotacao anotacao){
        FragmentManager manager = getSupportFragmentManager();
        Anotacaofragment fragment;

        if(tablet){
            fragment = (Anotacaofragment) manager
                    .findFragmentById(R.id.fragment_anotacao);
            fragment.prepararEdicao(anotacao);
        }else {
            fragment = new Anotacaofragment();
            fragment.setAnotacao(anotacao);

            manager.beginTransaction()
                    .replace(R.id.fragment_unico, fragment)
                    .addToBackStack(null).commit();
        }
    }

    @Override
    public void novaAnotacao(){
        FragmentManager manager = getSupportFragmentManager();
        Anotacaofragment fragment;

        if(tablet){
            fragment = (Anotacaofragment) manager
                    .findFragmentById(R.id.fragment_anotacao);
            fragment.criarNovaAnotacao();
        } else {
            fragment = new Anotacaofragment();
            manager.beginTransaction()
                    .replace(R.id.fragment_unico,fragment)
                    .addToBackStack(null).commit();
        }
    }
}
