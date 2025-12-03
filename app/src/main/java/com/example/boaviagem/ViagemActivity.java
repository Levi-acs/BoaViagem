package com.example.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViagemActivity extends Activity {

    private Date dataChegada, dataSaida;
    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private Button dataChegadaButton, dataSaidaButton;
    private int ano, mes, dia;
    private RadioGroup radioGroup;
    private String id;

    // Constante para identificar qual botão foi clicado
    private static final int BOTAO_CHEGADA = 1;
    private static final int BOTAO_SAIDA = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        if (getActionBar() != null) {
            getActionBar().setTitle("Nova Viagem");
        }

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);

        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);

        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        if (id != null) {
            prepararEdicao();
        }
    }

    // ✅ ADICIONE ESTE MÉTODO
    public void criarViagem(View view) {
        // Chama o método de salvar
        salvarViagem(view);
    }

    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();

        // ✅ CORRIGIDO: Faltavam vírgulas e espaços na query
        Cursor cursor = db.rawQuery(
                "SELECT tipo_viagem, destino, data_chegada, " +
                        "data_saida, quantidade_pessoas, orcamento " +
                        "FROM viagem WHERE _id = ?",
                new String[]{id});

        if (cursor.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            if (cursor.getInt(0) == Constantes.VIAGEM_LAZER) {
                radioGroup.check(R.id.lazer);
            } else {
                radioGroup.check(R.id.negocios);
            }

            destino.setText(cursor.getString(1));
            dataChegada = new Date(cursor.getLong(2));
            dataSaida = new Date(cursor.getLong(3));
            dataChegadaButton.setText(dateFormat.format(dataChegada));
            dataSaidaButton.setText(dateFormat.format(dataSaida));
            quantidadePessoas.setText(cursor.getString(4));
            orcamento.setText(cursor.getString(5));
        }

        cursor.close();
    }

    public void selecionarData(View view) {
        final int botaoClicado;

        if (view.getId() == R.id.dataChegada) {
            botaoClicado = BOTAO_CHEGADA;
        } else {
            botaoClicado = BOTAO_SAIDA;
        }

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        Calendar dataSelecionada = Calendar.getInstance();
                        dataSelecionada.set(year, month, dayOfMonth);

                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String dataFormatada = formato.format(dataSelecionada.getTime());

                        if (botaoClicado == BOTAO_CHEGADA) {
                            dataChegadaButton.setText(dataFormatada);
                            dataChegada = dataSelecionada.getTime();
                        } else {
                            dataSaidaButton.setText(dataFormatada);
                            dataSaida = dataSelecionada.getTime();
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.novo_gasto) {
            Intent intent = new Intent(this, GastoActivity.class);
            if (id != null) {
                intent.putExtra(Constantes.VIAGEM_ID, id);
            }
            startActivity(intent);
            return true;
        } else if (itemId == R.id.remover) {
            // Implementar remoção
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void salvarViagem(View view) {
        // Validação básica
        if (destino.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha o destino", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dataChegada == null || dataSaida == null) {
            Toast.makeText(this, "Selecione as datas", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino", destino.getText().toString());
        values.put("data_chegada", dataChegada.getTime());
        values.put("data_saida", dataSaida.getTime());
        values.put("orcamento", orcamento.getText().toString());
        values.put("quantidade_pessoas", quantidadePessoas.getText().toString());

        int tipo = radioGroup.getCheckedRadioButtonId();

        if (tipo == R.id.lazer) {
            values.put("tipo_viagem", Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);
        }

        //  CORRIGIDO: Lógica de insert/update estava duplicada
        long resultado;

        if (id == null) {
            // Nova viagem - INSERT
            resultado = db.insert("viagem", null, values);
        } else {
            // Editar viagem existente - UPDATE
            resultado = db.update("viagem", values, "_id = ?", new String[]{id});
        }

        if (resultado != -1) {
            Toast.makeText(this, getString(R.string.registro_salvo),
                    Toast.LENGTH_SHORT).show();
            finish(); // Fecha a activity após salvar
        } else {
            Toast.makeText(this, getString(R.string.erro_salvar),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}