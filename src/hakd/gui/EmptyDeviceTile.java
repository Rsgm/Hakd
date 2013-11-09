package hakd.gui;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class EmptyDeviceTile {
	int isoX;
	int isoY;
	private Sprite tile;

	public EmptyDeviceTile(int x, int y) {
		isoX = x;
		isoY = y;

	}

	public int getIsoX() {
		return isoX;
	}

	public void setIsoX(int isoX) {
		this.isoX = isoX;
	}

	public int getIsoY() {
		return isoY;
	}

	public void setIsoY(int isoY) {
		this.isoY = isoY;
	}

	public Sprite getTile() {
		return tile;
	}

	public void setTile(Sprite tile) {
		this.tile = tile;
	}
}
