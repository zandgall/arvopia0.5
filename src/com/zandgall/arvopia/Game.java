package com.zandgall.arvopia;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.zandgall.arvopia.display.Display;
import com.zandgall.arvopia.gfx.GameCamera;
import com.zandgall.arvopia.input.*;
import com.zandgall.arvopia.state.*;
import com.zandgall.arvopia.utils.LoaderException;
import com.zandgall.arvopia.utils.Public;
import com.zandgall.arvopia.utils.Utils;

public class Game implements Runnable { // Runnable = Thread
	
	public boolean paused;
	
	public void pause() {
		paused = true;
	}
	
	public void unPause() {
		paused = false;
	}
	
	
	public static double scale;
	
	// Get public assets
	
	// Work display
	private Display display; // Display class
	private int width = 0, height = 0; // Game size
	
	public int fps = 60, tps = 60, ticks = 0, fullTicks, renTicks;
	private double timePerTick = 1000000000 / tps, renderPerTick = 1000000000 / fps;
	
	public Display getDisplay() {
		return display;
	}
	
	public void setTps(int tps) {
		this.tps = tps;
		timePerTick = 1000000000 / tps;
	}

	public void setFps(int fps) {
		this.fps = fps;
		renderPerTick = 1000000000 / fps;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String title; // Game name
	public boolean resizable;

	// Input
	private KeyManager keyManager;
	private MouseManager mouse;

	// Camera
	private GameCamera gameCamera;

	// Handler
	public Handler handler;

	// Thread stuff
	private static Thread thread; // Game loop

	// Log
	public static Log log;
	String main = "C:\\Arvopia\\logs\\main.txt";

	private static boolean running = false; // Game loop boolean

	// Graphics
	private BufferStrategy bf;
	private Graphics g;
	private Graphics2D g2d;
	private boolean renOnce = false;
	
	AffineTransform af;

	public State optionState, gameState, menuState, worldState, changelogState, instructionsState, reportingState;
	
	
	public AffineTransform getDefaultTransform() {
		return af;
	}
	
	
	public Recorder recorder;
	
	// Initialate the Game class
	public Game(String title, int width, int height, boolean resizable, Log log) {
		
		Reporter reporter = new Reporter();
		
		scale = 1;
		
		Game.log = log;

		this.width = width; // Set width
		this.height = height; // Set height
		this.title = title; // Set title
		this.resizable = resizable; // Allows resizing of canvas
		keyManager = new KeyManager();
		mouse = new MouseManager(handler);
		
		
		recorder = new Recorder(handler);
		
	}
	
	public void setDimension(int width, int height) {
		this.width = width;
		this.height = height;
	}

	private void init() {
		display = new Display(title, width, height); // Initialize display adding width, height, and title
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouse);
		display.getFrame().addMouseMotionListener(mouse);
		display.getFrame().setResizable(resizable);
		display.getCanvas().addMouseListener(mouse);
		display.getCanvas().addMouseMotionListener(mouse);
		display.getCanvas().addMouseWheelListener(mouse);

		handler = new Handler(this);
		handler.init();
		gameCamera = new GameCamera(handler, 0, 0);
		
		optionState = new OptionState(handler);
		gameState = new GameState(handler);
		menuState = new TitleState(handler);
		worldState = new WorldLoaderState(handler);
		changelogState = new Changelog(handler);
		instructionsState = new Instructions(handler);
		reportingState = new ReportingState(handler);
		
		handler.log("Will open? : " + LoaderException.readFile("C:\\Arvopia\\DontShowThisAgain"));
		
		if(Utils.parseBoolean(LoaderException.readFile("C:\\Arvopia\\DontShowThisAgain"))) {
			State.setState(menuState);
			handler.log("True");
		} else 
			State.setState(instructionsState);
		
		State.getState().init();
		
		Public.init(handler);
		
		log.log("Successfully initiated " + title + "'s Game loop");
	}
	
