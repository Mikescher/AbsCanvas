package de.abscanvas.math;

/**
 * a Implementation of an 2D-MPoint with X and Y as Doubles
 * 
 * @author Mikescher
 */
public class MDPoint {
	/**
	 * Attribute der Klasse
	 */

	private double x;
	private double y;

	/**
	 * MPoint: Methoden
	 */

	/**
	 * MPoint Constructor
	 * 
	 * erzeugt ein neues Objekt MPoint an der Position (-1|-1)
	 */
	public MDPoint() {
		x = -1;
		y = -1;
	}

	/**
	 * MPoint Constructor - erzeugt ein neues Objekt MPoint auf Basis eines X und eines Y-Wertes
	 * 
	 * @param iX
	 *            Die Position auf der X-Achse
	 * @param iY
	 *            Die Position auf der Y-Achse
	 */
	public MDPoint(double iX, double iY) {
		x = iX;
		y = iY;
	}

	/**
	 * MPoint Constructor - erzeugt ein neues Objekt MPoint auf Basis eines anderen MPoint
	 * 
	 * @param iPos
	 *            Die Position die in MPoint gespeichert werden soll
	 */
	public MDPoint(MDPoint iPos) {
		x = iPos.getX();
		y = iPos.getY();
	}

	public MDPoint(MPoint iPos) {
		x = iPos.getX();
		y = iPos.getY();
	}

	public MDPoint(java.awt.Point p) {
		if (p != null) {
			x = p.x;
			y = p.y;
		} else {
			x = -1;
			y = -1;
		}
	}

	/**
	 * Die Fabrikmethode der Klasse MPoint - es wird ohne große Umwege ein neues Objekt vom Typ MPoint erzeugt (auf Basis eines anderen MPoint-Objekts)
	 */
	static MDPoint Pos(MDPoint iPos) {
		return new MDPoint(iPos.getX(), iPos.getY());
	}

	/**
	 * Die Fabrikmethode der Klasse MPoint - es wird ohne große Umwege ein neues Objekt vom Typ MPoint erzeugt (auf Basis eines X und eines Y-Wertes)
	 */
	static MDPoint Pos(double iX, double iY) {
		return new MDPoint(iX, iY);
	}

	/**
	 * gibt den aktuellen X-Wert zurück
	 */
	public double getX() {
		return x;
	}

	/**
	 * gibt den aktuellen X-Wert zurück
	 */
	public double getY() {
		return y;
	}

	/**
	 * setzt die Position auf der X-Achse
	 */
	public void setX(double aX) {
		x = aX;
	}

	/**
	 * setzt die Position auf der Y-Achse
	 */
	public void setY(double aY) {
		y = aY;
	}

	/**
	 * setzt die Position des MPoint (X und Y)
	 */
	public void set(double aX, double aY) {
		setX(aX);
		setY(aY);
	}

	/**
	 * setzt die Position des MPoint
	 */
	public void set(MDPoint pos) {
		set(pos.getX(), pos.getY());
	}

	/**
	 * setzt die Position des MPoint
	 */
	public void set(MPoint pos) {
		set(pos.getX(), pos.getY());
	}

	@Override
	public MDPoint clone() {
		return MDPoint.Pos(this);
	}

	/**
	 * Bewegt den Punkt auf der X-Achse
	 * 
	 * @param addX
	 *            Die Distanz auf der X-Achse
	 */
	public void moveX(double addX) {
		x += addX;
	}

