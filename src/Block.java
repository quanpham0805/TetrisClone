import java.awt.Color;
import java.awt.Graphics;

class Block {

	public int colorIndex;

	public static Color[] colors = {Color.cyan, Color.blue, Color.orange,
			Color.yellow, Color.red, Color.green, Color.magenta};

	public Block(int colorIndex) {
		this.colorIndex = colorIndex;
	}

	public void draw(Graphics g, int scale, int x, int y) {
		g.setColor(colors[colorIndex]);
		g.fillRect(x * scale + 1, y * scale + 1, scale - 2, scale - 2);
	}

}
