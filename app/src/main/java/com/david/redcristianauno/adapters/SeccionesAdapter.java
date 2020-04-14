package com.david.redcristianauno.adapters;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class SeccionesAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> listaFrgaments = new ArrayList<>();
    private final List<String> listaTitulos = new ArrayList<>();

    public SeccionesAdapter(FragmentManager fm) {
        super(fm);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return listaFrgaments.get(i);
    }


    @Override
    public int getCount() {
        return listaFrgaments.size();
    }

    public void addFragment(Fragment fragment, String titulo){

        listaFrgaments.add(fragment);
        listaTitulos.add(titulo);

    }

    private boolean mIsUpdating = false;

    public void setIsUpdating(boolean mIsUpdating){
        this.mIsUpdating = mIsUpdating;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(mIsUpdating){
            return POSITION_NONE;
        }else{
            return super.getItemPosition(object);
        }
    }
}
