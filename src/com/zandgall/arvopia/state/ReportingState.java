package com.zandgall.arvopia.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.zandgall.arvopia.Handler;
import com.zandgall.arvopia.Reporter;
import com.zandgall.arvopia.utils.Button;
import com.zandgall.arvopia.utils.TextEditor;

public class ReportingState extends State {
	
	private TextEditor subject, message;
	private Button send, back;
	
	public ReportingState(Handler handler) {
		super(handler);
		
		subject = new TextEditor(handler, 10,10, 700, 1, "Subject");
		message = new TextEditor(handler, 10,30, 700, 26, "Message");
		
		send = new Button(handler, 10, 350, 57, 25, "Click to send a message with the parameters above", "Send");
		back = new Button(handler, 70, 350, 55, 25, "Takes you back to title screen", "Back");
	}

	public void tick() {
		subject.tick();
		message.tick();
		send.tick();
		back.tick();
		
		if(back.on)
			State.setState(handler.getGame().menuState);
		if(send.on)
			Reporter.sendMessage(subject.getContent(), message.getContent());
	}

	public void render(Graphics g, Graphics2D g2d) {
		g2d.setTransform(handler.getGame().getDefaultTransform());

		g.setColor(new Color(120, 225, 255));
		
		g.fillRect(0, 0, handler.getWidth(), handler.getHeight());	
		
		subject.render(g);
		message.render(g);
		
		send.render(g);
		back.render(g);
		
	}

	public void init() {
		
	}

}
