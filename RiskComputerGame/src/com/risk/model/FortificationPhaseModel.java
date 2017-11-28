package com.risk.model;

import java.io.IOException;
import java.util.*;

public class FortificationPhaseModel extends Observable {

	
	public static TreeSet<String> fortifySet = new TreeSet<String>();
	

	/**
	 * This method allows the player to move the armies after attack phase has been completed.
	 * <p>
	 * The armies will only be moved if the there is adjacency between two countries and also
	 * If each territory has armies equal to or more than one.
	 * </p>
	 * @author Navjot
	 */

	public static int fortifySetEmpty = 0;

	private int fortifyUnits ;
	
	private int updatedSource;
	
	private int updatedDest;
	
	private String sourceTerr; 
	
	private String destTerr;
	
	private String player;
	
	private String msgUI;
	
	public String getMsgUI() {
		return msgUI;
	}


	public void setMsgUI(String msgUI) {
		this.msgUI = msgUI;
	}


	public String getPlayer() {
		return player;
	}


	public void setPlayer(String player) {
		this.player = player;
	}


	public String getDestTerr() {
		return destTerr;
	}


	public void setDestTerr(String destTerr) {
		this.destTerr = destTerr;
	}


	public String getSourceTerr() {
		return sourceTerr;
	}


	public void setSourceTerr(String sourceTerr) {
		this.sourceTerr = sourceTerr;
	}


	public int getFortifyUnits() {
		return fortifyUnits;
	}


	public void setFortifyUnits(int randomUnits) {
		this.fortifyUnits = randomUnits;
	}

	public int getUpdatedSource() {
		return updatedSource;
	}


	public void setUpdatedSource(int updatedSource) {
		this.updatedSource = updatedSource;
	}

	
	public int getUpdatedDest() {
		return updatedDest;
	}


	public void setUpdatedDest(int updatedDest) {
		this.updatedDest = updatedDest;
	}

	public static HashMap<String,List<String>> territoryMap = new HashMap<String,List<String>>();
	
