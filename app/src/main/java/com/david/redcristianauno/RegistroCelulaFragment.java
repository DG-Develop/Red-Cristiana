package com.david.redcristianauno;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.redcristianauno.Firestore.InsertarDatos;
import com.david.redcristianauno.POJOs.Celula;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.Subred;
import com.david.redcristianauno.POJOs.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class RegistroCelulaFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private EditText etAsistencia, etInvitados,etNiños, etOfrenda, etDireccion, etAnfitrion;
    public static EditText etFecha;
    private Spinner sp1,sp2,sp3;
    private Button btnEnviar, btnVerDireccion;
    private ImageButton btnFecha;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> listaSubred = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterSubred;
    private List<String> listaCelula = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterCelula;

    private String idUsuario;
    private String idCelula;
    private String idSubred;
    public static String correo_usuario = "";

    private InsertarDatos inda = new InsertarDatos();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_celula, container,false);

        etAsistencia = (EditText)view.findViewById(R.id.txt_asistencia);
        etInvitados = (EditText)view.findViewById(R.id.txt_invitados);
        etAnfitrion = (EditText)view.findViewById(R.id.txtnombre_anfitrion);
        etDireccion = (EditText)view.findViewById(R.id.txtDireccion);
        etNiños = (EditText)view.findViewById(R.id.txt_niños);
        etOfrenda = (EditText)view.findViewById(R.id.txt_ofrenda);
        sp1 = (Spinner)view.findViewById(R.id.spinner_nombres);
        sp2 = (Spinner)view.findViewById(R.id.spinner_cedulas);
        sp3 = (Spinner)view.findViewById(R.id.spinner_centavos_celula);
        etFecha = (EditText) view.findViewById(R.id.et_mostrar_fecha_picker);
        btnEnviar = (Button)view.findViewById(R.id.btnEnviar);
        btnFecha = (ImageButton) view.findViewById(R.id.ib_obtener_fecha_celula);
        btnVerDireccion = view.findViewById(R.id.btnverDireccion);

        etFecha.setFocusable(false);

        String [] centavos= {".00",".50"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_david,centavos);
        sp3.setAdapter(adapter);

        correo_usuario = Preferences.obtenerPreferencesString(getActivity(), Preferences.PREFERENCES_USUARIO_LOGIN);

        inicializarFirebase();
        listarSubred();

        verUsuario(correo_usuario);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final String nombres = sp1.getSelectedItem().toString();
                    verSubred(nombres);
                    listarCelula();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "No has seleccionado nada", Toast.LENGTH_SHORT).show();
            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                verCelula(sp2.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAsistencia.getText().toString().isEmpty() || etFecha.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Por favor llenar los campos de asistencia y de fecha", Toast.LENGTH_SHORT).show();
                }else{
                    //registrarDatosCelula();
                    registrarDatos();
                }
            }
        });

        btnVerDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultar();
            }
        });
        return view;
    }

    public void registrarDatos(){
        final String centavos = sp3.getSelectedItem().toString();
        String opc_ofrenda = etOfrenda.getText().toString();
        String res =opc_ofrenda.concat(centavos);

        int asistencia = Integer.parseInt(etAsistencia.getText().toString());
        int invitados;
        if(etInvitados.getText().toString().isEmpty()){
            invitados = 0;
        }else{
            invitados = Integer.parseInt(etInvitados.getText().toString());
        }
        int ninos;
        if(etNiños.getText().toString().isEmpty()){
            ninos = 0;
        }else{
            ninos = Integer.parseInt(etNiños.getText().toString());
        }
        double ofrenda;
        if (etOfrenda.getText().toString().isEmpty()) {
            ofrenda=0.0;
        }else {
            ofrenda = Double.parseDouble(res);
        }

        if(asistencia < invitados || asistencia < ninos){
            Toast.makeText(getContext(), "El valor de la asistencia no debe de ser menor que el de invitados o niños", Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "No se registraron los datos", Toast.LENGTH_SHORT).show();
        }else {
            RegistroCelula rc = new RegistroCelula();
            rc.setNombre_anfitrion(etAnfitrion.getText().toString().trim());
            if(pusoDireccion()){
                rc.setDomicilio(etDireccion.getText().toString().trim());
            }else{
                rc.setDomicilio(sp2.getSelectedItem().toString());
            }
            rc.setAsistencia_celula(asistencia);
            rc.setInvitados_celula(invitados);
            rc.setNinos_celula(ninos);
            rc.setOfrenda_celula(ofrenda);
            rc.setFecha_celula(etFecha.getText().toString().trim());

            inda.crearRegistroCelula(rc,Preferences.obtenerPreferencesString(getContext(), Preferences.PREFERENCES_ID_USUARIO));
            Toast.makeText(getContext(), "Regitrado Correctamente", Toast.LENGTH_SHORT).show();
        }

        limpiarCampos();
    }

    private void ocultar() {
        etDireccion.setVisibility(View.VISIBLE);
        btnVerDireccion.setVisibility(View.INVISIBLE);
    }

    public String getIdSubred() {
        return idSubred;
    }

    public void setIdSubred(String idSubred) {
        this.idSubred = idSubred;
    }

    public void verSubred(final String nombre){
        databaseReference.child("Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Subred sb = snapshot.getValue(Subred.class);

                    if(nombre.equals(sb.getNombre_subred())){
                        setIdSubred(sb.getId_subred());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    private void verUsuario(final String correo_usuario){
        databaseReference.child("Usuario").orderByChild("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);

                    if(correo_usuario.equals(u.getCorreo())){
                        setIdUsuario(u.getId_usuario());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getIdCelula() {
        return idCelula;
    }

    public void setIdCelula(String idCelula) {
        this.idCelula = idCelula;
    }

    private void verCelula(final String nombre_celula){
        databaseReference.child("Celula").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Celula c = snapshot.getValue(Celula.class);

                    if(nombre_celula.equals(c.getNombre_celula())){
                        setIdCelula(c.getId_celula());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void registrarDatosCelula(){
        final String centavos = sp3.getSelectedItem().toString();
        String opc_ofrenda = etOfrenda.getText().toString();
        String res =opc_ofrenda.concat(centavos);

        int asistencia = Integer.parseInt(etAsistencia.getText().toString());
        int invitados;
        if(etInvitados.getText().toString().isEmpty()){
            invitados = 0;
        }else{
            invitados = Integer.parseInt(etInvitados.getText().toString());
        }
        int ninos;
        if(etNiños.getText().toString().isEmpty()){
            ninos = 0;
        }else{
            ninos = Integer.parseInt(etNiños.getText().toString());
        }
        double ofrenda;
        if (etOfrenda.getText().toString().isEmpty()) {
            ofrenda=0.0;
        }else {
            ofrenda = Double.parseDouble(res);
        }

        if(asistencia < invitados || asistencia < ninos){
            Toast.makeText(getContext(), "El valor de la asistencia no debe de ser menor que el de invitados o niños", Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "No se registraron los datos", Toast.LENGTH_SHORT).show();
        }else {
            RegistroCelula rc = new RegistroCelula();
            rc.setId_registroCelula(UUID.randomUUID().toString());
            rc.setId_usuario(getIdUsuario());
            if(pusoDireccion()){
                rc.setDomicilio(etDireccion.getText().toString().trim());
            }else{
                rc.setDomicilio(sp2.getSelectedItem().toString());
            }
            rc.setNombre_anfitrion(etAnfitrion.getText().toString().trim());
            rc.setAsistencia_celula(asistencia);
            rc.setInvitados_celula(invitados);
            rc.setNinos_celula(ninos);
            rc.setOfrenda_celula(ofrenda);
            rc.setFecha_celula(etFecha.getText().toString().trim());

            databaseReference.child("Registro Celula").child(rc.getId_registroCelula()).setValue(rc);
            Toast.makeText(getContext(), "Regitrado Correctamente", Toast.LENGTH_SHORT).show();
        }

        limpiarCampos();
    }

    public boolean pusoDireccion(){
        boolean bandera = true;

        if(etDireccion.getText().toString().isEmpty()){
            bandera = false;
        }

        return bandera;
    }

    private void limpiarCampos() {
        etAsistencia.setText("");
        etInvitados.setText("");
        etNiños.setText("");
        etOfrenda.setText("");
        etFecha.setText("");
    }
    public static void setFecha(String fecha){
        etFecha.setText(fecha);
    }

    private void listarCelula() {
        databaseReference.child("Celula").orderByChild("nombre_celula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listaCelula.clear();
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Celula cl = snapshot.getValue(Celula.class);
                    String nombre_subred = cl.getId_subred();

                    if (getIdSubred().equals(nombre_subred)) {
                        String nombre_celula = cl.getNombre_celula();

                        listaCelula.add(nombre_celula);
                    }

                    arrayAdapterCelula = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_david, listaCelula);
                    sp2.setAdapter(arrayAdapterCelula);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listarSubred() {

        databaseReference.child("Subred").orderByChild("nombre_subred").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaSubred.clear();
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    databaseReference.child("Subred").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Subred s = snapshot.getValue(Subred.class);
                            String nombre_subred = s.getNombre_subred();

                                listaSubred.add(nombre_subred);

                                arrayAdapterSubred = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_david,listaSubred);
                                sp1.setAdapter(arrayAdapterSubred);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void obtenerFecha(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getActivity().getSupportFragmentManager(),"date picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        etFecha.setFocusable(false);
    }
}
