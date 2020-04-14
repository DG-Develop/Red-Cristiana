package com.david.redcristianauno;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.david.redcristianauno.Firestore.InsertarDatos;
import com.david.redcristianauno.Firestore.LeerDatos;
import com.david.redcristianauno.POJOs.RegistroSubred;
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


public class RegistroSubredFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private Spinner sp1,sp2;
    private EditText etAsistencia, etOfrenda;
    public static EditText etFecha;
    private Button btnEnviar;
    private ImageButton btnFecha;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> listaSubred = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterSubred;

    private String idUsuario;
    public static String correo_usuario = "";
    private InsertarDatos inda = new InsertarDatos();
    private LeerDatos l = new LeerDatos();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registro_subred,container,false);

        sp1 = (Spinner) view.findViewById(R.id.spinner_registro_subred);
        sp2 = (Spinner) view.findViewById(R.id.spinner_centavos_subred);
        etAsistencia = (EditText) view.findViewById(R.id.txt_asistencia_subred);
        etOfrenda = (EditText) view.findViewById(R.id.txt_ofrenda_subred);
        etFecha = (EditText) view.findViewById(R.id.txt_date);
        btnEnviar = (Button) view.findViewById(R.id.btnEnviarSubred);
        btnFecha = (ImageButton) view.findViewById(R.id.ibFechaSubred);


        etFecha.setFocusable(false);

        String [] centavos= {".00",".50"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_david,centavos);
        sp2.setAdapter(adapter);

        correo_usuario = Preferences.obtenerPreferencesString(getActivity(), Preferences.PREFERENCES_USUARIO_LOGIN);

        inicializarFirebase();
        listarSubred();
        verUsuario(correo_usuario);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha_subred();
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAsistencia.getText().toString().isEmpty() || etFecha.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor llenar los campos de asistencia y de fecha", Toast.LENGTH_SHORT).show();
                }else{
                    //registrarDatosSubred();
                    registrarDatos();
                }
            }
        });
        return view;
    }

    public void registrarDatos(){
        final String centavos = sp2.getSelectedItem().toString();
        String opc_ofrenda = etOfrenda.getText().toString();
        String res = opc_ofrenda.concat(centavos);

        int asistencia = Integer.parseInt(etAsistencia.getText().toString());
        double ofrenda;
        if(etOfrenda.getText().toString().isEmpty()){
            ofrenda = 0.0;
        }else{
            ofrenda = Double.parseDouble(res);
        }
        String fecha = etFecha.getText().toString();

        RegistroSubred rs = new RegistroSubred();
        rs.setId_usuario(Preferences.obtenerPreferencesString(getContext(), Preferences.PREFERENCES_ID_USUARIO));
        rs.setAsistencia_subred(asistencia);
        rs.setOfrenda_subred(ofrenda);
        rs.setFecha_subred(fecha);

        l.leerUsuarioRegistroSubred(rs, Preferences.obtenerPreferencesString(getContext(),Preferences.PREFERENCES_ID_USUARIO));

        Toast.makeText(getActivity(), "Registrado Correctamente", Toast.LENGTH_SHORT).show();
        limpiarCampos();

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


    private void limpiarCampos() {
        etAsistencia.setText("");
        etOfrenda.setText("");
        etFecha.setText("");
    }
    public static void setFecha(String fecha){
       etFecha.setText(fecha);
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
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void obtenerFecha_subred(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getActivity().getSupportFragmentManager(),"date picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        etFecha.setFocusable(false);
    }

}
