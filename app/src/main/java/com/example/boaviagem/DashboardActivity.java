package com.example.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        if (getActionBar() != null) {
            getActionBar().setTitle("Boa Viagem");
        }
    }

    // ✅ ADICIONE ESTE MÉTODO
    public void criarViagem(View view) {
        startActivity(new Intent(this, ViagemActivity.class));
    }

    public void selecionarOpcao(View view) {
        int id = view.getId();

        if (id == R.id.nova_viagem) {
            startActivity(new Intent(this, ViagemActivity.class));
        } else if (id == R.id.novo_gasto) {
            startActivity(new Intent(this, GastoActivity.class));
        } else if (id == R.id.minhas_viagens) {
            startActivity(new Intent(this, ViagemListActivity.class));
        } else if (id == R.id.configuracoes) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sair) {
            // Limpar preferências de login
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("manter_conectado", false);
            editor.apply();

            // Voltar para tela de login
            startActivity(new Intent(this, BoaViagemActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}