	private void tick() { // Update vars posistions and objects
		keyManager.tick();
		
		if (State.getState() != null && !paused) {
			State.getState().tick();
		} else if(paused) {
			handler.getWorld().getEntityManager().getPlayer().tick();
		}
		mouse.tick();
	}
	
	public void forceRender() {
		render();
	}
	
	private void render() { // Draws to screen

		bf = display.getCanvas().getBufferStrategy();
		if (bf == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}

		g = bf.getDrawGraphics();
		g2d = (Graphics2D) g;
		
		if(!renOnce) {
			af = g2d.getTransform();
			renOnce = true;
		}
		
		// Clear screen
			g.clearRect(0, 0, width, height);
		/// Start draw
		if (State.getState() != null) {
			State.getState().render(g, g2d);
		}
		
		g2d.transform(af);
		
		// End draw

		bf.show();
		g.dispose();
		g2d.dispose();
	}
	
	public int prepreTicks, preTicks, preRenTicks;
	public boolean stable;
	
	public void run() { // Game loop

		init(); // Initiates graphics
		
		double delta = 0, renderDelta = 0;
		long now;
		long nowSec;
		long lastTime = System.nanoTime();
		long timer = 0;
		
		long prevCheck = System.currentTimeMillis() / 1000;
		
		while (isRunning()) { // official loop
			now = System.nanoTime();
			nowSec = Math.round(System.currentTimeMillis() / 1000);
			delta += (now - lastTime) / timePerTick;
			renderDelta += (now-lastTime) / renderPerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				ticks++;
				delta=0;
			}
			
			if(renderDelta >= 1) {
				render();
				renTicks++;
				renderDelta=0;
			}
			
			if (timer >= 1000000000) {
					fullTicks = ticks;
					handler.saveFps(fullTicks);
					
					
					stable = (Public.difference(preTicks, prepreTicks))<2 && ((Public.difference(preTicks, ticks))<2) && Public.difference(ticks, fps) < 5 || ticks<Public.range(0, fps, fps-20);
					
					
					
					prevCheck = nowSec;
					
					if (nowSec - prevCheck <= 2 && (Public.difference(renTicks, preRenTicks) > 2 || Public.difference(ticks, preTicks)>2)) {
						log.log("FPS Fluctuating a little... : " + renTicks + " : " + ticks);
					} else if (stable) {
						log.log("FPS: " + renTicks + " : " + ticks);
					} else {
						log.log("FPS Not stable: " + renTicks +  " : "+ ticks);
					}
					
					prepreTicks = preTicks;
					preTicks = ticks;
					preRenTicks = renTicks;
					
					mouse.reset();

				ticks = 0;
				renTicks = 0;
				timer = 0;
			}
		}

		stop(); // X_X

	}

	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public MouseManager getMouse() {
		return mouse;
	}

	public GameCamera getGameCamera() {
		return gameCamera;
	}

	public synchronized void start() {
		if (isRunning()) // Safety
			return; // Returns to loop
		setRunning(true);
		thread = new Thread(this); // Creates thread
		thread.start(); // Starts it

	}

	public synchronized void stop() {
		if (!isRunning()) // Safety
			return; // Returns to loop
		try {
			log.out("Terminated");
			thread.join(); // Stops: requires try/catch
			display.getFrame().dispatchEvent(new WindowEvent(display.getFrame(), WindowEvent.WINDOW_CLOSING));
			display.getFrame().dispose();
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static boolean isRunning() {
		return running;
	}

	public static void setRunning(boolean running) {
		Game.running = running;
	}

	public Image makeImage(Shape s) {
		Rectangle r = s.getBounds();
		Image image = new BufferedImage(r.width, r.height, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D gr = (Graphics2D) image.getGraphics();
		// move the shape in the region of the image
		gr.translate(-r.x, -r.y);
		gr.draw(s);
		gr.dispose();
		return image;
	}
	
	public Graphics2D get2D() {
		return g2d;
	}

	public JFrame getFrame() {
		return display.getFrame();
	}

}
