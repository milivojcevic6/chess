package chess;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChessGame {

	public static void main(String[] args) {

		new Board();	//TODO add figure, top, el pasan, sah, tajmer, pijun u kraljicu, mat, pat
	}
}

class Board {

	JFrame frame;
	JPanel panel;
	JButton[][] cell;
	figura[][] figure;
	JLabel label;
	boolean previous;// is there previous position (checked figure)
	int x, y; // positions of previous position

	class figura {
		int figura = 0; // prazno 0, pesak 1, lovac 2, konj 3, top 4, kraljica 5, kralj 6
		boolean boja = true; // beli true, crni false
		int positionX;
		int positionY;
		JButton[][] cell;

		public figura(int figura, boolean boja, int x, int y, JButton[][] board) {
			this.figura = figura;
			this.boja = boja;
			this.positionX = x;
			this.positionY = y;
			this.cell = board;
			if (figura == 0) {
			} else if (boja)
				cell[x][y].setIcon(new ImageIcon("w" + figura + ".png"));
			else
				cell[x][y].setIcon(new ImageIcon("2.png"));
		}

		public void erase() {
			figura = 0;
			cell[positionX][positionY].setIcon(null);
		}

	}

	public Board() {
		frame = new JFrame();
		panel = new JPanel();
		panel.setLayout(null);
		cell = new JButton[8][8];
		figure = new figura[8][8];
		previous = false;

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				cell[i][j] = new JButton();
				cell[i][j].setLocation(j * 40 + 100, i * 40 + 200);
				cell[i][j].setSize(40, 40);
				if ((i + j) % 2 == 0)
					cell[i][j].setBackground(Color.DARK_GRAY);
				else
					cell[i][j].setBackground(Color.WHITE);
				int p = i, q = j;
				cell[i][j].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (figure[p][q].figura != 0 && !previous) {
							checkPiece(p, q);
						} else if (figure[p][q].figura == 0 && previous && (x != p || y != q)) {
							if ((x + y) % 2 == 0)
								cell[x][y].setBackground(Color.DARK_GRAY);
							else
								cell[x][y].setBackground(Color.WHITE);
							movePiece(p, q);
						}
						else if (figure[p][q].figura != 0 && x != p && y != q) {
							if ((x + y) % 2 == 0)
								cell[x][y].setBackground(Color.DARK_GRAY);
							else
								cell[x][y].setBackground(Color.WHITE);
							eatPiece(p, q);
						}
					}
				});

				panel.add(cell[i][j]);
			}

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if ((i == 0 || i == 9 || j == 0 || j == 9) && (i + j) % 9 != 0) {
					label = new JLabel("SUDOKU", SwingConstants.CENTER);
					if (j == 0 || j == 9)
						label.setText("" + (9 - i));
					else
						label.setText("" + (char) (64 + j));
					label.setLocation(60 + 40 * j, 160 + i * 40);
					label.setSize(40, 40);
					label.setForeground(Color.gray);
					label.setFont(new Font("Serif", Font.BOLD, 40));
					panel.add(label);
				}
			}
		}

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (i == 1)
					figure[i][j] = new figura(1, false, i, j, cell);
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
				else if (i == 7 && j == 3)
					figure[i][j] = new figura(6, true, i, j, cell);
				else
					figure[i][j] = new figura(0, true, i, j, cell);

		panel.setBackground(Color.pink);
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

	private boolean moveRook(int p, int q) {//TODO ravnonkurcu
		int xx = x, yy = y;
		if (!(xx == p && yy == q)) {
			if (xx == p) {
				while(yy!=q) {
					if(yy>q)yy--;
					else yy++;
					if(yy==q) return true;
					else if (figure[xx][yy].figura !=0 || yy<0 || yy>7)
						return false;
				}
			}if (yy==q) {
				while(xx!=p) {
					if(xx>p)xx--;
					else xx++;
					if(xx==p) return true;
					else if (figure[xx][yy].figura !=0 || xx<0 || xx> 7)
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
		previous = false;

		switch (figure[x][y].figura) {
		case 1: {
			if (figure[x][y].boja && ((x - p == 1) || (x == 6 && p == 4 && figure[5][q].figura == 0)) && y == q)
				changePositions(p, q);
			else if (!figure[x][y].boja && ((p - x == 1) || (x == 1 && p == 3 && figure[2][q].figura == 0)) && y == q)
				changePositions(p, q);
			break;
		}
		case 2: {
			if (moveBishop(p, q))
				changePositions(p, q);
			break;
		}
		case 3: {
			if (moveKnight(p, q)) {
				changePositions(p, q);
			}
			break;
		}
		case 4: {
			if (moveRook(p, q)) {
				changePositions(p, q);
			}
			break;
		}
		case 5: {
			if (moveRook(p, q) || moveBishop(p, q)) {
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

	protected void eatPiece(int p, int q) {

		previous = false;
		if (figure[x][y].boja != figure[p][q].boja) {

			switch (figure[x][y].figura) {
			case 1: {
				if (figure[x][y].boja && x == p + 1 && (y == q + 1 || y == q - 1))
					changePositions(p, q);
				else if (!figure[x][y].boja && x == p - 1 && (y == q + 1 || y == q - 1))
					changePositions(p, q);
				break;
			}
			case 2: {
				if (moveBishop(p, q)) {
					changePositions(p, q);
				}
				break;
			}
			case 3: {
				if (moveKnight(p, q)) {
					changePositions(p, q);
				}
				break;
			}
			case 4: { 
				if (moveRook(p, q)) {
					changePositions(p, q);
				}
				break;
			}
			case 5: {
				if (moveRook(p, q) || moveBishop(p, q)) {
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
			default: {
			}
			}
		}

	}

	private void changePositions(int p, int q) {

		figure[p][q] = new figura(figure[x][y].figura, figure[x][y].boja, p, q, cell);
		figure[x][y].erase();
	}

	private void checkPiece(int p, int q) {
		cell[p][q].setBackground(Color.GREEN);
		x = p;
		y = q;
		previous = true;
	}

	private boolean isCheck() {
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
				if (kingAttack(i, j))
					return true;
		return false;
	}

	private boolean kingAttack(int i, int j) {
		// TODO Auto-generated method stub
		switch (figure[i][j].figura) {
		case 1: {
			if (figure[i][j].boja && (figure[i - 1][j - 1].figura == 6 || figure[i - 1][j + 1].figura == 6))
				return true;
			if (!figure[i][j].boja && (figure[i + 1][j - 1].figura == 6 || figure[i + 1][j + 1].figura == 6))
				return true;
		}
		case 2: {
			return checkBishop(i,j);
		}
		case 3: {
			return checkKnight(i,j);
		}
		case 4: {

		}
		case 5: {

		}
		case 6: {

		}
		default:return false;
		}

		
	}

	private boolean checkKnight(int p, int q) {
		for(int i=p-2;i<=p+2;i++)
			for(int j=q-2;j<=q+2;j++) {
				if(i>=0&&i<8&&j>=0&&j<8) {
					//TODO provera tih 8 tacaka i ako da onda return true;					
				}
			}
		return false;
	}

	private boolean checkBishop(int i, int j) {

		int ii = i + 1, jj = j + 1;
		while (ii >= 0 && ii < 8 && jj >= 0 && jj < 8 && figure[ii][jj].figura == 0) {
			ii++;
			jj++;
		}
		if (figure[ii][jj].figura == 6&& figure[ii][jj].boja!=figure[i][j].boja)
			return true;
		ii = i - 1;
		jj = j - 1;
		while (ii >= 0 && ii < 8 && jj >= 0 && jj < 8 && figure[ii][jj].figura == 0) {
			ii--;
			jj--;
		}
		if (figure[ii][jj].figura == 6&& figure[ii][jj].boja!=figure[i][j].boja)
			return true;
		ii = i + 1;
		jj = j - 1;
		while (ii >= 0 && ii < 8 && jj >= 0 && jj < 8 && figure[ii][jj].figura == 0) {
			ii++;
			jj--;
		}
		if (figure[ii][jj].figura == 6&& figure[ii][jj].boja!=figure[i][j].boja)
			return true;
		ii = i - 1;
		jj = j + 1;
		while (ii >= 0 && ii < 8 && jj >= 0 && jj < 8 && figure[ii][jj].figura == 0) {
			ii--;
			jj++;
		}
		if (figure[ii][jj].figura == 6&& figure[ii][jj].boja!=figure[i][j].boja)
			return true;
		return false;
	}

}