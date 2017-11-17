package com.risk.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.risk.utility.RiskConstants;

public class AttackPhaseModel {

	private static final String outerloop = null;
	static int noOfAttackerArmies;
	static int noOfDefenderArmies;
	static String attackerKey;
	static String defenderKey;
	static String attackerPlayer;
	static String defenderPlayer;
	static boolean armiesDepleted = false;
	static String attackAgain = "Yes";
	static String attackSameCountryAgain = "Yes";
	static Scanner input = new Scanner(System.in);
	
	static boolean cardGet = false;

	// for getting data from player info entries for the specific player
	// list for attackers
	static List<String> playerAccToPlayerNo = new ArrayList<String>();
	// get countries of attacker
	static List<String> countryAccToAttacker = new ArrayList<String>();
	// list for attacked candidates
	static List<String> playerNotAccToPlayerNo = new ArrayList<String>();

	static Random random;

	/*
	 * method for checking adjacency of the attacker and defender
	 */
	public static void chooseCountryToBeAttacked(int plyr, HashMap<String, List<String>> territoryMap) {
		attackAgain = "Yes";
		attackSameCountryAgain = "Yes";
		noOfAttackerArmies = 0;
		noOfDefenderArmies = 0;

		// populating the lists for attacker and defender
		populateListsForAttackerAndDefender(plyr);

		
		
		
		// randomly select attacker and defender
		// for generating a stream of pseudo random numbers
		random = new Random();
		String attacked = "";
		String attacker = playerAccToPlayerNo.get(random.nextInt(playerAccToPlayerNo.size()));
		// global key initialized
		attackerKey = attacker;
		// getting number of attacker armies
		noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attacker);
		boolean foundAdjacency = false;
		
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("****************** ATTACK PHASE FOR PLAYER " + plyr + " BEGINS ***********************");
		System.out.println("---------------------------------------------------------------------------");
		
		
		while (attackAgain.trim().equalsIgnoreCase("Yes") || attackAgain.trim().equalsIgnoreCase("Y")) {
			// label for break
			// resetting as new attack is initiated
			foundAdjacency = false;
			// attacker country changed if one army is remaining
			if (StartUpPhaseModel.playerInfo.get(attackerKey) == 1) {
				attacker = playerAccToPlayerNo.get(random.nextInt(playerAccToPlayerNo.size()));
				//
				// global key initialized
				attackerKey = attacker;
				// getting number of attacker armies
				noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attacker);
			}

