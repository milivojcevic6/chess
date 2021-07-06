package chess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChessGame {

	public static void main(String[] args) {

		new Board(); // TODO dodaj pojedene sa strane, add figure, sah, rokada, tajmer, mat, pat
	}
}

class Board {

	JFrame frame;
	JPanel panel;
	JButton[][] cell;
	figura[][] figure;
	JLabel label, scoreTable;
	int score;
	boolean onMove; // true-white; false-black

	boolean previous; // is there previous position (checked figure)
	int x, y; // positions of previous position
	boolean EnPassant;
	int XeP, YeP; // position of moved pawn

	class figura {
		int figura = 0; // empty 0, pawn 1, bishop 2, knight 3, rook 4, queen 5, king 6
		boolean boja = true; // white true, black false
		int positionX;
		int positionY;
		JButton[][] cell;

		public figura(int figura, boolean boja, int x, int y, JButton[][] board) {
			this.figura = figura;
			this.boja = boja;
			this.positionX = x;
			this.positionY = y;
			this.cell = board;
			if (figura == 0) {		//icons from the https://icons8.com/
			} else if (boja)
				cell[x][y].setIcon(new ImageIcon("w" + figura + ".png"));
			else
				cell[x][y].setIcon(new ImageIcon("b" + figura + ".png"));
		}

		public void erase(int m) {
			figura = 0;
			cell[positionX][positionY].setIcon(null);
			if(m==2 || m==4)m+=1;
			else if(m==5)m+=4;
			if (boja)
				score += m;
			else
				score -= m;
			m=0;
			if (score == 0)
				scoreTable.setVisible(false);
			else if (score > 0) {
				scoreTable.setVisible(true);
				scoreTable.setText("score: +" + score);
				scoreTable.setLocation(70, 600);
			} else {
				scoreTable.setVisible(true);
				scoreTable.setText("score: +" + (-score));
				scoreTable.setLocation(70, 80);
			}
		}

	}

