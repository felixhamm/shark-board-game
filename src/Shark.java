import java.util.Random;

public class Shark {
	
	public static void main(String[] args) {

		int randNum;
		int activePlayer = 0;
		int numOfPlayers = 2;
		int activeCompany;
		int activeZone;
		
		Random rand = new Random();

		Company NYSO = new Company();
		Company Wings = new Company();
		Company Empire = new Company();
		Company SmithAndSmith = new Company();


		Player PlayerA = new Player("Felix_H");
		Player PlayerB = new Player("Felix_I");

		
		String playerList[] = {PlayerA.getName(),PlayerB.getName()};

		SharkGUI m = new SharkGUI(playerList);
		m.disableBoard();
		
		// Some GUI methods ...
		//m.setPlayerInfo(playerList[(int)(Math.random()*playerList.length)]);
		
		m.setSharePrice(SharkConstants.NYSO, NYSO.getSharePrice());
		m.setSharePrice(SharkConstants.WINGS, Wings.getSharePrice());
		m.setSharePrice(SharkConstants.EMPIRE, Empire.getSharePrice());
		m.setSharePrice(SharkConstants.SMITH_AND_SMITH, SmithAndSmith.getSharePrice());

		for (int i = 0; i < numOfPlayers; i++){			//Muss erweitert werden falls mehr als zwei Spieler spielen
			switch(i){
				case 0:
					randNum = rand.nextInt(4);
					PlayerA.setShares(randNum, 1);
					m.setNumOfShares(i,randNum+1,PlayerA.getShares(randNum));
					break;
				case 1:
					randNum = rand.nextInt(4);
					PlayerB.setShares(randNum, 1);
					m.setNumOfShares(i,randNum+1,PlayerB.getShares(randNum));
					break;
			}
		}

		
		m.setPlayerInfo(null);

		
		//m.setForcedSalePlayerLabel(playerList[(int)(Math.random()*playerList.length)]);
		
		//m.setForcedSaleBalance(-2000);
		
		//m.disableBoard();


		
		
	}

	private int numDice(){
		Random d = new Random();
		int num = d.nextInt(6) + 1;
		return num;
	}

	private int colorDice(){
		Random d = new Random();
		int num = d.nextInt(4) + 1;
		return num;
	}

}