package com.david.redcristianauno;

import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.david.redcristianauno.Clases.Utilidades;
import com.david.redcristianauno.Historico.HistoricoDiarioFragment;
import com.david.redcristianauno.Historico.HistoricoSemanalFragment;
import com.david.redcristianauno.adapters.SeccionesAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;


public class HistoricoFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_historical,container,false);

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
