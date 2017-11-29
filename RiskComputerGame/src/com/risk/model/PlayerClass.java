package com.risk.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Observable;
import java.util.TreeSet;

import com.risk.behavior.*;
import com.risk.ui.CardExchangeUI;
import com.risk.ui.DeployArmiesUI;
import com.risk.ui.FortificationUI;
import com.risk.ui.PlayerDominationView;
import com.risk.ui.ReinforcementsUI;

/**
 * This is an Observable Class which has implementations for state change in the
 * game play
 * 
 * 
 * @author Navjot
 * @author Ashish
 */

public class PlayerClass extends Observable {

	private HashMap<String, String> dominationOld = new HashMap<String, String>();

	private HashMap<String, String> dominationNew = new HashMap<String, String>();

	public HashMap<String, String> getDominationOld() {
		return dominationOld;
	}

	public void setDominationOld(HashMap<String, String> dominationOld) {
		this.dominationOld = dominationOld;
	}

	public HashMap<String, String> getDominationNew() {
		return dominationNew;
	}

	public void setDominationNew(HashMap<String, String> dominationNew) {
		this.dominationNew = dominationNew;
	}

	public static int players = 0;

	public static HashMap<String, List<String>> currentMap;

	public static String msg;

	public static StrategyContext contextObj = new StrategyContext();

