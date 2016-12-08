package carpooling.gui;

import java.util.logging.Level;

import Util.AlertUtil;
import jade.core.MicroRuntime;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.O2AException;
import jade.wrapper.StaleProxyException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import carpooling.agent.CarInterface;
import carpooling.agent.ChatClientInterface;
import carpooling.agent.PassengerInterface;
import carpooling.gui.PassangerResponse;
import carpooling.gui.R;
import carpooling.gui.CarResponse;

/**
 * This activity implement the chat interface.
 * 
 * @authors
 */

public class DriverActivity extends Activity implements CarResponse {
	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	static final int PARTICIPANTS_REQUEST = 0;

    
	private String nickname;
	private CarInterface carInterface;

	EditText destinyEditText;
	EditText originEditText;
	EditText departureTimeEditText;
	EditText arrivalTimeEditText;
	EditText priceEditText;
	EditText freeSeatsEditText;
	
	//String message = destinyField.getText().toString();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			nickname = extras.getString("nickname");
		}

		try {
			carInterface = MicroRuntime.getAgent(nickname)
					.getO2AInterface(CarInterface.class);
			if (carInterface == null)
			{
				AgentController controller = MicroRuntime.getAgent(nickname);
				logger.println("skljfsf");
				
			}
		} catch (StaleProxyException e) {
			AlertUtil.showDialog(getString(R.string.msg_interface_exc),this);
			//showAlertDialog(getString(R.string.msg_interface_exc), true);
		} catch (ControllerException e) {
			AlertUtil.showDialog(getString(R.string.msg_interface_exc),this);
			//showAlertDialog(getString(R.string.msg_controller_exc), true);
		}

		carInterface.setDelegate(this);

		setContentView(R.layout.driver);
		this.initUI();
		Button button = (Button) findViewById(R.id.button_send);
		button.setOnClickListener(buttonSendListener);
	}

	private void initUI()
	{
		destinyEditText =  (EditText) findViewById(R.id.editDestiny);
		originEditText =  (EditText) findViewById(R.id.editOrigin);
		departureTimeEditText =  (EditText) findViewById(R.id.editDeparture);
		arrivalTimeEditText =  (EditText) findViewById(R.id.editArrival);
		priceEditText =  (EditText) findViewById(R.id.editPrice);
		freeSeatsEditText = (EditText) findViewById(R.id.editFreeSeats);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		logger.log(Level.INFO, "Destroy activity!");
	}

	private OnClickListener buttonSendListener = new OnClickListener() {
		public void onClick(View v) {
			if (DriverActivity.this.validateData() == true)
			{
				
				try {
					carInterface.addRide(DriverActivity.this.originEditText.getText().toString(),
							DriverActivity.this.destinyEditText.getText().toString(),
							DriverActivity.this.departureTimeEditText.getText().toString(),
							DriverActivity.this.arrivalTimeEditText.getText().toString(),
							Integer.parseInt(DriverActivity.this.freeSeatsEditText.getText().toString()), 
							Integer.parseInt(DriverActivity.this.priceEditText.getText().toString()));
				} catch (O2AException e) {
					AlertUtil.showDialog(e.getMessage(), DriverActivity.this);
				}
			}
			
//			EditText destinyField =  (EditText) findViewById(R.id.editDestiny);
//			String message = destinyField.getText().toString();
//			if (message != null && !message.equals("")) {
//				
//			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.chat_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PARTICIPANTS_REQUEST) {
			if (resultCode == RESULT_OK) {
				// TODO: A partecipant was picked. Send a private message.
			}
		}
	}

	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}



	@Override
	public void onCarStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateRides(String msg) {
		// TODO Auto-generated method stub
		AlertUtil.showDialog(msg,this);
	}

	@Override
	public void onCarResponse(String msg) {
		// TODO Auto-generated method stub
		AlertUtil.showDialog(msg,this);
	}
	
	public boolean validateData()
	{
		boolean returnValue = true;
		if (this.destinyEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese el lugar de destino",this);
			returnValue = false;
		}
		else if (this.originEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese el lugar de origen",this);
			returnValue = false;
		}
		else if (this.departureTimeEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese la hora de partida",this);
			returnValue = false;
		}
		else if (this.arrivalTimeEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese la hora de llegada",this);
			returnValue = false;
		}
		else if (this.priceEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese el precio",this);
			returnValue = false;
		}
		else if (this.freeSeatsEditText.getText().toString().isEmpty())
		{
			AlertUtil.showDialog("Por favor ingrese los asientos disponibles",this);
			returnValue = false;
		}
		return returnValue;
	}
	
}
