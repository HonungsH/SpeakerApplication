package com.example.hanneh.speakerapplication;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import me.ithebk.barchart.BarChart;
import me.ithebk.barchart.BarChartModel;

public class EqualizerTab2 extends Fragment {
    TextView text;
    List<BarChartModel> barChartModelList;
    List<BarChartModel> barChartFrequencyBands;
    int[] frequencyBands;

public EqualizerTab2(){



}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup group, @Nullable Bundle savedstate){

        View rootView = inflater.inflate(R.layout.fragment_tab2, group, false);


        text = rootView.findViewById(R.id.section_label2);
        text.setText("TRYING STUFF HEHEHEHEHE");

        //Create a list with the Barchartmodels
        barChartModelList = new ArrayList<>();
        barChartFrequencyBands = new ArrayList<>();
        frequencyBands = new int[] {63, 125, 250, 500, 1000, 2000, 4000, 8000, 16000};

        BarChart barChartUpper = (BarChart) rootView.findViewById(R.id.bar_chart_vertical);
        barChartUpper.setBarMaxValue(100);

        BarChart barChartLower = rootView.findViewById(R.id.bar_chart_bands);
        barChartLower.setBarMaxValue(10);

        //for loop for adding upper bars
        for (int i = 1; i < 10; i++){
            BarChartModel barChar = new BarChartModel();
            barChar.setBarValue(70);
         //   barChar.setBarColor(Color.parseColor("#070707"));
            barChar.setBarTag(null); //You can set your own tag to bar model
            barChar.setBarText(String.valueOf(i) + "dB");
            barChartModelList.add(barChar);
        }

        //Add all the created bars to the list
        barChartUpper.addBar(barChartModelList);

        //for loop for adding lower bars
        for (int j = 1; j < 10; j++){
            BarChartModel barChar2 = new BarChartModel();
            barChar2.setBarValue(0);
            barChar2.setBarColor(Color.parseColor("#ffffff"));

            if(frequencyBands[j-1] >= 1000){
                String band = String.valueOf(frequencyBands[j-1]);
               band = band.replaceAll("000", "K");
                barChar2.setBarText(band + '\n' + "Hz");
            }
            else{
            barChar2.setBarText(String.valueOf(frequencyBands[j-1]) + '\n' +"Hz");

        }
            barChartFrequencyBands.add(barChar2);

        }




        barChartLower.addBar(barChartFrequencyBands);





        return rootView;
    }
}
