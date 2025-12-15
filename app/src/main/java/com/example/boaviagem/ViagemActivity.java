package com.example.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.example.boaviagem.dao.boaViagemDAO;
import com.example.boaviagem.domain.Viagem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViagemActivity extends Activity {

    private Date dataChegada, dataSaida;
    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private Button dataChegadaButton, dataSaidaButton;
    private RadioGroup radioGroup;
    private String id;
    private boaViagemDAO dao;

    private static final int BOTAO_CHEGADA = 1;
    private static final int BOTAO_SAIDA = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viagem);

        if (getActionBar() != null) {
            getActionBar().setTitle("Nova Viagem");
        }

        dataChegadaButton = findViewById(R.id.dataChegada);
        dataSaidaButton = findViewById(R.id.dataSaida);

        destino = findViewById(R.id.destino);
        quantidadePessoas = findViewById(R.id.quantidadePessoas);
        orcamento = findViewById(R.id.orcamento);

        radioGroup = findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        if (id != null) {
            prepararEdicao();
        }
    }

    private void prepararEdicao() {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT tipo_viagem, destino, data_chegada, data_saida, quantidade_pessoas, orcamento " +
                        "FROM viagem WHERE _id = ?",
                new String[]{id}
        );

        if (cursor.moveToFirst()) {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            if (cursor.getInt(0) == Constantes.VIAGEM_LAZER) {
                radioGroup.check(R.id.lazer);
            } else {
                radioGroup.check(R.id.negocios);
            }

            destino.setText(cursor.getString(1));

            dataChegada = new Date(cursor.getLong(2));
            dataSaida = new Date(cursor.getLong(3));

            dataChegadaButton.setText(formato.format(dataChegada));
            dataSaidaButton.setText(formato.format(dataSaida));

            quantidadePessoas.setText(cursor.getString(4));
            orcamento.setText(cursor.getString(5));
        }

        cursor.close();
    }

    public void selecionarData(View view) {
        final int botaoClicado =
                (view.getId() == R.id.dataChegada) ? BOTAO_CHEGADA : BOTAO_SAIDA;

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (DatePicker picker, int year, int month, int day) -> {
                    Calendar dataSelecionada = Calendar.getInstance();
                    dataSelecionada.set(year, month, day);

                    SimpleDateFormat formato =
                            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String dataFormatada = formato.format(dataSelecionada.getTime());

                    if (botaoClicado == BOTAO_CHEGADA) {
                        dataChegada = dataSelecionada.getTime();
                        dataChegadaButton.setText(dataFormatada);
                    } else {
                        dataSaida = dataSelecionada.getTime();
                        dataSaidaButton.setText(dataFormatada);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.show();
    }

    public void criarViagem(View view) {
        salvarViagem();
    }

    private void salvarViagem() {

        if (destino.getText().toString().isEmpty()) {
            Toast.makeText(this, "Informe o destino", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dataChegada == null || dataSaida == null) {
            Toast.makeText(this, "Selecione as datas", Toast.LENGTH_SHORT).show();
            return;
        }

        if (quantidadePessoas.getText().toString().isEmpty()
                || orcamento.getText().toString().isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        dao = new boaViagemDAO(this);

        Viagem viagem = new Viagem();
        viagem.setDestino(destino.getText().toString());
        viagem.setDataChegada(dataChegada);
        viagem.setDataSaida(dataSaida);
        viagem.setQuantidadePessoas(
                Integer.parseInt(quantidadePessoas.getText().toString()));
        viagem.setOrcamento(
                Double.parseDouble(orcamento.getText().toString()));

        int tipo = radioGroup.getCheckedRadioButtonId();
        viagem.setTipoViagem(
                tipo == R.id.lazer
                        ? Constantes.VIAGEM_LAZER
                        : Constantes.VIAGEM_NEGOCIOS
        );

        long resultado;

        if (id == null) {
            resultado = dao.inserir(viagem);
        } else {
            viagem.setId(Long.parseLong(id));
            resultado = dao.atualizar(viagem);
        }

        if (resultado != -1) {
            Toast.makeText(this, "Viagem salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao salvar viagem", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viagem_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.novo_gasto) {
            Intent intent = new Intent(this, GastoActivity.class);
            if (id != null) {
                intent.putExtra(Constantes.VIAGEM_ID, id);
            }
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}