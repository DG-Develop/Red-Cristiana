package com.david.redcristianauno;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BibliaFragment extends Fragment {
    private WebView wv1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biblia, container, false);
        wv1 = view.findViewById(R.id.wv1);
        wv1.setWebViewClient(new WebViewClient());
        wv1.loadUrl("http://www.iglesiabautistarecoleta.cl/Biblia_texto_y_audio/indice_general.htm");

        return view;
    }

}
