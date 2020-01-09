package com.david.redcristianauno.adapters;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


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
