package com.ucr.distribuidos.carpooling;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jade.android.AndroidHelper;
import jade.android.MicroRuntimeService;
import jade.android.MicroRuntimeServiceBinder;
import jade.android.RuntimeCallback;
import jade.core.MicroRuntime;
import jade.core.Profile;

import com.ucr.distribuidos.carpooling.Model.PassengerAgent;
import com.ucr.distribuidos.carpooling.Utils.AlertUtils;
import com.ucr.distribuidos.carpooling.Utils.LogUtils;
import com.ucr.distribuidos.carpooling.Utils.StringUtils;


import java.util.logging.Level;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class MainActivity extends AppCompatActivity {
    private Logger logger = Logger.getJADELogger(this.getClass().getName());
    final String _host = "127.0.0.1";
    final String _port = "80";
    private MicroRuntimeServiceBinder microRuntimeServiceBinder;
    private ServiceConnection serviceConnection;

//    @InjectView(R.id.login_button) Button _loginButton;
//    @InjectView(R.id.progress)
//    ProgressBar _progressBar;
//    @InjectView(R.id.register_button)
//    TextView _registerButton;
//
//    @InjectView(R.id.edit_text_email)EditText _editTextEmail;
//    @InjectView(R.id.edit_text_password) EditText _editTextPassword;

    @BindView(R.id.searchTripButton)
    Button _searchTripButton;

    @BindView(R.id.input_to_location)
    EditText _toLocation;

    @BindView(R.id.input_from_location)
    EditText _fromLocation;

    @BindView(R.id.input_arrival_time)
    EditText _arrivalTime;

    @BindView(R.id.input_departure_time)
    EditText _departureTime;

    private PassengerAgent _passengerAgent;

    private RuntimeCallback<AgentController> agentStartupCallback = new RuntimeCallback<AgentController>() {
        @Override
        public void onSuccess(AgentController agent) {
        }

        @Override
        public void onFailure(Throwable throwable) {
            LogUtils.LOGD("Main activity", "something failed");
            //myHandler.postError(getString(R.string.msg_nickname_in_use));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ACLMessage subscription = new ACLMessage(ACLMessage.SUBSCRIBE);

    }


    public boolean isValidTrip()
    {
        boolean returnValue = true;

        if (StringUtils.isEmpty(this._toLocation.getText().toString()) )
        {
            AlertUtils.showErrorDialog(R.string.app_name,R.string.empty_to_location,this);
            returnValue = false;
        }
        else if (StringUtils.isEmpty(this._fromLocation.getText().toString()) )
        {
            AlertUtils.showErrorDialog(R.string.app_name,R.string.empty_from_location,this);
            returnValue = false;
        }
        else if (StringUtils.isEmpty(this._departureTime.getText().toString()) )
        {
            AlertUtils.showErrorDialog(R.string.app_name,R.string.empty_Departure_time,this);
            returnValue = false;
        }
        else if (StringUtils.isEmpty(this._arrivalTime.getText().toString()) )
        {
            AlertUtils.showErrorDialog(R.string.app_name,R.string.empty_arrival_time,this);
            returnValue = false;
        }

        return returnValue;
    }


    @OnClick(R.id.searchTripButton)
    public void onSearchTripBtnClicked(View view){
        if (this.isValidTrip())
        {
            this.setupJade(agentStartupCallback);

        }
        else
        {
            Log.println(Log.ERROR,"main activity", "fallo");
        }
    }

    private void setupJade(final RuntimeCallback<AgentController> agentStartupCallback)
    {
        final Properties profile = new Properties();
        profile.setProperty(Profile.MAIN_HOST, _host);
        profile.setProperty(Profile.MAIN_PORT, _port);
        profile.setProperty(Profile.MAIN, Boolean.FALSE.toString());
        profile.setProperty(Profile.JVM, Profile.ANDROID);
        if (AndroidHelper.isEmulator()) {
            // Emulator: this is needed to work with emulated devices
            profile.setProperty(Profile.LOCAL_HOST, AndroidHelper.LOOPBACK);
        } else {
            profile.setProperty(Profile.LOCAL_HOST,
                    AndroidHelper.getLocalIPAddress());
        }

        // Emulator: this is not really needed on a real device
        profile.setProperty(Profile.LOCAL_PORT, "2000");

        if (microRuntimeServiceBinder == null) {
            serviceConnection = new ServiceConnection() {
                public void onServiceConnected(ComponentName className,
                                               IBinder service) {
                    microRuntimeServiceBinder = (MicroRuntimeServiceBinder) service;
                    logger.log(Level.INFO, "Gateway successfully bound to MicroRuntimeService");
                    startContainer(profile, agentStartupCallback);
                };

                public void onServiceDisconnected(ComponentName className) {
                    microRuntimeServiceBinder = null;
                    logger.log(Level.INFO, "Gateway unbound from MicroRuntimeService");
                }
            };
            logger.log(Level.INFO, "Binding Gateway to MicroRuntimeService...");
            bindService(new Intent(getApplicationContext(),
                            MicroRuntimeService.class), serviceConnection,
                    Context.BIND_AUTO_CREATE);
        } else {
            logger.log(Level.INFO, "MicroRumtimeGateway already binded to service");
            startContainer(profile, agentStartupCallback);
        }
//        _passengerAgent = new PassengerAgent(_fromLocation.getText().toString(),_toLocation.getText().toString(),_departureTime.getText().toString(),_arrivalTime.getText().toString());
//        _passengerAgent.askForRide();
    }


    private void startContainer(Properties profile,
                                final RuntimeCallback<AgentController> agentStartupCallback) {
        if (!MicroRuntime.isRunning()) {
            microRuntimeServiceBinder.startAgentContainer(profile,
                    new RuntimeCallback<Void>() {
                        @Override
                        public void onSuccess(Void thisIsNull) {
                            logger.log(Level.INFO, "Successfully start of the container...");
                            startAgent(agentStartupCallback);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            logger.log(Level.SEVERE, "Failed to start the container...");
                        }
                    });
        } else {
            startAgent(agentStartupCallback);
        }
    }

    private void startAgent(
                            final RuntimeCallback<AgentController> agentStartupCallback) {
        microRuntimeServiceBinder.startAgent(Profile.LOCAL_HOST,
                PassengerAgent.class.getName(),
                new Object[] { getApplicationContext() },
                new RuntimeCallback<Void>() {
                    @Override
                    public void onSuccess(Void thisIsNull) {
                        logger.log(Level.INFO, "Successfully start of the "
                                + PassengerAgent.class.getName() + "...");
                        try {
                            agentStartupCallback.onSuccess(MicroRuntime
                                    .getAgent(Profile.LOCAL_HOST));
                        } catch (ControllerException e) {
                            // Should never happen
                            agentStartupCallback.onFailure(e);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.log(Level.SEVERE, "Failed to start the "
                                + PassengerAgent.class.getName() + "...");
                        agentStartupCallback.onFailure(throwable);
                    }
                });
    }




}
