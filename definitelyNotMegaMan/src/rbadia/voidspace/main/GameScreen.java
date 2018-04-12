package rbadia.voidspace.main;
import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import rbadia.voidspace.graphics.GraphicsManager;
import rbadia.voidspace.model.Asteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletBoss;
import rbadia.voidspace.model.Floor;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;
import rbadia.voidspace.sounds.SoundManager;

/**
 * Main game screen. Handles all game graphics updates and some of the game logic.
 */
public class GameScreen extends BaseScreen{
	private static final long serialVersionUID = 1L;

	private BufferedImage backBuffer;
	private Graphics2D g2d;

	private static final int NEW_SHIP_DELAY = 500;
	private static final int NEW_ASTEROID_DELAY = 500;
	
	//	private static final int NEW_BIG_ASTEROID_DELAY = 500;

	//	private long lastShipTime;
	private long lastAsteroidTime;
	private long lastAsteroid2Time;
	private long lastAsteroid3Time;
	//	private long lastBigAsteroidTime;

	private Rectangle asteroidExplosion;
	
	//	private Rectangle bigAsteroidExplosion;
	//	private Rectangle shipExplosion;
	//	private Rectangle bossExplosion;

	private JLabel shipsValueLabel;
	private JLabel destroyedValueLabel;
	private JLabel levelValueLabel;

	private Random rand;

	private Font originalFont;
	private Font bigFont;
	private Font biggestFont;

	private GameStatus status;
	private SoundManager soundMan;
	private GraphicsManager graphicsMan;
	private GameLogic gameLogic;
	//private InputHandler input;
	//private Platform[] platforms;
	private long lastBulletBossTime;
	private long timeBossBulletLapse;
	private int boom=0;
	private double xMove = 0;
	private double xMove2 = 0;
	private double xMove3 = 0;
	public boolean isRebound = false;
	
	//private int damage=0;
	//	private int scroll=0;
	//	private int bossHealth=0;
	//	private int delay=0;


	/**
	 * This method initializes 
	 * 
	 */
	public GameScreen() {
		super();
		// initialize random number generator
		rand = new Random();

		initialize();

		// init graphics manager
		graphicsMan = new GraphicsManager();

		
		// init back buffer image
		backBuffer = new BufferedImage(500, 400, BufferedImage.TYPE_INT_RGB);
		g2d = backBuffer.createGraphics();
	}

	/**
	 * Initialization method (for VE compatibility).
	 */
	protected void initialize() {
		// set panel properties
		this.setSize(new Dimension(500, 400));
		this.setPreferredSize(new Dimension(500, 400));
		this.setBackground(Color.BLACK);
	}