			attacked = playerNotAccToPlayerNo.get(random.nextInt(playerNotAccToPlayerNo.size()));
			// global key initialized
			defenderKey = attacked;
			// getting number of attacker armies
			noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(attacked);
			String[] keySplit1 = attacker.split("-");
			attackerPlayer = keySplit1[0];
			String territoryAttacker = keySplit1[1];
			String[] keySplit2 = attacked.split("-");
			defenderPlayer = keySplit2[0];
			String territoryAttacked = keySplit2[1];
			for (Map.Entry<String, List<String>> iterate : territoryMap.entrySet()) {
				//
				String[] keyArray = iterate.getKey().split(",");
				String territory = keyArray[1];
				if (territoryAttacker.equals(territory)) {

					for (String country : iterate.getValue()) {

						if (territoryAttacked.equals(country)) {
							while (attackSameCountryAgain.trim().equalsIgnoreCase("Yes")
									|| attackSameCountryAgain.trim().equalsIgnoreCase("Y")) {

								
								
								// attackSameCountryAgain
								noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attacker);

								noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(attacked);
								
								System.out.println("\n\n\t\t" + "PLAYER " + keySplit1[0] + " : Attacking Territory - " + territoryAttacker +  " --> number of armies : "
										+ noOfAttackerArmies);
								System.out.println("\t\tPLAYER " + keySplit2[0] + ": Defending Territory - " + territoryAttacked +" --> number of armies : "
										+ noOfDefenderArmies);
								System.out.println();
								System.out.println();
								System.out.println("\t\tPLAYER " + keySplit1[0] + " from " + territoryAttacker
										+ " attacks on " + territoryAttacked + " owned by PLAYER " + keySplit2[0] );
								System.out.println();
								System.out.println();
								// Dice Rolled
								// country is attacked and armies are deducted
								// based on the dice roll obtained
								try {
									rollDice();
									populateListsForAttackerAndDefender(plyr);

								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								if (StartUpPhaseModel.playerInfo.get(attacked) != null
										&& StartUpPhaseModel.playerInfo.get(attacked) > 0) {
									if (StartUpPhaseModel.playerInfo.get(attacker) > 1) {
										System.out.println("\t--------------------------------------------------------------");
										System.out.println("\t\tDo you want to attack the Same Country again? ");
										System.out.println("\t\tPlease enter  Yes or No : ");
										attackSameCountryAgain = input.next();
									} else {
										attackSameCountryAgain = "No";
										break;
									}

								} else {
									attackSameCountryAgain = "No";
									break;
								}

								foundAdjacency = true;
							}
							if (foundAdjacency) {
								break;
							}

						}

					}
				}
				if (foundAdjacency) {
					break;
				}

			}
			if (foundAdjacency) {
				System.out.println("\n\t--------------------------------------------------------------");
				System.out.println("\t\tDo you want to attack again? ");
				System.out.println("\t\tPlease enter Yes or No :");
				attackAgain = input.next();
			}

		}
		
		addCards(attackerPlayer);
		
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("****************** ATTACK PHASE FOR PLAYER " + plyr + " ENDS *************************");
		System.out.println("---------------------------------------------------------------------------");
		

	}

	private static void populateListsForAttackerAndDefender(int plyr) {
		for (String playerInfoKey : StartUpPhaseModel.playerInfo.keySet()) {
			String[] playerInfoArr = playerInfoKey.split("-");
			if (playerInfoArr[0].equals(String.valueOf(plyr))) {
				playerAccToPlayerNo.add(playerInfoKey);
				countryAccToAttacker.add(playerInfoArr[1]);

			} else {
				playerNotAccToPlayerNo.add(playerInfoKey);

			}
		}
	}

	/*
	 * dice is rolled and numbers obtained are used to compute the result
	 */
	public static void rollDice() throws InterruptedException {

		Thread.sleep(1000);
		int diceRollsForAttacker, diceRollsForDefender;
		List<Integer> diceArrayForAttackers = new ArrayList<Integer>();
		List<Integer> diceArrayForDefenders = new ArrayList<Integer>();

		if (noOfAttackerArmies > 2) {

			diceRollsForAttacker = 3;
			diceArrayForAttackers = AttackPhaseModel.getDiceNumbers(diceRollsForAttacker);
			System.out.print("\t\tPLAYER " + attackerPlayer + " Dice Roll : ");
			for (Integer integer : diceArrayForAttackers) {
				System.out.print(integer + " ");

			}

			System.out.println();
			System.out.println();

			diceRollsForDefender = 2;
			diceArrayForDefenders = AttackPhaseModel.getDiceNumbers(diceRollsForDefender);
			System.out.print("\t\tPLAYER " + defenderPlayer + " Dice Roll : ");
			for (Integer integer : diceArrayForDefenders) {
				System.out.print(integer + " ");

			}

			System.out.println();
			System.out.println();
		} else if (noOfAttackerArmies == 2) {

			diceRollsForAttacker = 2;
			diceArrayForAttackers = AttackPhaseModel.getDiceNumbers(diceRollsForAttacker);
			System.out.print("\t\tPLAYER " + attackerPlayer + " Dice Roll : ");
			for (Integer integer : diceArrayForAttackers) {
				System.out.print(integer + " ");

			}

			System.out.println();
			System.out.println();
			diceRollsForDefender = 2;
			diceArrayForDefenders = AttackPhaseModel.getDiceNumbers(diceRollsForDefender);
			System.out.print("\t\tPLAYER " + defenderPlayer + " Dice Roll : ");
			System.out.print("\t\tPLAYER " + defenderPlayer + " Dice Roll : ");
			for (Integer integer : diceArrayForDefenders) {
				System.out.print(integer + " ");

			}
			System.out.println();
			System.out.println();
		} else if (noOfAttackerArmies == 1) {

			diceRollsForAttacker = 1;
			diceArrayForAttackers = AttackPhaseModel.getDiceNumbers(diceRollsForAttacker);
			System.out.println("\t\tPLAYER " + attackerPlayer + " Dice Roll : ");
			for (Integer integer : diceArrayForAttackers) {
				System.out.print(integer + " ");

			}
			System.out.println();
			System.out.println();

			diceRollsForDefender = 1;
			diceArrayForDefenders = AttackPhaseModel.getDiceNumbers(diceRollsForDefender);
			System.out.print("\t\tPLAYER " + defenderPlayer + " Dice Roll : ");
			System.out.print("\t\tPLAYER " + defenderPlayer + " Dice Roll : ");

			for (Integer integer : diceArrayForDefenders) {
				System.out.print(integer + " ");

			}

			System.out.println();
			System.out.println();
		}
		// analyzing the numbers obtained to compute the no of armies
		diceRollAnalysis(diceArrayForAttackers, diceArrayForDefenders);

	}

	/*
	 * analyzing the numbers obtained to compute the no of armies thus making
	 * changes in player data
	 */
	private static void diceRollAnalysis(List<Integer> diceArrayForAttackersList,
			List<Integer> diceArrayForDefendersList) throws InterruptedException {

		Collections.sort(diceArrayForAttackersList);
		Collections.sort(diceArrayForDefendersList);

		int maxAttacker = 0, attacker2ndBest = 0;
		int maxDefender = 0, minDefender2ndRoll = 0;
		int armiesObtainedForAttacker = 0, armiesObtainedForDefender = 0;

		// Case 1 : attacker and defender both are given more than 1 dice rolls
		if (diceArrayForAttackersList.size() >= 2) {
			maxAttacker = Collections.max(diceArrayForAttackersList);
			attacker2ndBest = (int) diceArrayForAttackersList.get(diceArrayForAttackersList.size() - 2);
			maxDefender = Collections.max(diceArrayForDefendersList);
			minDefender2ndRoll = (int) diceArrayForDefendersList.get(diceArrayForDefendersList.size() - 2);
			if (maxAttacker > maxDefender) {
				--armiesObtainedForDefender;

			} else {
				--armiesObtainedForAttacker;

			}
			if (attacker2ndBest > minDefender2ndRoll) {
				--armiesObtainedForDefender;
			} else {
				--armiesObtainedForAttacker;
			}

			// Updating the Player Data

			int noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attackerKey);
			noOfAttackerArmies = noOfAttackerArmies + armiesObtainedForAttacker;
			// Updating Attacker Data
			StartUpPhaseModel.playerInfo.replace(attackerKey, noOfAttackerArmies);

			int noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(defenderKey);
			noOfDefenderArmies = noOfDefenderArmies + armiesObtainedForDefender;
			// Updating Defender Data
			StartUpPhaseModel.playerInfo.replace(defenderKey, noOfDefenderArmies);
			
			String [] attackerKeySplit = attackerKey.split("-");
			String [] defenderKeySplit = defenderKey.split("-");
			if (noOfDefenderArmies <= 0) {

				Thread.sleep(1000);
				System.out.println();
				System.out.println();
				
				System.out.println("\t\tATTACKER CONQUERS THE TERRITORY");
			
				System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
						+ noOfAttackerArmies);
				System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : 0");
						
				cardGet = true;
				
				// assigning defender territory to attacker
				String[] defKeyString = defenderKey.split("-");
				defKeyString[0] = attackerPlayer;
				String modifiedKey = defKeyString[0] + "-" + defKeyString[1] + "-" + defKeyString[2];
				// attacking player must place a number of armies
				// in the conquered country which is greater or equal than the number of dice
				// that was used in the attack that
				// resulted in conquering the country
				StartUpPhaseModel.playerInfo.put(modifiedKey, diceArrayForAttackersList.size() - 1);
				noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(modifiedKey);
				StartUpPhaseModel.playerInfo.remove(defenderKey);
				Thread.sleep(2000);

			} else if (noOfAttackerArmies <= 0) {
				StartUpPhaseModel.playerInfo.replace(attackerKey, 1);
				noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attackerKey);
				System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
						+ "1");
				System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : "
						+ noOfDefenderArmies);
					
