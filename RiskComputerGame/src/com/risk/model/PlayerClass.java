package com.risk.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.TreeSet;

import com.risk.behavior.AggressiveBehaviorImpl;
import com.risk.behavior.BenevolantBehaviorImpl;
import com.risk.behavior.CheaterBehaviorImpl;
import com.risk.behavior.HumanBehaviorImpl;
import com.risk.behavior.RandomBehaviorImpl;
import com.risk.behavior.StrategyContext;
import com.risk.ui.CardExchangeUI;
import com.risk.ui.DeployArmiesUI;
import com.risk.ui.FortificationUI;
import com.risk.ui.ReinforcementsUI;


/**
 * This is an Observable Class which has implementations for state change of a player as the game proceeds* 
 * Observers for this class are {@link com.risk.ui.PhaseUI PhaseUI} and {@link com.risk.ui.PlayerDominationView PlayerDominationView}
 * @author Navjot
 * @author Ashish Sharma
 */

public class PlayerClass extends Observable {

	/** This HashMap stores the old domination values. The key stores the player as a string and it points to value which stores 
	 * a percentage of total territories owned by that player.
	 */
	private static HashMap<String, String> dominationOld = new HashMap<String, String>();

	/** This HashMap stores the new domination values. The key stores the player as a string and it points to value which stores 
	 * a percentage of total territories owned by that player.
	 */
	private static HashMap<String, String> dominationNew = new HashMap<String, String>();
	

	/**
	 * Gets the domination old.
	 *
	 * @return the domination old
	 */
	public static HashMap<String, String> getDominationOld() {
		return dominationOld;
	}

	/**
	 * Sets the domination old.
	 *
	 * @param dominationOld the domination old
	 */
	public static void setDominationOld(HashMap<String, String> dominationOld) {
		PlayerClass.dominationOld = dominationOld;
	}

	/**
	 * Gets the domination new.
	 *
	 * @return the domination new
	 */
	public static HashMap<String, String> getDominationNew() {
		return dominationNew;
	}

	/**
	 * Sets the domination new.
	 *
	 * @param dominationNew the domination new
	 */
	public static void setDominationNew(HashMap<String, String> dominationNew) {
		PlayerClass.dominationNew = dominationNew;
	}

	/**
	 *  Stores the current player
	 */
	private static int currentPlayer ;
	
	/**
	 * Gets the current player
	 * @return currentPlayer current player
	 */
	public static int getCurrentPlayer() {
		return currentPlayer;
	}

	/**
	 * Sets the current player
	 * @param currentPlayer Current Player
	 */
	public static void setCurrentPlayer(int currentPlayer) {
		PlayerClass.currentPlayer = currentPlayer;
	}

	/** Stores the number of players in the game. */
	public static int players = 0;

	/** Stores the map used in the current game. */
	public static HashMap<String, List<String>> currentMap;

	/** Stores the message user by the observers. */
	public static String msg;

	/** The StrategyContext Class object. */
	public static StrategyContext contextObj = new StrategyContext();
	
	/** The current players treeset object. */
	public static TreeSet<Integer> playerTreeSet = new TreeSet<Integer>();
	
	/** The current players list object. */
	public static ArrayList<Integer> playersList = new ArrayList<Integer>();
	
	/** The current player index in players list */
	public static int playersIndex;
	
	/**
	 * Gets the players index.
	 *
	 * @return the players index
	 */
	public static int getPlayersIndex() {
		return playersIndex;
	}

	/**
	 * Sets the players index.
	 *
	 * @param playersIndex the new players index
	 */
	public static void setPlayersIndex(int playersIndex) {
		PlayerClass.playersIndex = playersIndex;
	}

	SaveAndLoadGame objSaveAndLoadGame = new SaveAndLoadGame();

