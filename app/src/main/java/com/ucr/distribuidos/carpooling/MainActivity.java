package com.ucr.distribuidos.carpooling;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);

    }
}
