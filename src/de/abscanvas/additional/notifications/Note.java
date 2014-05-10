package de.abscanvas.additional.notifications;

import de.abscanvas.Screen;
import de.abscanvas.math.MPoint;

public class Note {
	public final static int MODE_SLIDEIN = 0;
	public final static int MODE_LIFE = 1;
	public final static int MODE_FADEOUT = 2;
	
	public final static int INIT_X = 15;
	public final static int INIT_Y = 50;
	
    private String message;
    
    private boolean removed = false;
    
    private int[] maxlife = new int[3];
    private int[] life = new int[3];
    
    Notifications o;
    
    private int mode = -1;
    
    private MPoint pos;
    private int width;
    private int alpha;

    public Note(String message, int life0, int life1, int life2, int count, Notifications o) {
        this.message = message;
        this.o = o;
        this.life[0] = life0;
        this.life[1] = life1;
        this.life[2] = life2;
        this.maxlife = this.life.clone();
        width = message.length()*8+4;
        pos = new MPoint(-width, INIT_Y + count * 10);
        alpha = 255;
        mode = 0;
    }

    public void tick(int count) {
    	int rPosY = INIT_Y + count * 10;
    	if (rPosY<pos.getY()) {
    		pos.subY(1);
    	}
    	life[mode] = life[mode] - 1;
    	if (life[mode] <= 0) {
    		mode++;
    	}
    	if (mode >= 3) {
    		remove();
    		return;
    	}
    	
    	switch(mode) {
    		case MODE_SLIDEIN:
    			double  x = INIT_X - (INIT_X+width)*((life[0]*1d)/maxlife[0]);
    			pos.setX((int)x);
    			break;
    		case MODE_LIFE:
    			// DISPLAY
    			break;
    		case MODE_FADEOUT:
    			double a = 255d * ((life[2]*1d)/maxlife[2]);
    			alpha = (int)a;
    			break;
    	}
    }
    
    private void remove() {
    	removed = true;
    }
    
    public void render(Screen o) {
    	o.drawStringAlpha(message, pos.getX(), pos.getY(), alpha);	
    }
    
    public boolean isRemoved() {
    	return removed;
    }
}