	public Board() {
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(null);
		cell = new JButton[8][8];
		figure = new figura[8][8];
		score = 0;
		onMove = true;
		previous = false;
		EnPassant = false;

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				cell[i][j] = new JButton();
				cell[i][j].setLocation(j * 50 + 70, i * 50 + 150);
				cell[i][j].setSize(50, 50);
				if ((i + j) % 2 == 0)
					cell[i][j].setBackground(new Color(153, 153, 0));
				else
					cell[i][j].setBackground(Color.WHITE);
				int p = i, q = j;
				cell[i][j].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (figure[p][q].figura != 0 && !previous && figure[p][q].boja == onMove) {
							checkPiece(p, q);
						} else if (previous) {
							if ((x + y) % 2 == 0)
								cell[x][y].setBackground(new Color(153, 153, 0));
							else
								cell[x][y].setBackground(Color.WHITE);
							previous = false;
							if (x != p || y != q)
								movePiece(p, q);
						}
					}
				});

				panel.add(cell[i][j]);
			}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if ((i == 0 || i == 9 || j == 0 || j == 9) && (i + j) % 9 != 0) {
					label = new JLabel("chess", SwingConstants.CENTER);
					if (j == 0 || j == 9)
						label.setText("" + (9 - i));
					else
						label.setText("" + (char) (64 + j));
					label.setLocation(20 + 50 * j, 100 + i * 50);
					label.setSize(50, 50);
					label.setForeground(Color.gray);
					label.setFont(new Font("Serif", Font.BOLD, 40));
					panel.add(label);
				}
			}
		}

		scoreTable = new JLabel();
		scoreTable.setSize(100, 15);
		scoreTable.setForeground(Color.black);
		scoreTable.setFont(new Font("Serif", Font.BOLD, 20));
		panel.add(scoreTable);

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (i == 1)
					figure[i][j] = new figura(1, false, i, j, cell);
				else if(i==0 && (j == 0 || j == 7))
					figure[i][j] = new figura(4, false, i, j, cell);
				else if (i == 0 && (j == 1 || j == 6))
					figure[i][j] = new figura(3, false, i, j, cell);
				else if (i == 0 && (j == 2 || j == 5))
					figure[i][j] = new figura(2, false, i, j, cell);
				else if (i == 0 && j == 4)
					figure[i][j] = new figura(5, false, i, j, cell);
				else if (i == 0 && j == 4)
					figure[i][j] = new figura(5, false, i, j, cell);
				else if (i == 0 && j == 3) 
					figure[i][j] = new figura(6, false, i, j, cell);
				else if (i == 6)
					figure[i][j] = new figura(1, true, i, j, cell);
				else if (i == 7 && (j == 0 || j == 7))
					figure[i][j] = new figura(4, true, i, j, cell);
				else if (i == 7 && (j == 1 || j == 6))
					figure[i][j] = new figura(3, true, i, j, cell);
				else if (i == 7 && (j == 2 || j == 5))
					figure[i][j] = new figura(2, true, i, j, cell);
				else if (i == 7 && j == 4)
					figure[i][j] = new figura(5, true, i, j, cell);
				else if (i == 7 && j == 3) {
					figure[i][j] = new figura(6, true, i, j, cell);
				} else
					figure[i][j] = new figura(0, true, i, j, cell);

		panel.setBackground(new Color(255, 255, 204));
		frame.add(panel);

		ImageIcon icon = new ImageIcon("chess.png");
		frame.setIconImage(icon.getImage());
		frame.setLocation(400, 100);
		frame.setSize(700, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.setTitle("CHESS");
		frame.setVisible(true);

	}

	private boolean moveKnight(int p, int q) {

		if (((x + 2 == p || x - 2 == p) && (y + 1 == q || y - 1 == q)
				|| ((x + 1 == p || x - 1 == p) && (y + 2 == q || y - 2 == q))))
			return true;
		return false;
	}

	private boolean moveRook(int p, int q) {
		int xx = x, yy = y;
		if (!(xx == p && yy == q)) {
			if (xx == p) {
				while (yy != q) {
					if (yy > q)
						yy--;
					else
						yy++;
					if (yy == q)
						return true;
					else if (figure[xx][yy].figura != 0 || yy < 0 || yy > 7)
						return false;
				}
			}
			if (yy == q) {
				while (xx != p) {
					if (xx > p)
						xx--;
					else
						xx++;
					if (xx == p)
						return true;
					else if (figure[xx][yy].figura != 0 || xx < 0 || xx > 7)
						return false;
				}
			}
		}
		return false;
	}

	private boolean moveBishop(int p, int q) {

		int xx = x, yy = y;
		if (Math.abs(xx - p) == Math.abs(yy - q)) {
			if (xx > p)
				xx--;
			else
				xx++;
			if (yy > q)
				yy--;
			else
				yy++;
			while (xx != p && yy != q) {
				if (figure[xx][yy].figura != 0)
					return false;
				if (xx < p)
					xx++;
				else
					xx--;
				if (yy < q)
					yy++;
				else
					yy--;
			}
			return true;
		}
		return false;
	}

	private boolean moveKing(int p, int q) {
		return p <= x + 1 && p >= x - 1 && q <= y + 1 && q >= y - 1 ? true : false;
	}

	protected void movePiece(int p, int q) {

		if (figure[p][q].figura == 0 || figure[x][y].boja != figure[p][q].boja) {

			switch (figure[x][y].figura) {
			case 1: {
				if (figure[p][q].figura == 0) {
					if ((p == 7 && x == 6) || (p == 0 && x == 1)) { // improve pawn to queen
						EnPassant = false;
						figure[p][q] = new figura(5, figure[x][y].boja, p, q, cell);
						figure[x][y].erase(-8);
						promena();
						break;
					}
					if (EnPassant && ((p == 2 && XeP == 3) || (p == 5 && XeP == 4)) && (y == YeP + 1 || y == YeP - 1)
							&& q == YeP) {
						EnPassant = false;
						figure[XeP][YeP].erase(-1);
						changePositions(p, q);
					} else {
						EnPassant = false;
						if (figure[x][y].boja && ((x - p == 1) || (x == 6 && p == 4 && figure[5][q].figura == 0))
								&& y == q) {
							if (x == 6 && p == 4) {
								EnPassant = true;
								XeP = p;
								YeP = q;
							} else
								EnPassant = false;
							changePositions(p, q);

						}
						if (!figure[x][y].boja && ((p - x == 1) || (x == 1 && p == 3 && figure[2][q].figura == 0))
								&& y == q) {
							if (x == 1 && p == 3) {
								EnPassant = true;
								XeP = p;
								YeP = q;
							} else
								EnPassant = false;
							changePositions(p, q);
						}
					}
				} else {
					EnPassant = false;
					if (figure[x][y].boja && x == p + 1 && (y == q + 1 || y == q - 1))
						changePositions(p, q);
					else if (!figure[x][y].boja && x == p - 1 && (y == q + 1 || y == q - 1))
						changePositions(p, q);
				}
				break;
			}
			case 2: {
				if (moveBishop(p, q)) {
					EnPassant = false;
					changePositions(p, q);
				}
				break;
			}
			case 3: {
				if (moveKnight(p, q)) {
					EnPassant = false;
					changePositions(p, q);
				}
				break;
			}
			case 4: {
				if (moveRook(p, q)) {
					EnPassant = false;
					changePositions(p, q);
				}
				break;
			}
			case 5: {
				if (moveRook(p, q) || moveBishop(p, q)) {
					EnPassant = false;
					changePositions(p, q);
				}
				break;
			}
			case 6: {
				if (moveKing(p, q)) {
					changePositions(p, q);
				}
				break;
			}
			default:
				break;
			}
		}
	}

	private void promena() {
		onMove = !onMove;		
	}

	private void changePositions(int p, int q) {
		int tmp=figure[p][q].figura;
		figure[p][q] = new figura(figure[x][y].figura, figure[x][y].boja, p, q, cell);
		figure[x][y].erase(tmp);
		promena();
	}

	private void checkPiece(int p, int q) {
		cell[p][q].setBackground(Color.GREEN);
		x = p;
		y = q;
		previous = true;
	}

}