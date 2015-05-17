package com.mace.fair.constants;

public class Constants {
	/*
	 * A class containing all asset locations & game constants. This makes asset
	 * loading far easier & also allows for changes to game-play constants at
	 * one location, which can affect all references to this.
	 */

	/****************************
	 * GAME ASSET LOCATIONS
	 ****************************/

	/****************
	 * MAP LOCATIONS
	 ***************/
	public static final String temp_map_1 = "res//map.tmx";

	/*******************
	 * IMAGE LOCATIONS
	 ******************/

	public static final String player1_image_loc = "res//player_0.png";
	public static final String player2_image_loc = "res//player_1.png";
	public static final String playerAura1_img_loc = "res//player_2.png";
	public static final String playerAura2_img_loc = "res//player_3.png";
	public static final String zombie1_image_loc = "res//zombie_0.png";
	public static final String zombie2_image_loc = "res//zombie_1.png";
	public static final String zombieAway1_img_loc = "res//zombie_2.png";
	public static final String zombieAway2_img_loc = "res//zombie_3.png";
	public static final String pill_img_loc = "res//pill.png";
	public static final String lives_img_loc = "res//life.png";
	public static final String icon_img_loc = "res//windowIcon.png";
	public static final String splash_img_loc = "res//splash.png";
	public static final String titleText_img_loc = "res//titletext.png";
	public static final String helpScreen_splash_loc = "res//HelpScreen.png";
	public static final String creditsScreen_splash_loc = "res//CreditsScreen.png";
	public static final String cheatsScreen_splash_loc = "res//CheatScreen.png";
	public static final String overlay_img_loc = "res//pause_overlay.png";
	public static final String playButton_img_loc = "res//play_button.png";
	public static final String helpButton_img_loc = "res//help_button.png";
	public static final String creditsButton_img_loc = "res//credits_button.png";
	public static final String cheatsButton_img_loc = "res//cheats_button.png";
	public static final String resumeButton_img_loc = "res//resume.png";
	public static final String menuButton_img_loc = "res//menu.png";

	/****************************
	 * GAME CONSTANTS
	 ****************************/

	public static final int TILESIZE = 64;
	public static final int MAX_ZOMBIES = 4;
	public static final int MAX_PILLS = 5;
	public static final int WALL_TILE_ID = 1;
	public static final int FLOOR_TILE_ID = 2;
	public static final int HOLE_TILE_ID = 3;
	public static final int MAX_MOVE = 10;
	public static final int START_LIVES = 3;
	public static final int MAX_HOLES = 12;

	/****************************
	 * STATE IDs
	 ****************************/
	public final static int MenuState = 0;
	public final static int GameState1 = 1;
	public final static int HelpMenuState = 2;
	public final static int CreditsMenuState = 3;
	public final static int CheatsMenuState = 4;
}
