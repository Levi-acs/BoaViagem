package com.example.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class GastoActivity extends Activity {

    private int ano, mes, dia;
    private Button dataGastos;

    private Spinner categoria;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

        getActionBar().setTitle("Novo Gasto");

        // pegando data atual
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        // conexão do botão com o layout(view)
        dataGastos = findViewById(R.id.data);
        dataGastos.setText(dia + "/" + (mes + 1) + "/" + ano);

        // Abrir o DatePicker ao clicar no botão
        dataGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDatePicker();
            }
        });

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,R.array.categoria_gasto, android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);

    }

    public void selecionarData(View view) {
        showDialog(view.getId());
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
                }


            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.remover){
            removerGasto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removerGasto() {
        // Aqui você coloca sua lógica
        Toast.makeText(this, "Gasto removido!", Toast.LENGTH_SHORT).show();
    }

}