	/**
	 * This method is where the game starts and proceeds into various phases
	 *
	 * @param numberOfPlayers the number of players
	 * @param territoryMap the territory map
	 * @param continentControlValueHashMap the continent control value hash map
	 * @param strategies the strategies HashMap to store behaviour of each player
	 * @param load boolean variable. True if a saved game is being loaded, False if it is a new game.
	 * @throws InterruptedException the interrupted exception
	 */
	public void gamePlay(int numberOfPlayers, HashMap<String, List<String>> territoryMap,
			HashMap<String, Integer> continentControlValueHashMap, HashMap<Integer, String> strategies, boolean load, 
			boolean trnmnt, int rounds, int currentPlayerIndex)
			throws InterruptedException {
		
		
		int roundRobins = 1;
		boolean tournament = trnmnt;
		boolean loadGame = load;
		currentMap = territoryMap;
		
		//populate playerTreeSet
		for(int i = 1; i<=numberOfPlayers ; i++){
			playerTreeSet.add(i);
			
			playersList.add(i);
		}
		
		PlayerClass.players = numberOfPlayers;

		PlayerClass playerClassObj = new PlayerClass();
		
		int plyr;
		
		//int plyrIndex = 0;
		
		PlayerClass.setPlayersIndex(0);
		
		plyr = playersList.get(PlayerClass.getPlayersIndex());
		
//		int plyr = 1;
		
		
		
		if(loadGame){
			
			//uncomment this
			PlayerClass.setPlayersIndex(currentPlayerIndex); 
			
			
			plyr =  playersList.get(PlayerClass.getPlayersIndex());
		
			//plyr = playerClassObj.getCurrentPlayer();
			
		}

		if(!loadGame) {
			
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
	
			startUpObj.deployArmiesRandomly(numberOfPlayers, strategies);
	
			PlayerClass.msg = "postDeployStartUp";
	
			setChanged();
	
			notifyObservers(this);
	
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			

			PlayerClass.setPlayersIndex(0);
			plyr  =  playersList.get(PlayerClass.getPlayersIndex());
			
		}//end if(!loadGame)
		
		
		int currentNumberOfPlayers = numberOfPlayers;

		
		if(!loadGame) {
			
			msg = "roundrobin";
	
			setChanged();
	
			notifyObservers(this);
			
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}//end if(!loadGame)
		

		// round robin for game starts
		
	
	
		while (true) {

			if(roundRobins == 1){
				msg = "ROUND," + roundRobins;
				
				setChanged();
		
				notifyObservers(this);
				
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				roundRobins++;
			}
			
			// calculate number of players after each round robin to update in case a player
			// is ousted
			currentNumberOfPlayers = PlayerClass.plyrsRemaining();

			// reset plyr to player 1 once all players have got their turn to play
			if (PlayerClass.getPlayersIndex() >= currentNumberOfPlayers) {
				
				PlayerClass.setPlayersIndex(0);
			
			//	plyrIndex = 0;
				//plyr = playersList.get(plyrIndex);
				
				if(tournament){
					if(roundRobins > rounds){
						
						System.out.println("---------------------------------------------------------------------------");
						System.out.println("***************************** GAME ENDS  *****************************");
						System.out.println("---------------------------------------------------------------------------");
						
						 break;
					}
				}
				
				msg = "ROUND," + roundRobins;
				
				setChanged();
		
				notifyObservers(this);
				
				try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				roundRobins++;

			}
			
			plyr = playersList.get(PlayerClass.getPlayersIndex());
			
			
			
			// set player strategy
			String currentPlyrStrategy = strategies.get(plyr);
			
			if(!loadGame) {
				
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
			}//end if(!loadGame)
			
			
			loadGame = false;
			
						
			// Save and Load Functionality
			// -----------------------------------------------------------------------------------------------------------------------------------

			if(!tournament){
				
				System.out.println("\t\t------------------------------------------------------------------------------------");
				System.out.println("\n\n\n\tDO YOU WANT TO SAVE THE MAP?");
				System.out.printf("\tPlease enter yes or y to save and anything else to continue : ");
				
				try {
					// Scanner input to save state of map
					Scanner inputToCheckSave = new Scanner(System.in);
					String strToCheckSave = inputToCheckSave.next();
	
					// Upon yes, assigning required variables to SaveAndLoadGame Class object and
					// then calling save method of ResourceManager to save to file
					if (strToCheckSave.trim().equalsIgnoreCase("Yes") || strToCheckSave.trim().equalsIgnoreCase("Y")) {
					
						
						objSaveAndLoadGame.mainMapToSave = currentMap;
						objSaveAndLoadGame.dominationOldToSave = dominationOld;
						objSaveAndLoadGame.dominationNewToSave = dominationNew;
						objSaveAndLoadGame.playerInfoToSave = StartUpPhaseModel.playerInfo;
						objSaveAndLoadGame.terrPerPlayerToSave = StartUpPhaseModel.terrPerPlayer;
						objSaveAndLoadGame.terrContToSave = StartUpPhaseModel.terrCont;
						objSaveAndLoadGame.terrPerContToSave = StartUpPhaseModel.terrPerCont;
						objSaveAndLoadGame.reinforcementToSave = ReinforcementPhaseModel.reinforcement;
						objSaveAndLoadGame.playerCardsToSave = ReinforcementPhaseModel.playerCards;
						objSaveAndLoadGame.prevPlayerCardsToSave = ReinforcementPhaseModel.prevPlayerCards;
						objSaveAndLoadGame.strategiesToSave = strategies;
						objSaveAndLoadGame.state = "Reinforcecomplete";
						objSaveAndLoadGame.currentPlyrStrategyToSave = currentPlyrStrategy;
						objSaveAndLoadGame.currentPlayer = plyr;
						objSaveAndLoadGame.continentControlValueHashMapToSave=continentControlValueHashMap;
						objSaveAndLoadGame.totalTerrToSave=StartUpPhaseModel.totalTerr;
						objSaveAndLoadGame.countryTakenToSave= StartUpPhaseModel.countryTaken;
						objSaveAndLoadGame.currentPlyrIndexToSave = PlayerClass.getPlayersIndex();
						objSaveAndLoadGame.currentPlyrsTreeSetToSave = PlayerClass.playerTreeSet;
						objSaveAndLoadGame.playersListToSave = PlayerClass.playersList;
						try {
							ResourceManager.save(objSaveAndLoadGame, "SaveToLoadAgain");
							System.out.println("SaveToLoadAgain is saved");
							System.exit(0);
	
						} catch (Exception exSave) {
							System.out.println("Unable to Save State" + exSave.getMessage());
						}
					}
				
				}
			
				catch (Exception exInput) {
					System.out.println("Unable to get input for Save" + exInput.getMessage());
				}

			}
			
			
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// attack phase method called
			PlayerClass.attackPhase(plyr,currentPlyrStrategy, territoryMap);
			msg = "attack done";
			setChanged();
			notifyObservers(msg);

			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
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
			boolean victory = PlayerClass.checkPlyrVictory();
			
			if (victory) {

				msg = "PLAYER " + plyr + " WINS";
				
				System.out.println("---------------------------------------------------------------------------");
				System.out.println("***************************** " + msg +"  *****************************");
				System.out.println("---------------------------------------------------------------------------");
				
				
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

			//plyr++;
			PlayerClass.setPlayersIndex(PlayerClass.getPlayersIndex() + 1); 
			//plyrIndex++;
		} // end while(true)

	}

	/**
	 *This method invokes the methods {@link com.risk.model.StartUpPhaseModel#terrPerPlayerPopulate terrPerPlayerPopulate} 
	 *and {@link com.risk.model.StartUpPhaseModel#assignTerritories assignTerritories} 
	 *to carry out the start up phase of the game.
	 *
	 * @param numberOfPlayers the number of players
	 */
	public void startUpPhase(int numberOfPlayers) {

		StartUpPhaseModel.terrPerPlayerPopulate(numberOfPlayers, StartUpPhaseModel.totalTerr);

		StartUpPhaseModel.assignTerritories(numberOfPlayers, StartUpPhaseModel.countryTaken,
				StartUpPhaseModel.totalTerr);

	}

	/**
	 * This method carries out the reinforcement phase.
	 *
	 * @param plyr The player number
	 * @param currentPlyrStrategy It has behaviour of the current player
	 * @param continentControlValueHashMap the continent control value hash map
	 * @param currentMap the current territory map
	 */
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
			reinforcmentModelObj.calcReinforcementByCards(Integer.toString(plyr),currentPlyrStrategy);
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

	/**
	 * This method carries out the attack phase.
	 *
	 * @param plyr The player number
	 * @param currentPlyrStrategy It has behaviour of the current player
	 * @param territoryMap the current territory map
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
			HumanBehaviorImpl humanObj = new HumanBehaviorImpl();
			contextObj.setStrategy(humanObj);
 			break;
		}
		default :
			break;
		}
		
		//executing the chosen strategy 
		contextObj.executeAttack(Integer.toString(plyr),territoryMap);

		
		//AttackPhaseModel.chooseCountryToBeAttacked(plyr, territoryMap);

	}

	/**
	 * This method carries out the attack phase.
	 *
	 * @param plyr The player number
	 * @param territoryMap the current territory map
	 * @param currentPlyrStrategy It has behaviour of the current player
	 */
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

	/**
	 * This method calculates the domination values and returns true if they have changed.
	 *
	 * @param playerInfo the player info
	 * @param numberOfPlayers the number of players
	 * @param totalTerr number of total territories
	 * @return true, if domination values change
	 */
	public boolean calcDominationValues(HashMap<String, Integer> playerInfo, int numberOfPlayers, int totalTerr) {

		HashMap<String, String> dominationMap = new HashMap<String, String>();

		if (getDominationOld().isEmpty()) {

			for (int i = 1; i <= numberOfPlayers; i++) {

				int terr = 0;
				float percentage = 0;
				for (String playerInfoKey : playerInfo.keySet()) {

					String[] keySplit = playerInfoKey.split("-");

					if (keySplit[0].equals(Integer.toString(i)) || keySplit[0] == Integer.toString(i)) {

						terr++;

					}

				}

				percentage = (terr * 100) / totalTerr;

				dominationMap.put(Integer.toString(i), percentage + " %");

				setDominationOld(dominationMap);
				setDominationNew(dominationMap);

				// PlayerClass.msg = "nochange";

			} // end for(int i = 1; i <= numberOfPlayers; i++)

			return false;

		} // end if(getDominationOld().isEmpty())

		for (int i = 1; i <= numberOfPlayers; i++) {

			int terr = 0;
			float percentage = 0;
			for (String playerInfoKey : playerInfo.keySet()) {

				String[] keySplit = playerInfoKey.split("-");

				if (keySplit[0].equals(Integer.toString(i)) || keySplit[0] == Integer.toString(i)) {

					terr++;

				}

			}

			percentage = (terr * 100) / totalTerr;

			dominationMap.put(Integer.toString(i), percentage + " %");

		}

		setDominationNew(dominationMap);
		
	


		if (dominationNew.equals(dominationOld)) {

			// PlayerClass.msg = "domination";
//			System.out.println("Domination Old -> " + getDominationOld());
//			System.out.println("Domination New -> " + getDominationNew());

			return false;

		}

		return true;

	}

	
	/**
	 * This method checks if the player has won. It is invoked after each attack phase.
	 * @return true, if a player wins
	 */
	public static boolean checkPlyrVictory() {

		TreeSet<Integer> playerCheck = new TreeSet<Integer>();

		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {
			
			String[] playerInfoArr = playerInfoKey.split("-");

			playerCheck.add(Integer.valueOf(playerInfoArr[0]));
			
		} //end for

		playerTreeSet = playerCheck;
		playersList = new ArrayList<Integer>(playerTreeSet);
		
		if (playerCheck.size() == 1) {
			return true;
		} //end if

		return false;
		
	}// end method checkPlyrVictory

	/**
	 * This method counts the number of players present at any instant in the game.
	 *
	 * @return currentNumberOfPlyrs Current Number of Players
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
