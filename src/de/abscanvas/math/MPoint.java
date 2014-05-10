package de.abscanvas.math;

/**
 * a Implementation of an 2D-MPoint with X and Y as Integers
 * 
 * @author Mikescher
 */
public class MPoint {
	/**
	 * Attribute der Klasse
	 */

	private int x;
	private int y;

	/**
	 * MPoint: Methoden
	 */

	/**
	 * MPoint Constructor
	 * 
	 * erzeugt ein neues Objekt MPoint an der Position (-1|-1)
	 */
	public MPoint() {
		x = -1;
		y = -1;
	}

	/**
	 * MPoint Constructor - erzeugt ein neues Objekt MPoint auf Basis eines X
	 * und eines Y-Wertes
	 * 
	 * @param iX
	 *            Die Position auf der X-Achse
	 * @param iY
	 *            Die Position auf der Y-Achse
	 */
	public MPoint(int iX, int iY) {
		x = iX;
		y = iY;
	}

	/**
	 * MPoint Constructor - erzeugt ein neues Objekt MPoint auf Basis eines
	 * anderen MPoint
	 * 
	 * @param iPos
	 *            Die Position die in MPoint gespeichert werden soll
	 */
	public MPoint(MPoint iPos) {
		x = iPos.getX();
		y = iPos.getY();
	}

	public MPoint(double dx, double dy) {
		x = (int) Math.round(dx);
		y = (int) Math.round(dy);
	}
	
	public MPoint(java.awt.Point p) {
		if (p != null) {
			x = p.x;
			y = p.y;
		} else {
			x = -1;
			y = -1;
		}
	}

	/**
	 * Die Fabrikmethode der Klasse MPoint - es wird ohne große Umwege ein neues
	 * Objekt vom Typ MPoint erzeugt (auf Basis eines anderen MPoint-Objekts)
	 */
	static MPoint Pos(MPoint iPos) {
		return new MPoint(iPos.getX(), iPos.getY());
	}

	/**
	 * Die Fabrikmethode der Klasse MPoint - es wird ohne große Umwege ein neues
	 * Objekt vom Typ MPoint erzeugt (auf Basis eines X und eines Y-Wertes)
	 */
	static MPoint Pos(int iX, int iY) {
		return new MPoint(iX, iY);
	}

	/**
	 * gibt den aktuellen X-Wert zurück
	 */
	public int getX() {
		return x;
	}

	/**
	 * gibt den aktuellen X-Wert zurück
	 */
	public int getY() {
		return y;
	}

	/**
	 * setzt die Position auf der X-Achse
	 */
	public void setX(int aX) {
		x = aX;
	}

	/**
	 * setzt die Position auf der Y-Achse
	 */
	public void setY(int aY) {
		y = aY;
	}

	/**
	 * setzt die Position des MPoint (X und Y)
	 */
	public void set(int aX, int aY) {
		setX(aX);
		setY(aY);
	}

	/**
	 * setzt die Position des MPoint
	 */
	public void set(MPoint pos) {
		set(pos.getX(), pos.getY());
	}

	@Override
	public MPoint clone() {
		return MPoint.Pos(this);
	}

	/**
	 * Bewegt den Punkt auf der X-Achse
	 * 
	 * @param addX
	 *            Die Distanz auf der X-Achse
	 */
	public void moveX(int addX) {
		x += addX;
	}

	/**
	 * Bewegt den Punkt auf der Y-Achse
	 * 
	 * @param addY
	 *            Die Distanz auf der Y-Achse
	 */
	public void moveY(int addY) {
		y += addY;
	}
	
	/**
	 * Bewegt den Punkt auf beiden Achsen
	 * 
	 * @param addX
	 *            Die Distanz auf der X-Achse
	 * @param addY
	 *            Die Distanz auf der Y-Achse
	 */
	public void move(int addX, int addY) {
		moveX(addX);
		moveY(addY);
	}
	
	public void add(int n) {
		x += n;
		y += n;
	}
	
