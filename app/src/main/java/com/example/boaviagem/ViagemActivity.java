package com.example.boaviagem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;

import androidx.annotation.Nullable;

public class ViagemActivity  extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);
        getActionBar().setTitle("Nova Viagem");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id ==R.id.novo_gasto){
            startActivity(new Intent(this,GastoActivity.class));
            return true;
        } else if (id ==R.id.remover) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
}



