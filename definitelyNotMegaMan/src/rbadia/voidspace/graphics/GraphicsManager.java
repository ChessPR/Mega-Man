package rbadia.voidspace.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import rbadia.voidspace.model.Asteroid;
//import rbadia.voidspace.model.BigAsteroid;
import rbadia.voidspace.model.BigBullet;
import rbadia.voidspace.model.Boss;
import rbadia.voidspace.model.Bullet;
import rbadia.voidspace.model.BulletBoss;
import rbadia.voidspace.model.Floor;
//import rbadia.voidspace.model.BulletBoss;
//import rbadia.voidspace.model.BulletBoss2;
import rbadia.voidspace.model.MegaMan;
import rbadia.voidspace.model.Platform;

/**
 * Manages and draws game graphics and images.
 */
public class GraphicsManager {
	private BufferedImage megaManImg;
	private BufferedImage megaFallRImg;
	private BufferedImage megaFireRImg;
	private BufferedImage floorImg;
	private BufferedImage platformImg;
	private BufferedImage bulletImg;
	private BufferedImage bigBulletImg;
	private BufferedImage asteroidImg;
	private BufferedImage asteroidExplosionImg;
	private BufferedImage megaManExplosionImg;
	private BufferedImage backlevel1;
	private BufferedImage backlevel2;
	private BufferedImage backlevel3;
	private BufferedImage backlevel4;
	private BufferedImage backlevel5;
	private BufferedImage backlevel6;
	private BufferedImage extra;
	private BufferedImage extra2;
	private BufferedImage bluebullet;
	private BufferedImage starting;
	
	private BufferedImage bossImg;
	//	private BufferedImage bossImg2;
	//	private BufferedImage bigAsteroidImg;
	private BufferedImage bigAsteroidExplosionImg;

	/**
	 * Creates a new graphics manager and loads the game images.
	 */
	public GraphicsManager(){
		// load images
		try {
			this.megaManImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaMan3.png"));
			this.megaFallRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFallRight.png"));
			this.megaFireRImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFireRight.png"));
			this.floorImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaFloor.png"));
			this.platformImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/platform3.png"));
			this.bossImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/boss1.png"));
			//			this.bossImg2 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/boss2.png"));
			this.asteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroid.png"));
			this.asteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/asteroidExplosion.png"));
			//this.megaManExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/megaManExplosion.png"));
			this.bulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bullet.png"));
			this.bigBulletImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigBullet.png"));
			this.backlevel1 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level1.jpg"));
			this.backlevel2 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level2.jpg"));
			this.backlevel3 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level3.jpeg"));
			this.backlevel4 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level4.jpg"));
			this.backlevel5 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level5.jpg"));
			this.backlevel6 = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/level6.jpg"));
			this.bluebullet = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/blueBullet.png"));
			this.starting = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/starting.jpg"));
			
			//			this.bigAsteroidImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/BigAsteroid.png"));
			//			this.bigAsteroidExplosionImg = ImageIO.read(getClass().getResource("/rbadia/voidspace/graphics/bigAsteroidExplosion.png"));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "The graphic files are either corrupt or missing.",
					"VoidSpace - Fatal Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Draws a ship image to the specified graphics canvas.
	 * @param ship the ship to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */

	public void drawMegaMan (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaManImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFallR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFallRImg, megaMan.x, megaMan.y, observer);	
	}

	public void drawMegaFireR (MegaMan megaMan, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(megaFireRImg, megaMan.x, megaMan.y, observer);	
	}
	public void drawBoss (Boss boss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bossImg, boss.x, boss.y, observer);
	}

	public void drawFloor (Floor floor, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(floorImg, floor.x, floor.y, observer);				
	}
	public void drawPlatform(Platform platform, Graphics2D g2d, ImageObserver observer, int i){
			g2d.drawImage(platformImg, platform.x , platform.y, observer);	
	}
	
	public void drawPlatform2 (Platform platform, Graphics2D g2d, ImageObserver observer, int i){
		g2d.drawImage(platformImg, platform.x , platform.y, observer);	
}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBullet(Bullet bullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bulletImg, bullet.x, bullet.y, observer);
	}
	public void drawBulletBoss(BulletBoss bulletBoss, Graphics2D g2d, ImageObserver observer){
		g2d.drawImage(bluebullet, bulletBoss.x, bulletBoss.y, observer);
		}

	/**
	 * Draws a bullet image to the specified graphics canvas.
	 * @param bigBullet the bullet to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawBigBullet(BigBullet bigBullet, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigBulletImg, bigBullet.x, bigBullet.y, observer);
	}

	/**
	 * Draws an asteroid image to the specified graphics canvas.
	 * @param asteroid the asteroid to draw
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroid(Asteroid asteroid, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidImg, asteroid.x, asteroid.y, observer);
	}

	/**
	 * Draws a ship explosion image to the specified graphics canvas.
	 * @param shipExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawMegaManExplosion(Rectangle megaManExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(megaManExplosionImg, megaManExplosion.x, megaManExplosion.y, observer);
	}

	/**
	 * Draws an asteroid explosion image to the specified graphics canvas.
	 * @param asteroidExplosion the bounding rectangle of the explosion
	 * @param g2d the graphics canvas
	 * @param observer object to be notified
	 */
	public void drawAsteroidExplosion(Rectangle asteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(asteroidExplosionImg, asteroidExplosion.x, asteroidExplosion.y, observer);
	}

	public void drawBigAsteroidExplosion(Rectangle bigAsteroidExplosion, Graphics2D g2d, ImageObserver observer) {
		g2d.drawImage(bigAsteroidExplosionImg, bigAsteroidExplosion.x, bigAsteroidExplosion.y, observer);
	}

	public BufferedImage getBackgroundlevel1() {
		return backlevel1;
	}

	public void setBackgroundlevel1(BufferedImage backgroundlevel1) {
		this.backlevel1 = backgroundlevel1;
	}

	public BufferedImage getBacklevel1() {
		return backlevel1;
	}

	public void setBacklevel1(BufferedImage backlevel1) {
		this.backlevel1 = backlevel1;
	}

	public BufferedImage getBacklevel2() {
		return backlevel2;
	}

	public void setBacklevel2(BufferedImage backlevel2) {
		this.backlevel2 = backlevel2;
	}

	public BufferedImage getBacklevel3() {
		return backlevel3;
	}

	public void setBacklevel3(BufferedImage backlevel3) {
		this.backlevel3 = backlevel3;
	}

	public BufferedImage getBacklevel4() {
		return backlevel4;
	}

	public void setBacklevel4(BufferedImage backlevel4) {
		this.backlevel4 = backlevel4;
	}

	public BufferedImage getBacklevel5() {
		return backlevel5;
	}

	public void setBacklevel5(BufferedImage backlevel5) {
		this.backlevel5 = backlevel5;
	}

	public BufferedImage getBacklevel6() {
		return backlevel6;
	}

	public void setBacklevel6(BufferedImage backlevel6) {
		this.backlevel6 = backlevel6;
	}

	public BufferedImage getBluebullet() {
		return bluebullet;
	}

	public void setBluebullet(BufferedImage bluebullet) {
		this.bluebullet = bluebullet;
	}

	public BufferedImage getStarting() {
		return starting;
	}

	public void setStarting(BufferedImage starting) {
		this.starting = starting;
	}



}