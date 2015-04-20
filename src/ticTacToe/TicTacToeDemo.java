package ticTacToe;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class TicTacToeDemo extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int ROWS = 3;
	public static final int COLUMNS = 3;
	
	public static final int CELL_SIZE = 100;
	public static final int CANVAS_WIDTH = CELL_SIZE * ROWS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * COLUMNS;
	public static final int GRID_WIDTH = 8;
	public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
	
	public static final int CELL_PADDING = CELL_SIZE / 6;
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	public enum GameState
	{
		DRAW, PLAYING, CROSS_WON, KNOT_WON
	}
	
	private GameState currentState;
	
	public enum Seed
	{
		EMPTY, CROSS, KNOT
	}
	
	private Seed currentPlayer;
	private Seed[][] board;
	private DrawCanvas canvas;
	private JLabel statusBar;
	
	public TicTacToeDemo() 
	{
		canvas = new DrawCanvas();
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		
		canvas.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e)
			{
				int mouseX = e.getX();
				int mouseY = e.getY();
				
				int rowSelected = mouseY / CELL_SIZE;
				int columnSelected = mouseX / CELL_SIZE;
				
				if(currentState == GameState.PLAYING)
				{
					if(rowSelected >= 0 && rowSelected < ROWS 
							&& columnSelected >= 0 && columnSelected < COLUMNS
							&& board[rowSelected][columnSelected] == Seed.EMPTY)
					{
						board[rowSelected][columnSelected] = currentPlayer;
						updateGame(currentPlayer, rowSelected, columnSelected);
						currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.KNOT : Seed.CROSS;
					}
				}
				else
				{
					initGame();
				}
				
				repaint();
			}
		});
		
		statusBar = new JLabel(" ");
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
		
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(statusBar, BorderLayout.PAGE_END);
		
		setTitle("Tic Tac Toe");
		pack();
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		board = new Seed[ROWS][COLUMNS];
		initGame();
	}
	
	public void initGame()
	{
		for(int row = 0; row < ROWS; ++row)
		{
			for(int column = 0; column < COLUMNS; ++column)
			{
				board[row][column] = Seed.EMPTY;
			}
		}
		
		currentState = GameState.PLAYING;
		currentPlayer = Seed.CROSS;
	}
	
	public void updateGame(Seed theSeed, int rowSelected, int columnSelected)
	{
		if(hasWon(theSeed, rowSelected, columnSelected))
		{
			currentState = (theSeed == Seed.CROSS) ? GameState.CROSS_WON : GameState.KNOT_WON;
		}
		else if(isDraw())
		{
			currentState = GameState.DRAW;
		}
	}

	public boolean isDraw()
	{
		for(int row = 0; row < ROWS; ++row)
		{
			for(int column = 0; column < COLUMNS; ++column)
			{
				if(board[row][column] == Seed.EMPTY)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean hasWon(Seed theSeed, int rowSelected, int columnSelected)
	{
		return(board[rowSelected][0] == theSeed
				&& board[rowSelected][1] == theSeed
				&& board[rowSelected][2] == theSeed
				|| board[0][columnSelected] == theSeed
				&& board[1][columnSelected] == theSeed
				&& board[2][columnSelected] == theSeed
				|| rowSelected == columnSelected
				&& board[0][0] == theSeed
				&& board[1][1] == theSeed
				&& board[2][2] == theSeed
				|| rowSelected + columnSelected == 2
				&& board[0][2] == theSeed
				&& board[1][1] == theSeed
				&& board[2][0] == theSeed);
	}
	
	public class DrawCanvas extends JPanel
	{
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			setBackground(Color.white);
			
			g.setColor(Color.lightGray);
			for(int row = 1; row < ROWS; ++row)
			{
				g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDTH_HALF, CANVAS_WIDTH - 1, 
						GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
			}
			for(int column = 1; column < COLUMNS; ++column)
			{
				g.fillRoundRect(CELL_SIZE * column - GRID_WIDTH_HALF, 0, 
						GRID_WIDTH, CANVAS_HEIGHT -1, GRID_WIDTH, GRID_WIDTH);
			}
			
			Graphics2D g2d = (Graphics2D)g;
			g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND, 
					BasicStroke.JOIN_ROUND));
			
			for(int row = 0; row < ROWS; ++row)
			{
				for(int column = 0; column < COLUMNS; ++column)
				{
					int x1 = column * CELL_SIZE + CELL_PADDING;
					int y1 = row *CELL_SIZE + CELL_PADDING;
					
					if(board[row][column] == Seed.CROSS)
					{
						g2d.setColor(Color.red);
						int x2 = (column + 1) * CELL_SIZE - CELL_PADDING;
						int y2 = (row + 1) * CELL_SIZE - CELL_PADDING;
						g2d.drawLine(x1, y1, x2, y2);
						g2d.drawLine(x2, y1, x1, y2);
					}
					else if(board[row][column] == Seed.KNOT)
					{
						g2d.setColor(Color.blue);
						g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
					}
				}
			}
			
			if(currentState == GameState.PLAYING)
			{
				statusBar.setForeground(Color.black);
				if(currentPlayer == Seed.CROSS)
				{
					statusBar.setText("X's turn!");
				}
				else if(currentPlayer == Seed.KNOT)
				{
					statusBar.setText("O's turn1");
				}
			}
			else if(currentState == GameState.DRAW)
			{
				statusBar.setForeground(Color.red);
				statusBar.setText("It's a draw!!");
			}
			else if(currentState == GameState.CROSS_WON)
			{
				statusBar.setForeground(Color.red);
				statusBar.setText("X won!!");
			}
			else if(currentState == GameState.KNOT_WON)
			{
				statusBar.setForeground(Color.red);
				statusBar.setText("O won!!");
			}
		}
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new TicTacToeDemo();
				
			}
		});
	}
}
