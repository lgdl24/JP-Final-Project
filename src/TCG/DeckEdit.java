package TCG;

import java.util.ArrayList;
import java.util.List;

import EnemyCharactres.Enemy;
import EnemyCharactres.EnemyArcher;
import EnemyCharactres.EnemyCaptain;
import EnemyCharactres.EnemyGoblin;
import EnemyCharactres.EnemyHammerMan;
import EnemyCharactres.EnemyPawn;
import EnemyCharactres.EnemyPinkMon;
import EnemyCharactres.EnemyWarrior;
import EnemyCharactres.EnemyWhiteGiant;
import GC.Archer;
import GC.Captain;
import GC.GameCharacter;
import GC.Goblin;
import GC.HammerMan;
import GC.Pawn;
import GC.PinkMon;
import GC.Warrior;
import GC.WhiteGiant;

public class DeckEdit {
	List<GameCharacter> Deck;
	List<GameCharacter> PlayDeck;
	List<Enemy>enemyDeck;
	
    private Pawn pawn;
    private Archer archer;
    private Captain captain;
    private Goblin goblin;
    private HammerMan hammerMan;
    private PinkMon pinkMon;
    private WhiteGiant whiteGiant;
    private Warrior warrior;
    
    private EnemyPawn Epawn;
    private EnemyArcher Earcher;
    private EnemyCaptain Ecaptain;
    private EnemyGoblin Egoblin;
    private EnemyHammerMan EhammerMan;
    private EnemyPinkMon EpinkMon;
    private EnemyWhiteGiant EwhiteGiant;
    private EnemyWarrior Ewarrior;
    
	DeckEdit(){
		pawn=new Pawn(0, 0, GamePanel.characters, GamePanel.enemies);
		archer = new Archer(0, 0, GamePanel.characters, GamePanel.enemies);
		captain = new Captain(0, 0, GamePanel.characters, GamePanel.enemies);
		goblin  = new Goblin(0, 0, GamePanel.characters, GamePanel.enemies);
		hammerMan = new HammerMan(0, 0, GamePanel.characters, GamePanel.enemies);
		pinkMon = new PinkMon(0, 0, GamePanel.characters, GamePanel.enemies);
		whiteGiant = new WhiteGiant(0, 0, GamePanel.characters, GamePanel.enemies);
		warrior = new Warrior(0, 0, GamePanel.characters, GamePanel.enemies);
		
		Epawn=new EnemyPawn(0, 0, GamePanel.characters, GamePanel.enemies);
		Earcher = new EnemyArcher(0, 0, GamePanel.characters, GamePanel.enemies);
		Ecaptain = new EnemyCaptain(0, 0, GamePanel.characters, GamePanel.enemies);
		Egoblin  = new EnemyGoblin(0, 0, GamePanel.characters, GamePanel.enemies);
		EhammerMan = new EnemyHammerMan(0, 0, GamePanel.characters, GamePanel.enemies);
		EpinkMon = new EnemyPinkMon(0, 0, GamePanel.characters, GamePanel.enemies);
		EwhiteGiant = new EnemyWhiteGiant(0, 0, GamePanel.characters, GamePanel.enemies);
		Ewarrior = new EnemyWarrior(0, 0, GamePanel.characters, GamePanel.enemies);
		
		
		Deck = new ArrayList<>();
		PlayDeck = new ArrayList<>();
		enemyDeck = new ArrayList<>();
		
		allCard();
		setEnemyDeck();
		userPlayDeck();
	}
    
    void allCard() {
    	Deck.add(archer);
    	Deck.add(hammerMan);
    	Deck.add(pinkMon);
    	Deck.add(warrior);
    	Deck.add(captain);
    	Deck.add(pawn);
    	Deck.add(goblin);
    	Deck.add(whiteGiant);
    }
    
    void setEnemyDeck() {
    	enemyDeck.add(Earcher);
    	enemyDeck.add(EhammerMan);
    	enemyDeck.add(EpinkMon);
    	enemyDeck.add(Ewarrior);
    	enemyDeck.add(Ecaptain);
    	enemyDeck.add(Epawn);
    	enemyDeck.add(Egoblin);
    	enemyDeck.add(EwhiteGiant);
    }
    void userPlayDeck() {
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    	PlayDeck.add(null);
    }
}
