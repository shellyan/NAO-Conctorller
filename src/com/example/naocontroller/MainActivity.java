package com.example.naocontroller;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	EditText server, command, port;
	Button left, right, forward, back, submit, button;
	TextView textview;
	RelativeLayout pad;
	ArrayList<View> touchables = new ArrayList<View>();
	RemoteTask remoteTask;

	String address, socket;

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

		// left.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// remoteTask.execute("custom:left");
		// } else if(event.getAction()==MotionEvent.ACTION_UP){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// Log.e("tag","stop" );
		// remoteTask.execute("custom:stop");}
		// return false;
		// }
		// });
		// right.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// remoteTask.execute("custom:right");
		// }else if(event.getAction()==MotionEvent.ACTION_UP){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// Log.e("tag","stop" );
		// remoteTask.execute("custom:stop");}
		// return false;
		// }
		// });
		// forward.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// Log.e("tag","forward triggerred" );
		//
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// remoteTask.execute("custom:forward");
		// }else if(event.getAction()==MotionEvent.ACTION_UP){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// Log.e("tag","stop" );
		// remoteTask.execute("custom:stop");}
		// return false;
		// }
		// });
		// back.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// remoteTask.execute("custom:back");
		// }else if(event.getAction()==MotionEvent.ACTION_UP){
		// address = server.getText().toString();
		// socket = port.getText().toString();
		// remoteTask = new RemoteTask(MainActivity.this, address, socket);
		// Log.e("tag","stop" );
		// remoteTask.execute("custom:stop");}
		// return false;
		// }
		// });
		for (View touchable : touchables) {
			if (touchable instanceof Button) {
				button = ((Button) touchable);
				button.setEnabled(true);
				button.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						address = server.getText().toString();
						socket = port.getText().toString();
						remoteTask = new RemoteTask(MainActivity.this, address,
								socket);
						if (event.getAction() == MotionEvent.ACTION_UP) {

							Log.e("tag", "released");
							remoteTask.execute("custom:stop");
						} else if (event.getAction() == MotionEvent.ACTION_DOWN) {

							switch (v.getId()) {
							case R.id.left:
								command.setText("left");
								remoteTask.execute("custom:left");
								break;
							case R.id.right:
								command.setText("right");
								remoteTask.execute("custom:right");
								break;
							case R.id.forward:
								command.setText("forward");
								remoteTask.execute("custom:forward");
								break;
							case R.id.back:
								command.setText("back");
								remoteTask.execute("custom:back");
								break;
							case R.id.rotate_left:
								command.setText("rotate_left");
								remoteTask.execute("custom:"+"rotate_left");
								break;
							case R.id.rotate_right:
								command.setText("rotate_right");
								remoteTask.execute("custom:"+"rotate_right");
								break;
							case R.id.submit:
								Log.i("SEND", command.getText().toString());
								remoteTask
										.execute(command.getText().toString());
								break;
							default:
								throw new RuntimeException("Unknow button ID");

							}
						}

						return false;
					}
				});
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//	public void onSubmit(View view) {
	//		remoteTask = new RemoteTask(MainActivity.this, address, socket);
	//
	//		remoteTask.execute(command.getText().toString());
	//
	//	}

	public void onConnect(View view) {
		address = server.getText().toString();
		socket = port.getText().toString();
		Connection connection = new Connection(view.getContext(), address,
				socket);
		try {
			if (connection.execute("connected").get())
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