	public void add(MPoint n) {
		x += n.getX();
		y += n.getY();
	}
	
	public void add(int nx, int ny) {
		x += nx;
		y += ny;
	}
	
	public void addX(int nX) {
		moveX(nX);
	}

	public void addY(int nY) {
		moveY(nY);
	}
	
	public void sub(int n) {
		x -= n;
		y -= n;
	}
	
	public void sub(MPoint n) {
		x -= n.getX();
		y -= n.getY();
	}
	
	public void sub(int nx, int ny) {
		x -= nx;
		y -= ny;
	}
	
	public void subX(int nx) {
		x -= nx;
	}
	
	public void subY(int ny) {
		y -= ny;
	}
	
	public void div(int n) {
		x /= n;
		y /= n;
	}
	
	public void div(double n) {
		setRounded(x/n, y/n);
	}
	
	public void div(MPoint n) {
		x /= n.getX();
		y /= n.getY();
	}
	
	public void div(int nx, int ny) {
		x /= nx;
		y /= ny;
	}
	
	public void mult(int n) {
		x *= n;
		y *= n;
	}
	
	public void mult(double n) {
		setRounded(x*n, y*n);
	}
	
	public void mult(MPoint n) {
		x *= n.getX();
		y *= n.getY();
	}
	
	public void mult(int nx, int ny) {
		x *= nx;
		y *= ny;
	}
	
	public void abs() {
		absX();
		absY();
	}
	
	public void absX() {
		x = Math.abs(x);
	}
	
	public void absY() {
		y = Math.abs(y);
	}
	
	/**
	 * Bewegt den Punkt auf einem Winkel einen bestimten Weg
	 * 
	 * @param degAngle
	 *            Der Winkel auf dme sich der Punkt bewegt im Gradmaß
	 * @param distance
	 *            Die DIstanz die der Punkt zurücklegen soll
	 */
	public void moveByAngle(int degAngle, double distance) {
		if (distance == 0) {
			return;
		}

		double Angle = Math.toRadians(degAngle);

		double dX = Math.cos(Angle) * distance;
		double dY = -Math.sin(Angle) * distance;

		int idX = (int) Math.round(dX);
		int idY = (int) Math.round(dY);

		move(idX, idY);
	}

	/**
	 * Vergleicht zwei Punkte auf übereinstimmende Position
	 * 
	 * @param iPoint
	 *            Der Punkt mit dem verglichen werden soll
	 * @return gibt True zurück wenn die beiden Punkte identisch sind (gleiche
	 *         Position)
	 */
	public boolean compare(MPoint iPoint) {
		return getX() == iPoint.getX() && getY() == iPoint.getY();
	}

	/**
	 * Vergleicht zwei Punkte auf nahezu übereinstimmende Position (kann maximal
	 * um den Wert range abweichen)
	 * 
	 * @param iPoint
	 *            Der Punkt mit dem verglichen werden soll
	 * @param range
	 *            Der maximale Unterschied in X-Richtung als auch in Y-Richtung
	 * @return gibt True zurück wenn die beiden Punkte identisch sind (gleiche
	 *         Position)
	 */
	public boolean nearlyCompare(MPoint iPoint, int range) {
		int dX = Math.abs(getX() - iPoint.getX());
		int dY = Math.abs(getY() - iPoint.getY());
		return (dX <= range) && (dY <= range);
	}

	/**
	 * überprüft ein beliebiges Objekt, ob es ein Objekt von MPoint ist und mit
	 * diesem MPoint übereinstimmt
	 * 
	 * @param equalPoint
	 *            Das zu vergleichende Objekt
	 * @return gibt True zurück wenn das Objekt eine Instanz von MPoint ist und
	 *         die beiden Punkte identisch sind (gleiche Position)
	 */
	@Override
	public boolean equals(Object equalPoint) {
		if ((equalPoint == null) || !(equalPoint instanceof MPoint)) {
			return false;
		} else {
			return compare((MPoint) equalPoint);
		}
	}

