package com.david.redcristianauno.Firestore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.david.redcristianauno.POJOs.Celula;
import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.POJOs.HistoricoSemanalSubred;
import com.david.redcristianauno.POJOs.Red;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.RegistroSubred;
import com.david.redcristianauno.POJOs.Subred;
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

    // El metodo checa si no hay otro registro con la misma fecha del historico para poder crear dicho hisotrico
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

    //Checha los ultimos 7 dias despues del cierre de los diesmos
    public void formatoSemanal() {
        Calendar miCalendario = Calendar.getInstance();
        miCalendario.setFirstDayOfWeek(Calendar.TUESDAY);

        boolean bandera;

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

                if(mes == 1 ||mes == 2 || mes == 4 || mes == 6 || mes == 8 || mes == 9 || mes == 11){
                    if (nuevo <= 0){
                        mes = mes-1;
                        nuevo = 31-nuevo;
                    }
                }else if(mes == 3){
                    if(año%4 == 0 && año % 100 !=0 || año % 400 == 0){
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
                }else{
                    if (nuevo <= 0){
                        mes = mes -1;
                        nuevo = 30 - nuevo;
                    }
                }


                miCalendario.set(Calendar.DAY_OF_MONTH, nuevo);
                miCalendario.set(Calendar.MONTH, mes - 1);
                miCalendario.set(Calendar.YEAR, año);

                Log.d("Result", String.valueOf(miCalendario.get(Calendar.MONTH)));
                final String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(miCalendario.getTime());
                fechas[i] = fecha;
            }

            crearFormatoCelula(fechas);

            crearFormatoSubred(fechas);

        }
    }

    //Registra todos los datos pero determina quienes tienen la fecha de la semana seleccionada  para la subred
    public void crearFormatoSubred(final String[] fechas) {
        db.collection("Datos Subred")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private int total_asistencia;
                    private double total_ofrenda;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                RegistroSubred rs = document.toObject(RegistroSubred.class);
                                for (int i = 0; i < fechas.length; i++){
                                    //Log.d("Result", fechas[i]);
                                    if (fechas[i].equals(rs.getFecha_subred())){
                                        total_asistencia = total_asistencia + rs.getAsistencia_subred();
                                        total_ofrenda = total_ofrenda + rs.getOfrenda_subred();
                                    }
                                }
                            }
                            totalSemanalSubred(total_asistencia,total_ofrenda);
                        }else{
                            Log.d("Result", "Error al encontrar la tarea asignada", task.getException());
                        }
                    }
                });
    }

    //Crea el formato de subred con los datos proporcionados de subred
    private void totalSemanalSubred(final int total_asistencia, final double total_ofrenda) {
        Calendar c = Calendar.getInstance();
        c.get(Calendar.DAY_OF_MONTH);
        c.get(Calendar.MONTH);
        c.get(Calendar.YEAR);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        HistoricoSemanalSubred hs3 = new HistoricoSemanalSubred();
        hs3.setTotal_asistencia(total_asistencia);
        hs3.setTotal_ofrenda(total_ofrenda);
        hs3.setFecha(fecha);

        db.collection("Historico Subred").document().set(hs3);
    }

    //Registra todos los datos pero determina quienes tienen la fecha de la semana seleccionada  para la celula
    public void crearFormatoCelula(final String [] fechas) {

        db.collection("Datos Celula")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public int total_asistencia = 0;
                    public int total_invitados = 0;
                    public int total_ninos = 0;
                    public  double total_ofrenda = 0;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                RegistroCelula rc = document.toObject(RegistroCelula.class);
                                for (int i = 0; i < fechas.length; i++){
                                    if (fechas[i].equals(rc.getFecha_celula())){
                                        total_asistencia = total_asistencia + rc.getAsistencia_celula();
                                        total_invitados = total_invitados + rc.getInvitados_celula();
                                        total_ninos = total_ninos + rc.getNinos_celula();
                                        total_ofrenda = total_ofrenda + rc.getOfrenda_celula();
                                    }
                                }
                            }
                            totalSemanalCelula(total_asistencia,total_invitados,total_ninos,total_ofrenda);
                        }else{
                            Log.d("Result", "Error al encontrar la tarea asignada", task.getException());
                        }
                    }
                });
    }

    //Crea el formato de subred con los datos proporcionados de celula
    public void totalSemanalCelula(int total_asistencia, int total_invitados, int total_ninos, double total_ofrenda) {
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

    //Busca si no hay una red con el mismo nombre
    public void existeRed(final Red red, final String nombre, final Context context){
        db.collection("redes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Red r = document.toObject(Red.class);
                                if(nombre.equals(r.getNombre_red())){
                                    Toast.makeText(context, "ya existe una red con ese nombre", Toast.LENGTH_SHORT).show();
                                }else{
                                    crearRed(red);
                                }
                            }
                        }
                    }
                });
    }

    //En caso de no existir el mismo nombre crea la red
    private void crearRed(Red red) {
        db.collection("redes").document().set(red);
    }

    //Busca si no hay una subred con el mismo nombre
    public void existeSubred(final Subred subred, final String nombre, final Context context){
        db.collection("subredes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Subred sb = document.toObject(Subred.class);
                                if(nombre.equals(sb.getNombre_subred())){
                                    Toast.makeText(context, "ya existe una red con ese nombre", Toast.LENGTH_SHORT).show();
                                }else{
                                    crearSubred(subred);
                                }
                            }
                        }
                    }
                });
    }

    //En caso de no existir el mismo nombre crea la subred
    private void crearSubred(Subred subred) {
        db.collection("subredes").document().set(subred);
    }

    //Busca si no hay una celula con el mismo nombre
    public void existeCelula(final Celula celula, final String nombre, final Context context){
        db.collection("celulas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Celula c = document.toObject(Celula.class);
                                if(nombre.equals(c.getNombre_celula())){
                                    Toast.makeText(context, "ya existe una red con ese nombre", Toast.LENGTH_SHORT).show();
                                }else{
                                    crearCelula(celula);
                                }
                            }
                        }
                    }
                });
    }

    //En caso de no existir el mismo nombre crea la celula
    private void crearCelula(Celula celula) {
        db.collection("celulas").document().set(celula);
    }
}
