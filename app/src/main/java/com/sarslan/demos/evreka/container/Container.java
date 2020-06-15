package com.sarslan.demos.evreka.container;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.sarslan.demos.evreka.R;
import com.sarslan.demos.evreka.util.MyThread;
import com.sarslan.demos.evreka.util.ThreadManager;
import com.sarslan.demos.evreka.util.ThreadUpdateObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import kotlin.jvm.Transient;


public class Container implements Serializable, ThreadUpdateObject {

    //public Integer uuid;
    public Integer containerId;
    public Integer sensorId;
    public Integer occupancyRate;
    public Integer temperature;
    public String dateOfData;
    public Double latitude;
    public Double longitude;

    @Transient
    private static final float UPDATE_SHOW_TIME = 5;
    @Transient
    private boolean isTimeReached;
    @Transient
    private float timeElapsed;
    @Transient
    private MyThread intersTimer;
    @Transient
    private ContainerListener containerListener;
    @Transient
    public  static  final String dataRef = "containers";
    @Transient
    public static final LatLng defPos = new LatLng(39.891388,32.784721);
    @Transient
    private Marker marker;
    //public geoPoint;

    public  Container(){

    }
    public Container(Integer containerId,Integer sensorId,Integer occupancyRate,
                     Integer temperature, String dateOfData, Double latitude, Double longitude)
    {
        this.containerId=containerId;
        this.sensorId=sensorId;
        this.occupancyRate=occupancyRate;
        this.temperature=temperature;
        this.dateOfData=dateOfData;
        this.latitude=latitude;
        this.longitude=longitude;

        intersTimer = ThreadManager.instance.create("contTimer"+containerId, this, 1000);
        intersTimer.start();
    }
    public Container(Integer containerId)
    {
        this(containerId, containerId,0,0,"",0.0d,0.0d);
        occupancyRate = getRandomInt(0,100);
        temperature = getRandomInt(0,100);
        dateOfData = getDateStr();
        latitude = getRandomGeoPoint(defPos.latitude);
        longitude = getRandomGeoPoint(defPos.longitude);

    }
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("containerId", containerId);
        result.put("sensorId", sensorId);
        result.put("occupancyRate", occupancyRate);
        result.put("temperature", temperature);
        result.put("dateOfData", dateOfData);

        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
    @Exclude
    @NonNull
    @Override
    public String toString() {
        return "containerId :"+containerId
                +"\nsensorId: "+sensorId
                +"\noccupancyRate: "+occupancyRate
                +"\ntemperature: "+temperature
                +"\ndateOfData: "+dateOfData;

    }
    //timer update
    @Exclude
    @Override
    public void update(float dt) {
        if(isTimeReached)
            return;
        timeElapsed+=dt;
        if(timeElapsed>UPDATE_SHOW_TIME){
            timeElapsed = 0;
            isTimeReached = false;
            updateDb();
        }

    }
    @Exclude
    private void updateDb() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(dataRef);
        final Map<String, Object> dataMap = new HashMap<String, Object>();

        this.occupancyRate = getRandomInt(0,100);
        this.temperature = getRandomInt(0,100);
        this.dateOfData=getDateStr();

        dataMap.put(containerId.toString(), toMap());

        ref.updateChildren(dataMap);
    }
    @Exclude
    private String getDateStr(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    @Exclude
    private int getRandomInt(int min, int max){
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
    @Exclude
    private Double getRandomGeoPoint(double p){
        double range = 0.0001;
        return p+getRndmDouble(-range,range);
    }
    @Exclude
    private double getRndmDouble(double rangeMin, double rangeMax){
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }
    @Exclude
    public void setContainerListener(ContainerListener containerListener){
        this.containerListener = containerListener;
    }
    @Exclude
    public void onContainerUpdatedByDB(Container conDB) {
        this.containerId=conDB.containerId;
        this.sensorId=conDB.sensorId;
        this.occupancyRate=conDB.occupancyRate;
        this.temperature=conDB.temperature;
        this.dateOfData=conDB.dateOfData;
        this.latitude=conDB.latitude;
        this.longitude=conDB.longitude;

        if(containerListener!=null)
            containerListener.onContainerUpdateByDB(this);
        if(marker!=null)
            marker.setIcon(getIconFromRate());
    }
    @Exclude
    private BitmapDescriptor getIconFromRate(){
        if(occupancyRate<30)
            return BitmapDescriptorFactory.fromResource(R.mipmap.mrkr_red);
        if(occupancyRate<60)
            return BitmapDescriptorFactory.fromResource(R.mipmap.mrkr_blue);
        return BitmapDescriptorFactory.fromResource(R.mipmap.mrkr_dblue);
    }
    @Exclude
    public void setMarker(Marker marker) {
        this.marker = marker;
    }
    @Exclude
    public Marker getMarker() {
        return marker;
    }


}
