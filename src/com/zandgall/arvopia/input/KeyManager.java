package com.zandgall.arvopia.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.zandgall.arvopia.ArvopiaLauncher;
import com.zandgall.arvopia.Game;

public class KeyManager implements KeyListener {
	
	public boolean[] keys;
	public boolean up, down, left, right, esc, invtry, crft, k1, k2, k3, k4, k5, k6, k7, k8, k9, k0;
	public boolean b, prej;
	public boolean typed, preTyped;
	private Character typedKey;
	
	public KeyManager() {
		keys = new boolean[524];
	}
	
	public Character getNameOfKey() {
		return typedKey;
	}
	
	public int keyCode() {
		return KeyEvent.getExtendedKeyCodeForChar(typedKey);
	}
	
	public void tick(){
		up = (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_SPACE]);
		down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
		left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
		right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
		invtry = (keys[KeyEvent.VK_Z] && typed);
		crft = (keys[KeyEvent.VK_X] && typed);
		b = keys[KeyEvent.VK_B] && typed;
		
		k1 = keys[KeyEvent.VK_1];
		k2 = keys[KeyEvent.VK_2];
		k3 = keys[KeyEvent.VK_3];
		k4 = keys[KeyEvent.VK_4];
		k5 = keys[KeyEvent.VK_5];
		k6 = keys[KeyEvent.VK_6];
		k7 = keys[KeyEvent.VK_7];
		k8 = keys[KeyEvent.VK_8];
		k9 = keys[KeyEvent.VK_9];
		k0 = keys[KeyEvent.VK_0];
		
		if(keys[KeyEvent.VK_K] && keys[KeyEvent.CTRL_DOWN_MASK] && ArvopiaLauncher.game.recorder.recording) {
			ArvopiaLauncher.game.recorder.record();
		} else if(keys[KeyEvent.VK_J] && keys[KeyEvent.CTRL_DOWN_MASK] && ArvopiaLauncher.game.recorder.recording){
			System.out.println("STOP");
			ArvopiaLauncher.game.recorder.stop();
			System.out.println("DONE"+System.lineSeparator()+System.lineSeparator()+System.lineSeparator());
		}
		
		prej = keys[KeyEvent.VK_J];
		
		
		esc = keys[KeyEvent.VK_ESCAPE] && typed;
		
		preTyped = typed;
		
		typed = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!keys[e.getKeyCode()]) { 
			Game.log.log("Key Code pressed: "+e.getKeyCode() + " Name: "+e.getKeyChar());
		}
		keys[e.getKeyCode()]=true;
		typed = false;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()]=false;
		typed = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		keys[e.getKeyCode()]=true;
		typedKey = e.getKeyChar();
		typed = true;
	}

}
