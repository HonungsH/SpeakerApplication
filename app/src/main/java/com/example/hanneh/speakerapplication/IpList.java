package com.example.hanneh.speakerapplication;

import java.util.ArrayList;

/**
 * Created by hanneh on 3/20/18.
 */

    public final class IpList {
        static ArrayList<String> ipList;


        //Communicate with server to get active IPs
    //Plats 0 i IPLIST == id 0

    public static String[] getIpList() {

        return new String[]  {
                "172.25.9.38",      //id == 0
                "172.25.13.200",    //id == 1
                "172.25.14.27"      //id == 2
        };
    }


    //Add IPs to an ArrayList with ID as index
    public void addIPwithID(int id){
        ipList.add(id, "IP here");
    }

    public ArrayList<String> getIDList(){
        return ipList;
    }

    public static String getIP(int id){
        return getIpList()[id];
    }
}