	/**
	 * Bewegt den Punkt auf der Y-Achse
	 * 
	 * @param addY
	 *            Die Distanz auf der Y-Achse
	 */
	public void moveY(double addY) {
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
	public void move(double addX, double addY) {
		moveX(addX);
		moveY(addY);
	}

	public void add(double n) {
		x += n;
		y += n;
	}

	public void add(MDPoint n) {
		x += n.getX();
		y += n.getY();
	}

	public void add(double nx, double ny) {
		x += nx;
		y += ny;
	}

	public void addX(double nX) {
		moveX(nX);
	}

	public void addY(double nY) {
		moveY(nY);
	}

	public void sub(double n) {
		x -= n;
		y -= n;
	}

	public void sub(MDPoint n) {
		x -= n.getX();
		y -= n.getY();
	}

	public void sub(double nx, double ny) {
		x -= nx;
		y -= ny;
	}

	public void subX(double nx) {
		x -= nx;
	}

	public void subY(double ny) {
		y -= ny;
	}

	public void div(double n) {
		x /= n;
		y /= n;
	}

	public void div(MDPoint n) {
		x /= n.getX();
		y /= n.getY();
	}

	public void div(double nx, double ny) {
		x /= nx;
		y /= ny;
	}

	public void mult(double n) {
		x *= n;
		y *= n;
	}

	public void mult(MDPoint n) {
		x *= n.getX();
		y *= n.getY();
	}

	public void mult(double nx, double ny) {
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

		move(dX, dY);
	}

	/**
	 * Vergleicht zwei Punkte auf übereinstimmende Position
	 * 
	 * @param iPoint
	 *            Der Punkt mit dem verglichen werden soll
	 * @return gibt True zurück wenn die beiden Punkte identisch sind (gleiche Position)
	 */
	public boolean compare(MDPoint iPoint) {
		return getX() == iPoint.getX() && getY() == iPoint.getY();
	}

	/**
	 * Vergleicht zwei Punkte auf nahezu übereinstimmende Position (kann maximal um den Wert range abweichen)
	 * 
	 * @param iPoint
	 *            Der Punkt mit dem verglichen werden soll
	 * @param range
	 *            Der maximale Unterschied in X-Richtung als auch in Y-Richtung
	 * @return gibt True zurück wenn die beiden Punkte identisch sind (gleiche Position)
	 */
	public boolean nearlyCompare(MDPoint iPoint, int range) {
		double dX = Math.abs(getX() - iPoint.getX());
		double dY = Math.abs(getY() - iPoint.getY());
		return (dX <= range) && (dY <= range);
	}

	/**
	 * überprüft ein beliebiges Objekt, ob es ein Objekt von MPoint ist und mit diesem MPoint übereinstimmt
	 * 
	 * @param equalPoint
	 *            Das zu vergleichende Objekt
	 * @return gibt True zurück wenn das Objekt eine Instanz von MPoint ist und die beiden Punkte identisch sind (gleiche Position)
	 */
	@Override
	public boolean equals(Object equalPoint) {
		if ((equalPoint == null) || !(equalPoint instanceof MDPoint)) {
			return false;
		} else {
			return compare((MDPoint) equalPoint);
		}
	}

	/**
	 * überprüft ein beliebiges Objekt, ob es ein Objekt von MPoint ist und mit diesem MPoint übereinstimmt oder deren unterschiede nur sehr gering sind (kleiner als range)
	 * 
	 * @param equalPoint
	 *            Das zu vergleichende Objekt
	 * @param range
	 *            Der maximale Unterschied in X-Richtung als auch in Y-Richtung
	 * @return gibt True zurück wenn das Objekt eine Instanz von MPoint ist und die beiden Punkte identisch sind (gleiche Position)
	 */
	public boolean nearlyEquals(Object equalPoint, int range) {
		if ((equalPoint == null) || !(equalPoint instanceof MDPoint)) {
			return false;
		} else {
			return nearlyCompare((MDPoint) equalPoint, range);
		}
	}

	/**
	 * berechnet den Abstand zweier Punkte über den Satz des Pythagoras
	 * 
	 * @param otherP
	 *            Den 2. Punkt der Abstandberechnung
	 * @return Der Abstand der 2 Punkte
	 */
	public double getDistance(MDPoint otherP) {
		double dx = otherP.getX() - this.getX();
		double dy = otherP.getY() - this.getY();

		return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

	}

	/**
	 * berechnet den Winkel zweier Punkte relativ zur X-Achse im Gradmaß
	 * 
	 * @param otherP
	 *            Den 2. Punkt der Winkelberechnung
	 * @return Der Winkel der 2 Punkte
	 */
	public double getAngle(MDPoint otherP) {
		double dx = otherP.getX() - this.getX();
		double dy = otherP.getY() - this.getY();
		double adx = Math.abs(dx);
		double ady = Math.abs(dy);

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

		double rwinkel = Math.atan(ady / adx);
		double dWinkel = 0;

		if (dx > 0 && dy > 0) // 1. Quartal Winkkel von 270° - 359°
			dWinkel = Math.toDegrees(rwinkel);
		else if (dx < 0 && dy > 0) // 2. Quartal Winkkel von 180° - 269°
			dWinkel = 180 - Math.toDegrees(rwinkel);
		else if (dx > 0 && dy < 0) // 3. Quartal Winkkel von 90° - 179°
			dWinkel = 360 - Math.toDegrees(rwinkel);
		else if (dx < 0 && dy < 0) // 4. Quartal Winkkel von 0° - 89°
			dWinkel = 180 + Math.toDegrees(rwinkel);

		if (dWinkel == 360) dWinkel = 0;

		return dWinkel;
	}

	public double getAngle() {
		return getAngle(new MDPoint(0, 0));
	}

	public MDPoint rotate(double radian, double aroundX, double aroundY, boolean makeToMultipleOf8) {
		double sw = Math.sin(radian);
		double cw = Math.cos(radian);
		double Hx = x - aroundX;
		double Hy = y - aroundY;

		MDPoint result = new MDPoint();
		double nx = aroundX + (Hx * cw - Hy * sw);
		double ny = aroundY + (Hx * sw + Hy * cw);
		result.set(nx, ny);

		if (makeToMultipleOf8) {
			result.roundToMultipleOf(8);
		}

		return result;
	}

	public MDPoint rotateQuarter(int rotation, double aroundX, double aroundY, boolean makeToMultipleOf8) {
		MDPoint result = new MDPoint(aroundX, aroundY);
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

		double tx = x - aroundX;
		double ty = y - aroundY;

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
		return "(" + Double.toString(getX()) + "|" + Double.toString(getY()) + ")";
	}

	public boolean isInRect(MDRect rect) {
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
		return Math.sqrt(x * x + y * y);
	}

	public void setLength(double length) {
		if (!isZero()) {
			mult(length / getLength());
		}
	}

	public boolean isValid() {
		return x != -1 || y != -1;
	}
	
	public boolean isInvalid() {
		return !isValid();
	}

	public boolean isZero() {
		return x == 0.0 && y == 0.0;
	}

	public MPoint roundToMPoint() {
		return MPoint.Pos((int) Math.round(x), (int) Math.round(y));
	}

	public MPoint trunkToMPoint() {
		return MPoint.Pos((int) x, (int) y);
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
	public void mirrorAt(MDPoint axis) {
		MDPoint normale = axis.clone();
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
		sp.sub(this);
		
		set(sp);
	}
}
