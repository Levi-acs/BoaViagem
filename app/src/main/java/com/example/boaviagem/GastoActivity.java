package com.example.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class GastoActivity extends Activity {

    private int ano, mes, dia;
    private Button dataGastos;
    private Spinner categoria;
    private EditText descricao, valor;
    private DatabaseHelper helper;
    private String viagemId;
    private Date dataGasto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

        // para verificar existencia de action bar
        if (getActionBar() != null) {
            getActionBar().setTitle("Novo Gasto");
        }

        // Inicializar DatabaseHelper
        helper = new DatabaseHelper(this);

        // Pegar o ID da viagem passado pela Intent
        viagemId = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        // Pegando data atual
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        // Atualizar a data com a data atual
        dataGasto = calendar.getTime();

        // Conexão dos campos com o layout
        dataGastos = findViewById(R.id.data);
        descricao = findViewById(R.id.descricao);
        valor = findViewById(R.id.valor);
        categoria = findViewById(R.id.categoria);

        dataGastos.setText(dia + "/" + (mes + 1) + "/" + ano);

        // Abrir o DatePicker ao clicar no botão
        dataGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDatePicker();
            }
        });

        // Configurar Spinner de categoria
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.categoria_gasto, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(adapter);
    }

    // registrar gastos
    public void registrarGasto(View view) {
        // Validações
        String desc = descricao.getText().toString();
        String val = valor.getText().toString();
        String cat = categoria.getSelectedItem().toString();

        // garantindo que todos os campos estão preenchidos
        if (desc.isEmpty()) {
            Toast.makeText(this, "Preencha a descrição", Toast.LENGTH_SHORT).show();
            return;
        }

        if (val.isEmpty()) {
            Toast.makeText(this, "Preencha o valor", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dataGasto == null) {
            Toast.makeText(this, "Selecione a data", Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar no banco de dados
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("descricao", desc);
        values.put("valor", Double.parseDouble(val));
        values.put("categoria", cat);
        values.put("data", dataGasto.getTime());

        if (viagemId != null) {
            values.put("viagem_id", viagemId);
        }

        long resultado = db.insert("gasto", null, values);

        if (resultado != -1) {
            Toast.makeText(this, "Gasto registrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish(); // Volta para a tela anterior
        } else {
            Toast.makeText(this, "Erro ao salvar gasto", Toast.LENGTH_SHORT).show();
        }
    }

    public void selecionarData(View view) {
        abrirDatePicker();
    }

    private void abrirDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                listener,
                ano,
                mes,
                dia
        );
        dialog.show();
    }

    private final DatePickerDialog.OnDateSetListener listener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    ano = year;
                    mes = monthOfYear;
                    dia = dayOfMonth;

                    dataGastos.setText(dia + "/" + (mes + 1) + "/" + ano);

                    //  Atualizar o objeto Date
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, monthOfYear, dayOfMonth);
                    dataGasto = calendar.getTime();
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.remover) {
            removerGasto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removerGasto() {
        // Implementar lógica de remoção se necessário
        Toast.makeText(this, "Gasto removido!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}