	/**
	 * überprüft ein beliebiges Objekt, ob es ein Objekt von MPoint ist und mit
	 * diesem MPoint übereinstimmt oder deren unterschiede nur sehr gering sind
	 * (kleiner als range)
	 * 
	 * @param equalPoint
	 *            Das zu vergleichende Objekt
	 * @param range
	 *            Der maximale Unterschied in X-Richtung als auch in Y-Richtung
	 * @return gibt True zurück wenn das Objekt eine Instanz von MPoint ist und
	 *         die beiden Punkte identisch sind (gleiche Position)
	 */
	public boolean nearlyEquals(Object equalPoint, int range) {
		if ((equalPoint == null) || !(equalPoint instanceof MPoint)) {
			return false;
		} else {
			return nearlyCompare((MPoint) equalPoint, range);
		}
	}

	/**
	 * berechnet den Abstand zweier Punkte über den Satz des Pythagoras
	 * 
	 * @param otherP
	 *            Den 2. Punkt der Abstandberechnung
	 * @return Der Abstand der 2 Punkte
	 */
	public double getDistance(MPoint otherP) {
		int dx = otherP.getX() - this.getX();
		int dy = otherP.getY() - this.getY();

		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

	}

	/**
	 * berechnet den Winkel zweier Punkte relativ zur X-Achse im Gradmaß
	 * 
	 * @param otherP
	 *            Den 2. Punkt der Winkelberechnung
	 * @return Der Winkel der 2 Punkte
	 */
	public int getAngle(MPoint otherP) {
		int dx = otherP.getX() - this.getX();
		int dy = otherP.getY() - this.getY();
		int adx = Math.abs(dx);
		int ady = Math.abs(dy);

		if (dy == 0 && dx == 0)
			return 0;
		else if (dy == 0 && dx > 0)
			return 0;
		else if (dy == 0 && dx < 0)
			return 180;
		else if (dy > 0 && dx == 0)
			return 90;
		else if (dy < 0 && dx == 0)
			return 270;

		double rwinkel = Math.atan((double) ady / adx);
		double dWinkel = 0;

		if (dx > 0 && dy > 0) // 1. Quartal Winkkel von 270° - 359°
			dWinkel = Math.toDegrees(rwinkel);
		else if (dx < 0 && dy > 0) // 2. Quartal Winkkel von 180° - 269°
			dWinkel = 180 - Math.toDegrees(rwinkel);
		else if (dx > 0 && dy < 0) // 3. Quartal Winkkel von 90° - 179°
			dWinkel = 360 - Math.toDegrees(rwinkel);
		else if (dx < 0 && dy < 0) // 4. Quartal Winkkel von 0° - 89°
			dWinkel = 180 + Math.toDegrees(rwinkel);

		int iWinkel = (int) dWinkel;

		if (iWinkel == 360)
			iWinkel = 0;

		return iWinkel;

	}
	
	public int getAngle() {
		return getAngle(new MPoint(0, 0));
	}

	public MPoint rotate(double radian, int aroundX, int aroundY, boolean makeToMultipleOf8) {
		double sw = Math.sin(radian);
		double cw = Math.cos(radian);
		double Hx = x - aroundX;
		double Hy = y - aroundY;

		MPoint result = new MPoint();
		double nx = aroundX + (Hx * cw - Hy * sw);
		double ny = aroundY + (Hx * sw + Hy * cw);
		result.set((int) Math.round(nx), (int) Math.round(ny));

		if (makeToMultipleOf8) {
			result.roundToMultipleOf(8);
		}

		return result;
	}
	
