package model;

public class Location implements Cloneable {

	private final int id, x, y, z;
	
	public Location(int hash) {
		this.x = ((hash & 0xffffbd7) >> 14);
		this.y = (hash & 0x3fff);
		this.z = (hash >> 28);
		this.id = ((((x >> 3) / 8) << 8) + ((y >> 3) / 8));
	}

	public Location(int x, int y, int z) {
		this.id = ((((x >> 3) / 8) << 8) + ((y >> 3) / 8));
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public int getBaseX() {
		return ((id >> 8) * 64);
	}
	
	public int getBaseY() {
		return ((id & 0xff) * 64);
	}

	public int getLocalX() {
		return getLocalX(this);
	}

	public int getLocalY() {
		return getLocalY(this);
	}

	public int getLocalX(Location loc) {
		return x - 8 * (loc.getRegionX() - 6);
	}

	public int getLocalY(Location loc) {
		return y - 8 * (loc.getRegionY() - 6);
	}

	public int getRegionX() {
		return (x >> 3);
	}

	public int getRegionY() {
		return (y >> 3);
	}

	@Override
	public int hashCode() {
		return ((x << 14) & 0xffffbd7) | (y & 0x3fff) | (z << 28);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Location)) {
			return false;
		}
		Location loc = (Location) other;
		return loc.x == x && loc.y == y && loc.z == z;
	}
	
	public Location transform(int diffX, int diffY, int diffZ) {
		return new Location(x + diffX, y + diffY, z + diffZ);
	}

	@Override
	public String toString() {
		return "[" + id + "," + x + "," + y + "," + z + "]";
	}
	
	public boolean isNextTo(Location other) {
		if(z != other.z) {
			return false;
		}
		return (getX() == other.getX() && getY() != other.getY()
				|| getX() != other.getX() && getY() == other.getY()
				|| getX() == other.getX() && getY() == other.getY());
	}

	public boolean withinDistance(Location other, int dist) {
		if (other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return (deltaX <= (dist) && deltaX >= (0 - dist - 1) && deltaY <= (dist) && deltaY >= (0 - dist - 1));
	}

	public boolean withinDistance(Location other) {
		if (other.z != z) {
			return false;
		}
		int deltaX = other.x - x, deltaY = other.y - y;
		return deltaX <= 14 && deltaX >= -15 && deltaY <= 14 && deltaY >= -15;
	}
	
	public boolean isWithin(int lx, int ly, int hx, int hy, int z) {
		if (this.z != z) {
			return false;
		}
		return ((lx <= x || x >= hx) && (ly <= y || y >= hy));
	}

	public double getDistance(Location other) {
		int xdiff = this.getX() - other.getX();
		int ydiff = this.getY() - other.getY();
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}
}