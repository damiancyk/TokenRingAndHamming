import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class Computer extends Thread {
	private int nr; // unikatowy numer komputera
	Frame frame = null;
	private Frame frameToSend;
	private int dataReceived[];
	private boolean iWishToSend;
	public Error error = null;
	Channel channel[] = new Channel[2];
	public boolean working;
	public Monitor monitor = null;
	int nrMonitor;
	boolean goodSender = true;

	Hamming hamming;

	/** komponenty swing, kt�re obiekt komputera zmienia (te co wida� w okienku) */
	private JButton bComputers;
	private JTextArea tObszarTekstowy;
	private JScrollPane jScrollPane;

	Computer(int nr, int nrMonitor, Channel channelL, Channel channelR,
			JButton bComputers, JTextArea tObszarTekstowy,
			JScrollPane jScrollPane) {
		this.nr = nr;
		this.nrMonitor = nrMonitor;
		this.channel[0] = channelL;
		this.channel[1] = channelR;
		this.bComputers = bComputers;
		this.tObszarTekstowy = tObszarTekstowy;
		this.jScrollPane = jScrollPane;
		this.iWishToSend = false;
		this.working = true;

		hamming = new Hamming(tObszarTekstowy, jScrollPane);
	}

	public void run() {
		try {
			while (true) {
				if (monitor != null) {
					if (!monitor.vftControll(20)) {
						write("!!nie widzialem za d�ugo tokena, wystawiam nowy!!");
						send(monitor.newToken(), 1);
					}
				}
				listen();
			}
		} catch (InterruptedException e) {
		}
		write("ko�cz� swoje dzia�anie..");
	}

	public void startSending(int nrChannel) {
		if (nrChannel == 0) {
			channel[nrChannel].IO = true;
		} else if (nrChannel == 1) {
			channel[nrChannel].OI = true;
		} else {
			write("nie mo�na da� sygna�u nadawania wiadomo�ci, niepradiw�owy parametr)");
		}
	}

	public void stopSending(int nrChannel) {
		if (nrChannel == 0) {
			channel[nrChannel].IO = false;
		} else if (nrChannel == 1) {
			channel[nrChannel].OI = false;
		} else {
			write("nie mo�na da� sygna�u przestania nadawania wiadomo�ci, niepradiw�owy parametr)");
		}
	}

	public void listen() throws InterruptedException {
		int SLEEP_VALUE = 500;

		if (error != null) {
			if (error.errorNode) {
				bComputers.setBorder(BorderFactory.createLineBorder(Color.red,
						3));
				this.working = false;
				channel[0].bChannels.setBorder(BorderFactory.createLineBorder(
						Color.red, 3));
				channel[0].working = false;
			}

			if (error.errorSender) {
				bComputers.setBorder(BorderFactory.createLineBorder(Color.red,
						3));
				this.goodSender = false;
			}
		}

		// gubimy token
		if (channel[0].IO || channel[1].OI) {
			if (this.error != null) {
				if (error.errorToken) {
					channel[0].IO = false;
					channel[1].OI = false;
					error = null;
					write("!!znikn�� token!!");
				}
			}
		}

		// co� przysz�o
		if (channel[0].IO || channel[1].OI) {
			if (this.monitor != null) {
				monitor.VFT = 0;
			}
			// ustalenie z kt�rego do kt�rego kana�u b�dzie przesy�ana ramka
			int from, to;
			if (channel[0].IO) {
				from = 0;
				to = 1;
			} else {
				from = 1;
				to = 0;
			}
			channel[from].bChannels.setBorder(BorderFactory.createLineBorder(
					Color.green, 3));

			sleep(SLEEP_VALUE);
			channel[from].bChannels.setBorder(BorderFactory.createLineBorder(
					Color.gray, 1));
			// zapis ramki do pami�ci tymczasowej kompa
			this.frame = channel[from].frame;
			this.bComputers.setBorder(BorderFactory.createLineBorder(
					Color.green, 3));
			// oczyszczenie kana�u, z kt�rego co� przysz�o
			clearChannel(channel, from);

			if (this.monitor != null) {
				if (this.frame.visited) {
					// ta ramka juz odwiedzila monitor, wiec generuj� nowy token
					write("!!ta ramka mnie odwiedzila! generuj� nowy token..!!");
					this.frame = monitor.newToken();
				}

				if (!this.frame.visited)
					this.frame.visited = true;
			}

			if (this.frame.token) { // przysz�a ramka z tokenem
				if (this.frame.busy) { // je�li token jest zaj�ty
					// czy to do tego komputera ta ramka mia�a by�
					if (this.frame.to == this.nr) {
						this.dataReceived = hamming.decode(this.frame.data);
						String sDataReceived = "";
						if (dataReceived != null) {
							for (int i = 0; i < dataReceived.length; i++)
								sDataReceived += dataReceived[i];
						}

						this.frame.data = null; // usuwamy dane z ramki

						write("odbieram ramk� do mnie i wysy�am potwierdzenie");
						write("odebrane dane: " + sDataReceived);
						// przesy�amy zaj�t� ramk� bez danych (potwierdzenie)
						this.frame.visited = false;
						send(this.frame, to);
					}
					// pusta ramka zwrotna
					else if (this.frame.from == this.nr) {
						// zwolnienie ramki
						if (goodSender) {
							write("(nadawca)>> otrzyma�em potwierdzenie o dostarczeniu ramki");
							this.frame.busy = false;
							this.frame.visited = false;
						} else {
							write("!!uszkodzenie, nie odbieram potwierdzenia!!");
							this.frame.visited = true;
							this.frame.from = -1;
							this.frame.to = -1;
							this.frame.busy = true;
						}
					}
					// ta ramka mnie nie dotyczy, ale ma tokena i jest zaj�ta
					else {
						if (frame.data != null)
							checkData();

						// je�li s� jakie� b��dy
						if (error != null) {
							if (error.errorBits) {
								String sWhichPos = " ";
								if (error.pos1 >= 0)
									sWhichPos += error.pos1 + " ";
								if (error.pos2 >= 0)
									sWhichPos += error.pos2 + " ";
								if (error.pos3 >= 0)
									sWhichPos += error.pos3 + " ";
								error.doErrorBadBits(this.frame);
								write("wprowadzi�em z�e dane na pozycjach: "
										+ sWhichPos);
							}
						}

						// nic tu nie robi�, na ko�cu procesu listen i tak wy�l�
						// t� ramk� z pami�ci podr�cznej komputera
					}
				}
				// ramka jest wolna, mo�e ju� by�a wolna, ale je�li zwolnili�my
				// j� po otrzymaniu zwrotnego potwierdzenia
				// to i tak nie skorzystamy z mo�liwo�ci wys�ania
				else if (!this.frame.busy) {
					// je�li mam co� do nadania
					if (this.iWishToSend) {
						write("wolny token, wysy�am to, co mam do wys�ania..");
						this.frame = this.frameToSend;
						this.iWishToSend = false;
					} else {
						this.frame.visited = false;
						write("nic do wys�ania, przesy�am dalej tokena..");
					}
				}

			} else {
				// przysz�a ramka bez tokena, magazynuj� wi�c ramki
			}

			// jestem monitorem, a jest informacja o z�ym w�le
			if ((frame.badNode) && (this.monitor != null)) {
				// zaprzestanie wysy�ania i komunikat o z�ym w�le
				write("!!z�y w�ze� na pozycji!!: " + this.frame.whereBadNode);
				write("!!zatrzymuj� token!!");
				bComputers.setBorder(BorderFactory.createLineBorder(Color.blue,
						5));
			} else {
				// wysy�am odpowiednio "przerobion�" ramk�
				if (channel[1].working) {
					// sasiedni wezel nie jest uszkodzony, wiec wysylam dalej
					send(this.frame, to);
				} else {
					// uszkodzenie s�siada, wi�c odsylam z powrotem
					// ramke z dodatkowym bitem
					this.frame.badNode = true;
					this.frame.whereBadNode = this.next();
					send(this.frame, from);
				}
			}

			// usuwam potencjalne b��dy
			error = null;
		}

		sleep(SLEEP_VALUE);
		if (frame != null) {
			if (!((frame.badNode) && (this.monitor != null)))
				this.bComputers.setBorder(BorderFactory.createLineBorder(
						Color.gray, 1));
		}
	}

	public void clearChannel(Channel channel[], int from) {
		channel[from].frame = null; // usuwamy dane z kana�u
		// przychodz�cego
		// usuwamy aktywno�� kana�u przychodz�cego
		if (from == 0)
			channel[from].IO = false;
		else
			channel[from].OI = false;
	}

	public void send(Frame f, int to) {
		channel[to].frame = f;
		if (to == 0)
			channel[to].OI = true;
		else
			channel[to].IO = true;
	}

	public void iWishToSend(int to, int data[]) {
		int totalSize = 0;
		if (data.length == 8)
			totalSize = 12;
		else
			totalSize = 21;

		int dataCoded[] = new int[totalSize];

		if ((data.length == 8) || (data.length == 16)) {

			write("chc� wys�a� dane d�ug�ci " + data.length + "(" + totalSize
					+ ")" + " bit�w do komputera nr " + to);
			if (data.length == 8)
				hamming.code_12_8(data, dataCoded, false);
			else
				hamming.code_21_16(data, dataCoded, false);

			String sData = "";
			for (int i = 0; i < data.length; i++)
				sData += data[i];
			String sDataCoded = "";
			for (int i = 0; i < totalSize; i++)
				sDataCoded += dataCoded[i];

			write(sData + " - dane do wys�ania");
			write(sDataCoded + " - dane po zakodowaniu");
		} else {
			write("b��dna ilo�� bit�w dla wysy�anej wiadomo�ci");
		}

		frameToSend = new Frame(true, this.nr, to);
		frameToSend.data = dataCoded;
		frameToSend.busy = true;
		this.iWishToSend = true;
	}

	private void checkData() {
		int posBadBit = -1;
		String sDataCoded = "";
		for (int i = 0; i < frame.data.length; i++)
			sDataCoded += frame.data[i];

		// potencjalna naprawa
		if (frame.data.length == 12)
			posBadBit = hamming.check_12_8(frame.data, false);
		else
			posBadBit = hamming.check_21_16(frame.data, false);

		// pisz�, �e dane by�y od razu poprawne
		if (posBadBit < 0) {
			write("odebra�em poprawne dane: " + sDataCoded);
		}
		// pisz�, �e pierwsza naprawa by�a potrzebna
		else {
			write("odebra�em niepoprawne dane: " + sDataCoded + ", z�y bit:"
					+ posBadBit);
			write("..naprawiam..");
			String sDataCodedE1 = "";
			for (int i = 0; i < frame.data.length; i++)
				sDataCodedE1 += frame.data[i];
			write("odtworzone dane: " + sDataCodedE1);
			if (error != null) {
				if (error.manyBits) {
					write("..kolejna naprawa..");
					// potencjalna naprawa
					if (frame.data.length == 12)
						posBadBit = hamming.check_12_8(frame.data, false);
					else
						posBadBit = hamming.check_21_16(frame.data, false);
					String sDataCodedE2 = "";
					for (int i = 0; i < frame.data.length; i++)
						sDataCodedE2 += frame.data[i];
					write("odtworzone dane: " + sDataCodedE2);
				}
			}
		}

	}

	public void setError(Error error) {
		this.error = error;
	}

	public int next() {
		int next = 0;
		if (this.nr < 7)
			next = this.nr + 1;
		return next;
	}

	public int previous() {
		int previous = 7;
		if (this.nr > 0)
			previous = this.nr - 1;
		return previous;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
		write("==zosta�em monitorem==");
	}

	public void write(String x) {
		String text = "";
		if (monitor != null)
			text = "@@k" + nr + " (monitor)>>" + x + "\n";
		else
			text = "@@komp " + nr + ">>" + x + "\n";

		tObszarTekstowy.append(text);
		jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

}
