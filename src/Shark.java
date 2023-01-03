public class Shark {
	
	public static void main(String[] args) {
		String playerList[] = {"Player A", "Player B", "Player C"}; // Test
		int sharesPerCompany = 20;
		int markersPerCompany = 18;
		
		SharkGUI m = new SharkGUI(playerList);
		Company NYSO = new Company(sharesPerCompany, markersPerCompany);
		Company Wings = new Company(sharesPerCompany, markersPerCompany);
		Company Empire = new Company(sharesPerCompany, markersPerCompany);
		Company SmithAndSmith = new Company(sharesPerCompany, markersPerCompany);
		
		// Some GUI methods ...
		m.setPlayerInfo(playerList[(int)(Math.random()*playerList.length)]);
		
		m.setSharePrice(0, 1000);
		m.setSharePrice(1, 7000);
		m.setSharePrice(2, 14000);
		m.setSharePrice(3, 12000);
		
		m.setForcedSalePlayerLabel(playerList[(int)(Math.random()*playerList.length)]);
		
		m.setForcedSaleBalance(-2000);
		
		//m.disableBoard();
		
	}
}