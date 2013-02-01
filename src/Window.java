import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Window extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Container components;
	Simulation simulation;
	private JButton bNewSimulation;
	private JButton bSave;
	private JButton bQuit;
	private JButton bPause;
	private JButton bValueOk;
	private JButton bError;
	private JButton bValueEdit;
	private JButton bLoadParameters;
	private JButton bComputers[] = new JButton[8];
	private JButton bChannels[] = new JButton[8];
	private JTextArea tObszarTekstowy;
	private JScrollPane jScrollPane;
	private JTextField tPoleTekstowe;
	private JTextField tValue;
	private JTextField tComputer1;
	private JTextField tComputer2;

	private Icon iComputer = new ImageIcon("images/computer.jpg");

	public Window() {
		/** ustawienia okna */
		setTitle("==SYMULACJA TOKEN RING + KOREKCJA BLEDOW KODEM HAMMINGA==");
		int hMenuBar = 55; // rozmiar h menu w pasku
		int WIDTH = 800, HEIGHT = 600;
		setSize(WIDTH, HEIGHT);

		/** komponenty w oknie */
		components = getContentPane();
		components.setLayout(null);

		/** menu w pasku */
		MenuBar menuBar = new MenuBar();
		setMenuBar(menuBar);
		Menu menu = new Menu("Plik");
		MenuItem miClose = new MenuItem("Zamknij", new MenuShortcut('1'));
		MenuItem miSave = new MenuItem("Zapisz", new MenuShortcut('2'));
		menu.add(miClose);
		menu.add(miSave);
		menu.addActionListener(this);
		menuBar.add(menu);

		/** konsola */
		tObszarTekstowy = new JTextArea(8, 2);
		tPoleTekstowe = new JTextField();
		jScrollPane = new JScrollPane(tObszarTekstowy);
		jScrollPane.setBounds(325, 5, 464, 540);
		jScrollPane.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		tPoleTekstowe.setBounds(325, 430, 464, 540);
		tObszarTekstowy.setBackground(new Color(1, 1, 1));
		tObszarTekstowy.setForeground(new Color(1, 150, 1));
		tObszarTekstowy.setBorder(BorderFactory
				.createLineBorder(Color.black, 1));
		tObszarTekstowy.setEditable(false);
		tObszarTekstowy.setLineWrap(true);
		tObszarTekstowy.setFont(new Font("Monospaced", Font.ITALIC, 12));
		tObszarTekstowy.setAutoscrolls(true);
		tPoleTekstowe.setBackground(new Color(191, 191, 191));
		tPoleTekstowe.setForeground(new Color(190, 10, 11));
		tPoleTekstowe.setEnabled(false);
		components.add(jScrollPane);

		/** przycisk NOWA SYMULACJA */
		bNewSimulation = new JButton("NOWA SYMULACJA");
		bNewSimulation.addActionListener(this);
		bNewSimulation.setBounds(5, 327, 310, 20);
		bNewSimulation.setForeground(new Color(0, 0, 0));
		bNewSimulation.setEnabled(true);
		bNewSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				write("rozpoczêto now¹ symulacjê..");
				simulation.newSimulation(0);
				bError.setEnabled(true);
			}
		});
		components.add(bNewSimulation);

		/** przycisk pauza */
		bPause = new JButton("Pauza");
		bPause.setEnabled(false);
		bPause.addActionListener(this);
		bPause.setForeground(new Color(0, 0, 0));
		bPause.setBounds(5, 360, 70, 20);
		bPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color colorBlack = new Color(0, 0, 0);
				Color colorRed = new Color(200, 0, 0);
				if (simulation.working) {
					simulation.pause(true);
					bPause.setForeground(colorRed);
				} else if (!simulation.working) {
					simulation.pause(false);
					bPause.setForeground(colorBlack);
				}
			}
		});
		components.add(bPause);

		/** elementy "z komputera do komputera" */
		JLabel lFromComputer;
		lFromComputer = new JLabel("Z komputera:");
		lFromComputer.setBounds(140, 350, 110, 20);
		lFromComputer.setVisible(true);
		components.add(lFromComputer);
		JLabel lToComputer;
		lToComputer = new JLabel("Do komputera:");
		lToComputer.setBounds(140, 370, 100, 20);
		lToComputer.setVisible(true);
		components.add(lToComputer);
		tComputer1 = new JTextField();
		tComputer1.setEnabled(false);
		tComputer1.setBounds(229, 352, 30, 18);
		components.add(tComputer1);
		tComputer2 = new JTextField();
		tComputer2.setEnabled(false);
		tComputer2.setBounds(229, 371, 30, 18);
		components.add(tComputer2);

		/** przycisk ZAPISZ */
		bSave = new JButton("Zapisz");
		bSave.setEnabled(true);
		bSave.addActionListener(this);
		bSave.setBounds(2, HEIGHT - 25 - hMenuBar, 160, 25);
		bSave.setBackground(new Color(160, 160, 220));
		bSave.setForeground(new Color(0, 0, 0));
		bSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		components.add(bSave);

		/** przycisk WYJDZ */
		bQuit = new JButton("Wyjdz");
		bQuit.addActionListener(this);
		bQuit.setBounds(164, HEIGHT - 25 - hMenuBar, 158, 25);
		bQuit.setBackground(new Color(120, 120, 220));
		bQuit.setForeground(new Color(0, 0, 0));
		bQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		components.add(bQuit);

		/** pole wartosc */
		JLabel lValue = new JLabel("Wartosc:");
		lValue.setBounds(30, 390, 80, 20);
		components.add(lValue);
		tValue = new JTextField();
		tValue.setEnabled(false);
		tValue.setBounds(110, 390, 149, 20);
		components.add(tValue);

		/** pole edycji wartosci */
		bValueEdit = new JButton("edit");
		bValueEdit.addActionListener(this);
		bValueEdit.setBounds(260, 352, 55, 39);
		bValueEdit.setEnabled(false);
		bValueEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bValueEdit.setEnabled(false);
				bValueOk.setEnabled(true);
				tValue.setEnabled(true);
				tComputer1.setEnabled(true);
				tComputer2.setEnabled(true);
			}
		});
		components.add(bValueEdit);

		/** pole wprowadzenia bledu */
		bError = new JButton("nowy b³¹d");
		bError.addActionListener(this);
		bError.setBounds(10, 430, 150, 20);
		bError.setEnabled(true);
		bError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulation.error = new Error(simulation);
			}
		});
		components.add(bError);

		/** pole za³adowania parametrów */
		bLoadParameters = new JButton("za³aduj parametry");
		bLoadParameters.addActionListener(this);
		bLoadParameters.setBounds(10, 460, 150, 20);
		bLoadParameters.setEnabled(false);
		bLoadParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					simulation.loadParameters();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		components.add(bLoadParameters);

		/** pole potwierdzenia wartosci */
		bValueOk = new JButton("ok");
		bValueOk.addActionListener(this);
		bValueOk.setBounds(260, 392, 55, 39);
		bValueOk.setEnabled(false);
		bValueOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bValueOk.setEnabled(false);
				bValueEdit.setEnabled(true);
				simulation.sendMessage();
			}
		});
		components.add(bValueOk);

		/** ikonki komputerow */
		int w = 50, h = 50; // rozmiar komputerkow
		int wWindow = WIDTH - 400, hWindow = 600 - hMenuBar - 150; // obszar
																	// rysowania
																	// dla
																	// komputerkow
		int xBetween = (int) ((wWindow - 3 * w) / 6); // odstep pomiedzy
														// komputerkami w x
		int yBetween = (int) ((hWindow - 3 * h) / 6); // odstep pomiedzy
														// komputerkami w y
		for (int i = 0; i < 8; i++) {
			bComputers[i] = new JButton("" + i, iComputer);
			bComputers[i].setEnabled(false);
			bComputers[i].setBackground(new Color(255, 255, 255));
			bComputers[i].addActionListener(this);
			components.add(bComputers[i]);
		}
		bComputers[0].setBounds(1 * xBetween + 0 * w, 1 * yBetween, w + 10,
				h + 10);
		bComputers[1].setBounds(2 * xBetween + 1 * w, 1 * yBetween, w, h);
		bComputers[2].setBounds(3 * xBetween + 2 * w, 1 * yBetween, w, h);
		bComputers[3].setBounds(3 * xBetween + 2 * w, 2 * yBetween + 1 * h, w,
				h);
		bComputers[4].setBounds(3 * xBetween + 2 * w, 3 * yBetween + 2 * h, w,
				h);
		bComputers[5].setBounds(2 * xBetween + 1 * w, 3 * yBetween + 2 * h, w,
				h);
		bComputers[6].setBounds(1 * xBetween + 0 * w, 3 * yBetween + 2 * h, w,
				h);
		bComputers[7].setBounds(1 * xBetween + 0 * w, 2 * yBetween + h, w, h);

		/** ikonki kanalow */
		int hChannel = 27;
		for (int i = 0; i < 8; i++) {
			if (i != 7) {
				bChannels[i] = new JButton("" + i + "->" + (i + 1));
			} else {
				bChannels[i] = new JButton("" + i + "->" + 0);
			}
			bChannels[i].setBackground(new Color(0, 0, 0));
			bChannels[i].setForeground(new Color(255, 255, 255));
			bChannels[i].setEnabled(false);
			bChannels[i].addActionListener(this);
			components.add(bChannels[i]);
		}
		bChannels[0].setBounds(1 * xBetween + w, 1 * yBetween + h / 2
				- hChannel / 2, xBetween, hChannel);
		bChannels[1].setBounds(2 * xBetween + 2 * w, 1 * yBetween + h / 2
				- hChannel / 2, xBetween, hChannel);
		bChannels[2].setBounds(3 * xBetween + 2 * w + w / 2 - hChannel / 2, 1
				* yBetween + h, hChannel, yBetween);
		bChannels[3].setBounds(3 * xBetween + 2 * w + w / 2 - hChannel / 2, 2
				* yBetween + 2 * h, hChannel, yBetween);
		bChannels[4].setBounds(2 * xBetween + 2 * w, 3 * yBetween + 2 * h + h
				/ 2 - hChannel / 2, xBetween, hChannel);
		bChannels[5].setBounds(1 * xBetween + w, 3 * yBetween + 2 * h + h / 2
				- hChannel / 2, xBetween, hChannel);
		bChannels[6].setBounds(1 * xBetween + w / 2 - hChannel / 2, 2
				* yBetween + 2 * h, hChannel, yBetween);
		bChannels[7].setBounds(1 * xBetween + w / 2 - hChannel / 2, 1
				* yBetween + h, hChannel, yBetween);

		/** panele */
		JPanel panelComputers = new JPanel();
		panelComputers.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		panelComputers.setBackground(new Color(255, 255, 255));
		panelComputers.setBounds(0, 0, 320, 320);
		components.add(panelComputers);

		JPanel panelSettings = new JPanel();
		panelSettings.setBorder(BorderFactory.createLineBorder(Color.black, 0));
		panelSettings.setBackground(new Color(200, 200, 250));
		panelSettings.setBounds(2, 324, 320, 221);
		components.add(panelSettings);

		/** pozostale ustawienia */
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setVisible(true);

		write("czekam na now¹ symulacjê..");

		/** stworzenie symulacji */
		simulation = new Simulation(bComputers, bChannels, bNewSimulation,
				tComputer1, tComputer2, bValueOk, tObszarTekstowy, jScrollPane,
				tValue, bPause, bLoadParameters);
	}

	// metoda obslugujaca nacisniecie przycisku
	public void actionPerformed(ActionEvent e) {
		String label = e.getActionCommand();
		if (label.equals("Zamiana")) {
			try {

			} catch (NumberFormatException ev) {
				System.out
						.println("Blad argumentow!? Wpisz poprawne wartosci!");
			}
		} else if (label.equals("Zamknij")) {
			System.exit(0);
		} else if (label.equals("Zapisz")) {
			try {
				simulation.save();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			write("==zapisano wyniki symulacji==");
		} else if (label.equals("0")) {
			simulation.write("symulacja 0");
		} else if (label.equals("1")) {
			simulation.write("komputer 1");
		} else if (label.equals("2")) {
			simulation.write("komputer 2");
		} else if (label.equals("3")) {
			simulation.write("komputer 3");
		} else if (label.equals("4")) {
			simulation.write("komputer 4");
		} else if (label.equals("5")) {
			simulation.write("komputer 5");
		} else if (label.equals("6")) {
			simulation.write("komputer 6");
		} else if (label.equals("7")) {
			simulation.write("komputer 7");
		} else if (label.equals("0->1")) {
			simulation.write("kanal 0->1");
		} else if (label.equals("1->2")) {
			simulation.write("kanal 1->2");
		} else if (label.equals("2->3")) {
			simulation.write("kanal 2->3");
		} else if (label.equals("3->4")) {
			simulation.write("kanal 3->4");
		} else if (label.equals("4->5")) {
			simulation.write("kanal 4->5");
		} else if (label.equals("5->6")) {
			simulation.write("kanal 5->6");
		} else if (label.equals("6->7")) {
			simulation.write("kanal 6->7");
		} else if (label.equals("7->0")) {
			simulation.write("kanal 7->0");
		}
	}

	public void write(String x) {
		tObszarTekstowy.append("@@program>> " + x + "\n");
		jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

}
