package com.example.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViagemListActivity extends ListActivity
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener, SimpleAdapter.ViewBinder {

    private DatabaseHelper helper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;
    private AlertDialog alertDialog;
    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private List<Map<String, Object>> viagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActionBar() != null) {
            getActionBar().setTitle("Viagens");
        }

        helper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferences.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        this.alertDialog = criarAlertDialog();
        this.dialogConfirmacao = criarDialogConfirmacao();

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};

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

    // ✅ ADICIONE ESTE MÉTODO
    public void criarViagem(View view) {
        startActivity(new Intent(this, ViagemActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {

        Map<String, Object> viagem = viagens.get(position);
        String destino = (String) viagem.get("destino");

        String mensagem = "Viagem selecionada: " + destino;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();

        this.viagemSelecionada = position;

        alertDialog.show();
    }

    private List<Map<String, Object>> listaViagens() {

        SQLiteDatabase db = helper.getReadableDatabase();

        // ✅ CORRIJA: Faltava vírgula na query
        Cursor cursor = db.rawQuery(
                "SELECT _id, tipo_viagem, destino, data_chegada, data_saida, orcamento FROM viagem",
                null);

        viagens = new ArrayList<Map<String, Object>>();

        if (cursor.moveToFirst()) {
            do {
                Map<String, Object> item = new HashMap<String, Object>();

                String id = cursor.getString(0);
                int tipoViagem = cursor.getInt(1);
                String destino = cursor.getString(2);
                long dataChegada = cursor.getLong(3);
                long dataSaida = cursor.getLong(4);
                double orcamento = cursor.getDouble(5);

                item.put("id", id);

                if (tipoViagem == Constantes.VIAGEM_LAZER) {
                    item.put("imagem", R.drawable.lazer);
                } else {
                    item.put("imagem", R.drawable.negocios);
                }

                item.put("destino", destino);

                Date dataChegadaDate = new Date(dataChegada);
                Date dataSaidaDate = new Date(dataSaida);

                String periodo = dateFormat.format(dataChegadaDate) + " a " + dateFormat.format(dataSaidaDate);
                item.put("data", periodo);

                Double totalGasto = calcularTotalGasto(db, id);
                item.put("total", "Gasto total R$ " + totalGasto);

                double alerta = orcamento * valorLimite / 100;
                Double[] valores = new Double[]{orcamento, alerta, totalGasto};
                item.put("barraProgresso", valores);

                viagens.add(item);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM gasto WHERE viagem_id = ?",
                new String[]{id});

        cursor.moveToFirst();
        Double total = cursor.getDouble(0);
        cursor.close();
        return total;
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view.getId() == R.id.barraProgresso) {
            Double valores[] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");

        switch (item) {
            case 0: // editar viagem
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, GastosListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 3:
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                // Remover do banco de dados
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("viagem", "_id = ?", new String[]{id});

                viagens.remove(viagemSelecionada);
                getListView().invalidateViews();

                Toast.makeText(this, "Viagem removida", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}