package com.example.boaviagem;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViagemListActivity extends ListActivity
        implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> viagens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Viagens");

        String[] de = {"imagem","destino","data","total"};
        int[] para = {R.id.tipoViagem,R.id.destino,R.id.data,R.id.valor};

        // Criando adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listaViagens(),
                R.layout.lista_viagem,
                de,
                para
   );
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

        Map<String,Object> viagem  = viagens.get(position);
        String destino = (String) viagem.get("destino");

        String mensagem = "Viagem selecionada: " + destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

        // Abre a tela de gastos
        startActivity(new Intent(this, GastosListActivity.class));
    }

    private List<Map<String,Object>> listaViagens() {
        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.negocios);
        item.put("destino", "São Paulo");
        item.put("data", "02/02/2012 a 04/02/2012");
        item.put("total", " gasto total R$ 300,00");
        viagens.add(item);

        item = new HashMap<String, Object>();
        item.put("imagem",R.drawable.lazer);
        item.put("destino","Maceió");
        item.put("data","14/05/2012 a 16/05/2012");
        item.put("total","gasto total R$ 25834,67");
        viagens.add(item);

        return viagens;


    };
}
