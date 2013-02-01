import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

public class Simulation {
	boolean working;
	private Computer computer[] = new Computer[8];
	private Channel channel[] = new Channel[8];
	private JButton bComputers[];
	private JButton bChannels[];
	private JButton bNewSimulation;
	private JButton bValueOk;
	private JTextArea tObszarTekstowy;
	private JScrollPane jScrollPane;
	private JTextField tValue;
	private JTextField tComputer1;
	private JTextField tComputer2;
	private JButton bPause;
	private JButton bLoadParameters;

	public Error error;

	Simulation(JButton bComputers[], JButton bChannels[],
			JButton bNewSimulation, JTextField tComputer1,
			JTextField tComputer2, JButton bValueOk, JTextArea tObszarTekstowy,
			JScrollPane jScrollPane, JTextField tValue, JButton bPause,
			JButton bLoadParameters) {
		this.bComputers = bComputers;
		this.bChannels = bChannels;
		this.bNewSimulation = bNewSimulation;
		this.tComputer1 = tComputer1;
		this.tComputer2 = tComputer2;
		this.bValueOk = bValueOk;
		this.tObszarTekstowy = tObszarTekstowy;
		this.jScrollPane = jScrollPane;
		this.tValue = tValue;
		this.bPause = bPause;
		this.bLoadParameters = bLoadParameters;

		for (int i = 0; i < 8; i++) {
			channel[i] = new Channel(i, bChannels[i]);
			channel[i].IO = false;
			channel[i].OI = false;
		}

		for (int i = 0; i < 8; i++) {
			if (i != 0) {
				computer[i] = new Computer(i, 0, channel[i - 1], channel[i],
						bComputers[i], tObszarTekstowy, jScrollPane);
			} else if (i == 0) {
				computer[i] = new Computer(i, 0, channel[7], channel[0],
						bComputers[i], tObszarTekstowy, jScrollPane);
			}

		}
	}

	public void newSimulation(int nrMonitor) {
		tComputer1.setText("2");
		tComputer2.setText("5");
		tValue.setText("11111111");

		bNewSimulation.setEnabled(false);
		tComputer1.setEnabled(true);
		tComputer2.setEnabled(true);
		for (int i = 0; i < 8; i++) {
			bComputers[i].setEnabled(true);
			bChannels[i].setEnabled(true);
		}
		bValueOk.setEnabled(true);
		tValue.setEnabled(true);
		bLoadParameters.setEnabled(true);
		write("komputery gotowe do pracy..");

		/** komputer 0 dostaje tokena */
		computer[nrMonitor].channel[nrMonitor].frame = new Frame(true);
		computer[nrMonitor].channel[nrMonitor].IO = true;

		/** mkomputer 0 zostaje monitorem */
		computer[nrMonitor].setMonitor(new Monitor());

		/** uruchomienie w¹tków dzia³ania komputerów */
		for (int i = 0; i < 8; i++)
			computer[i].start();

		bPause.setEnabled(true);

		Color colorRed = new Color(200, 0, 0);
		this.pause(true);
		bPause.setForeground(colorRed);
	}

	@SuppressWarnings("deprecation")
	public void pause(boolean yes) {
		if (yes) {

			for (int i = 0; i < 8; i++)
				computer[i].suspend();
			write("-=zatrzymanie pracy komputerów=-");
			this.working = false;
		} else {
			write("-=wznowienie pracy komputerów=-");
			for (int i = 0; i < 8; i++)
				computer[i].resume();
			this.working = true;
		}
	}

	public void sendMessage() {
		int message[] = null;
		int from, to;

		long length = tValue.getText().length();
		boolean okData = true, okFrom = true, okTo = true;

		if (length == 8) {
			message = new int[8];
			for (int i = 0; i < 8; i++) {
				try {
					message[i] = Integer.parseInt(tValue.getText(i, 1));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		} else if (length == 16) {
			message = new int[16];
			for (int i = 0; i < 16; i++) {
				try {
					message[i] = Integer.parseInt(tValue.getText(i, 1));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
		} else {
			okData = false;
		}

		if (okData) {
			for (int i = 0; i < length; i++) {
				if (!((message[i] == 0) || (message[i] == 1)))
					okData = false;
			}
		}

		from = Integer.parseInt(tComputer1.getText());
		to = Integer.parseInt(tComputer2.getText());

		if (!((from >= 0) && (from <= 7)))
			okFrom = false;

		if (!((to >= 0) && (to <= 7)))
			okTo = false;

		if (okData && okFrom && okTo) {
			/** wys³anie danych */
			computer[from].iWishToSend(to, message);
		} else if (!okData) {
			write("!!nieprawid³owe bity!!");
		} else if (!okFrom) {
			write("!!nieprawid³owe pole komputera >z<!!");
		} else if (!okTo) {
			write("!!nieprawid³owe pole komputera >do<!!");
		}

		tValue.setEnabled(false);
		tComputer1.setEnabled(false);
		tComputer2.setEnabled(false);
	}

	public void setError() {
		computer[error.nr].error = error;
	}

	@SuppressWarnings("deprecation")
	public void save() throws IOException {
		Date date = new java.util.Date();
		String s = "SIMULATION..";
		s += "day" + date.getDay() + ",";
		s += "minutes" + date.getMinutes() + ",";
		s += "seconds" + date.getSeconds();
		s += ".txt";
		FileWriter file = new FileWriter(s);
		String text = tObszarTekstowy.getText();
		file.write(text);
		file.close();
	}

	public int setRouting(int from, int to) {
		int side = 1;
		if (to > from) {
			if ((to - from) <= 4) {
				side = 1;
			} else {
				side = 0;
			}
		} else {
			if ((from - to) <= 4) {
				side = 0;
			} else {
				side = 1;
			}
		}
		return side;
	}

	public void loadParameters() throws IOException {
		FileReader fr = new FileReader("parameters.txt");
		BufferedReader br = new BufferedReader(fr);
		String s;
		int pos = 0;
		while ((s = br.readLine()) != null) {
			if (pos == 0)
				tComputer1.setText(s);
			if (pos == 1)
				tComputer2.setText(s);
			if (pos == 2)
				tValue.setText(s);
			pos++;
		}

		fr.close();
	}

	public void write(String x) {
		tObszarTekstowy.append("@@symulacja>> " + x + "\n");
		jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

}