	public MPoint rotateQuarter(int rotation, int aroundX, int aroundY, boolean makeToMultipleOf8) {
		MPoint result = new MPoint(aroundX, aroundY);
		int rot = -1000;
		
		switch (rotation) {
		case -4:
			return this;
		case -3:
			rot = 3;
			break;
		case -2:
			rot = 2;
			break;
		case -1:
			rot = 1;
			break;
		case 0:
			return this;
		case 1:
			rot = 3;
			break;
		case 2:
			rot = 2;
			break;
		case 3:
			rot = 1;
			break;
		case 4:
			return this;
		default:
			return this;
		}
		
		int tx = x - aroundX;
		int ty = y - aroundY;
		
		switch (rot) {
		case 1:
			result.move(ty, -tx);
			break;
		case 2:
			result.move(-tx, -ty);
			break;
		case 3:
			result.move(-ty, tx);
			break;
		}
		

		if (makeToMultipleOf8) {
			result.roundToMultipleOf(8);
		}

		return result;
	}

	public void roundToMultipleOf(float i) {
		x = (int) (Math.round(x / i) * i);
		y = (int) (Math.round(y / i) * i);
	}

	public String asString() {
		return "(" + Integer.toString(getX()) + "|" + Integer.toString(getY()) + ")";
	}

	public void setRounded(double d, double e) {
		set((int) Math.round(d), (int) Math.round(e));
	}
	
	public boolean isInRect(MRect rect) {	
		return (x>rect.getLeft()) && (x<rect.getRight()) && (y>rect.getTop()) && (y<rect.getBottom());
	}
	
	public void negate() {
		x = -x;
		y = -y;
	}
	
	/**
	 * @return the Length from (0|0) to this MPoint (Length of Vektor x)
	 */
	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}
	
	public void setLength(double length) {
		mult(length/getLength());
	}
	
	public boolean isValid() {
		return x!=-1 || y!=-1;
	}

	public boolean isInvalid() {
		return !isValid();
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}

	public MDPoint asMDPoint() {
		return new MDPoint(this);
	}
	
	public MPoint getSuperiorAxis() {
		if (isZero()) {
			return new MPoint(0, 0);
		}
		
		if (Math.abs(x) > Math.abs(y)) {
			if (x < 0) {
				return new MPoint(-1, 0);
			} else {
				return new MPoint(1, 0);
			}
		} else if (Math.abs(x) < Math.abs(y)) {
			if (y < 0) {
				return new MPoint(0, -1);
			} else {
				return new MPoint(0, 1);
			}
		} else {
			if (x < 0) {
				if (y < 0) {
					return new MPoint(-1, -1);
				} else {
					return new MPoint(-1, 1);
				}
			} else {
				if (y < 0) {
					return new MPoint(1, -1);
				} else {
					return new MPoint(1, 1);
				}
			}
		}
	}
	
	public void setXPositive() {
		x = Math.abs(x);
	}
	
	public void setXNegative() {
		x = - Math.abs(x);
	}
	
	public void setYPositive() {
		y = Math.abs(y);
	}
	
	public void setYNegative() {
		y = - Math.abs(y);
	}
	
	public void setXYPositive() {
		setXPositive();
		setYPositive();
	}
	
	public void setXYNegative() {
		setXNegative();
		setYNegative();
	}
	
	public void makeOrthogonal() {
		if (isZero()) return;

		set(-y, x);
	}
	
	/**
	 * Interprets points as vektors !
	 * 
	 * @param m the vektor to mirror our vektor
	 */
	public void mirrorAt(MPoint axis) {
		MPoint normale = axis.clone();
		normale.makeOrthogonal();
		
		if (isZero()) {
			return;
		}
		
		if (axis.isZero()) {
			return;
		}
		
		if (axis.getX() == 0) {
			setX(- getX());
			return;
		}
		
		if (axis.getY() == 0) {
			setY(- getY());
			return;
		}
		
		double b = axis.getY() / axis.getX();
		
		double s = (0 - this.getY() + b*(this.getX())) / (normale.getY() - normale.getX()*b);
		
		MDPoint sp = new MDPoint(this.getX() + s * normale.getX(), this.getY() + s * normale.getY());
		
		sp.mult(2);		
		sp.sub(this.asMDPoint());
		
		set(sp.roundToMPoint());
	}
}

