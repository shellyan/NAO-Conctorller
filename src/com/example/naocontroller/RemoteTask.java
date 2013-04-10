package com.example.naocontroller;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class RemoteTask extends AsyncTask<String, Void, Boolean> {

	private BufferedInputStream in;

	private Context context = null;
	private Boolean connected = false;
	private String address = "";
	private int port;
	private String input = "";
	public boolean getConnection() {
		return connected;
	}

	public void alert(String s) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Alert");
		dialog.setMessage(s);
		dialog.setNeutralButton("Cancel", null);
		dialog.create().show();
	}

	public RemoteTask(Context context, String... strings) {
		this.context = context;
		address = strings[0];
		port = Integer.parseInt(strings[1]);
	}



	@Override
	protected Boolean doInBackground(String... arg0) {
		Socket socket = null;
		InputStream is = null;
		input =	arg0[0];
		try {
			socket = new Socket();
			InetSocketAddress sockaddr = new InetSocketAddress(address, port);
			socket.connect(sockaddr, 2000);
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;

		}

		try {
			Log.i("socket", "sending hello");
			is = socket.getInputStream();
			in = new BufferedInputStream(is);
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream())), true);
			out.print("say:" + input);
			out.flush();
			// textIn.setText(dataInputStream.readUTF());
		} catch (UnknownHostException e) {
			Log.i("UnknownHostException", "sending hello");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.i("IOException", "sending hello");
			e.printStackTrace();
			return false;

		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (is != null) {
				try {
					is.close();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean connected) {
		// dismiss the dialog once done
		
		if (!connected)
			alert("Failed to connect.");
		else{if(input=="connected")
			alert("Connected successfully!");}

	}

}
