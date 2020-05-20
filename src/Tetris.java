import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;

class Tetris {

	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Tetrominos!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Board board = new Board(10, 24);

		frame.getContentPane().add(board);

		//Display the window.
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);

		Timer timer = new Timer(750, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				board.nextTurn();
			}

		});

		frame.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				// System.out.println("key pressed " + e.getKeyCode());
				if(key == KeyEvent.VK_UP) {
					board.rotateLeft();
				} else if (key == KeyEvent.VK_DOWN) {
					board.rotateRight();
				} else if (key == KeyEvent.VK_LEFT) {
					board.slide(-1);
				}  else if (key == KeyEvent.VK_RIGHT) {
					board.slide(1);
				} else if (key == KeyEvent.VK_R) {
					board.restartGame();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});

		timer.start();
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});

	}

}
