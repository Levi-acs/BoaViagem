package com.example.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class GastoActivity extends Activity {

    private int ano, mes, dia;
    private Button dataGastos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gasto);

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
}
