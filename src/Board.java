import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JComponent;

class Board extends JComponent {

	private static final int SCALE = 16; // number of pixels per square
	private int cols, rows, shape, curCol = -4, curRow = -4;
	private boolean canMove = false;
	private Piece tetrisPiece = new Piece();
	private Random rand = new Random();
	private ArrayList< ArrayList<Integer> > occupied = new ArrayList< ArrayList<Integer> >();


	public Board(int cols, int rows)
	{
		super();
		this.cols = cols;
		this.rows = rows;
		setPreferredSize(new Dimension(cols * SCALE, rows * SCALE));
		for (int i = 0 ; i < rows ; i ++)
		{
			occupied.add(new ArrayList());
			for (int j = 0 ; j < cols ; j ++)
				occupied.get(i).add(-1);
		}
	}

	public void paintComponent(Graphics g) {
		// clear the screen with black
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0 ; i < rows ; i ++)
			for (int j = 0 ; j < cols ; j ++)
				if (occupied.get(i).get(j) != -1)
				{
					Block bl = new Block(occupied.get(i).get(j));
					bl.draw(g, SCALE, j, i);
				}

		for (int i = 0 ; i < 4 ; i ++)
		{
			Block bl = new Block(shape);
			int X = tetrisPiece.toPrint[i].x + curCol;
			int Y = tetrisPiece.toPrint[i].y + curRow;
			// System.out.format("%d %d\n", X, Y);
			bl.draw(g, SCALE, X, Y);
		}
	}


	public boolean checkCollision()
	{
		for (int i = 0 ; i < 4 ; i ++)
		{
			int X = tetrisPiece.toPrint[i].x + curCol;
			int Y = tetrisPiece.toPrint[i].y + curRow;
			if (X >= cols || X < 0 || Y >= rows) return true;
			if (Y >= 0 && occupied.get(Y).get(X) != -1) return true;
		}
		return false;
	}

	public void paintBoard()
	{
		for (int i = 0 ; i < 4 ; i ++)
		{
			int X = tetrisPiece.toPrint[i].x + curCol;
			int Y = tetrisPiece.toPrint[i].y + curRow;
			occupied.get(Y).set(X, shape);
		}
	}

	public void clearBoard()
	{
		for (int i = 0 ; i < rows ; i ++)
		{
			for (int j = 0 ; j < cols ; j ++)
				occupied.get(i).set(j, -1);
		}
	}

	public void clearRow()
	{
		boolean chk = true;
		for (int i = 0 ; i < rows ; i ++)
		{
			chk = true;
			for (int j = 0 ; j < cols ; j ++)
				if (occupied.get(i).get(j) == -1)
				{
					chk = false;
					break;
				}
			if (!chk) continue;


			// to do: creating collapsing function here



			// for (int j = 0 ; j < cols ; j ++)
			// {
			// 	for (int k = i ; k >= 1 ; k --)
			// 	{
			// 		occupied.get(k).
			// 	}
			// }




			for (int j = 0 ; j < cols ; j ++)
				occupied.get(0).set(j, -1);
		}
	}

	//  Check for complete rows that can be destroyed.
	public void nextTurn()
	{
		clearRow();
		if (!canMove)
		{
			canMove = true;
			int upperBound = 7;
			shape = rand.nextInt(upperBound);
			shape = 0;
			this.curCol = this.cols / 2 - 3;
			this.curRow = -2;
			tetrisPiece.setShape(shape);
		}
		curRow ++;
		if (checkCollision())
		{
			curRow --;
			canMove = false;
			// check game over here huh
			if (curRow < 0)
			{
				clearBoard();
				canMove = false;
				repaint();
				return;
			}
			paintBoard();
		}
		repaint();
	}

	public void slide(int dx)
	{
		curCol += dx;
		if (checkCollision()) curCol -= dx;
		repaint();
	}

	public void rotateRight()
	{
		if (shape == 3)
		{
			repaint();
			return;
		}
		tetrisPiece.rotate(-1);
		if (checkCollision()) tetrisPiece.rotate(1);
		repaint();
	}

	public void rotateLeft()
	{
		if (shape == 3)
		{
			repaint();
			return;
		}
		tetrisPiece.rotate(1);
		if (checkCollision()) tetrisPiece.rotate(-1);
		repaint();
	}

}
