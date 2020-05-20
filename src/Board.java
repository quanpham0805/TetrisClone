import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JComponent;

class Board extends JComponent {

	private static final int SCALE = 30; // number of pixels per square
	private int cols, rows, shape, curCol = -4, curRow = -4, score = 0, bestScore = 0, rightPanelWidth;
	private boolean canMove = false, gameOver = false;
	private Piece tetrisPiece = new Piece();
	private Random rand = new Random();
	private ArrayList< ArrayList<Integer> > occupied = new ArrayList< ArrayList<Integer> >();
	private String gameState = "Tetris!";


	public Board(int cols, int rows)
	{
		super();
		this.cols = cols;
		this.rows = rows;
		rightPanelWidth = (int)(cols * 9 / 10);
		setPreferredSize(new Dimension(cols * SCALE + rightPanelWidth * SCALE, rows * SCALE));
		for (int i = 0 ; i < rows ; i ++)
		{
			occupied.add(new ArrayList<Integer>());
			for (int j = 0 ; j < cols ; j ++)
				occupied.get(i).add(-1);
		}
	}

	public void drawBoard(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0 ; i <= cols ; i ++)
		{
				g.setColor(Color.gray);
				g.fillRect(i * SCALE, 0, 1, getHeight());
		}

		for (int i = 0 ; i < rows ; i ++)
		{
			g.setColor(Color.gray);
			g.fillRect(0, i * SCALE, cols * SCALE, 1);
		}
	}

	public void paintOccupiedBlock(Graphics g)
	{
		for (int i = 0 ; i < rows ; i ++)
			for (int j = 0 ; j < cols ; j ++)
				if (occupied.get(i).get(j) != -1)
				{
					Block bl = new Block(occupied.get(i).get(j));
					bl.draw(g, SCALE, j, i);
				}
	}

	public void paintCurrentPiece(Graphics g)
	{
		for (int i = 0 ; i < 4 ; i ++)
		{
			Block bl = new Block(shape);
			int X = tetrisPiece.toPrint[i].x + curCol;
			int Y = tetrisPiece.toPrint[i].y + curRow;
			// System.out.format("%d %d\n", X, Y);
			bl.draw(g, SCALE, X, Y);
		}
	}

	public void drawCenterString(Graphics g, String text, Font font, int lineNo, Color c)
	{
		FontMetrics metrics = g.getFontMetrics(font);
		int x = cols * SCALE + (rightPanelWidth * SCALE - metrics.stringWidth(text)) / 2;
		int y = SCALE * lineNo + metrics.getHeight();
		g.setColor(c);
		g.setFont(font);
		g.drawString(text, x, y);
	}

	public void paintRightPanel(Graphics g)
	{
		int lineNo = rows / 2;
		Font font = new Font("Roboto", Font.PLAIN, SCALE);
		drawCenterString(g, gameState, font, 1, Color.red);
		drawCenterString(g, "Press R to restart", font, rows - 3, Color.magenta);
		drawCenterString(g, "Score", font, lineNo - 3, Color.white);
		drawCenterString(g, Integer.toString(score), font, lineNo - 2, Color.white);
		drawCenterString(g, "Record", font, lineNo, Color.yellow);
		drawCenterString(g, Integer.toString(bestScore), font, lineNo + 1, Color.yellow);
	}

	public void paintComponent(Graphics g) {
		drawBoard(g);
		paintOccupiedBlock(g);
		paintCurrentPiece(g);
		paintRightPanel(g);
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

	public void setPermaBlock()
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

	public void restartGame()
	{
		clearBoard();
		score = 0;
		canMove = false;
		gameOver = false;
		gameState = "Tetris!";
	}


	public int simplePower(int x, int n)
	{
		int result = 1;
		for (int i = 0 ; i < n ; i ++)
			result *= x;
		return result;
	}

	public void calculateScore(int rowsCleared)
	{
		if (rowsCleared <= 0) return;
		if (rowsCleared > 3) rowsCleared = 3;
		score += 40 + 60 * simplePower(5, rowsCleared);
		bestScore = Math.max(bestScore, score);
	}

	public void clearRow()
	{
		boolean chk = true; int rowsCleared = 0;
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
			rowsCleared ++;
			for (int j = 0 ; j < cols ; j ++)
				for (int k = i ; k >= 1 ; k --)
					occupied.get(k).set(j, occupied.get(k - 1).get(j)); // Integer is immutable.

			for (int j = 0 ; j < cols ; j ++)
				occupied.get(0).set(j, -1);
		}
		calculateScore(rowsCleared);
	}

	public void genNextPiece()
	{
		int upperBound = 7;
		shape = rand.nextInt(upperBound);
		curCol = cols / 2 - 3;
		curRow = -2;
		tetrisPiece.setShape(shape);
	}

	public void nextTurn()
	{
		if (gameOver)
		{
			gameState = "GAME OVER";
			repaint();
			return;
		}

		clearRow();
		if (!canMove)
		{
			canMove = true;
			genNextPiece();
		}
		curRow ++;
		if (checkCollision())
		{
			curRow --;
			canMove = false;
			// check game over here
			if (curRow < 0)
			{
				gameOver = true;
				repaint();
				return;
			}
			setPermaBlock();
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
