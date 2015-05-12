package com.mace.fair.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;

import com.mace.fair.constants.Constants;

public class CreditsMenuState extends BasicGameState {

	private final int stateID;
	private Image background;

	public CreditsMenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image(Constants.creditsScreen_splash_loc);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_B))
			sbg.enterState(Constants.MenuState);

	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return stateID;
	}

}
