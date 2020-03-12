package com.david.redcristianauno;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.david.redcristianauno.Clases.Utilidades;
import com.david.redcristianauno.FragmentsSuperusuario.LideresCelulaFragment;
import com.david.redcristianauno.FragmentsSuperusuario.NormalFragment;
import com.david.redcristianauno.FragmentsSuperusuario.SuperUserFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserGeneralFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserRedFragment;
import com.david.redcristianauno.FragmentsSuperusuario.UserSuberdFragment;
import com.david.redcristianauno.adapters.SeccionesAdapter;



public class PermisosUsuariosFragment extends Fragment {


    //Tabbed o viewPager
    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    SeccionesAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_permisos_usuarios, container, false);


        //poner el viewPager
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
        adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragment(new UserGeneralFragment(),  "Lista usuarios");
        adapter.addFragment(new NormalFragment(),  "Usuarios normales");
        adapter.addFragment(new LideresCelulaFragment(),  "Lideres de Celula");
        adapter.addFragment(new UserSuberdFragment(),  "Usuarios subred");
        adapter.addFragment(new UserRedFragment(),  "Usuarios red");
        adapter.addFragment(new SuperUserFragment(),  "Super Usuarios");

        viewPager.setAdapter(adapter);
    }

   /* public  void actualizar(){
        adapter.setIsUpdating(true);
        adapter.notifyDataSetChanged();
        adapter.setIsUpdating(false);
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(Utilidades.rotacion==0){
            appBar.removeView(pestanas);
        }

    }
}
