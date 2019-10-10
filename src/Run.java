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
	final static int DEPTH = 2;
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
	final static ArrayList<Point> bPieces = new ArrayList<>(19);//black pieces
	final static ArrayList<Point> wPieces = new ArrayList<>(19);//white pieces
	static long time = System.currentTimeMillis();
	public static void main(String[] args) throws Exception
	{
//		long time = System.currentTimeMillis();
		BufferedReader input = new BufferedReader(new FileReader("input.txt"));
		String mode = input.readLine();
		int player = (input.readLine().equals("BLACK")) ? 0:1;
		double timeLeft = Double.parseDouble(input.readLine());
		char[][] charBoard = new char[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
		{
			charBoard[i] = input.readLine().toCharArray();
		}
		
		buildBoard(charBoard);
//		System.out.println(bPieces);
//		System.out.println(wPieces);
		for (ArrayList<Point> list:generateMove(new Point(9,9)))
		{
			System.out.println(list);
		}
//		System.out.println(generateMove(new Point(15,15)).toString());
//		System.out.println(eval(player));
//		ArrayList<Point> nextMove = treeSearch(player);
//		System.out.println(nextMove);
		
//		System.out.println(eval(board, 'B'));
//		System.out.println(eval(board, 'W'));

//		String s = generateMove(board, new Point(2, 2), 1).stream().map(Object::toString)
//				.collect(Collectors.joining(", "));
//		System.out.println(s);
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		
//		for (Map.Entry<Point, Integer> e:board.entrySet())
//		{
//			writer.write(e.getKey().toString() + e.getValue() + "\n");
//		}
		writer.close();
	}
	private static ArrayList<Point> treeSearch(int player)
	{
		int max = -10000;
		ArrayList<Point> nextMove = new ArrayList<>();
		ArrayList<Point> pieces = (player==0) ? bPieces:wPieces;
		for (Point p:pieces)
		{
			System.out.println(max);
			System.out.println(p);
			System.out.println(System.currentTimeMillis()-time+"ms");
			ArrayList<ArrayList<Point>> moves = generateMove(p);
			
			for (ArrayList<Point> move:moves)
			{
//				System.out.println(move.toString());
				p.setLocation(move.get(move.size()-1));
				int value = minimax(player, DEPTH, max, 10000, false);
				if (value>max)
				{
					max = value;
					nextMove = move;
					System.out.println(nextMove.toString());
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
			int value = -1000000;
			pieces = (player==0) ? bPieces:wPieces;
			for (Point p:pieces)
			{
				moves = generateMove(p);
				for (ArrayList<Point> move:moves)
				{
					p.setLocation(move.get(move.size()-1));
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
			int value = 1000000;
			pieces = (player==0) ? wPieces:bPieces;
			for (Point p:pieces)
			{
				moves = generateMove(p);
				for (ArrayList<Point> move:moves)
				{
					p.setLocation(move.get(move.size()-1));
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
	private static int eval(int player)
	{
//		Point[] myBase = (player==0) ? bBase:wBase;
//		Point[] oppoBase = (player==0) ? wBase:bBase;
		int score = 0;
		int x = (player==0) ? 0:15;
		int y = (player==0) ? 15:0;
		ArrayList<Point> myPieces = (player==0) ? bPieces : wPieces;
		ArrayList<Point> oppoPieces = (player==0) ? wPieces : bPieces;
		for (Point p:myPieces)
		{
			score += Math.abs(x-p.x) + Math.abs(x-p.y);
		}
		for (Point p:oppoPieces)
		{
			score -= Math.abs(y-p.x) + Math.abs(y-p.y);
		}
		return score;
	}
	private static ArrayList<ArrayList<Point>> generateMove(Point piece)
	{
		ArrayList<ArrayList<Point>> listsOfMoves = new ArrayList<>();
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (i!=0||j!=0)
				{
					if (piece.x + i<SIZE&&piece.x + i>=0&&piece.y + j<SIZE&&piece.y + j>=0)
					{
						Point target = new Point(piece.x + i, piece.y + j);
						if (!bPieces.contains(target)&&!wPieces.contains(target))
						{
							//regular moves
							ArrayList<Point> moves = new ArrayList<>();
							moves.add(piece);
							moves.add(target);
							listsOfMoves.add(moves);
						}else
						{
							//check if a hop is possible
							if (piece.x + 2*i<SIZE&&piece.x + 2*i>=0&&piece.y + 2*j<SIZE&&piece.y + 2*j>=0)
							{
								Point hop = new Point(piece.x + 2*i, piece.y + 2*j);
								if (!bPieces.contains(hop)&&!wPieces.contains(hop))
								{
									ArrayList<Point> moves = new ArrayList<>();
									moves.add(piece);
									moves.add(hop);
									listsOfMoves.add(moves);
//									System.out.println(moves);
									continousHop(listsOfMoves, moves);
								}
							}
						}
					}
					
				}
			}
		}
		return listsOfMoves;
	}
	private static void continousHop(ArrayList<ArrayList<Point>> LOM, ArrayList<Point> LM)
	{
		Point current = LM.get(LM.size()-1);
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (current.x + 2*i<SIZE&&current.x + 2*i>=0&&current.y + 2*j<SIZE&&current.y + 2*j>=0)
				{
					Point p = new Point(current.x-i, current.y-j);
					if (bPieces.contains(p)||wPieces.contains(p)){
						Point hop = new Point(current.x-2*i, current.y-2*j);
						if (!bPieces.contains(hop)&&!wPieces.contains(hop) && !LM.contains(hop))
						{
							ArrayList<Point> move = (ArrayList<Point>)LM.clone();
							move.add(hop);
//							System.out.println(move.toString());
							LOM.add(move);
							continousHop(LOM, move);
						}
					}
				}
			}
		}
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
class Piece
{
	Point location;
	
	public Piece(Point location, int player)
	{
		this.location = location;
	}
	
	@Override
	public int hashCode()
	{
		return this.location.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Piece other = (Piece) obj;
		return this.location.equals(other.location);
	}
	
	@Override
	public String toString()
	{
		return this.location.toString();
	}
}
//class Board
//{
//}