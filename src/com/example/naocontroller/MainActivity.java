package com.example.naocontroller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText server, command, port;
	Button left, right, forward, back, submit;
	TextView textview;
	RelativeLayout pad;
	ArrayList<View> touchables = new ArrayList<View>();
	RemoteTask remoteTask;
	

	String address,socket;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		server = (EditText) findViewById(R.id.server);
		port = (EditText) findViewById(R.id.port);
		command = (EditText) findViewById(R.id.command);
		left = (Button) findViewById(R.id.left);
		right = (Button) findViewById(R.id.right);
		forward = (Button) findViewById(R.id.forward);
		back = (Button) findViewById(R.id.back);
		submit = (Button) findViewById(R.id.submit);
		textview = (TextView) findViewById(R.id.textview);
		pad = (RelativeLayout) findViewById(R.id.Pad);
		touchables = pad.getTouchables();
		

		for (View touchable : touchables) {
			if (touchable instanceof Button)
				((Button) touchable).setEnabled(false);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClicked(View view) {
		remoteTask = new RemoteTask(view.getContext(),address,socket);
		switch (view.getId()) {
		case R.id.left:
			command.setText("left");
			remoteTask.execute("left");
			break;
		case R.id.right:
			command.setText("right");
			remoteTask.execute("right");
			break;
		case R.id.forward:
			command.setText("forward");
			remoteTask.execute("forward");
			break;
		case R.id.back:
			command.setText("back");
			remoteTask.execute("back");
			break;
		case R.id.submit:
			remoteTask.execute(command.getText().toString());
			break;
		default:
			throw new RuntimeException("Unknow button ID");

		}
	}
	
	public void onSubmit(String command){
		
		
		
	}

	public void onConnect(View view) {
		 address = server.getText().toString();
		 socket = port.getText().toString();
		Connection connection = new Connection(view.getContext(),address,socket);
		try {
			if(connection.execute("connected").get())
				for (View touchable : touchables) {
					if (touchable instanceof Button)
						((Button) touchable).setEnabled(true);
				}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
