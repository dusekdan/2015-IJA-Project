# FIT BUT, IJA - Java game Labyrinth
*School project for IJA (Java) class, Faculty of Information Technology, Brno University of Technology.*

Board game Labyrinth created in Java by  [Daniel Dusek](https://github.com/DusekDan) (xdusek21) and [Filip Kalous](https://github.com/Strihtrs) (xkalou03).

To run the game, enter the directory where this project structure is located in console and run command `ant compile`.

This action will download images from the student's website and store them to application resources. Note that this will stop working the very moment xdusek21's school account seize to exist. 

Right after that run the game with command `ant run` and you are ready to go. 

After launching the game, you will have several options how to start playing:

1. Create a new game using button that is located in the left menu
    * while creating a new game, you can choose the size of the gameboard (5x5, 7x7, 9x9, 11x11),
    * you can also select how many players will be in the game,
    * and finally, the amount of treasures that will be generated on the gamefield (12 or 24, for 5x5 it is only allowed the amount of 12)

2. Load the game, from a saved file on your hard drive

3. When playing, there is option to save the game, that will save the current state of the game. This process saves the game to the *.dat file, that you can use later to load the game.

GOOD LUCK & HAVE FUN!

- xdusek21, xkalou03
___
# FIT VUT, IJA - Java hra Labyrinth
*Školní projekt do předmětu IJA (Java), Fakulta Informačních Technologií, Vysoké Učení Technické Brno.*

Implementace deskové hry Labyrinth napsaná studenty [Daniel Dusek](https://github.com/DusekDan) (xdusek21) a [Filip Kalous](https://github.com/Strihtrs) (xkalou03).

Pro spuštění hry se v terminálu navigujte do složky obsahující `build.xml` soubor a spusťte příkaz `ant compile`.

Tato akce stáhne obrázková data ze studentského webu hostované na stránkách fakulty a uloží je mezi resources aplikace. Dobou co xdusek21 ukončí studia na VUT, přestane stahování obrázkových dat fungovat, neb tou dobou i zanikne jeho účet.

Po stažení obrázkových dat nastartujte hru pomocí příkazu `ant run`. Nyní můžete začít hru několika způsoby:

1. Vytvořením nové hry tlačítkem v levém menu
   * při vytváření hry je možné vybrat velikost herní plochy (5x5, 7x7, 9x9, 11x11),
   * je možné vybrat počet hráčů,
   * je možné nastavit počet pokladů, které budou generovány v herní ploše (12 nebo 24, pro plochu 5x5 je možné vybrat pouze 12).
   
2. Načíst hru za save file-u uloženého na disku.

3. Při hře je navíc možné uložit hru pomocí tlačítka **Save game**. Tato operace uloží aktuální stav hry do *.dat souboru.

GL & HF -xdusek21, xkalou03