	/**
	 * Update the game screen.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// draw current backbuffer to the actual game screen
		g.drawImage(backBuffer, 0, 0, this);
	}

	/**
	 * Update the game screen's backbuffer image.
	 */
	public void updateScreen(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();
		Platform[] numPlatforms = gameLogic.getNumPlatforms();
		List<Bullet> bullets = gameLogic.getBullets();
		Asteroid asteroid = gameLogic.getAsteroid();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		Asteroid asteroid2 = gameLogic.getAsteroid2();
		Asteroid asteroid3 = gameLogic.getAsteroid3();
		//		BigAsteroid bigAsteroid = gameLogic.getBigAsteroid();
		List<BulletBoss> bulletsBoss = gameLogic.getBulletBoss();
		//		List<BulletBoss2> bulletsBoss2 = gameLogic.getBulletBoss2();		
		Boss boss = gameLogic.getBoss();
		//		Boss boss2 = gameLogic.getBoss2();


		// set orignal font - for later use
		if(this.originalFont == null){
			this.originalFont = g2d.getFont();
			this.bigFont = originalFont;
		}

		// erase screen
		
		g2d.fillRect(0, 0, getSize().width, getSize().height);
		g2d.drawImage(graphicsMan.getBacklevel1(), 0, 0, null);
		
		
		switch (status.getLevel()){
		case 2:
			g2d.drawImage(graphicsMan.getBacklevel2(), 0, 0, null);
			break;
		case 3:
			g2d.drawImage(graphicsMan.getBacklevel3(), 0, 0, null);
			break;
		case 4:
			g2d.drawImage(graphicsMan.getBacklevel4(), 0, 0, null);
			break;
		case 5:
			g2d.drawImage(graphicsMan.getBacklevel5(), 0, 0, null);
			break;
		case 6:
			g2d.drawImage(graphicsMan.getBacklevel6(), 0, 0, null);
			break;
		default:
			//nothing
		}
		
		//g2d.setPaint(Color.BLACK);
		

		// draw 50 random stars
		//drawStars(50);

		// if the game is starting, draw "Get Ready" message
		if(status.isGameStarting()){
			drawGetReady();
			return;
		}

		// if the game is over, draw the "Game Over" message
		if(status.isGameOver()){
			// draw the message
			drawGameOver();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			if(currentTime - lastAsteroid2Time < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			else 
			
			//			if((currentTime - lastShipTime) < NEW_SHIP_DELAY){
			//				graphicsMan.drawShipExplosion(shipExplosion, g2d, this);
			//			}
			return;
		}
		
			
		//if the game is won, draw the "You Win!!!" message
		if(status.isGameWon()){
			// draw the message

			drawYouWin();

			long currentTime = System.currentTimeMillis();
			// draw the explosions until their time passes
			if((currentTime - lastAsteroidTime) < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}
			else if(currentTime - lastAsteroid2Time < NEW_ASTEROID_DELAY){
				graphicsMan.drawAsteroidExplosion(asteroidExplosion, g2d, this);
			}


			return;
		}
		

		// the game has not started yet
		if(!status.isGameStarted()){
			// draw game title screen
			initialMessage();
			return;
		}

		
		//draw Floor
		for(int i=0; i<9; i++){
			graphicsMan.drawFloor(floor[i], g2d, this, i);	
		}


		
		//Create plataforms.
		structure();
		
	

		//draw MegaMan
		if(!status.isNewMegaMan()){
			if((Gravity() == true) || ((Gravity() == true) && (Fire() == true || Fire2() == true))){
				graphicsMan.drawMegaFallR(megaMan, g2d, this);
			}
		}

		if((Fire() == true || Fire2()== true) && (Gravity()==false)){
			graphicsMan.drawMegaFireR(megaMan, g2d, this);
		}

		if((Gravity()==false) && (Fire()==false) && (Fire2()==false)){
			graphicsMan.drawMegaMan(megaMan, g2d, this);
			
		}
//Ateroids for levels
		switch (status.getLevel()){
		case 1:
			asteroid = createAsteroidlevel1(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel1(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			break;
		case 2:
			asteroid = createAsteroidlevel2(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel2(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			break;

		case 3:
			asteroid = createAsteroidlevel1(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel2(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			asteroid3 = createAsteroidlevel2(asteroid3, lastAsteroid3Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid3(), asteroidExplosion, 3);
			break;

		case 4:
			asteroid = createAsteroidlevel3(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel3(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			asteroid3 = createAsteroidlevel1(asteroid3, lastAsteroid3Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid3(), asteroidExplosion, 3);
			break;

		case 5:
			asteroid = createAsteroidlevel1(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel2(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			asteroid3 = createAsteroidlevel3(asteroid3, lastAsteroid3Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid3(), asteroidExplosion, 3);
			break;

		case 6:
			asteroid = createAsteroidlevel1(asteroid, lastAsteroidTime, NEW_ASTEROID_DELAY, 
					status.isNewAsteroid(), asteroidExplosion, 1);
			asteroid2 = createAsteroidlevel2(asteroid2, lastAsteroid2Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid2(), asteroidExplosion, 2);
			asteroid3 = createAsteroidlevel3(asteroid3, lastAsteroid3Time, NEW_ASTEROID_DELAY,
					status.isNewAsteroid3(), asteroidExplosion, 3);
			break;

		default:
			//nothing
		}
		
		
		
		// draw bullets   
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			graphicsMan.drawBullet(bullet, g2d, this);

			boolean remove = gameLogic.moveBullet(bullet);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		// draw Boss bullets
		for(int i=0; i<bulletsBoss.size(); i++){
			BulletBoss bulletBoss = bulletsBoss.get(i);
			graphicsMan.drawBulletBoss(bulletBoss, g2d, this);

			boolean remove = gameLogic.moveBulletBoss(bulletBoss);
			if(remove){
				bullets.remove(i);
				i--;
			}
		}

		// draw big bullets
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			graphicsMan.drawBigBullet(bigBullet, g2d, this);

			boolean remove = gameLogic.moveBigBullet(bigBullet);
			if(remove){
				bigBullets.remove(i);
				i--;
			}
		}
		// check MMbullet-boss collision
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(bullet.intersects(boss)){
				status.setBossLife(status.getBossLife() - 1);
				
				// remove bullet
				bullets.remove(i);
				break;
			}
			if(status.getBossLife() == 0){
				graphicsMan.drawAsteroidExplosion(boss, g2d, this);
				status.setGameWon(true);
			}
		}
		// check Boss bullet-MM collision
		for(int i=0; i<bulletsBoss.size(); i++){
			BulletBoss bulletBoss = bulletsBoss.get(i);
			if(bulletBoss.intersects(megaMan)){
				
				status.setShipsLeft(status.getShipsLeft() - 1);
				long currentTime = System.currentTimeMillis();
				if((currentTime - timeBossBulletLapse) > 1000){
						graphicsMan.drawAsteroidExplosion(bulletBoss, g2d, this);
				}
			
				// remove bullet
				bulletsBoss.remove(i);
				break;
				
					
			}
		}
		// check bullet-asteroid collisions
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if(asteroid.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);

				removeAsteroid(asteroid, 1);
				boom++;
				
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
			if(asteroid2.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid2, 2);
				boom++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
			if(asteroid3.intersects(bullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);
				removeAsteroid(asteroid3, 3);
				boom++;
				damage=0;
				// remove bullet
				bullets.remove(i);
				break;
			}
		}
	
	/*	// check big bullet-asteroid collisions
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if(asteroid.intersects(bigBullet)){
				// increase asteroids destroyed count
				status.setAsteroidsDestroyed(status.getAsteroidsDestroyed() + 100);

				removeAsteroid(asteroid);



				if(boom != 5 && boom != 15){
					boom=boom + 1;
				}
				damage=0;
			}
		}*/
		// MM Boss collision
		if(boss.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			megaMan.setLocation(boss.getBossWidth(), this.getHeight() - megaMan.getMegaManHeight() - 32);
		}

		//MM-Asteroid collision
		if(asteroid.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeAsteroid(asteroid, 1);
		}
		if(asteroid2.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeAsteroid(asteroid2, 2);
		}
		if(asteroid3.intersects(megaMan)){
			status.setShipsLeft(status.getShipsLeft() - 1);
			removeAsteroid(asteroid3, 3);
		}

		//Asteroid-Floor collision
		for(int i=0; i<9; i++){
			if(asteroid.intersects(floor[i]))
				removeAsteroid(asteroid, 1);
			if(asteroid2.intersects(floor[i]))
				removeAsteroid(asteroid2, 2);
			if(asteroid3.intersects(floor[i]))
				removeAsteroid(asteroid3, 3);
		}
		
		
		//Creates levels
		switch (status.getLevel()){
		case 2:
			restructure2();
			break;
		case 3:
			restructure3();
			break;
		case 4:
			restructure4();
			break;
		case 5:
			restructure5();
			break;
		case 6:
			restructure6();
			bossBattle(boss);
			bossFire(null);
			moveBoss(boss);
			break;
		default:
			//nothing
		}
		
		status.getAsteroidsDestroyed();
		status.getShipsLeft();
		status.getLevel();

		// update asteroids destroyed label  
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));

		// update ships left label
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));

		//update level label
		levelValueLabel.setText(Long.toString(status.getLevel()));
		
	}

	/**
	 * Draws the "Game Over" message.
	 */
	protected void drawGameOver() {
		String gameOverStr = "GAME OVER";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameOverStr);
		if(strWidth > this.getWidth() - 100){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(gameOverStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.WHITE);
		g2d.drawString(gameOverStr, strX, strY);

		boomReset();
		healthReset();
		delayReset();
		status.setLevel(0);
	}
	
	protected void drawYouAreDone() {
		String youWinStr = "You are the Boss!";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(youWinStr);
		if(strWidth > this.getWidth() - 100){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(youWinStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.RED);
		g2d.drawString(youWinStr, strX, strY); 
	

	}
	
	protected void advert() {
		String advertstr = "Beware the boss!";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(advertstr);
		if(strWidth > this.getWidth() - 100){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(advertstr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.ORANGE);
		g2d.drawString(advertstr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Next level starting soon";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.ORANGE);
		g2d.drawString(newGameStr, strX, strY);
		if(gameLogic.changeLevel()){
			boom++;
			status.setLevel(status.getLevel()+1);
			}
		

	}

	protected void drawYouWin() {
		String youWinStr = "You Pass";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(youWinStr);
		if(strWidth > this.getWidth() - 10){
			biggestFont = currentFont;
			bigFont = biggestFont;
			fm = g2d.getFontMetrics(bigFont);
			strWidth = fm.stringWidth(youWinStr);
		}
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setFont(bigFont);
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(youWinStr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Next level starting soon";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(newGameStr, strX, strY);
		if(gameLogic.changeLevel()){
			boom++;
			status.setLevel(status.getLevel()+1);
			}
		
	 

	}

	/**
	 * Draws the initial "Get Ready!" message.
	 */

	protected void drawGetReady() {
		String readyStr = "Get Ready"; 
		g2d.setFont(originalFont.deriveFont(originalFont.getSize2D() + 1));
		FontMetrics fm = g2d.getFontMetrics();
		int ascent = fm.getAscent();
		int strWidth = fm.stringWidth(readyStr);
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(readyStr, strX, strY);
	}

	/**
	 * Draws the specified number of stars randomly on the game screen.
	 * @param numberOfStars the number of stars to draw
	 */
	protected void drawStars(int numberOfStars) {
		g2d.setColor(Color.WHITE);
		for(int i=0; i<numberOfStars; i++){
			int x = (int)(Math.random() * this.getWidth());
			int y = (int)(Math.random() * this.getHeight());
			g2d.drawLine(x, y, x, y);
		}
	}

	/**
	 * Display initial game title screen.
	 */
	protected void initialMessage() {
		String gameTitleStr = "Definitely Not MegaMan";

		Font currentFont = biggestFont == null? bigFont : biggestFont;
		float fontSize = currentFont.getSize2D();
		bigFont = currentFont.deriveFont(fontSize + 1).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
		FontMetrics fm = g2d.getFontMetrics(bigFont);
		int strWidth = fm.stringWidth(gameTitleStr);
		if(strWidth > this.getWidth() - 10){
			bigFont = currentFont;
			biggestFont = currentFont;
			fm = g2d.getFontMetrics(currentFont);
			strWidth = fm.stringWidth(gameTitleStr);
		}
		g2d.setFont(bigFont);
		int ascent = fm.getAscent();
		int strX = (this.getWidth() - strWidth)/2;
		int strY = (this.getHeight() + ascent)/2 - ascent;
		g2d.setPaint(Color.YELLOW);
		g2d.drawString(gameTitleStr, strX, strY);

		g2d.setFont(originalFont);
		fm = g2d.getFontMetrics();
		String newGameStr = "Press <Space> to Start a New Game.";
		strWidth = fm.stringWidth(newGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = (this.getHeight() + fm.getAscent())/2 + ascent + 16;
		g2d.setPaint(Color.WHITE);
		g2d.drawString(newGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String itemGameStr = "Press <I> for Item Menu.";
		strWidth = fm.stringWidth(itemGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(itemGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String shopGameStr = "Press <S> for Shop Menu.";
		strWidth = fm.stringWidth(shopGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(shopGameStr, strX, strY);

		fm = g2d.getFontMetrics();
		String exitGameStr = "Press <Esc> to Exit the Game.";
		strWidth = fm.stringWidth(exitGameStr);
		strX = (this.getWidth() - strWidth)/2;
		strY = strY + 16;
		g2d.drawString(exitGameStr, strX, strY);
	}

	/**
	 * Prepare screen for game over.
	 */
	public void doGameOver(){
		shipsValueLabel.setForeground(new Color(128, 0, 0));
	}


	/**
	 * Prepare screen for a new game.
	 */
	public void doNewGame(){		
		lastAsteroidTime = -NEW_ASTEROID_DELAY;
		lastAsteroid2Time = -NEW_ASTEROID_DELAY;
		//lastBigAsteroidTime = -NEW_BIG_ASTEROID_DELAY;
		lastShipTime = -NEW_SHIP_DELAY;

		bigFont = originalFont;
		biggestFont = null;

		// set labels' text
		shipsValueLabel.setForeground(Color.BLACK);
		shipsValueLabel.setText(Integer.toString(status.getShipsLeft()));
		destroyedValueLabel.setText(Long.toString(status.getAsteroidsDestroyed()));
		levelValueLabel.setText(Long.toString(status.getLevel()));
	}

	/**
	 * Sets the game graphics manager.
	 * @param graphicsMan the graphics manager
	 */
	public void setGraphicsMan(GraphicsManager graphicsMan) {
		this.graphicsMan = graphicsMan;
	}

	/**
	 * Sets the game logic handler
	 * @param gameLogic the game logic handler
	 */
	public void setGameLogic(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
		this.status = gameLogic.getStatus();
		this.soundMan = gameLogic.getSoundMan();
	}

	/**
	 * Sets the label that displays the value for asteroids destroyed.
	 * @param destroyedValueLabel the label to set
	 */
	public void setDestroyedValueLabel(JLabel destroyedValueLabel) {
		this.destroyedValueLabel = destroyedValueLabel;
	}

	/**
	 * Sets the label that displays the value for ship (lives) left
	 * @param shipsValueLabel the label to set
	 */
	public void setShipsValueLabel(JLabel shipsValueLabel) {
		this.shipsValueLabel = shipsValueLabel;
	}

	public void setLevelValueLabel(JLabel levelValueLabel){
		this.levelValueLabel = levelValueLabel;
	}

	public int getBoom(){
		return boom;
	}
	public void setBoom(int b){
		this.boom = b;
	}
	public int boomReset(){
		boom= 0;
		return boom;
	}
	public long healthReset(){
		boom= 0;
		return boom;
	}
	public long delayReset(){
		boom= 0;
		return boom;
	}

	protected boolean Gravity(){
		MegaMan megaMan = gameLogic.getMegaMan();
		Floor[] floor = gameLogic.getFloor();
		
		for(int i=0; i<9; i++){
			if((megaMan.getY() + megaMan.getMegaManHeight() -17 < this.getHeight() - floor[i].getFloorHeight()/2) 
					&& Fall() == true){

				megaMan.translate(0 , 2);
				return true;

			}
		}
		return false;
	}
	//Bullet fire pose
	protected boolean Fire(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<Bullet> bullets = gameLogic.getBullets();
		for(int i=0; i<bullets.size(); i++){
			Bullet bullet = bullets.get(i);
			if((bullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//BigBullet fire pose
	protected boolean Fire2(){
		MegaMan megaMan = gameLogic.getMegaMan();
		List<BigBullet> bigBullets = gameLogic.getBigBullets();
		for(int i=0; i<bigBullets.size(); i++){
			BigBullet bigBullet = bigBullets.get(i);
			if((bigBullet.getX() > megaMan.getX() + megaMan.getMegaManWidth()) && 
					(bigBullet.getX() <= megaMan.getX() + megaMan.getMegaManWidth() + 60)){
				return true;
			}
		}
		return false;
	}

	//Platform Gravity
	public boolean Fall(){
		MegaMan megaMan = gameLogic.getMegaMan(); 
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if((((platform[i].getX() < megaMan.getX()) && (megaMan.getX()< platform[i].getX() + platform[i].getPlatformWidth()))
					|| ((platform[i].getX() < megaMan.getX() + megaMan.getMegaManWidth()) 
							&& (megaMan.getX() + megaMan.getMegaManWidth()< platform[i].getX() + platform[i].getPlatformWidth())))
					&& megaMan.getY() + megaMan.getMegaManHeight() == platform[i].getY()
					){
				return false;
			}
		}
		return true;
	}
	// Makes the second level platforms
	public void restructure3(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if(i<4)	platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i==4) platform[i].setLocation(50 +i*50, getHeight()/2 + 140 - 3*40);
			if(i>4){	
				int n=4;
				platform[i].setLocation(50 + i*50, getHeight()/2 + 20 + (i-n)*40 );
				n=n+2;
			}
		}
	}
	// Makes the third level platforms
	public void restructure2(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if(i<8)platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i==8) platform[i].setLocation(50 +i*50, getHeight()/2 + 140 - 3*40);
			//if(i>4){	
				//int n=0;
				//platform[i].setLocation(50 + i*50, getHeight()/2 + 20 + (i)*40 );
				//n=n+2;
			//}
		}
	}
	// New level Triangle Flag PR
	public void restructure4(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if(i<4)platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i==4) platform[i].setLocation(i*50 , getHeight()/2 + 140 - i*40);
			if(i>4){	
				platform[i].setLocation(4*50 - 50*(i-4), getHeight()/2 + 140 - i*40);
			}
		}
		
		;
	}
	// New level zigzag
	public void restructure5(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			if(i<2)platform[i].setLocation(50+ i*50, getHeight()/2 + 140 - i*40);
			if(i<5 && i>2)platform[i].setLocation(50+ (i-3)*50, getHeight()/2 + 140 - i*40);
			if(i<8 && i>5)platform[i].setLocation(50+ (i-6)*50, getHeight()/2 + 140 - i*40);
		}
		;// Infinite ?
	}
	// Level ZIGZAG 
	public void restructure6(){
		Platform[] platform = gameLogic.getNumPlatforms();
		int h=340;
		for(int i=0; i<16; i++){
			
			if(i%2==1){
				platform[i].setLocation(50, getHeight()- h  );
				
			}
			else{
				platform[i].setLocation(0, getHeight() - h);
			}
			h-=40;
				
		}
		
	}
	
	public void restructure7(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<16; i++){
				platform[i].setLocation(50*i, getHeight() - 60);
				
		}
		
	}
	
	// Creates boss 1
		public Boss bossBattle(Boss boss){
			return boss;
		}
		public Boss moveBoss(Boss boss){
			if (!isRebound){
				if(boss.getY() + boss.getBossHeight() < this.getHeight()){
					boss.translate(0, boss.getSpeed3());
					graphicsMan.drawBoss(boss, g2d, this);
				}else {isRebound = true;}
				
				
				return boss;
				}
				else
					if(boss.getY() + boss.getBossHeight() > boss.getBossHeight()){
						boss.translate(0, -boss.getSpeed3());
						graphicsMan.drawBoss(boss, g2d, this);
					}else {isRebound = false;}
					return boss;
		}
		public void bossFire(BulletBoss bulletsboss){
			long currentTime = System.currentTimeMillis();
			if((currentTime - lastBulletBossTime) > 800){
				lastBulletBossTime = currentTime;
				gameLogic.fireBulletBoss();
			}
		}

	public void removeAsteroid(Asteroid asteroid, int numOfAster){
		// "remove" asteroid
		asteroidExplosion = new Rectangle(
				asteroid.x,
				asteroid.y,
				asteroid.width,
				asteroid.height);
		asteroid.setLocation(-asteroid.width, -asteroid.height);
		switch (numOfAster){
		case 1:
			status.setNewAsteroid(true);
		case 2:
			status.setNewAsteroid2(true);
		case 3:
			status.setNewAsteroid3(true);
		}
		lastAsteroidTime = System.currentTimeMillis();

		// play asteroid explosion sound
		soundMan.playAsteroidExplosionSound();
	} 

	
	public void structure(){
		Platform[] platform = gameLogic.getNumPlatforms();
		for(int i=0; i<8; i++){
			graphicsMan.drawPlatform(platform[i], g2d, this, i);
			//			}
		}
	}
	
	public Asteroid createAsteroidlevel1(Asteroid asters, long lastAsteroidTime, int NEW_ASTEROID_DELAY, boolean newA, Rectangle explotion, int numOfAster){
		long currentTime = System.currentTimeMillis();
		if(!newA){
			if(asters.getX() + asters.getAsteroidWidth() >  0){
				asters.translate(-asters.getSpeed(), 0);
				graphicsMan.drawAsteroid(asters, g2d, this);	
			}
			// If missed asteroid
			else{
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
		}
		else
		{	
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				switch (numOfAster){
				case 1:
				status.setNewAsteroid(false);
				case 2:
				status.setNewAsteroid2(false);
				case 3:
				status.setNewAsteroid3(false);
				}
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(explotion, g2d, this);
			}

		}
		
		return asters;
		

		
	}
	public Asteroid createAsteroidlevel3(Asteroid asters, long lastAsteroidTime, int NEW_ASTEROID_DELAY, boolean newA, Rectangle explotion, int numOfAster){
		long currentTime = System.currentTimeMillis();
		if(!newA){
			if(asters.getX() + asters.getAsteroidWidth() >  0)
			{
				switch (numOfAster){
				case 1:
					asters.translate(-asters.getRandSpeed(), sinMovement(1));
					break;
				case 2:
					asters.translate(-asters.getRandSpeed(), sinMovement(2));
					break;
				case 3:
					asters.translate(-asters.getRandSpeed(), sinMovement(3));
					break;
				}
				graphicsMan.drawAsteroid(asters, g2d, this);	
			}
			// If missed asteroid
			else{
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
		}
		else
		{	
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				switch (numOfAster){
				case 1:
					status.setNewAsteroid(false);
				case 2:
					status.setNewAsteroid2(false);
				case 3:
					status.setNewAsteroid3(false);
				}
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(explotion, g2d, this);
			}

		}

		return asters;



	}
	public Asteroid createAsteroidlevel2(Asteroid asters, long lastAsteroidTime, int NEW_ASTEROID_DELAY, boolean newA, Rectangle explotion, int numOfAster){
		long currentTime = System.currentTimeMillis();
		if(!newA){
			if(asters.getX() + asters.getAsteroidWidth() >  0)
			{
				asters.translate(-asters.getRandSpeed(), asters.getRandSpeed());
				graphicsMan.drawAsteroid(asters, g2d, this);	
			}
			// If missed asteroid
			else{
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
		}
		else
		{	
			if((currentTime - lastAsteroidTime) > NEW_ASTEROID_DELAY){
				// draw a new asteroid
				lastAsteroidTime = currentTime;
				switch (numOfAster){
				case 1:
					status.setNewAsteroid(false);
				case 2:
					status.setNewAsteroid2(false);
				case 3:
					status.setNewAsteroid3(false);
				}
				asters.setLocation(this.getWidth() - asters.getAsteroidWidth(),
						rand.nextInt(this.getHeight() - asters.getAsteroidHeight() - 32));
			}
			else{
				// draw explosion
				graphicsMan.drawAsteroidExplosion(explotion, g2d, this);
			}

		}

		return asters;

	}
	public int sinMovement(int numOfAster){
		int result = 0;
		switch (numOfAster){
		case 1:
			result = (int)(5.0*Math.sin(0.5*this.xMove));
			this.xMove= this.xMove+((Math.PI)/24);
			return result;
		case 2:
			result = (int)(3.0*Math.sin(0.5*this.xMove2));
			this.xMove2= this.xMove2+((0.05*Math.PI)/12.0);
			return result;
		case 3:
			result = (int)(5.0*Math.sin(0.5*this.xMove3));
			this.xMove3= this.xMove3+((Math.PI)/12.0);
			return result;
		}
		return result;
	}

}





