package de.abscanvas.input;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Keys {
    public final class Key {
        public boolean nextState = false;
        public boolean wasDown = false;
        public boolean isDown = false;

        public Key() {
            keyList.add(this);
        }

        public void tick() {
            wasDown = isDown;
            isDown = nextState;
        }

        public boolean wasPressed() {
            return !wasDown && isDown;
        }

        public boolean wasReleased() {
            return wasDown && !isDown;
        }

        public void release() {
            nextState = false;
        }
    }

    private List<Key> keyList = new ArrayList<Key>();
    
    private String keyHistory = "";
    
    private ArrayList<KeyEvent> pressedKeys = new ArrayList<KeyEvent>();

    public void tick() {
        for (Key key : keyList) {
        	key.tick();
        }
        
        pressedKeys.clear();
    }

    public void release() {
        for (Key key : keyList) {
        	key.release();
        }  
        
        pressedKeys.clear();
    }
    
    public boolean isKeyPressed(int keyCode) {
    	for (int i = 0; i < pressedKeys.size(); i++) {
			if (pressedKeys.get(i).getKeyCode() == keyCode) {
				return true;
			}
		}
    	return false;
    }

    public List<Key> getAll() {
        return keyList;
    }

	public void keyEvent(KeyEvent ke, boolean state) {
		if(state) {
			pressedKeys.add(ke);
			addToHistory(ke);
		}
	}
	
	public String getKeyHistory() {
		return keyHistory;
	}
	
	public void addReadSignToHistory() {
		keyHistory += "[#getKey]";
	}
	
	public boolean keyHistoryEndsWith(String eW) {
		return keyHistory.endsWith(eW);
	}
	
	private void addToHistory(KeyEvent ke) {
		if (ke.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
			if  (Character.isLetterOrDigit(ke.getKeyChar())) {
				keyHistory += ke.getKeyChar();
				return;
			}	
		}
		switch(ke.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				keyHistory += " ";
				return;
			case KeyEvent.VK_F1:
				keyHistory += "[#F1]";
				return;
			case KeyEvent.VK_F2:
				keyHistory += "[#F2]";
				return;
			case KeyEvent.VK_F3:
				keyHistory += "[#F3]";
				return;
			case KeyEvent.VK_F4:
				keyHistory += "[#F4]";
				return;
			case KeyEvent.VK_F5:
				keyHistory += "[#F5]";
				return;
			case KeyEvent.VK_F6:
				keyHistory += "[#F6]";
				return;
			case KeyEvent.VK_F7:
				keyHistory += "[#F7]";
				return;
			case KeyEvent.VK_F8:
				keyHistory += "[#F8]";
				return;
			case KeyEvent.VK_F9:
				keyHistory += "[#F9]";
				return;
			case KeyEvent.VK_F10:
				keyHistory += "[#F10]";
				return;
			case KeyEvent.VK_F11:
				keyHistory += "[#F11]";
				return;
			case KeyEvent.VK_F12:
				keyHistory += "[#F12]";
				return;
			case KeyEvent.VK_BACK_SPACE:
				keyHistory += "[#BackSpace]";
				return;
			case KeyEvent.VK_ENTER:
				keyHistory += "[#Enter]";
				return;
		}
	}

	public ArrayList<KeyEvent> getPressedKeys() {
		return pressedKeys;
	}
}
