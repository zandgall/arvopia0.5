package com.zandgall.arvopia.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.zandgall.arvopia.Game;
import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.utils.Button;
import com.zandgall.arvopia.utils.LoaderException;
import com.zandgall.arvopia.utils.Slider;
import com.zandgall.arvopia.utils.ToggleButton;
import com.zandgall.arvopia.utils.Utils;

public class OptionState extends State{
	
	public Slider fps, timeSpeed, scale, lightQuality, volume;
	private int preVolume = 0;
	private Button back;
	public ToggleButton sliderProblem, pauseOnExit;
	
	public OptionState(Handler handler) {
		super(handler);
		
		back = new Button(handler, 10, handler.getHeight()-30, 55, 20, "Brings you back to the main menu", "Back");
		//screenset =
		new Button(handler, 10, 140, 55, 20, "Switches between fullscreen and deafault size", "Fullscreen");
		volume = new Slider(handler, 0, 100, 75, true, "Volume");
		sliderProblem = new ToggleButton(handler, 10, 140, 130, 20, new BufferedImage[] {}, "Turn on if you're having problems with the sliders", "Slider Debug");
		pauseOnExit = new ToggleButton(handler, 150, 140, 195, 20, new BufferedImage [] {}, "Changes whether the game pauses when your mouse leaves the screen", "Pause on Mouse Exit");
		fps = new Slider(handler, 1, 90, 60, true, "FPS");
		scale = new Slider(handler, 1, 25, 5, true, "Scale");
		timeSpeed = new Slider(handler, 0, 100, 2, true, "Time Speed");
		lightQuality = new Slider(handler, 1, 36, 6, true, "Light Quality");
		
		if(LoaderException.readFile("C:\\Arvopia\\Options.txt") != null) {
			
			String[] tokens = new String[6]; 
			tokens = LoaderException.readFile("C:\\Arvopia\\Options.txt").split("\\s+");
			
			for(String i : tokens)
				handler.log(i);
			
			if(tokens.length < 6 || !(tokens[4].contains("false") || tokens[4].contains("true"))) {
				
				handler.log("			"+(tokens[4].contains("false") || tokens[4].contains("true")) + " " + (tokens.length<6));
				
				Utils.fileWriter(fps.getValue()+" "+timeSpeed.getValue()+" "+lightQuality.getValue()+" "+volume.getValue()+" "+sliderProblem.on+" "+true, "C:\\Arvopia\\Options.txt");
				
				tokens = LoaderException.readFile("C:\\Arvopia\\Options.txt").split("\\s+");
			}
			
			fps.setValue(Utils.parseInt(tokens[0]));
			timeSpeed.setValue(Utils.parseInt(tokens[1]));
			lightQuality.setValue(Utils.parseInt(tokens[2]));
			volume.setValue(Utils.parseInt(tokens[3]));
			
			sliderProblem.on = Utils.parseBoolean(tokens[4]);
			pauseOnExit.on = Utils.parseBoolean(tokens[5]);
			
			handler.log("			" + pauseOnExit.on + tokens[5]);
			
		}
		
	}

	@Override
	public void tick() {
		
//		if(scale.getValue() == 5) {
			Game.scale = 1;
//		} else {
//			Game.scale = scale.getWholeValue()/5;
//		}
		
		if(back.on) {
			State.setState(State.getPrev());
			
			String filename = "C:\\Arvopia\\Options.txt";
			
			Utils.fileWriter(fps.getValue()+" "+timeSpeed.getValue()+ " "+lightQuality.getValue()+" "+volume.getValue()+" "+sliderProblem.on+" "+pauseOnExit.on, filename);
			
			
		}
		
		handler.getMouse().setSliderMalfunction(sliderProblem.on);
		
//		screenset.tick();
		lightQuality.tick(130, 20);
		sliderProblem.tick();
		pauseOnExit.tick();
		back.tick();
		timeSpeed.tick(15, 60);
		fps.tick(15, 20);
//		scale.tick(15, 100);
		volume.tick(130, 60);
		
		handler.getWorld().getEnviornment().lightQuality = lightQuality.getValue();
		
		handler.getGame().setFps(fps.getValue());
		handler.getWorld().getEnviornment().setTimeSpeed(timeSpeed.getValue());
		
		if(volume.getValue() != preVolume)
			handler.setVolume();
		preVolume = volume.getValue();
		
		
		if(lightQuality.hovered || sliderProblem.hovered || pauseOnExit.hovered || back.hovered || timeSpeed.hovered || fps.hovered || volume.hovered)
			handler.getMouse().changeCursor("HAND");
		else handler.getMouse().changeCursor("");
		
	}

	@Override
	public void render(Graphics g, Graphics2D g2d) {
		
		g2d.setTransform(handler.getGame().getDefaultTransform());
		
		g.setColor(new Color(134,200,255));
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());
//		screenset.render(g);
		sliderProblem.render(g);
		pauseOnExit.render(g);
		back.render(g);
		lightQuality.render(g);
		volume.render(g);
		timeSpeed.render(g);
		fps.render(g);
//		scale.render(g);
	}

	@Override
	public void init() {
		
	}

}