	public void gamePlay(int numberOfPlayers, HashMap<String, List<String>> territoryMap,
			HashMap<String, Integer> continentControlValueHashMap, HashMap<Integer, String> strategies)
			throws InterruptedException {

		currentMap = territoryMap;

		PlayerClass.players = numberOfPlayers;

		PlayerClass playerClassObj = new PlayerClass();

		// startUpPhase method called
		playerClassObj.startUpPhase(numberOfPlayers);

		PlayerClass.msg = "preDeployStartUp";

		setChanged();

		notifyObservers(this);

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StartUpPhaseModel startUpObj = new StartUpPhaseModel();
		DeployArmiesUI deployViewObj = new DeployArmiesUI();
		startUpObj.addObserver(deployViewObj);

		startUpObj.deployArmiesRandomly(numberOfPlayers);

		PlayerClass.msg = "postDeployStartUp";

		setChanged();

		notifyObservers(this);

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		calcDominationValues(StartUpPhaseModel.playerInfo, numberOfPlayers, StartUpPhaseModel.totalTerr);

		int plyr = 1;

		int currentNumberOfPlayers = numberOfPlayers;

		// PlayerDominationModel playerDominationObj = new PlayerDominationModel();

		// PlayerDominationView playerDominationView = new PlayerDominationView();

		// playerDominationObj.addObserver(playerDominationView);

		msg = "roundrobin";

		setChanged();

		notifyObservers(this);

		// playerDominationObj.calcDominationValues(StartUpPhaseModel.playerInfo,numberOfPlayers,
		// StartUpPhaseModel.totalTerr);

		// round robin for game starts

		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {

			// calculate number of players after each round robin to update in case a player
			// is ousted
			currentNumberOfPlayers = PlayerClass.plyrsRemaining();

			// reset plyr to player 1 once all players have got their turn to play
			if (plyr > currentNumberOfPlayers) {
				plyr = 1;

			}
			// set player strategy
			String currentPlyrStrategy = strategies.get(plyr);

			msg = "reinforceHead," + plyr;

			setChanged();
			notifyObservers(this);

			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// reinforcement phase method called
			playerClassObj.reinforcementPhase(plyr,currentPlyrStrategy, continentControlValueHashMap, currentMap);

			msg = "reinforce done," + plyr;
			setChanged();
			notifyObservers(msg);

			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			// attack phase method called
//			PlayerClass.attackPhase(plyr,currentPlyrStrategy, territoryMap);
//			msg = "attack done";
//			setChanged();
//			notifyObservers(msg);
//
//			try {
//				System.in.read();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			// playerDominationObj.calcDominationValues(StartUpPhaseModel.playerInfo,numberOfPlayers,
			// StartUpPhaseModel.totalTerr);

			// check for domination change
			boolean checkDomination = calcDominationValues(StartUpPhaseModel.playerInfo, numberOfPlayers,
					StartUpPhaseModel.totalTerr);

			if (checkDomination) {

				PlayerClass.msg = "domination";

				setChanged();

				notifyObservers(msg);

				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// if one player has all the territories i.e if player has won the game then
			// break out of loop
			boolean victory = PlayerClass.checkPlyrVictory(plyr);
			if (victory) {

				msg = "PLAYER " + plyr + " WINS";
				setChanged();
				notifyObservers(this);
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}

			msg = "pre fortification," + plyr;
			setChanged();
			notifyObservers(this);

			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// fortification method called
			playerClassObj.fortificationPhase(plyr, territoryMap,currentPlyrStrategy);

			msg = "fortification done," + plyr;
			setChanged();
			notifyObservers(this);

			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			plyr++;

		} // end while(true)

	}

	public void startUpPhase(int numberOfPlayers) {

		StartUpPhaseModel.terrPerPlayerPopulate(numberOfPlayers, StartUpPhaseModel.totalTerr);

		StartUpPhaseModel.assignTerritories(numberOfPlayers, StartUpPhaseModel.countryTaken,
				StartUpPhaseModel.totalTerr);

	}

	public void reinforcementPhase(int plyr, String currentPlyrStrategy, HashMap<String, Integer> continentControlValueHashMap,
			HashMap<String, List<String>> currentMap) {

		ReinforcementPhaseModel reinforcmentModelObj = new ReinforcementPhaseModel();
		ReinforcementsUI uiObj = new ReinforcementsUI();
		CardExchangeUI uiCardObj = new CardExchangeUI();

		reinforcmentModelObj.addObserver(uiObj);
		reinforcmentModelObj.addObserver(uiCardObj);
		
		
		if(currentPlyrStrategy.charAt(0) != 'c'){
			// call method to calculate reinforcements by number of territories
			String reinTerrMsg = reinforcmentModelObj.calcReinforcementsByTerr(Integer.toString(plyr));
	
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			PlayerClass.msg = "reinforcements by number of territories," + reinTerrMsg + ",";
			setChanged();
			notifyObservers(this);
	
			// call method to calculate reinforcements by cards
			reinforcmentModelObj.calcReinforcementByCards(Integer.toString(plyr));
			// PlayerClass.msg = "reinforcements by exchanging cards|" + reinTerrMsg + "|";
	
			setChanged();
	
			notifyObservers(this);
	
			try {
				System.in.read();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			// call method to calculate reinforcements if a player owns the whole continent
			ArrayList<String> cntrlValMsg = reinforcmentModelObj.calcReinforcementByCntrlVal(Integer.toString(plyr),
					continentControlValueHashMap);
	
			try {
				System.in.read();
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			String[] cntrlVal = cntrlValMsg.toArray(new String[cntrlValMsg.size()]);
	
			PlayerClass.msg = "reinforcements by control value-";
	
			for (String cntrValIndividual : cntrlVal) {
				PlayerClass.msg = PlayerClass.msg + cntrValIndividual + "-";
			}
	
			setChanged();
			notifyObservers(this);
		
		}//end if(currentPlyrStrategy.charAt(0) != 'c')


		ReinforcementsUI uiObj2 = new ReinforcementsUI();

		switch (currentPlyrStrategy.charAt(0)) {

		case 'a': {
			AggressiveBehaviorImpl agressiveObj = new AggressiveBehaviorImpl();
			contextObj.setStrategy(agressiveObj);
			break;
		}
		case 'r': {
			RandomBehaviorImpl randomObj = new RandomBehaviorImpl();
			contextObj.setStrategy(randomObj);
			break;
		}
		case 'b': {
			BenevolantBehaviorImpl benevolentObj = new BenevolantBehaviorImpl();
			contextObj.setStrategy(benevolentObj);
			break;
		}
		case 'c': {
			CheaterBehaviorImpl cheaterObj = new CheaterBehaviorImpl();
			contextObj.setStrategy(cheaterObj);
			//cheaterObj.addObserver(uiObj2);
			break;
		}
		
		case 'h': {
			HumanBehaviorImpl humanObj = new HumanBehaviorImpl();
			contextObj.setStrategy(humanObj);
			break;
		}
		
		default :
			break;
		}

		contextObj.executeReinforce(Integer.toString(plyr));

		try {
			System.in.read();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * method for initiating the attack phase
	 */
	public static void attackPhase(int plyr, String currentPlyrStrategy, HashMap<String, List<String>> territoryMap) {
		 
		
		switch (currentPlyrStrategy.charAt(0)) {

		case 'a': {
			AggressiveBehaviorImpl agressiveObj = new AggressiveBehaviorImpl();
			contextObj.setStrategy(agressiveObj);
 			break;
		}
		case 'r': {
			RandomBehaviorImpl randomObj = new RandomBehaviorImpl();
			contextObj.setStrategy(randomObj);
 			break;
		}
		case 'b': {
			BenevolantBehaviorImpl benevolentObj = new BenevolantBehaviorImpl();
			contextObj.setStrategy(benevolentObj);
 			break;
		}
		case 'c': {
			CheaterBehaviorImpl cheaterObj = new CheaterBehaviorImpl();
			contextObj.setStrategy(cheaterObj);
 			break;
		}
		case 'h': {
			CheaterBehaviorImpl cheaterObj = new CheaterBehaviorImpl();
			contextObj.setStrategy(cheaterObj);
 			break;
		}
		default :
			break;
		}
		
		//executing the chosen strategy 
		contextObj.executeAttack(Integer.toString(plyr),territoryMap);

		
		//AttackPhaseModel.chooseCountryToBeAttacked(plyr, territoryMap);

	}

	public void fortificationPhase(int plyr, HashMap<String, List<String>> territoryMap, String currentPlyrStrategy) {

		try {
			System.in.read();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(currentPlyrStrategy.charAt(0) != 'c'){
			
			FortificationPhaseModel fortificationObj = new FortificationPhaseModel();
	
			FortificationUI uiObj = new FortificationUI();
	
			fortificationObj.addObserver(uiObj);
	
			FortificationPhaseModel.createFortifySet(Integer.toString(plyr), territoryMap);

		}//end if(currentPlyrStrategy.charAt(0) != 'c')
		
		switch (currentPlyrStrategy.charAt(0)) {

		case 'a': {
			
			AggressiveBehaviorImpl agressiveObj = new AggressiveBehaviorImpl();
			contextObj.setStrategy(agressiveObj);
			
			break;
		}
		case 'r': {
			
			RandomBehaviorImpl randomObj = new RandomBehaviorImpl();
			contextObj.setStrategy(randomObj);
		
			break;
		}
		case 'b': {
			
			
			BenevolantBehaviorImpl benevolentObj = new BenevolantBehaviorImpl();
			contextObj.setStrategy(benevolentObj);

			break;
		}
		case 'c': {
			
			CheaterBehaviorImpl cheaterObj = new CheaterBehaviorImpl();
			contextObj.setStrategy(cheaterObj);

			break;
		}
		
		case 'h': {
			
			HumanBehaviorImpl humanObj = new HumanBehaviorImpl();
			contextObj.setStrategy(humanObj);
			
			break;
		}
		
		default :
			break;
		}
		
		contextObj.executeFortification(Integer.toString(plyr));

		try {
			System.in.read();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}//end FortificationPhase

	public boolean calcDominationValues(HashMap<String, Integer> playerInfo, int numberOfPlayers, int totalTerr) {

		HashMap<String, String> dominationMap = new HashMap<String, String>();

		if (getDominationOld().isEmpty()) {

			for (int i = 1; i <= numberOfPlayers; i++) {

				int terr = 0;
				int percentage = 0;
				for (String playerInfoKey : playerInfo.keySet()) {

					String[] keySplit = playerInfoKey.split("-");

					if (keySplit[0].equals(Integer.toString(i)) || keySplit[0] == Integer.toString(i)) {

						terr++;

					}

				}

				percentage = (terr * 100) / totalTerr;

				dominationMap.put(Integer.toString(i), percentage + "%");

				setDominationOld(dominationMap);
				setDominationNew(dominationMap);

				// PlayerClass.msg = "nochange";

			} // end for(int i = 1; i <= numberOfPlayers; i++)

			return false;

		} // end if(getDominationOld().isEmpty())

		for (int i = 1; i <= numberOfPlayers; i++) {

			int terr = 0;
			int percentage = 0;
			for (String playerInfoKey : playerInfo.keySet()) {

				String[] keySplit = playerInfoKey.split("-");

				if (keySplit[0].equals(Integer.toString(i)) || keySplit[0] == Integer.toString(i)) {

					terr++;

				}

			}

			percentage = (terr * 100) / totalTerr;

			dominationMap.put(Integer.toString(i), percentage + "%");

		}

		setDominationNew(dominationMap);

		if (dominationNew.equals(dominationOld)) {

			// PlayerClass.msg = "domination";
			System.out.println("Domination Old -> " + getDominationOld());
			System.out.println("Domination New -> " + getDominationNew());

			return false;

		}

		return true;

	}

	public static boolean checkPlyrVictory(int plyr) {

		TreeSet<Integer> playerCheck = new TreeSet<Integer>();

		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {
			String[] playerInfoArr = playerInfoKey.split("-");

			playerCheck.add(Integer.valueOf(playerInfoArr[0]));
		} // end for

		if (playerCheck.size() == 1) {
			return true;
		} // end if

		return false;
	}// end method checkPlyrVictory

	/**
	 * This method counts the number of players present at any instant in the game
	 * 
	 * @return Current Number of Players
	 */
	public static int plyrsRemaining() {

		int currentNumberOfPlyrs = 0;

		TreeSet<Integer> playerCheck = new TreeSet<Integer>();

		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {
			String[] playerInfoArr = playerInfoKey.split("-");

			playerCheck.add(Integer.valueOf(playerInfoArr[0]));
		} // end for

		currentNumberOfPlyrs = playerCheck.size();

		return currentNumberOfPlyrs;

	}// end method plyrsRemaining()

}