//				System.out.println("Player" + attackerPlayer + "(Attacker) --> number of armies : " + "0");
//				System.out
//						.println("Player" + defenderPlayer + "(Defender) --> number of armies : " + noOfDefenderArmies);

				Thread.sleep(2000);

			} else {
				Thread.sleep(2000);
				System.out.println("\t\tATTACK COMPLETED");
				System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
						+ noOfAttackerArmies);
				System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : "
						+ noOfDefenderArmies);

			}

		}
		// Case 2 : attacker and defender both are given 1 dice roll
		else if (diceArrayForAttackersList.size() == 1) {
			maxAttacker = diceArrayForAttackersList.get(0);
			maxDefender = diceArrayForAttackersList.get(0);
			if (maxAttacker > maxDefender) {
				--armiesObtainedForDefender;

			} else {
				--armiesObtainedForAttacker;
			}

			// Updating the Player Data
			int noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attackerKey);
			noOfAttackerArmies = noOfAttackerArmies + armiesObtainedForAttacker;
			// Updating Attacker Data
			StartUpPhaseModel.playerInfo.replace(attackerKey, noOfAttackerArmies);

			int noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(defenderKey);
			noOfDefenderArmies = noOfDefenderArmies + armiesObtainedForDefender;
			// Updating Defender Data
			StartUpPhaseModel.playerInfo.replace(defenderKey, noOfDefenderArmies);
			
			String [] attackerKeySplit = attackerKey.split("-");
			String [] defenderKeySplit = defenderKey.split("-");

			if (noOfDefenderArmies <= 0) {

				Thread.sleep(1000);
				System.out.println();
				System.out.println("\t\tATTACKER CONQUERS THE TERRITORY");
				
				System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
						+ noOfAttackerArmies);
				System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : 0");
				
				cardGet = true;
				
				// assigning defender territory to attacker
				String[] defKeyString = defenderKey.split("-");
				defKeyString[0] = attackerPlayer;
				String modifiedKey = defKeyString[0] + "-" + defKeyString[1] + "-" + defKeyString[2];
				StartUpPhaseModel.playerInfo.put(modifiedKey, diceArrayForAttackersList.size() - 1);
				noOfDefenderArmies = StartUpPhaseModel.playerInfo.get(modifiedKey);
				StartUpPhaseModel.playerInfo.remove(defenderKey);
			}
			if (noOfAttackerArmies <= 0) {
				StartUpPhaseModel.playerInfo.put(attackerKey, 1);
				noOfAttackerArmies = StartUpPhaseModel.playerInfo.get(attackerKey);
				System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
						+ "1");
				System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : "
						+ noOfDefenderArmies);
				Thread.sleep(2000);

			}
			Thread.sleep(2000);
			System.out.println("\t\tATTACK COMPLETED");
			System.out.println("\n\n\t\t" + "PLAYER " + attackerPlayer + " : Attacking Territory - " + attackerKeySplit[1] +  " --> number of armies : "
					+ noOfAttackerArmies);
			System.out.println("\t\t" + "PLAYER " + defenderPlayer + " : Defending Territory - " + defenderKeySplit[1] +  " --> number of armies : "
					+ noOfDefenderArmies);

		}
		// calling function to decide whether the player should or shouldn't attack
		// again

	}

	/*
	 * this method generate and returns the numbers obtained in the dice rolled
	 */
	public static List<Integer> getDiceNumbers(int dicerolls) {
		random = new Random();
		List<Integer> diceArrayList = new ArrayList<Integer>();

		switch (dicerolls) {
		case 1:
			diceArrayList.add(random.nextInt(6) + 1);
			break;
		case 2:
			diceArrayList.add(random.nextInt(6) + 1);
			diceArrayList.add(random.nextInt(6) + 1);

			break;
		case 3:
			diceArrayList.add(random.nextInt(6) + 1);
			diceArrayList.add(random.nextInt(6) + 1);
			diceArrayList.add(random.nextInt(6) + 1);
			break;
		default:
			break;
		}
		return diceArrayList;
	}
	
	public static void addCards(String player){
		
		if(cardGet){
			Random random = new Random();
			
			int index = random.nextInt(2);
			
			String card;
			
			card = RiskConstants.CARD_TYPES[index];
			
			System.out.println("\t\t" + "PLAYER " +player + " gets card : " + card);
			ArrayList<String> list = new ArrayList<String>();
			for(String cardKey : ReinforcementPhaseModel.playerCards.keySet()){
				
				String [] keySplit = cardKey.split("-");
				if(keySplit[0].equals(player) || keySplit[0] == player){
					list = ReinforcementPhaseModel.playerCards.get(cardKey);
					list.add(card);
					ReinforcementPhaseModel.playerCards.put(cardKey,list);
					break;
				}
			
				
			}
		}
		cardGet = false;
	}

}