package com.example.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class DashboardActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
    }

    public void selecionarOpcao(View view) {
        int id = view.getId();

        if (id == R.id.nova_viagem) {
            startActivity(new Intent(this, ViagemActivity.class));
        } else if (id == R.id.novo_gasto) {
            startActivity(new Intent(this, GastoActivity.class));
        } else if (id == R.id.minhas_viagens) {
            startActivity(new Intent(this, ViagemListActivity.class));
        }


    }
    }
