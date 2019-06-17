package com.example.eventos_denuncia.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.eventos_denuncia.activities.MainActivity;
import com.example.eventos_denuncia.secciones.Fechas;

import java.util.Calendar;

public class AccionElegirFecha {

    private EditText editTextFechaN;

    public AccionElegirFecha(EditText editTextFechaN){
        this.editTextFechaN = editTextFechaN;
    }

    public void setFecha(Context context) {
        Calendar c = Calendar.getInstance();
        int dia = c.get(Calendar.DAY_OF_MONTH);
        int mes = c.get(Calendar.MONTH);
        int anio = c.get(Calendar.YEAR);


        DatePickerDialog dPG = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextFechaN.setText(year + "-" + (month+1) + "-" + dayOfMonth);
            }
        },anio,mes,dia);

        long milisegundos = Fechas.setMaximumDate();
        dPG.getDatePicker().setMaxDate(milisegundos);

        dPG.show();
    }
}