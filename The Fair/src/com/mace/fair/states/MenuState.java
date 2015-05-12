package com.mace.fair.states;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.mace.fair.constants.Constants;

public class MenuState extends BasicGameState {

	private final int stateID;
	private float playX, playY;
	private Image background, text, playButton, helpButton, creditsButton, cheatsButton;
	private int mouseX, mouseY;
	private int helpX, helpY;
	private int cheatX, cheatY;
	private int creditsX, creditsY;
	private Rectangle play, credits, help, cheats;
	private boolean isShowingCheats;

	public MenuState(int stateID) {
		this.stateID = stateID;
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		background = new Image(Constants.splash_img_loc);
		text = new Image(Constants.titleText_img_loc);
		playButton = new Image(Constants.playButton_img_loc).getScaledCopy(0.5f);
		helpButton = new Image(Constants.helpButton_img_loc).getScaledCopy(0.5f);
		creditsButton = new Image(Constants.creditsButton_img_loc).getScaledCopy(0.5f);
		cheatsButton = new Image(Constants.cheatsButton_img_loc).getScaledCopy(0.5f);

		playX = 179 - (playButton.getWidth() / 2);
		playY = 399 - playButton.getHeight() - 40;

		helpX = 564 - helpButton.getWidth();
		helpY = 364 - helpButton.getHeight();

		cheatX = 450;
		cheatY = 50;

		creditsX = (Display.getWidth() / 2) - (creditsButton.getWidth() / 2);
		creditsY = 300;

		play = new Rectangle(playX, playY, playButton.getWidth(), playButton.getHeight());
		help = new Rectangle(helpX, helpY, helpButton.getWidth(), helpButton.getHeight());
		cheats = new Rectangle(cheatX, cheatY, cheatsButton.getWidth(), cheatsButton.getHeight());
		credits = new Rectangle(creditsX, creditsY, creditsButton.getWidth(), creditsButton.getHeight());

		isShowingCheats = false;

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		g.drawImage(background, 0, 0);
		g.drawImage(text, 0, 0);
		g.drawImage(playButton, playX, playY);
		g.drawImage(helpButton, helpX, helpY);
		if (isShowingCheats)
			g.drawImage(cheatsButton, cheatX, cheatY);
		g.drawImage(creditsButton, creditsX, creditsY);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();

		gc.getInput().clearKeyPressedRecord();

		if (play.contains(mouseX, mouseY)) {
			if (gc.getInput().isMousePressed(0)) {
				sbg.getState(Constants.GameState1).init(gc, sbg);
				sbg.enterState(Constants.GameState1);
			}
		}
		if (help.contains(mouseX, mouseY)) {
			if (gc.getInput().isMousePressed(0)) {
				sbg.enterState(Constants.HelpMenuState);
			}
		}
		if (credits.contains(mouseX, mouseY)) {
			if (gc.getInput().isMousePressed(0)) {
				sbg.enterState(Constants.CreditsMenuState);
			}
		}
		if (cheats.contains(mouseX, mouseY)) {
			isShowingCheats = true;
			if (gc.getInput().isMousePressed(0)) {
				sbg.enterState(Constants.CheatsMenuState);
			}
		} else {
			isShowingCheats = false;
		}

	}

	@Override
	public int getID() {
		return stateID;
	}

}