	public static void createFortifySet(String player,HashMap<String,List<String>> territoryMap){
		
	   fortifySet.clear();
		
       for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()){
    	   
    	   String[] keyArray = playerInfoKey.split("-");

    	   if(keyArray[0].equals(player) || keyArray[0] == player){
    		   
    		   
	    	   if(StartUpPhaseModel.playerInfo.get(playerInfoKey) >= 1){
	    		   
	    		   
	    		   
	    		   String continent = keyArray[2];
	    		   String territory = keyArray[1];
	    		   String mapKey = continent + "," + territory;
	    		   
	    		   //change to continentHashMap
	    		   List<String> adjacencyList = territoryMap.get(mapKey);
	    		   
	    		   //check if player own any adjacent territory to one which already owns
	    		   for(String adjacentTerr : adjacencyList){
	    			   
	    			   String key = player + "-" + adjacentTerr + "-" + StartUpPhaseModel.terrCont.get(adjacentTerr);
	    			   
	    			   if(StartUpPhaseModel.playerInfo.containsKey(key)){
	    				   
	    				   fortifySet.add(territory + "-" +adjacentTerr);
	    				   
	    			   }
	    			   
	    		   }//end for(String adjacentTerr : adjacencyList)
	    		   
	    	   }//end if(PlayerClass.playerInfo.get(playerInfoKey) >= 1)
	    	   
    	   }//end if(keyArray[0].equals(player) || keyArray == player){
    	   
       }//end for(String playerInfoKey : PlayerClass.playerInfo.keySet())
    	
	}//end method createFortifySet
	
	
	public void randomFortification(String player){
		
		Random random = new Random();
		
		List<String> fortifyList = new ArrayList<String>(fortifySet);
		
		fortifySetEmpty = 0;
		
		setPlayer(player);
		
		if(fortifyList.isEmpty()){
			
			fortifySetEmpty = 1;
			
			setChanged();
			
			notifyObservers(this);
			
			try {
				System.in.read();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
			
		}
		

		String randomPath = fortifyList.get(random.nextInt(fortifyList.size()));
				
		String[] randomTerr = randomPath.split("-");
		
		String fromTerr = randomTerr[0];
		
		String toTerr = randomTerr[1];
		
		String fromCont = StartUpPhaseModel.terrCont.get(fromTerr);
		
		String toCont = StartUpPhaseModel.terrCont.get(toTerr);
		
		String from = player + "-" + fromTerr + "-" + fromCont;
		
		String to = player + "-" + toTerr + "-" + toCont;
		
		int fromArmies = StartUpPhaseModel.playerInfo.get(from);
		
		int toArmies = StartUpPhaseModel.playerInfo.get(to);
		
		int value = 0;
		
		if(fromArmies > 1){
			Random randomArmies = new Random();
			
			value = randomArmies.nextInt(fromArmies - 1) + 1; 
		}
		
		fromArmies = fromArmies - value;
		
		toArmies = toArmies + value;
		
		StartUpPhaseModel.playerInfo.put(from, fromArmies);
		
		StartUpPhaseModel.playerInfo.put(to, toArmies);
		
		setPlayer(player);
		
		setFortifyUnits(value);
		
		setUpdatedSource(fromArmies);
		
		setUpdatedDest(toArmies);
	
		setSourceTerr(fromTerr);
		
		setDestTerr(toTerr);
		
		setChanged();
		
		notifyObservers(this);
		
		try {
			System.in.read();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    public void aggressiveFortification(String player){
		
		List<String> fortifyList = new ArrayList<String>(fortifySet);
		
		fortifySetEmpty = 0;
		
		setPlayer(player);
		
		if(fortifyList.isEmpty()){
			
			fortifySetEmpty = 1;
			
			setChanged();
			
			notifyObservers(this);
			
			try {
				System.in.read();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;	
		}
		
		int highestArmies = 0;
				
		TreeSet<Integer> unitsSet = new TreeSet<Integer>();

		for(String path : fortifyList){
			
			String[] pathSplit = path.split("-");
			
			String fromTerr = pathSplit[0];
			
			String toTerr = pathSplit[1];
			
			
			//populate unitsSet with the different number armies of receiving territories
			for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {

				String[] playerVals = playerInfoKey.split("-");

				if (playerVals[0].equals(player) || playerVals[0] == player) {
					
					if(playerVals[1].equals(toTerr) || playerVals[1] == toTerr){
						
						unitsSet.add(StartUpPhaseModel.playerInfo.get(playerInfoKey));
						
					}//end if(playerVals[1].equals(fromTerr) || playerVals[1] == fromTerr)

				}// end if(playerVals[0].equals(player) || playerVals[0] == player)

			}// end for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet())
			
		}//end for(String path : fortifyList)
		
		
		//get the highest number of armies among the recieving territories
		highestArmies = unitsSet.last();
		
		
		// create a list of eligible keys from playerInfo HashMap
		// for only the player concerned with the highestArmies
		List<String> strongestTerritoryList = new ArrayList<String>();


		// populate strongestTerritoryList
		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {

			String[] playerVals = playerInfoKey.split("-");

			if (playerVals[0].equals(player) || playerVals[0] == player) {

				if (StartUpPhaseModel.playerInfo.get(playerInfoKey) == highestArmies) {

					strongestTerritoryList.add(playerInfoKey);

				} // end if(StartUpPhaseModel.playerInfo.get(playerInfoKey) > highestArmies)

			} // end if(playerVals[0].equals(player) || playerVals[0] == player)

		} // end for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()

		Random randomKey = new Random();

		// choose strongest recieving territory to move armies into
		String randomStrongestKey = strongestTerritoryList.get(randomKey.nextInt(strongestTerritoryList.size()));
		
		
		
		String toTerr = randomStrongestKey.split("-")[1];
		
		String fromTerr = new String();
		
		//get fromTerr
		
		for(String path : fortifyList){
			
			String[] pathSplit = path.split("-");
			
			if(toTerr.equals(pathSplit[1])){
				
				fromTerr = pathSplit[0];
				break;
				
			}//end if(toTerr.equals(pathSplit[1]))
			
		}//end for(String path : fortifyList)
		
		int playerInfoValue = StartUpPhaseModel.playerInfo.get(randomStrongestKey);
		
		String fromCont = StartUpPhaseModel.terrCont.get(fromTerr);
		
		String toCont = StartUpPhaseModel.terrCont.get(toTerr);
		
		String from = player + "-" + fromTerr + "-" + fromCont;
		
		String to = player + "-" + toTerr + "-" + toCont;
		
		int fromArmies = StartUpPhaseModel.playerInfo.get(from);
		
		int toArmies = StartUpPhaseModel.playerInfo.get(to);
		
		int value = fromArmies - 1;
		
		fromArmies = fromArmies - value;
		
		toArmies = toArmies + value;
		
		StartUpPhaseModel.playerInfo.put(from, fromArmies);
		
		StartUpPhaseModel.playerInfo.put(to, toArmies);
		
		setPlayer(player);
		
		setFortifyUnits(value);
		
		setUpdatedSource(fromArmies);
		
		setUpdatedDest(toArmies);
	
		setSourceTerr(fromTerr);
		
		setDestTerr(toTerr);
		
		setChanged();
		
		notifyObservers(this);
		
		try {
			System.in.read();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}//end aggressiveFortification
	
	public void benevolentFortification(String player){
		

		List<String> fortifyList = new ArrayList<String>(fortifySet);
		
		fortifySetEmpty = 0;
		
		setPlayer(player);
		
		if(fortifyList.isEmpty()){
			
			fortifySetEmpty = 1;
			
			setChanged();
			
			notifyObservers(this);
			
			try {
				System.in.read();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;	
		}
		
		int lowestArmies = 0;
				
		TreeSet<Integer> unitsSet = new TreeSet<Integer>();

		for(String path : fortifyList){
			
			String[] pathSplit = path.split("-");
			
			String fromTerr = pathSplit[0];
			
			String toTerr = pathSplit[1];
			
			
			//populate unitsSet with the different number armies of receiving territories
			for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {

				String[] playerVals = playerInfoKey.split("-");

				if (playerVals[0].equals(player) || playerVals[0] == player) {
					
					if(playerVals[1].equals(toTerr) || playerVals[1] == toTerr){
						
						unitsSet.add(StartUpPhaseModel.playerInfo.get(playerInfoKey));
						
					}//end if(playerVals[1].equals(fromTerr) || playerVals[1] == fromTerr)

				}// end if(playerVals[0].equals(player) || playerVals[0] == player)

			}// end for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet())
			
		}//end for(String path : fortifyList)
		
		
		//get the lowest number of armies among the recieving territories
		lowestArmies = unitsSet.first();
		
		
		//create a list of eligible keys from playerInfo HashMap
		//for only the player concerned with the lowestArmies
		List<String> weakestTerritoryList = new ArrayList<String>();


		//populate weakestTerritoryList
		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {

			String[] playerVals = playerInfoKey.split("-");

			if (playerVals[0].equals(player) || playerVals[0] == player) {

				if (StartUpPhaseModel.playerInfo.get(playerInfoKey) == lowestArmies) {

					weakestTerritoryList.add(playerInfoKey);

				} // end if(StartUpPhaseModel.playerInfo.get(playerInfoKey) > highestArmies)

			} // end if(playerVals[0].equals(player) || playerVals[0] == player)

		} // end for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()

		Random randomKey = new Random();

		// choose strongest recieving territory to move armies into
		String randomStrongestKey = weakestTerritoryList.get(randomKey.nextInt(weakestTerritoryList.size()));
		
		String toTerr = randomStrongestKey.split("-")[1];
		
		String fromTerr = new String();
		
		//get fromTerr
		
		for(String path : fortifyList){
			
			String[] pathSplit = path.split("-");
			
			if(toTerr.equals(pathSplit[1])){
				
				fromTerr = pathSplit[0];
				break;
				
			}//end if(toTerr.equals(pathSplit[1]))
			
		}//end for(String path : fortifyList)
		
		int playerInfoValue = StartUpPhaseModel.playerInfo.get(randomStrongestKey);
		
		String fromCont = StartUpPhaseModel.terrCont.get(fromTerr);
		
		String toCont = StartUpPhaseModel.terrCont.get(toTerr);
		
		String from = player + "-" + fromTerr + "-" + fromCont;
		
		String to = player + "-" + toTerr + "-" + toCont;
		
		int fromArmies = StartUpPhaseModel.playerInfo.get(from);
		
		int toArmies = StartUpPhaseModel.playerInfo.get(to);
		
		int value = fromArmies - 1;
		
		fromArmies = fromArmies - value;
		
		toArmies = toArmies + value;
		
		StartUpPhaseModel.playerInfo.put(from, fromArmies);
		
		StartUpPhaseModel.playerInfo.put(to, toArmies);
		
		
		setPlayer(player);
		
		setFortifyUnits(value);
		
		setUpdatedSource(fromArmies);
		
		setUpdatedDest(toArmies);
	
		setSourceTerr(fromTerr);
		
		setDestTerr(toTerr);
		
		
		setChanged();
		
		notifyObservers(this);
		
		try {
			System.in.read();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}//end benevolentFortification
	

	

	public static void main(String [] args){
		
		HashMap<String,List<String>> territoryMap = new HashMap<String,List<String>>();
		
		List<String> adjList = new ArrayList<String>();
		adjList.add("China");
		adjList.add("Pakistan");
		adjList.add("Sri Lanka");
		territoryMap.put("Asia,India",adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Afghanistan");
		adjList.add("Iran");
		territoryMap.put("Asia,Pakistan",adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("India");
		adjList.add("Japan");
		adjList.add("Russia");
		territoryMap.put("Asia,China",adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("China");
		adjList.add("Russia");
		territoryMap.put("Asia,Japan", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Pakistan");
		adjList.add("Iran");
		territoryMap.put("Asia,Afghanistan", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("China");
		adjList.add("Japan");
		adjList.add("Canada");
		territoryMap.put("Asia,Russia", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Pakistan");
		adjList.add("Afghanistan");
		adjList.add("Greece");
		territoryMap.put("Asia,Iran", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("India");
		territoryMap.put("Asia,Sri Lanka", adjList);
		 
		
		adjList = new ArrayList<String>();
		adjList.add("France");
		adjList.add("Ireland");
		territoryMap.put("Europe,England", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("England");
		adjList.add("Spain");
		adjList.add("Germany");
		territoryMap.put("Europe,France", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("France");
		adjList.add("Italy");
		territoryMap.put("Europe,Spain", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("France");
		adjList.add("Ukraine");
		territoryMap.put("Europe,Germany", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Spain");
		adjList.add("Greece");
		adjList.add("Germany");
		territoryMap.put("Europe,Italy", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("England");
		adjList.add("Canada");
		territoryMap.put("Europe,Ireland", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Russia");
		adjList.add("Germany");
		territoryMap.put("Europe,Ukraine", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Italy");
		adjList.add("Iran");
		territoryMap.put("Europe,Greece", adjList);
	
		adjList = new ArrayList<String>();
		adjList.add("USA");
		territoryMap.put("North America,Mexico", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Canada");		
		territoryMap.put("North America,USA", adjList);
		
		adjList = new ArrayList<String>();
		adjList.add("Russia");	
		adjList.add("Ireland");
		adjList.add("USA");
		territoryMap.put("North America,Canada", adjList);
		
		/*
		territoryMap.put("South America,Brazil", null);
		territoryMap.put("South America,Colombia", null);
		territoryMap.put("South America,Argentina", null);
		territoryMap.put("South America,Peru", null);
		
		territoryMap.put("Africa,Egypt", null);
		territoryMap.put("Africa,Zimbabwe", null);
		territoryMap.put("Africa,Libya", null);
		territoryMap.put("Africa,Sudan", null);
		territoryMap.put("Africa,South Africa", null);
		territoryMap.put("Africa,Ghana", null);
	*/
		
		

		int numberOfPlayers = 6;

		StartUpPhaseModel.preStartUp(numberOfPlayers, territoryMap);
		
//		ReinforcementPhaseModel.calculateReinforcement("1");
//		ReinforcementPhaseModel.calculateReinforcement("2");
//		ReinforcementPhaseModel.calculateReinforcement("3");
		
		System.out.println("----------------------------------------");
//		ReinforcementPhaseModel.reinforceRandom("1");
//		ReinforcementPhaseModel.reinforceRandom("2");
//		ReinforcementPhaseModel.reinforceRandom("3");
		
		
		System.out.println(StartUpPhaseModel.playerInfo);
		
		createFortifySet("1",territoryMap);
		System.out.println("--------\nPlayer1 ::"+fortifySet);
		System.out.println("--------\n"+StartUpPhaseModel.playerInfo);
		
//		randomFortification("1");
//		System.out.println("--------\nAfter::"+StartUpPhaseModel.playerInfo);
//		
//		createFortifySet("2",territoryMap);
//		System.out.println("--------\nPlayer2 ::"+fortifySet);
//		System.out.println("--------\n"+StartUpPhaseModel.playerInfo);
//		
//		randomFortification("2");
//		System.out.println("--------\nAfter::"+StartUpPhaseModel.playerInfo);
//		
//		createFortifySet("3",territoryMap);
//		System.out.println("--------\nPlayer3 ::"+fortifySet);
//		System.out.println("--------\n"+StartUpPhaseModel.playerInfo);
//		
//
//		randomFortification("3");
//		System.out.println("--------\nAfter::"+StartUpPhaseModel.playerInfo);
	}
	
	
}



