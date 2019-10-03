package com.david.redcristianauno;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;


public class MenuFragment extends Fragment {
    private ViewFlipper vfliper;
    private Button ibizq, ibder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_menu, container, false);

        int images[] = {R.drawable.flor, R.drawable.tuli, R.drawable.paloma};

        vfliper = (ViewFlipper) view.findViewById(R.id.slideNoticias);

        ibizq = (Button) view.findViewById(R.id.ibizq);
        ibder = (Button) view.findViewById(R.id.ibder);


        for (int image : images){
            fliperImages(image);
        }

        ibder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vfliper.showNext();
            }
        });

        ibizq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vfliper.showPrevious();
            }
        });

        return view;
    }
    public void fliperImages(int image){
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(image);

        vfliper.addView(imageView);
        vfliper.setFlipInterval(4000);
        vfliper.setAutoStart(true);
        vfliper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Soy una imagen", Toast.LENGTH_SHORT).show();
            }
        });

        //animation
        vfliper.setInAnimation(getActivity(), android.R.anim.slide_in_left);
        //vfliper.setInAnimation(getActivity(), android.R.anim.slide_out_right);

    }

}
