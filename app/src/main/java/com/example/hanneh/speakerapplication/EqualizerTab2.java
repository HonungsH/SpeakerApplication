package com.example.hanneh.speakerapplication;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class EqualizerTab2 extends Fragment {
    TextView text;

public EqualizerTab2(){



}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle savedstate){

        View rootView = inflater.inflate(R.layout.fragment_tab2, group, false);


        text = rootView.findViewById(R.id.section_label2);
        text.setText("TRYING STUFF HEHEHEHEHE");


        return rootView;
    }
}
