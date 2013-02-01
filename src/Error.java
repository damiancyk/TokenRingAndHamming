import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/*1 zle bity
 * rozwi¹zanie: kod Hamminga
 * 
 */

/*2 uszkodzony wêze³ (wêze³ nie odpowiada)
 * rozwi¹zanie: komputer, który rozpoznaje uszkodzenie s¹siada, wysy³a komunikat do monitora o uszkodzeniu. 
 * ten sygnalizuje to administratorowi i zaprzestaje siê transmisji
 * pole dla Frame boolean badNode
 */

/*3 ginie nadawca (ramka z dan¹ krazy ciagle w obiegu)
 * rozwi¹zanie: dodatkowy bit "odwiedzenia" monitora
 * pole boolean visited
 */

/*4 ginie token (ramka z tokenem nie odwiedza monitora, czyli ¿adnych transmisji nie ma na ³¹czach)
 * rozwi¹zanie: jeœli ¿adna ramka nie odwiedzi³a monitora po czasie VFT, generowany jest nowy token
 * 
 */

public class Error extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public int nr;
	private JTextField tWhere;

	boolean errorBits = true, manyBits = false;
	int pos1 = -1, pos2 = -1, pos3 = -1;
	private JTextField tPos1, tPos2, tPos3;

	boolean errorNode = false;

	boolean errorSender = false;

	boolean errorToken = false;

	private JButton bOk;

	class RadioListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "bit") {
				tPos1.setEnabled(true);
				tPos2.setEnabled(true);
				tPos3.setEnabled(true);
				errorBits = true;
				errorNode = false;
				errorSender = false;
				errorToken = false;
			} else if (e.getActionCommand() == "node") {
				tPos1.setEnabled(false);
				tPos2.setEnabled(false);
				tPos3.setEnabled(false);
				errorBits = false;
				errorNode = true;
				errorSender = false;
				errorToken = false;
			} else if (e.getActionCommand() == "sender") {
				tPos1.setEnabled(false);
				tPos2.setEnabled(false);
				tPos3.setEnabled(false);
				errorBits = false;
				errorNode = false;
				errorSender = true;
				errorToken = false;
			} else if (e.getActionCommand() == "token") {
				tPos1.setEnabled(false);
				tPos2.setEnabled(false);
				tPos3.setEnabled(false);
				errorBits = false;
				errorNode = false;
				errorSender = true;
				errorToken = true;
			} else if (e.getActionCommand() == "1bit") {
				tPos1.setEnabled(true);
				tPos2.setEnabled(false);
				tPos3.setEnabled(false);
				manyBits = false;
			} else if (e.getActionCommand() == "manybits") {
				tPos1.setEnabled(true);
				tPos2.setEnabled(true);
				tPos3.setEnabled(true);
				manyBits = true;
			}
		}

	}

	public Error(final Simulation simulation) {
		/** ustawienia okna */
		setTitle("==WSKAZ BLAD==");
		int WIDTH = 300, HEIGHT = 250;
		setSize(WIDTH, HEIGHT);
		this.setLayout(null);

		// listener dla radioButtonow
		RadioListener myListener = new RadioListener();

		// grupa bledow
		ButtonGroup errorGroup = new ButtonGroup();
		ButtonGroup bitGroup = new ButtonGroup();

		// bledy do wyboru
		JLabel lError = new JLabel("-=RODZAJ B£ÊDU=-");
		lError.setBounds(20, 0, 150, 20);
		add(lError);

		JRadioButton rErrorBit = new JRadioButton("uszkodzone bity");
		rErrorBit.setActionCommand("bit");
		rErrorBit.setSelected(true);
		rErrorBit.setBounds(0, 30, 150, 20);
		rErrorBit.addActionListener(myListener);
		add(rErrorBit);
		errorGroup.add(rErrorBit);

		// wartosci bitow
		JLabel lBitCount = new JLabel("Pozycje:");
		lBitCount.setForeground(new Color(150, 150, 150));
		lBitCount.setBounds(160, 30, 60, 20);
		add(lBitCount);
		tPos1 = new JTextField();
		tPos1.setText("1");
		tPos1.setEnabled(true);
		tPos1.setBounds(220, 30, 20, 20);
		add(tPos1);
		tPos2 = new JTextField();
		tPos2.setText("-1");
		tPos2.setEnabled(false);
		tPos2.setBounds(240, 30, 20, 20);
		add(tPos2);
		tPos3 = new JTextField();
		tPos3.setText("-1");
		tPos3.setEnabled(false);
		tPos3.setBounds(260, 30, 20, 20);
		add(tPos3);

		JRadioButton eErrorComputer = new JRadioButton("uszkodzony wêze³");
		eErrorComputer.setActionCommand("node");
		eErrorComputer.setBounds(0, 60, 160, 20);
		eErrorComputer.addActionListener(myListener);
		add(eErrorComputer);
		errorGroup.add(eErrorComputer);

		JRadioButton eErrorCable = new JRadioButton("uszkodzony nadawca");
		eErrorCable.setActionCommand("sender");
		eErrorCable.setBounds(0, 90, 160, 20);
		eErrorCable.addActionListener(myListener);
		add(eErrorCable);
		errorGroup.add(eErrorCable);

		JRadioButton eErrorToken = new JRadioButton("zagubienie tokena");
		eErrorToken.setActionCommand("token");
		eErrorToken.setBounds(0, 120, 160, 20);
		eErrorToken.addActionListener(myListener);
		add(eErrorToken);
		errorGroup.add(eErrorToken);

		JRadioButton eErrorBit1 = new JRadioButton("1 bit");
		eErrorBit1.setSelected(true);
		eErrorBit1.setActionCommand("1bit");
		eErrorBit1.setBounds(160, 50, 50, 20);
		eErrorBit1.addActionListener(myListener);
		add(eErrorBit1);
		bitGroup.add(eErrorBit1);

		JRadioButton eErrorBitMany = new JRadioButton("wiele bitów");
		eErrorBitMany.setActionCommand("manybits");
		eErrorBitMany.setBounds(160, 70, 100, 20);
		eErrorBitMany.addActionListener(myListener);
		add(eErrorBitMany);
		bitGroup.add(eErrorBitMany);

		/** pole gdzie */
		JLabel lWhere = new JLabel("Który komputer:");
		lWhere.setBounds(15, 150, 100, 20);
		add(lWhere);
		tWhere = new JTextField();
		tWhere.setText("0");
		tWhere.setEnabled(true);
		tWhere.setBounds(120, 150, 30, 20);
		add(tWhere);

		/** potwierdzenie bledu */
		bOk = new JButton("OK");
		bOk.addActionListener(this);
		bOk.setBounds(5, 198, 285, 20);
		bOk.setEnabled(true);
		bOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// bOk.setEnabled(false);
				// tWhere.setEnabled(false);
				pos1 = Integer.parseInt(tPos1.getText());
				pos2 = Integer.parseInt(tPos2.getText());
				pos3 = Integer.parseInt(tPos3.getText());
				nr = Integer.parseInt(tWhere.getText());
				simulation.setError();
				if (errorBits) {
					simulation.write("ustawiono zle bity na pozycjach: " + pos1
							+ " " + pos2 + " " + pos3);
					simulation.write("dla komputera nr: " + nr);
					simulation.write("(rozwi¹zanie: kod Hamminga)");
				} else if (errorNode) {
					simulation.write("ustawiono wadliwy wêze³ nr: " + nr);
					simulation
							.write("(rozwi¹zanie: informacja dla monitora o uszkodzeniu)");
				} else if (errorSender) {
					simulation.write("ustawiono wadliwego nadawcê nr: " + nr);
					simulation
							.write("(rozwi¹zanie: krazacy token przechwycony zostaje przez monitor)");
				} else if (errorToken) {
					simulation
							.write("ustawiono zagubienie tokena dla komputera nr: "
									+ nr);
					simulation
							.write("(rozwi¹zanie: po jakims czasie wystawiony zostaje nowy token)");
				}
			}
		});
		add(bOk);

		/** pozostale ustawienia */
		this.setResizable(false);
		setVisible(true);
	}

	public void doErrorBadBits(Frame frame) {
		if ((errorBits) && (frame != null)) {
			if (pos1 >= 0) {
				if (frame.data[pos1] == 1)
					frame.data[pos1] = 0;
				else
					frame.data[pos1] = 1;
			}

			if (pos2 >= 0) {
				if (frame.data[pos2] == 1)
					frame.data[pos2] = 0;
				else
					frame.data[pos2] = 1;
			}

			if (pos3 >= 0) {
				if (frame.data[pos3] == 1)
					frame.data[pos3] = 0;
				else
					frame.data[pos3] = 1;
			}
		}

	}

	public void actionPerformed(ActionEvent e) {
	}

}
