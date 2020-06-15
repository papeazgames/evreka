package com.sarslan.demos.evreka.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sarslan.demos.evreka.R;
import com.sarslan.demos.evreka.container.Container;
import com.sarslan.demos.evreka.container.ContainerListener;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter, ContainerListener {
    private final View v;
    private Context context;
    public MarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v =  inflater.inflate(R.layout.map_marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {


        Container con = (Container) arg0.getTag();

        TextView title = (TextView) v.findViewById(R.id.mrkr_title);
        TextView txt = (TextView) v.findViewById(R.id.mrkr_body);
        title.setText("Container : "+con.containerId.toString());
        txt.setText(con.toString());

        con.setContainerListener(this);
        return v;
    }

    @Override
    public void onContainerUpdateByDB(Container con) {

        TextView txt = (TextView) v.findViewById(R.id.mrkr_body);
        txt.setText(con.toString());
    }
}