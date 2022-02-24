package Connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import Events.OnAnswerListener;
import Events.OnBWListener;
import Events.OnConnectionListener;
import Events.OnRTTListener;
import Events.OnRTTReceiberListener;

public class TCPConnection extends Thread {

	private String ip = "127.0.0.1";

	private Socket socket;

	private BufferedWriter bw;

	private OnAnswerListener answer;

	private OnConnectionListener connection;

	private OnRTTListener rtt;

	private OnRTTReceiberListener rttReciber;
	
	private OnBWListener bwListener;
	
	private int option;

	@Override
	public void run() {

		try {
			
			socket = new Socket(ip, 6000);
			// connection.Connected();

			OutputStream os = socket.getOutputStream();
			bw = new BufferedWriter(new OutputStreamWriter(os));
			

			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			bwListener.bufferedWritterReady(option);


			String msg = br.readLine();
			rttReciber.onMessageReceived();
			answer.MessageReceived(msg);
						try {
				socket.close();
			} catch (SocketException e) {
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String line) {

		new Thread(() -> {

			try {
				rtt.onNotificationSend();
				bw.write(line + "\n");
				
				// System.out.println(line);
				bw.flush();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).start();
		;

	}

	public void setAnswer(OnAnswerListener answer) {
		this.answer = answer;
	}

	public void setConnection(OnConnectionListener connection) {
		this.connection = connection;
	}

	public void setNotification(OnRTTListener rtt) {
		this.rtt = rtt;
	}

	public void setRttReciber(OnRTTReceiberListener rttReciber) {
		this.rttReciber = rttReciber;
	}

	public void setBwListener(OnBWListener bwListener) {
		this.bwListener = bwListener;
	}

	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}
	
	
}
