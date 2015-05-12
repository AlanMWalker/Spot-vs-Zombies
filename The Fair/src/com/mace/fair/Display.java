package com.mace.fair;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.mace.fair.constants.Constants;
import com.mace.fair.states.CheatsMenuState;
import com.mace.fair.states.CreditsMenuState;
import com.mace.fair.states.GameState1;
import com.mace.fair.states.MenuState;
import com.mace.fair.states.HelpMenuState;

public class Display extends StateBasedGame {// Inherit the behaviour of a
												// StateBasedGame
	/*
	 * Main class where the entire game is initialised
	 */
	private final static int WIDTH = 640; // Width of the game window
	private final static int HEIGHT = 480; // Height of the game window

	public Display(String WINDOW_TITLE) {
		super(WINDOW_TITLE); // Pass to the parent class the window title
		this.addState(new MenuState(Constants.MenuState)); // Main menu
		this.addState(new GameState1(Constants.GameState1)); // Gameplay state
		this.addState(new HelpMenuState(Constants.HelpMenuState)); // Help
																	// screen
		this.addState(new CreditsMenuState(Constants.CreditsMenuState)); // Credits
																			// Screen
		this.addState(new CheatsMenuState(Constants.CheatsMenuState)); // Cheats
																		// screen

		/*
		 * Multiple states were used because I intend to add further features in
		 * the future and upload the game to my website as part of a portfolio.
		 * This is why some states may seem empty and not required.
		 */
	}

	// Inhereted abstract function

	public void initStatesList(GameContainer gc) throws SlickException {
		// The first state to be entered when the game loads is the MenuState
		this.enterState(Constants.MenuState);

		/*
		 * This method is supposed to be used for state initialisation. However
		 * a recent update left this method mostly useless as whenever a state
		 * is added to the list of states, it is initialised automatically.
		 */
	}

	public static void main(String args[]) {
		AppGameContainer agc; // Create an object of type AppGameContainer
		try {
			// Initialise appgamecontainer passing Display as a SBG parameter
			agc = new AppGameContainer(new Display("Spot vs Zombies!"));
			// Set the windows dimensions to the specified & set fullscreen to
			// false
			agc.setDisplayMode(WIDTH, HEIGHT, false);
			// Cap the games FPS to the refresh rate of the screen
			agc.setVSync(true);
			agc.setIcon(Constants.icon_img_loc);
			agc.start();// Create & open the window
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

}
