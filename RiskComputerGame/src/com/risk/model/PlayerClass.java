package com.risk.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Observable;
import java.util.TreeSet;

public class PlayerClass extends Observable {
	
	
	
	public static String msg ; 
	
	
	public void gamePlay(int numberOfPlayers, HashMap<String,List<String>> territoryMap, HashMap<String, Integer> continentControlValueHashMap) throws InterruptedException{
		
		//startUpPhase method called
		PlayerClass.startUpPhase(numberOfPlayers);
		
		msg = "startup completed";
		setChanged();
		
		notifyObservers(msg);
		
		Thread.sleep(5000);
		
		
		
		
		int plyr = 1;
		
		int currentNumberOfPlayers = numberOfPlayers;
		
		//round robin for game starts
		while(true){
			
			//calculate number of players after each round robin to update in case a player is ousted
			currentNumberOfPlayers = PlayerClass.plyrsRemaining();
			
			
			//reset plyr to player 1 once all players have got their turn to play
			if(plyr > currentNumberOfPlayers ){
				plyr = 1;
			}
			
			
			//reinforcement phase method called
			PlayerClass playerClassObj = new PlayerClass();
			playerClassObj.reinforcementPhase(plyr);
			
			msg = "reinforce done";
			setChanged();
			notifyObservers(msg);
			
			
			Thread.sleep(5000);
			
			
			
			
			//attack phase method called
			PlayerClass.attackPhase(plyr);
			msg = "attack done";
			setChanged();
			notifyObservers(msg);
			
			
			Thread.sleep(5000);
			
			
			//if one player has all the territories i.e if player has won the game then break out of loop
			boolean victory = PlayerClass.checkPlyrVictory(plyr);
			if(victory){
				break;
			}
			
			
			Thread.sleep(5000);
			
			
			
			//fortification method called
			PlayerClass.fortificationPhase(plyr, territoryMap);
			msg = "fortification done";
			setChanged();
			notifyObservers(msg);
			
			Thread.sleep(5000);
			
			
			plyr++;
			
		}//end while(true)
		
		
	}
	
	
	
	public static void startUpPhase(int numberOfPlayers){
		
		StartUpPhaseModel.terrPerPlayerPopulate(numberOfPlayers,StartUpPhaseModel.totalTerr);
		
		
		StartUpPhaseModel.assignTerritories(numberOfPlayers, StartUpPhaseModel.countryTaken, StartUpPhaseModel.totalTerr);
		
		
		StartUpPhaseModel.deployArmiesRandomly(numberOfPlayers);
		
	}
	
	
	
	public  void reinforcementPhase(int plyr){
		
		//call method to calculate reinforcements by number of territories
		String reinTerrMsg = ReinforcementPhaseModel.calcReinforcementsByTerr(Integer.toString(plyr));
		
		PlayerClass.msg = "reinforcements by number of territories," + reinTerrMsg + ",";
		setChanged();
		notifyObservers(this);
		
		//call method to calculate cards
		
		
		
		
		//call method to calculate reinforcements if a player owns the whole continent
		ArrayList<String> cntrlValMsg = ReinforcementPhaseModel.calcReinforcementByCntrlVal(Integer.toString(plyr));
		String[] cntrlVal = cntrlValMsg.toArray(new String[cntrlValMsg.size()]);
		
		PlayerClass.msg = "reinforcements by control value-";
		for(String cntrValIndividual : cntrlVal){
			PlayerClass.msg = PlayerClass.msg + cntrValIndividual + "-";
		}
		setChanged();
		notifyObservers(this);
		
		
		
		ReinforcementPhaseModel.calculateReinforcement(Integer.toString(plyr));
		
		ReinforcementPhaseModel.reinforceRandom(Integer.toString(plyr));
	}
	
	
	
	public static void attackPhase(int plyr){
		
	}
	
	
	
	
	public static void fortificationPhase(int plyr,HashMap<String, List<String>> territoryMap){
		
		
		FortificationPhaseModel.createFortifySet(Integer.toString(plyr), territoryMap);
		
		FortificationPhaseModel.randomFortification(Integer.toString(plyr));
	}
	
	
	
	
	public static boolean checkPlyrVictory(int plyr){
		
		TreeSet<Integer> playerCheck = new TreeSet<Integer>();
		
		for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()){
			String[] playerInfoArr = playerInfoKey.split("-");
			
			playerCheck.add(Integer.valueOf(playerInfoArr[0]));
		}
		
		if(playerCheck.size() == 1){
			return true;
		}
		
		return false;
	}
	
	
	
	public static int plyrsRemaining(){
		
		int currentNumberOfPlyrs = 0;
		
		TreeSet<Integer> playerCheck = new TreeSet<Integer>();
		
		for(String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()){
			String[] playerInfoArr = playerInfoKey.split("-");
			
			playerCheck.add(Integer.valueOf(playerInfoArr[0]));
		}
		
		currentNumberOfPlyrs = playerCheck.size();
		
		return currentNumberOfPlyrs;
		
	}
	
	
}
