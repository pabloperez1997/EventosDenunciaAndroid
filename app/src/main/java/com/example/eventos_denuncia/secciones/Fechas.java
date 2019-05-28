package com.example.eventos_denuncia.secciones;

import org.joda.time.LocalDate;

public class Fechas {

    public static long setMaximumDate(){
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusYears(18).minusDays(1);
        return localDate.toDate().getTime();
    }
}
