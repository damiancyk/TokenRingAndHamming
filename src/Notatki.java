
public class Notatki {

}
/*
 
 TEMAT
 symulacja pracy 8 komputerów po³¹czonych w pierœcieñ (token ring) stosuj¹cych korekcjê b³êdów.
 pomiêdzy wybranymi przez u¿ytkownika wêz³ami mo¿na przesy³aæ 8 i 16-bitowe informacje zabezpieczone kodem Hamminga.
 
1) komputery 1 i 3 maj¹ coœ do nadania
2) komputer 1 dostaje tokena i wysy³a z tokenem dane do 5 komputera
3) komputer 3 przechwytuje ramkê danych z tokenem, jednak przekazuje j¹ dalej, bo ramka jest zajêta (zawiera dane, które nie nale¿¹ do komputera 3)
4) dane z komputera 1 dostaje adresat, czyli komputer 5. odsy³a ramkê, ci¹gle "zajêt¹"
5) komputer 1 dostaje "zajêt¹" ramkê. zwalnia jej "zajêtoœæ"
6) token idzie do komputera 3, który mo¿e sobie do³¹czyæ swoje dane do niej i "zaj¹æ" j¹, wysy³aj¹c dalej
7) je¿eli komputer 2 te¿ bêdzie coœ chcia³ nadawaæ, to przechwyci tokena przed komputerem 3

ZREALIZOWANE ZABEZPIECZENIA:
1. kod Hamminga - rozpoznawanie i korekcja jednego bitu oraz rozpoznawanie b³êdu na dwóch bitach, a nawet trzech.
	algorytm podejmuje siê próby naprawy takiej informacji.
	operacje te wykonywane s¹ na danych d³ygoœci 8 i 16 bitów danych (plus bity kodu Hamminga).
2. ramka z tokenem posiada bit informuj¹cy o tym, ¿e odwiedzi³ on ju¿ monitor. 
	je¿eli tak siê sta³o, a ramka znowu odwiedzi³a monitor, to zostaje ona usuniêta, generowany jest nowy token, oraz sieæ zostaje naprawiona
	je¿eli ¿adna ramka nie odwiedzi³a monitora przez jakiœ czas VFT(Valid Frame Timer), to zostaje wystawiony nowy token
3. przerwanie kabla zostaje rozpoznane przez komputer, który chcia³ nim coœ przes³aæ.
	komputer taki wysy³a informacjê o przekonfigurowaniu sieci do monitora
4. uszkodzenie komputera tak¿e zostaje rozpoznane i sieæ zostaje przekonfigurowana.
	monitor czeka na ramkê z tokenem jakiœ czas. je¿eli jej nie dostanie, to.....
5. co jakiœ czas AMT(Active Monitor Timer) monitor wysy³a informacjê o poprawnym dzia³aniu do pozosta³ych komputerów

SCENARIUSZ:
1. symulacja startuje z zatrzymanymi w¹tkami
2. mamy mo¿liwoœæ edycji danych z k¹d dok¹d ma byc przesy³any komunikat
3. odpauzowyjemy sykumalcjê
4. wszelkie b³êdy ustawiamy w nowym okienkui po klikniêciu odpowiedniego przycisku
5. informacje o b³êdach znajduj¹ siê w dodatkowym polu


*/