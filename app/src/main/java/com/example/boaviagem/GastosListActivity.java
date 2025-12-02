package com.example.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastosListActivity extends ListActivity
        implements AdapterView.OnItemClickListener {
    private	List<Map<String,Object>>gastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Gastos");

     String[] de = {"data","descricao","valor","categoria"};
     int [] para = {R.id.data,R.id.descricao,R.id.valor,R.id.categoria};

        SimpleAdapter adapter = new SimpleAdapter(this,ListarGastos(),R.layout.lista_gastos,de,para);

        adapter.setViewBinder(new GastosViewBinder());

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

        Map<String,Object>map = gastos.get(position);
        String descricao = (String) map.get("descricao");

        String mensagem = "Gastos selecionados" + descricao;

        Toast.makeText(
                this,mensagem, Toast.LENGTH_SHORT).show();
    }

    private List<Map<String,Object>> ListarGastos(){
        gastos = new ArrayList<Map<String,Object>>();

        Map<String,Object> item = new HashMap<String,Object>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Diária de hotel");
        item.put("valor", "R$ 260,00");
        item.put("categoria",R.color.categoria_outros);
        gastos.add(item);


        item = new HashMap<String, Object>();
        item.put("data", "03/02/2012");
        item.put("descricao", "Wifi");
        item.put("valor", "R$ 7,00");
        item.put("categoria", R.color.categoria_outros);
        gastos.add(item);

        item = new HashMap<String, Object>();
        item.put("data", "02/02/2012");
        item.put("descricao", "Táxi Aeroporto - Hotel");
        item.put("valor", "R$ 34,00");
        item.put("categoria", R.color.categoria_transporte);
        gastos.add(item);


        item = new HashMap<String, Object>();
        item.put("data", "02/02/2012");
        item.put("descricao", "Sanduíche");
        item.put("valor", "R$ 19,90");
        item.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item);

        return gastos;


    }

    private class GastosViewBinder implements SimpleAdapter.ViewBinder {
        private String dataAnterior = "";


        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if (view.getId() == R.id.data) {
                if (!dataAnterior.equals(data)) {
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                } else {
                    view.setVisibility(View.GONE);
                }
                return true;
            }

            if (view.getId() == R.id.categoria) {
                Integer id = (Integer) data;
                int cor = ContextCompat.getColor(view.getContext(), id);
                view.setBackgroundColor(cor);
                return true;
            }
            return false;

        }

    }


}
