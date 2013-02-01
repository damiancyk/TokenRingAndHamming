import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Hamming {
	private JTextArea tObszarTekstowy;
	private JScrollPane jScrollPane;

	int bitsActivity[][] = {
			{ 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 }, // 1(0)
			{ 0, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0 }, // 2(1)
			{ 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1 }, // 4(3)
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 }, // 8(7)
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 } // 16(15)
	};

	public Hamming(JTextArea tObszarTekstowy, JScrollPane jScrollPane) {

	}

	public void code_12_8(int m[], int mC[], boolean showInfo) {
		// (12,8)//xx1x111x1111 //wzorzec

		int posMessage = 0;
		for (int x = 0; x < 12; x++) {
			if (!((x == 0) || (x == 1) || (x == 3) || (x == 7))) {
				mC[x] = m[posMessage];
				posMessage++;
			}
		}

		for (int i = 0; i < 4; i++) {
			int x = (int) Math.pow(2, i) - 1;
			for (int y = 0; y < 12; y++) {
				mC[x] += mC[y] * bitsActivity[i][y];
			}
			mC[x] %= 2;
		}

		if (showInfo) {
			for (int i = 0; i < 12; i++)
				System.out.print(mC[i] + " ");
			System.out.print(" - zakodowana wiadomoœæ");
			System.out.print("\n");
		}
	}

	public int check_12_8(int mC[], boolean showInfo) {
		// (12,8)//xx1x111x1111 //wzorzec

		int posBadBit = -1;

		if (showInfo) {
			for (int i = 0; i < 12; i++)
				System.out.print(mC[i] + " ");
			System.out.print(" - uszkodzona wiadomoœæ");
			System.out.print("\n");
		}

		int mCC[] = new int[4]; // pomocnicza tablica zliczaj¹ca bity kontrolne
								// sprawdzanej wiadomoœci

		// ponowne zliczenie bitów kontrolnych
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 12; y++) {
				mCC[x] += mC[y] * bitsActivity[x][y];
			}
			mCC[x] %= 2;
		}

		if ((mCC[0] == mC[0]) && (mCC[1] == mC[1]) && (mCC[2] == mC[3])
				&& (mCC[3] == mC[7])) {
			if (showInfo)
				System.out
						.println("wszystkie bity kontrolne maj¹ dobre wskazania, wiadomoœæ jest poprawna");
		} else {
			int whichBit = 0; // który bit jest uszkodzony
			if (mCC[0] != mC[0]) {
				if (showInfo)
					System.out.println("bit kontrolny 1(0) Ÿle wskazuje");
				whichBit++;
			}
			if (mCC[1] != mC[1]) {
				if (showInfo)
					System.out.println("bit kontrolny 2(1) Ÿle wskazuje");
				whichBit += 2;
			}
			if (mCC[2] != mC[3]) {
				if (showInfo)
					System.out.println("bit kontrolny 4(3) Ÿle wskazuje");
				whichBit += 4;
			}
			if (mCC[3] != mC[7]) {
				if (showInfo)
					System.out.println("bit kontrolny 8(7) Ÿle wskazuje");
				whichBit += 8;
			}
			if (showInfo)
				System.out.print("uszkodzony bit: " + whichBit
						+ " (pozycja w tablicy: " + (whichBit - 1) + ")\n");
			if (mC[whichBit - 1] == 1)
				mC[whichBit - 1] = 0;
			else
				mC[whichBit - 1] = 1;
			posBadBit = (whichBit - 1);
		}

		return posBadBit;
	}

	public void code_21_16(int m[], int mC[], boolean showInfo) {
		// (21,16)//xx1x111x1111111x11111 //wzorzec

		int posMessage = 0;
		for (int x = 0; x < 21; x++) {
			if (!((x == 0) || (x == 1) || (x == 3) || (x == 7) || (x == 15))) {
				mC[x] = m[posMessage];
				posMessage++;
			}
		}

		for (int i = 0; i < 5; i++) {
			int x = (int) Math.pow(2, i) - 1;
			for (int y = 0; y < 21; y++) {
				mC[x] += mC[y] * bitsActivity[i][y];
			}
			mC[x] %= 2;
		}

		if (showInfo) {
			for (int i = 0; i < 21; i++)
				System.out.print(mC[i] + " ");
			System.out.print(" - zakodowana wiadomoœæ");
			System.out.print("\n");
		}
	}

	public int check_21_16(int mC[], boolean showInfo) {
		// (21,16)//xx1x111x1111111x11111 //wzorzec

		int posBadBit = -1;

		if (showInfo) {
			for (int i = 0; i < 21; i++)
				System.out.print(mC[i] + " ");
			System.out.print(" - uszkodzona wiadomoœæ");
			System.out.print("\n");
		}

		int mCC[] = new int[5]; // pomocnicza tablica zliczaj¹ca bity kontrolne
								// sprawdzanej wiadomoœci

		// ponowne zliczenie bitów kontrolnych
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 21; y++) {
				mCC[x] += mC[y] * bitsActivity[x][y];
			}
			mCC[x] %= 2;
		}

		if ((mCC[0] == mC[0]) && (mCC[1] == mC[1]) && (mCC[2] == mC[3])
				&& (mCC[3] == mC[7]) && (mCC[4] == mC[15])) {
			if (showInfo)
				System.out
						.println("wszystkie bity kontrolne maj¹ dobre wskazania, wiadomoœæ jest poprawna");
		} else {
			int whichBit = 0; // który bit jest uszkodzony
			if (mCC[0] != mC[0]) {
				if (showInfo)
					System.out.println("bit kontrolny 1(0) Ÿle wskazuje");
				whichBit++;
			}
			if (mCC[1] != mC[1]) {
				if (showInfo)
					System.out.println("bit kontrolny 2(1) Ÿle wskazuje");
				whichBit += 2;
			}
			if (mCC[2] != mC[3]) {
				if (showInfo)
					System.out.println("bit kontrolny 4(3) Ÿle wskazuje");
				whichBit += 4;
			}
			if (mCC[3] != mC[7]) {
				if (showInfo)
					System.out.println("bit kontrolny 8(7) Ÿle wskazuje");
				whichBit += 8;
			}
			if (mCC[4] != mC[15]) {
				if (showInfo)
					System.out.println("bit kontrolny 16(15) Ÿle wskazuje");
				whichBit += 16;
			}
			if (showInfo)
				System.out.print("uszkodzony bit: " + whichBit
						+ " (pozycja w tablicy: " + (whichBit - 1) + ")\n");
			if (mC[whichBit - 1] == 1)
				mC[whichBit - 1] = 0;
			else
				mC[whichBit - 1] = 1;
			posBadBit = whichBit - 1;
		}

		return posBadBit;
	}

	public int[] decode(int mC[]) {
		int length = mC.length;
		int posMessage = 0;
		int m[] = null;

		if (length == 12) {
			m = new int[8];
			for (int x = 0; x < 12; x++) {
				if (!((x == 0) || (x == 1) || (x == 3) || (x == 7))) {
					m[posMessage] = mC[x];
					posMessage++;
				} else {

				}
			}
		} else if (length == 21) {
			m = new int[16];
			for (int x = 0; x < 21; x++) {
				if (!((x == 0) || (x == 1) || (x == 3) || (x == 7) || (x == 15))) {
					m[posMessage] = mC[x];
					posMessage++;
				} else {

				}
			}
		} else {

		}
		
		return m;
	}

	public void write(String x) {
		tObszarTekstowy.append("@@Hamming>> " + x + "\n");
		jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

}
