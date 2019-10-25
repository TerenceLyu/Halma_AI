import java.awt.*;
import java.io.*;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Botong Lyu
 * botongly@usc.edu
 * Halma_AI
 * 2019/10/6
 */
public class Run
{
	final static int SIZE = 16;
	static int DEPTH = 2;
	final static Point[] bBase={new Point(0,0), new Point(1,0), new Point(2,0),
			new Point(3,0), new Point(4,0), new Point(0,1), new Point(1,1),
			new Point(2,1), new Point(3,1), new Point(4,1), new Point(0,2),
			new Point(1,2), new Point(2,2), new Point(3,2), new Point(0,3),
			new Point(1,3), new Point(2,3), new Point(0,4), new Point(1,4)};//black bases
	final static Point[] wBase={new Point(15,15), new Point(14,15), new Point(13,15),
			new Point(12,15), new Point(11,15), new Point(15,14), new Point(14,14),
			new Point(13,14), new Point(12,14), new Point(11,14), new Point(15,13),
			new Point(14,13), new Point(13,13), new Point(12,13), new Point(15,12),
			new Point(14,12), new Point(13,12), new Point(15,11), new Point(14,11)};//white bases
	final static Set<Point> bSet = new HashSet<>(Arrays.asList(bBase));
	final static Set<Point> wSet = new HashSet<>(Arrays.asList(wBase));
	final static ArrayList<Point> bPieces = new ArrayList<>(19);//black pieces
	final static ArrayList<Point> wPieces = new ArrayList<>(19);//white pieces
	static long time = System.currentTimeMillis();
	public static void main(String[] args) throws Exception
	{
//		long time = System.currentTimeMillis();
		BufferedReader input = new BufferedReader(new FileReader("input.txt"));
		String mode = input.readLine();
//		black is 0 and white is 1
		int player = (input.readLine().equals("BLACK")) ? 0:1;
		double timeLeft = Double.parseDouble(input.readLine());
		if (mode.equals("SINGLE"))
		{
			DEPTH = (int) timeLeft/10 + 3;
		}else
		{
			DEPTH = (int) timeLeft/100 + 3;
		}
		System.out.println(DEPTH);
		char[][] charBoard = new char[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
		{
			charBoard[i] = input.readLine().toCharArray();
		}
		
		buildBoard(charBoard);
//		System.out.println(checkWin(player-1));
//		System.out.println(bPieces);
//		System.out.println(wPieces);
//		System.out.println(generateMove(new Point(1,0)).toString());
//		System.out.println(eval(player));
		ArrayList<Point> nextMove = treeSearch(player);
		System.out.println(nextMove);
		
		if (nextMove.size()==2&&Math.abs(nextMove.get(0).x - nextMove.get(1).x) + Math.abs(nextMove.get(0).y - nextMove.get(1).y) <=2)
		{
			output("E", nextMove);
		}else
		{
			output("J", nextMove);
		}
		System.out.println((System.currentTimeMillis()-time)/1000.0+"s");
		
	}
	private static ArrayList<Point> treeSearch(int player)
	{
		int max = -10000;
		ArrayList<Point> nextMove = new ArrayList<>();
		ArrayList<Point> pieces = (player==0) ? bPieces:wPieces;
//		System.out.println(pieces);
		for (Point p:pieces)
		{
//			System.out.println(max);
//			System.out.println((System.currentTimeMillis()-time)/1000.0+"s");
			ArrayList<ArrayList<Point>> moves = generateMove(p, player);
//			System.out.println(p);
//			System.out.println(moves);
//			System.out.println(pieces);
			for (ArrayList<Point> move:moves)
			{
//				System.out.println(move.toString());
				p.setLocation(move.get(move.size()-1));
				
				if (checkWin(player))
				{
					return move;
				}
				int value = minimax(player, DEPTH, max, 1000000, false);
				if (value>max)
				{
					max = value;
					nextMove = move;
//					System.out.println(max);
//					System.out.println(nextMove.toString());
				}
				p.setLocation(move.get(0));
			}
		}
		return nextMove;
	}
	private static int minimax(int player, int level, int alpha, int beta, boolean maxOrMin)
	{
		if (level == 0)
		{
//			System.out.println("leaf");
			return eval(player);
		}
		ArrayList<Point> pieces;
		ArrayList<ArrayList<Point>> moves;
		if (maxOrMin)
		{
			//at max level
			int value = -1000000;
			pieces = getPoints(player, bPieces, wPieces);
			for (Point p:pieces)
			{
				moves = generateMove(p, player);
				for (ArrayList<Point> move:moves)
				{
					p.setLocation(move.get(move.size()-1));
					if (checkWin(player))
					{
						p.setLocation(move.get(0));
						return 99999-level;
					}
					value = Math.max(value, minimax(player, level-1, alpha, beta, false));
					alpha = Math.max(alpha, value);
					p.setLocation(move.get(0));
					if (alpha>=beta)
					{
						return value;
					}
				}
			}
			return value;
		}else
		{
			//at min level
			int value = 1000000;
			pieces = getPoints(player, wPieces, bPieces);
			for (Point p:pieces)
			{
				moves = generateMove(p, player);
				for (ArrayList<Point> move:moves)
				{
					p.setLocation(move.get(move.size()-1));
					if (checkWin((player+1)%2))
					{
						p.setLocation(move.get(0));
						return -99999+level;
					}
					value = Math.min(value, minimax(player, level-1, alpha, beta, true));
					beta = Math.min(beta, value);
					p.setLocation(move.get(0));
					if (alpha>=beta)
					{
						return value;
					}
				}
			}
			return value;
		}
	}

	private static ArrayList<Point> getPoints(int player, ArrayList<Point> wPieces, ArrayList<Point> bPieces)
	{
		ArrayList<Point> pieces = (player==0) ? wPieces : bPieces;
		ArrayList<Point> temp = new ArrayList<>();
		Set<Point> base = (player==0) ? bSet:wSet;
		for (Point piece : pieces)
		{
			if (base.contains(piece))
			{
				temp.add(piece);
			}
		}
		if (temp.size()!=0)
		{
			pieces = temp;
		}
		return pieces;
	}
	private static int eval(int player)
	{
		Set<Point> myBase = (player==0) ? bSet:wSet;
		Set<Point> oppoBase = (player==0) ? wSet:bSet;
		int score = 0;
		int x = (player==0) ? 0:15;
		int y = (player==0) ? 15:0;
		ArrayList<Point> myPieces = (player==0) ? bPieces : wPieces;
		ArrayList<Point> oppoPieces = (player==0) ? wPieces : bPieces;
		int iWin = 0;
		int iLose = 0;
		for (Point p:myPieces)
		{
			score += Math.abs(x-p.x) + Math.abs(x-p.y);
			if (myBase.contains(p))
			{
				score -=10;
				iLose++;
			}
			if (oppoBase.contains(p))
			{
				score +=10;
				iWin++;
			}
		}
		for (Point p:oppoPieces)
		{
			score -= Math.abs(y-p.x) + Math.abs(y-p.y);
			if (myBase.contains(p))
			{
				score -=10;
				iLose++;
			}
			if (oppoBase.contains(p))
			{
				score +=10;
				iWin++;
			}
		}
		if (iWin == 19)
		{
			return 9999;
		}
		if (iLose == 19)
		{
			return -9999;
		}
		return score;
	}
	private static ArrayList<ArrayList<Point>> generateMove(Point piece, int player)
	{
		ArrayList<ArrayList<Point>> listsOfMoves = new ArrayList<>();
		for (int i = 0; i < 2; i++) 
		{
			for (int j = 0; j < 2; j++) 
			{
				if (i!=0||j!=0)
				{
					Point target;
					if (player == 0)
					{
						target = new Point(piece.x + i, piece.y + j);
					}else
					{
						target = new Point(piece.x - i, piece.y - j);
					}
					if (target.x<SIZE && target.x>=0 && target.y<SIZE && piece.y + j>=0)
					{
						if (!bPieces.contains(target)&&!wPieces.contains(target))
						{
							//regular moves
							ArrayList<Point> moves = new ArrayList<>();
							moves.add(piece.getLocation());
							moves.add(target);
							listsOfMoves.add(moves);
						}else
						{
							//check if a hop is possible

							Point hop;
							if (player==0)
							{
								hop = new Point(piece.x + 2*i, piece.y + 2*j);
							}else
							{
								hop = new Point(piece.x - 2*i, piece.y - 2*j);
							}
							if (hop.x<SIZE && hop.x>=0 && hop.y<SIZE && hop.y>=0)
							{

								if (!bPieces.contains(hop)&&!wPieces.contains(hop))
								{
									ArrayList<Point> moves = new ArrayList<>();
									moves.add(piece.getLocation());
									moves.add(hop);
									listsOfMoves.add(moves);
//									System.out.println(moves);
									continousHop(listsOfMoves, moves, player);
								}
							}
						}
					}
				}
			}
		}
		return listsOfMoves;
	}
	private static void continousHop(ArrayList<ArrayList<Point>> LOM, ArrayList<Point> LM, int player)
	{
		Point current = LM.get(LM.size()-1);
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 2; j++)
			{
				Point p, hop;
				if (player == 0)
				{
					p = new Point(current.x+i, current.y+j);
					hop = new Point(current.x+2*i, current.y+2*j);
				}else
				{
					p = new Point(current.x-i, current.y-j);
					hop = new Point(current.x-2*i, current.y-2*j);
				}

				if (hop.x<SIZE&&hop.x>=0&&hop.y<SIZE&&hop.y>=0)
				{
					if (bPieces.contains(p)||wPieces.contains(p))
					{
						if (!bPieces.contains(hop)&&!wPieces.contains(hop) && !LM.contains(hop))
						{
							ArrayList<Point> move = (ArrayList<Point>)LM.clone();
							move.add(hop);
//							System.out.println(move.toString());
							LOM.add(move);
							continousHop(LOM, move, player);
						}
					}
				}
			}
		}
	}
	private static void output(String s, ArrayList<Point> nextMove) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		
		for (int i = 0; i < nextMove.size()-1; i++)
		{
			writer.write(s);
			writer.write(" "+nextMove.get(i).x+","+nextMove.get(i).y);
			writer.write(" "+nextMove.get(i+1).x+","+nextMove.get(i+1).y + "\n");
		}
		
		writer.close();
		
	}
	private static boolean checkWin(int player)
	{
		Set<Point> base = (player == 0) ? wSet:bSet;
		ArrayList<Point> myPieces = (player==0) ? bPieces : wPieces;
		ArrayList<Point> oppoPieces = (player==0) ? wPieces : bPieces;
		int count = 0;
		for (Point p:myPieces)
		{
			if (base.contains(p))
			{
				count++;
			}
		}
		if (count==0){
			return false;
		}
		for (Point p:oppoPieces)
		{
			if (base.contains(p))
			{
				count++;
			}
		}
		return count==19;
	}
	private static void buildBoard(char[][] cBoard)
	{
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				if (cBoard[i][j] != '.'){
					if (cBoard[i][j] == 'B')
					{
						bPieces.add(new Point(j, i));
					}else
					{
						wPieces.add(new Point(j, i));
					}
				}
			}
		}
	}
}