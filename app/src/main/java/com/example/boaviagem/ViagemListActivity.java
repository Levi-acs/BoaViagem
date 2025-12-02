package com.example.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViagemListActivity extends ListActivity
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {


    private AlertDialog alertDialog;

    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private List<Map<String, Object>> viagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle("Viagens");

        this.alertDialog = criarAlertDialog();

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};

        // Criando adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listaViagens(),
                R.layout.lista_viagem,
                de,
                para
        );

        adapter.setViewBinder(this);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    ;


    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

        Map<String, Object> viagem = viagens.get(position);
        String destino = (String) viagem.get("destino");

        String mensagem = "Viagem selecionada: " + destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

        this.viagemSelecionada = position;
        this.alertDialog = criarAlertDialog();
        this.dialogConfirmacao = criarDialogConfirmacao();

        alertDialog.show();

    }

    private List<Map<String, Object>> listaViagens() {
        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.negocios);
        item.put("destino", "São Paulo");
        item.put("data", "02/02/2012 a 04/02/2012");
        item.put("total", " gasto total R$ 300,00");
        item.put("barraProgresso",
                new Double[]{500.0, 450.0, 314.98});
        viagens.add(item);

        item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.lazer);
        item.put("destino", "Maceió");
        item.put("data", "14/05/2012 a 16/05/2012");
        item.put("total", "gasto total R$ 25834,67");
        item.put("barraProgresso",
                new Double[]{30000.0, 28600.0, 25834.67});
        viagens.add(item);

        return viagens;


    }

    ;

    @Override
    public boolean setViewValue(View view, Object data,
                                String textRepresentation) {
        if (view.getId() == R.id.barraProgresso) {
            Double valores[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(
                    valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }
        return false;
    }


    @Override
    public void onClick(DialogInterface dialog, int item) {
        switch (item) {
            case 0:
                startActivity(new Intent(this, ViagemActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastosListActivity.class));
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialogConfirmacao.dismiss();
                break;
        }
    }


    private AlertDialog criarAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.edita),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();

    }

    private AlertDialog criarDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_de_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();

    }


}

