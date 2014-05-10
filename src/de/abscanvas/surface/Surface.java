package de.abscanvas.surface;

public interface Surface{
    public void setOffset(double xOffset, double yOffset);
    
    public void alphaBlit(AbstractBitmap bitmap, int x, int y, int alpha);
    
    public void alphaBlit(AbstractBitmap bitmap, double x, double y, int alpha);
    
    public void blit(AbstractBitmap bitmap, double x, double y);

    public void blit(AbstractBitmap bitmap, int x, int y);

    public void blit(AbstractBitmap bitmap, int x, int y, int w, int h);
    
    public void blit(AbstractBitmap bitmap, double x, double y, int w, int h);

    public void colorBlit(AbstractBitmap bitmap, double x, double y, int color);

    public void colorBlit(AbstractBitmap bitmap, int x, int y, int color);

    public void fill(int x, int y, int width, int height, int color);
    
    public Object getImage();

	public int getWidth();

	public int getHeight();
}
