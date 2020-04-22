package com.david.redcristianauno;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.view.MenuItem;


import android.view.Menu;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


import com.david.redcristianauno.Firestore.InsertarDatos;
import com.david.redcristianauno.Firestore.LeerDatos;
import com.david.redcristianauno.Historico.HistoricoDiarioFragment;
import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.POJOs.HistoricoSemanalSubred;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.RegistroSubred;
import com.david.redcristianauno.ui.activities.LoginActivity;
import com.david.redcristianauno.ui.fragments.CreateConfigurationDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    private TextView tv_nombre_usuario, tv_correo_usuario;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> listaSemanal = new ArrayList<String>();
    static int asistencia = 0;
    static int invitados = 0;
    static int ninos = 0;
    static double ofrenda = 0;
    public static String correo_usuario = "";
    private NavigationView navigationView;
    private Menu menu;
    public static String res = "";
    public int resvar;

    long maxid = 0;
    long maxidSubred = 0;

    private LeerDatos l = new LeerDatos();
    private InsertarDatos inda = new InsertarDatos();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View nombreview = navigationView.getHeaderView(0);
        View correoview = navigationView.getHeaderView(0);

        tv_nombre_usuario = (TextView) nombreview.findViewById(R.id.tv_nombre_usuario);
        tv_correo_usuario = (TextView) correoview.findViewById(R.id.tv_correo);

        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contenedor,new MenuFragment()).commit();

        inicializarFirebase();

        Calendar c = Calendar.getInstance();
        c.get(Calendar.DAY_OF_MONTH);
        c.get(Calendar.MONTH);
        c.get(Calendar.YEAR);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        //checar(fecha);

        inda.leerHistorico(fecha);

        correo_usuario = Preferences.obtenerPreferencesString(this, Preferences.PREFERENCES_USUARIO_LOGIN);

        tv_correo_usuario.setText(correo_usuario);

        l.datosSesion(tv_nombre_usuario,correo_usuario, menu, navigationView, PrincipalActivity.this);

        //ocultardatos();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } /*else {
            super.onBackPressed();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.salida) {
            new AlertDialog.Builder(PrincipalActivity.this)
                    .setTitle("Cerrar Sesión")
                    .setMessage("¿Estás seguro de cerrar la sesión?")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Preferences.savePreferenceBoolean(PrincipalActivity.this,false,Preferences.PREFENCE_ESTADO_BUTTON_SESION);
                            Intent i = new Intent(PrincipalActivity.this, LoginActivity.class);
                            startActivity(i);
                            PrincipalActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new MenuFragment()).commit();
        } else if (id == R.id.nav_gallery) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new RegistroSubredFragment()).commit();
            resvar = 1;
        } else if (id == R.id.nav_slideshow) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new RegistroCelulaFragment()).commit();
            resvar=2;
        } else if (id == R.id.nav_tools) {
            fragmentManager.beginTransaction().replace(R.id.contenedor,new HistoricoFragment()).commit();
            resvar=3;
        }else if(id == R.id.nav_biblia){
            fragmentManager.beginTransaction().replace(R.id.contenedor,new BibliaFragment()).commit();
        } else if (id == R.id.nav_share) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PermisosUsuariosFragment()).commit();
        }else if(id == R.id.crear){
            fragmentManager.beginTransaction().replace(R.id.contenedor, new CreateConfigurationDialogFragment()).commit();
        }else if(id == R.id.noticias){
            fragmentManager.beginTransaction().replace(R.id.contenedor, new noticiasFragment()).commit();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void checar(final String fecha) {
        databaseReference.child("Historico").orderByChild("fecha").addListenerForSingleValueEvent(new ValueEventListener() {
            boolean bandera = true;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    HistoricoSemanal rc = snapshot.getValue(HistoricoSemanal.class);
                    String fecha_celula = rc.getFecha();

                    System.out.println("Fecha historico:" + fecha_celula);
                    if(fecha.equals(fecha_celula)){
                        bandera=false;
                    }
                    System.out.println("Bandera: " +  bandera);
                }

                if (bandera==true){
                    formatoSemanal();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void formatoSemanal(){
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
                System.out.println(fecha);
                fechas[i] = fecha;
            }
            crearFormato(fechas);
            crearFormatoSubred(fechas);
        }
    }



    private void crearFormatoSubred(final String[] fechas) {
        databaseReference.child("Registro Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int total_asistencia = 0;
                double total_ofrenda = 0;

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RegistroSubred rc = snapshot.getValue(RegistroSubred.class);
                    String fecha_celula = rc.getFecha_subred();

                    for (int i = 0; i < fechas.length; i++) {
                        if (fechas[i].equals(fecha_celula)) {
                            total_asistencia = total_asistencia + rc.getAsistencia_subred();
                            total_ofrenda = total_ofrenda + rc.getOfrenda_subred();
                        }
                    }
                }

                totalSemanalSubred(total_asistencia,total_ofrenda);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void totalSemanalSubred(final int total_asistencia, final double total_ofrenda) {
        databaseReference.child("Historico Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    maxidSubred = (dataSnapshot.getChildrenCount());

                    Calendar c = Calendar.getInstance();
                    c.get(Calendar.DAY_OF_MONTH);
                    c.get(Calendar.MONTH);
                    c.get(Calendar.YEAR);
                    String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

                    HistoricoSemanalSubred hs = new HistoricoSemanalSubred();
                    hs.setId_historico(String.valueOf(maxidSubred+1));
                    hs.setTotal_asistencia(total_asistencia);
                    hs.setTotal_ofrenda(total_ofrenda);
                    hs.setFecha(fecha);

                    databaseReference.child("Historico Subred").child(hs.getId_historico()).setValue(hs);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void crearFormato(final String [] fecha) {
        databaseReference.child("Registro Celula").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int total_asistencia = 0;
                int total_invitados = 0;
                int total_ninos = 0;
                double total_ofrenda = 0;

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RegistroCelula rc = snapshot.getValue(RegistroCelula.class);
                    String fecha_celula = rc.getFecha_celula();

                    for (int i = 0; i < fecha.length; i++) {
                        if (fecha[i].equals(fecha_celula)) {
                            total_asistencia = total_asistencia + rc.getAsistencia_celula();
                            total_invitados = total_invitados + rc.getInvitados_celula();
                            total_ninos = total_ninos + rc.getNinos_celula();
                            total_ofrenda = total_ofrenda + rc.getOfrenda_celula();
                        }
                    }
                }

                totalSemanal(total_asistencia,total_invitados,total_ninos,total_ofrenda);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void totalSemanal(final int total_asistencia, final int total_invitados, final int total_ninos, final double total_ofrenda) {

        databaseReference.child("Historico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=(dataSnapshot.getChildrenCount());
                    Toast.makeText(PrincipalActivity.this, String.valueOf(maxid), Toast.LENGTH_SHORT).show();

                    Calendar c = Calendar.getInstance();
                    c.get(Calendar.DAY_OF_MONTH);
                    c.get(Calendar.MONTH);
                    c.get(Calendar.YEAR);
                    String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

                    HistoricoSemanal hs3 = new HistoricoSemanal();
                    hs3.setId_historico(String.valueOf(maxid+1));
                    hs3.setTotal_asistencia(total_asistencia);
                    hs3.setTotal_invitados(total_invitados);
                    hs3.setTotal_ninos(total_ninos);
                    hs3.setTotal_ofrenda(total_ofrenda);
                    hs3.setFecha(fecha);

                    databaseReference.child("Historico").child(hs3.getId_historico()).setValue(hs3);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Bundle bundle = new Bundle();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


        c.setFirstDayOfWeek(Calendar.TUESDAY);

        Calendar c2 = Calendar.getInstance();
        c2.setFirstDayOfWeek(Calendar.TUESDAY);

        int numero_semana_original = c2.get(Calendar.WEEK_OF_YEAR);
        int numero_semana = c.get(Calendar.WEEK_OF_YEAR);


        System.out.println("El numero de la semana es: " + numero_semana);
        System.out.println("El numero de la semana de hoy es: " + numero_semana_original);

        if (numero_semana_original == numero_semana) {
            System.out.println(currentDateString);
            if (resvar == 1) {
                RegistroSubredFragment.setFecha(currentDateString);
            }
            if (resvar == 2) {
                RegistroCelulaFragment.setFecha(currentDateString);
            }

        } else {
            Toast.makeText(PrincipalActivity.this, "La fecha de la semana no coincide con el de hoy", Toast.LENGTH_LONG).show();
        }

        if (resvar == 3) {
            HistoricoDiarioFragment.setFecha(currentDateString);
        }
    }
}
