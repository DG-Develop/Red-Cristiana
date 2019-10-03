package com.david.redcristianauno;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.david.redcristianauno.Clases.Utilidades;
import com.david.redcristianauno.FragmentsSuperusuario.LideresCelulaFragment;
import com.david.redcristianauno.FragmentsSuperusuario.NormalFragment;
import com.david.redcristianauno.FragmentsSuperusuario.SuperUserFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserGeneralFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserRedFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserSuberdFragment;
import com.david.redcristianauno.Historico.HistoricoDiarioFragment;
import com.david.redcristianauno.Historico.HistoricoSemanalFragment;
import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.adapters.SeccionesAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HistoricoFragment extends Fragment{

    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico,container,false);

        if(Utilidades.rotacion==0){

            View parent = (View) container.getParent();

            if(appBar == null){
                appBar = parent.findViewById(R.id.appBar);
                pestanas = new TabLayout(getActivity());
                pestanas.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                pestanas.setTabGravity(TabLayout.GRAVITY_CENTER);
                pestanas.setTabMode(TabLayout.MODE_SCROLLABLE);
                appBar.addView(pestanas);
                viewPager = (ViewPager) view.findViewById(R.id.view_pager_information);

                llenarViewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);
            }
            pestanas.setTabGravity(TabLayout.GRAVITY_FILL);
        }else {
            Utilidades.rotacion=1;
        }


        return view;
    }

    private void llenarViewPager(ViewPager viewPager) {
        SeccionesAdapter adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragment(new HistoricoDiarioFragment(),  "Datos Diarios");
        adapter.addFragment(new HistoricoSemanalFragment(),  "Datos Semanales");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(Utilidades.rotacion==0){
            appBar.removeView(pestanas);
        }

    }
}
