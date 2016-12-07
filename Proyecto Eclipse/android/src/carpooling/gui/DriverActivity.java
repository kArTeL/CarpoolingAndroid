package carpooling.gui;

import java.util.logging.Level;

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
import chat.client.gui.R;

/**
 * This activity implement the chat interface.
 * 
 * @authors
 */

public class DriverActivity extends Activity {
	private Logger logger = Logger.getJADELogger(this.getClass().getName());

	static final int PARTICIPANTS_REQUEST = 0;


	private String nickname;
	private CarInterface carInterface;

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
			showAlertDialog(getString(R.string.msg_interface_exc), true);
		} catch (ControllerException e) {
			showAlertDialog(getString(R.string.msg_controller_exc), true);
		}


		setContentView(R.layout.driver);

		Button button = (Button) findViewById(R.id.button_send);
		button.setOnClickListener(buttonSendListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		logger.log(Level.INFO, "Destroy activity!");
	}

	private OnClickListener buttonSendListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				carInterface.addRide("Alajuela","Coronado","11:00","12:00",2, 1500);
			} catch (O2AException e) {
				showAlertDialog(e.getMessage(), false);
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

	private void showAlertDialog(String message, final boolean fatal) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DriverActivity.this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialog, int id) {
								dialog.cancel();
								if(fatal) finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();		
	}
}
