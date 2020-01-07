package com.david.redcristianauno.Firestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.RegistroSubred;
import com.david.redcristianauno.POJOs.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;

public class InsertarDatos {
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public static int total_asistencia = 0;
    public static int total_invitados = 0;
    public static int total_ninos = 0;
    public static double total_ofrenda = 0;

    public InsertarDatos(){

    }

    public void crearUsuario(Usuarios usuario){
        db.collection("usuarios").document().set(usuario);
    }

    public void crearRegistroCelula(RegistroCelula registroCelula){
        db.collection("Datos Celula").document().set(registroCelula);
    }

    public void crearRegistroSubred(RegistroSubred registroSubred){
        db.collection("Datos Subred").document().set(registroSubred);
    }

    public void leerHistorico(final String fecha){
        db.collection("Historico Celulas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    boolean bandera = true;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                HistoricoSemanal hs = document.toObject(HistoricoSemanal.class);
                                if(fecha.equals(hs.getFecha())){
                                    bandera = false;
                                }
                            }
                            if (bandera){
                                formatoSemanal();
                            }
                        }
                    }
                });
    }

    public void formatoSemanal() {
        Calendar miCalendario = Calendar.getInstance();
        miCalendario.setFirstDayOfWeek(Calendar.TUESDAY);

        boolean bandera = false;

        int diaSemana = miCalendario.get(Calendar.DAY_OF_WEEK);

        if(diaSemana!=miCalendario.getFirstDayOfWeek()){
            bandera=false;
        }else{
            bandera=true;
        }

        if(bandera == true){

            int dia_del_mes = miCalendario.get(Calendar.DAY_OF_MONTH);
            System.out.println("El dia es: " + dia_del_mes);
            int mes = miCalendario.get(Calendar.MONTH) + 1;
            System.out.println("El mes es: " +  mes);
            int año = miCalendario.get(Calendar.YEAR);
            System.out.println("El año es: " +  año);

            int nuevo = dia_del_mes;

            String fechas[] = new String[7];

            for (int i = 0; i<7; i++){
                if(nuevo  == 0){
                    nuevo = 1;
                }
                nuevo-=1;

                switch (mes){
                    case 1:
                        if (nuevo <= 0){
                            mes = mes-1;
                            nuevo = 31-nuevo;
                        }
                        break;
                    case 2:
                        if (nuevo <= 0){
                            mes = mes-1;
                            nuevo = 31-nuevo;
                        }
                        break;
                    case 3:
                        if((año%4 == 0) && ((año % 100 !=0) || (año % 400 == 0))){
                            if(nuevo<=0){
                                mes = mes -1;
                                nuevo = 29-nuevo;
                            }
                        }else {
                            if(nuevo<=0){
                                mes = mes -1;
                                nuevo = 28 - nuevo;
                            }
                        }
                        break;
                    case 4:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 31 - nuevo;
                        }
                        break;
                    case 5:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 30 - nuevo;
                        }
                        break;
                    case 6:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 31 - nuevo;
                        }
                        break;
                    case 7:
                        if (nuevo <= 0){
                            mes = mes-1;
                            nuevo = 30 - nuevo;
                        }
                        break;
                    case 8:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 31 - nuevo;
                        }
                        break;
                    case 9:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 31 - nuevo;
                        }
                        break;
                    case 10:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 30 - nuevo;
                        }
                        break;
                    case 11:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 31 - nuevo;
                        }
                        break;
                    case 12:
                        if (nuevo <= 0){
                            mes = mes -1;
                            nuevo = 30 - nuevo;
                        }
                        break;
                }
                miCalendario.set(Calendar.DAY_OF_MONTH, nuevo);
                miCalendario.set(Calendar.MONTH, mes - 1);
                miCalendario.set(Calendar.YEAR, año);
                final String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(miCalendario.getTime());
                fechas[i] = fecha;
            }

            leerIdUsuario(fechas);

            crearFormatoSubred(fechas);

        }
    }

    public void leerIdUsuario(final String[] fechas){

        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){
                                crearFormato(document.getId());

                            }
                            //totalSemanal(total_asistencia, total_invitados,total_ninos,total_ofrenda);
                        }
                    }
                });
    }


    public void crearFormatoSubred(String[] fechas) {

    }

    public void crearFormato(String id) {

        /*db.collection("usuarios").document(id).collection("Datos Celula")
                .get()
                .addOnCompleteListener();*/


    }
    public void conteoTotal(int total_asistencia, int total_invitados, int total_ninos, double total_ofrenda){

        this.total_asistencia += total_asistencia;
        this.total_invitados += total_invitados;
        this.total_ninos += total_ninos;
        this.total_ofrenda += total_ofrenda;
    }


    public void totalSemanal(int total_asistencia, int total_invitados, int total_ninos, double total_ofrenda) {
        Calendar c = Calendar.getInstance();
        c.get(Calendar.DAY_OF_MONTH);
        c.get(Calendar.MONTH);
        c.get(Calendar.YEAR);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        HistoricoSemanal hs3 = new HistoricoSemanal();
        hs3.setTotal_asistencia(total_asistencia);
        hs3.setTotal_invitados(total_invitados);
        hs3.setTotal_ninos(total_ninos);
        hs3.setTotal_ofrenda(total_ofrenda);
        hs3.setFecha(fecha);

        db.collection("Historico Celulas").document().set(hs3);
    }

}
