package com.example.boaviagem;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.boaviagem.fragment.ViagemListFragment;

public class AnotacaoActivity extends FragmentActivity {

    private boolean tablet = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
