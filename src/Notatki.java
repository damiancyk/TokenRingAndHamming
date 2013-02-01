
public class Notatki {

}
/*
 
 TEMAT
 symulacja pracy 8 komputer�w po��czonych w pier�cie� (token ring) stosuj�cych korekcj� b��d�w.
 pomi�dzy wybranymi przez u�ytkownika w�z�ami mo�na przesy�a� 8 i 16-bitowe informacje zabezpieczone kodem Hamminga.
 
1) komputery 1 i 3 maj� co� do nadania
2) komputer 1 dostaje tokena i wysy�a z tokenem dane do 5 komputera
3) komputer 3 przechwytuje ramk� danych z tokenem, jednak przekazuje j� dalej, bo ramka jest zaj�ta (zawiera dane, kt�re nie nale�� do komputera 3)
4) dane z komputera 1 dostaje adresat, czyli komputer 5. odsy�a ramk�, ci�gle "zaj�t�"
5) komputer 1 dostaje "zaj�t�" ramk�. zwalnia jej "zaj�to��"
6) token idzie do komputera 3, kt�ry mo�e sobie do��czy� swoje dane do niej i "zaj��" j�, wysy�aj�c dalej
7) je�eli komputer 2 te� b�dzie co� chcia� nadawa�, to przechwyci tokena przed komputerem 3

ZREALIZOWANE ZABEZPIECZENIA:
1. kod Hamminga - rozpoznawanie i korekcja jednego bitu oraz rozpoznawanie b��du na dw�ch bitach, a nawet trzech.
	algorytm podejmuje si� pr�by naprawy takiej informacji.
	operacje te wykonywane s� na danych d�ygo�ci 8 i 16 bit�w danych (plus bity kodu Hamminga).
2. ramka z tokenem posiada bit informuj�cy o tym, �e odwiedzi� on ju� monitor. 
	je�eli tak si� sta�o, a ramka znowu odwiedzi�a monitor, to zostaje ona usuni�ta, generowany jest nowy token, oraz sie� zostaje naprawiona
	je�eli �adna ramka nie odwiedzi�a monitora przez jaki� czas VFT(Valid Frame Timer), to zostaje wystawiony nowy token
3. przerwanie kabla zostaje rozpoznane przez komputer, kt�ry chcia� nim co� przes�a�.
	komputer taki wysy�a informacj� o przekonfigurowaniu sieci do monitora
4. uszkodzenie komputera tak�e zostaje rozpoznane i sie� zostaje przekonfigurowana.
	monitor czeka na ramk� z tokenem jaki� czas. je�eli jej nie dostanie, to.....
5. co jaki� czas AMT(Active Monitor Timer) monitor wysy�a informacj� o poprawnym dzia�aniu do pozosta�ych komputer�w

SCENARIUSZ:
1. symulacja startuje z zatrzymanymi w�tkami
2. mamy mo�liwo�� edycji danych z k�d dok�d ma byc przesy�any komunikat
3. odpauzowyjemy sykumalcj�
4. wszelkie b��dy ustawiamy w nowym okienkui po klikni�ciu odpowiedniego przycisku
5. informacje o b��dach znajduj� si� w dodatkowym polu


*/