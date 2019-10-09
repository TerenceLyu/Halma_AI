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
			new Point(1,3), new Point(2,3), new Point(0,4), new Point(1,4)};
	final static Point[] wBase={new Point(15,15), new Point(14,15), new Point(13,15),
			new Point(12,15), new Point(11,15), new Point(15,14), new Point(14,14),
			new Point(13,14), new Point(12,14), new Point(11,14), new Point(15,13),
			new Point(14,13), new Point(13,13), new Point(12,13), new Point(15,12),
			new Point(14,12), new Point(13,12), new Point(15,11), new Point(14,11)};
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
		
		HashMap<Point, Integer> board = buildBoard(charBoard);
		ArrayList<Point> nextMove = treeSearch(board, player);
		System.out.println(nextMove.stream().map(Objects::toString).collect(Collectors.joining()));
		
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
	private static ArrayList<Point> treeSearch(HashMap<Point, Integer> board, int player)
	{
		int max = -1;
		ArrayList<Point> nextMove = new ArrayList<>();
		for (Map.Entry<Point, Integer> e:board.entrySet())
		{
			if (e.getValue() == player)
			{
				System.out.println(max);
				System.out.println(System.currentTimeMillis()-time+"ms");
				ArrayList<ArrayList<Point>> moves = generateMove(board, e.getKey(), player);
				for (ArrayList<Point> move:moves)
				{
					HashMap<Point, Integer> nBoard = (HashMap<Point, Integer>)board.clone();
					nBoard.put(move.get(move.size()-1), board.get(move.get(0)));
					nBoard.remove(move.get(0));

					
					int value = recSearch(nBoard, (player+1)%2, 0, max, 10000);
					if (value>max)
					{
						max = value;
						nextMove = move;
					}
					
				}
			}
		}
		return nextMove;
	}
	private static int recSearch(HashMap<Point, Integer> board, int player, int level, int alpha, int beta)
	{
		int value = (level%2 == 0) ? -1:10000;
		for (Map.Entry<Point, Integer> e:board.entrySet())
		{
			if (e.getValue() == player)
			{
				ArrayList<ArrayList<Point>> moves = generateMove(board, e.getKey(), player);
				for (ArrayList<Point> move:moves)
				{
					HashMap<Point, Integer> nBoard = (HashMap<Point, Integer>)board.clone();
//					board.remove(e.getKey());
//					board.put(move.get(move.size()-1), player);
					nBoard.put(move.get(move.size()-1), board.get(move.get(0)));
					nBoard.remove(move.get(0));
					//check if we are at leaf
					if (level == DEPTH)
					{
//						System.out.println("leaf");
						if (level%2 == 0)
						{
							//max level
							value = Math.max(value, eval(nBoard, player));
						}else
						{
							//min level
							value = Math.min(value, eval(nBoard, player));
						}
					}else
					{
						if (level%2 == 0)
						{
							//max level
							value = Math.max(value, recSearch(nBoard, (player+1)%2, level+1, value, beta));
						}else
						{
							//min level
							value = Math.min(value, recSearch(nBoard, (player+1)%2, level+1, alpha, value));
						}
					}
					//compare with either alpha or beta for pruning
					if (level%2 == 0)
					{
						//at max
						if (value>beta)
						{
							return value;
						}
					}else
					{
						//at min
						if (value<alpha)
						{
							return value;
						}
					}
//					board.remove(move.get(move.size()-1));
//					board.put(e.getKey(), player);
					
				}
			}
		}
		return value;
	}
	private static int eval(HashMap<Point, Integer> board, int player)
	{
		Point[] base = (player=='B') ? wBase:bBase;
		int score = 0;
		for (int i = 0; i < 19; i++)
		{
			//each occupied opponent's base grid is worth 10 points
			if (board.containsKey(base[i]))
			{
				score += 10;
			}
		}
		int x = (player==0) ? 15:0;
		int y = x;
		for (Map.Entry<Point, Integer> e:board.entrySet())
		{
			if (e.getValue().equals(player))
			{
				score += Math.min(30 - (Math.abs(x-e.getKey().x) + Math.abs(y-e.getKey().y)), 25);
			}
		}
		return score-50;
	}
	private static ArrayList<ArrayList<Point>> generateMove(HashMap<Point, Integer> board, Point piece, int player)
	{
		ArrayList<ArrayList<Point>> listsOfMoves = new ArrayList<>();
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				if (i!=0||j!=0)
				{
					Point target = new Point(piece.x + i, piece.y + j);
					if (!board.containsKey(target))
					{
						//regular moves
						ArrayList<Point> moves = new ArrayList<>();
						moves.add(piece);
						moves.add(target);
						listsOfMoves.add(moves);
					}else
					{
						//check if a hop is possible
						Point hop = new Point(piece.x + 2*i, piece.y + 2*j);
						if (!board.containsKey(hop))
						{
							ArrayList<Point> moves = new ArrayList<>();
							moves.add(piece);
							moves.add(hop);
							listsOfMoves.add(moves);
							continousHop(listsOfMoves, moves, board);
						}
					}
				}
			}
		}
		return listsOfMoves;
	}
	private static void continousHop(ArrayList<ArrayList<Point>> LOM, ArrayList<Point> LM, HashMap<Point, Integer> board)
	{
		Point current = LM.get(LM.size()-1);
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				Point p = new Point(current.x-i, current.y-j);
				if (board.containsKey(p)){
					Point hop = new Point(current.x-2*i, current.y-2*j);
					if (!board.containsKey(hop) && !LM.contains(hop))
					{
						ArrayList<Point> move = (ArrayList<Point>)LM.clone();
						move.add(hop);
						LOM.add(move);
						continousHop(LOM, move, board);
					}
				}
			}
		}
	}
	private static HashMap<Point, Integer> buildBoard(char[][] cBoard)
	{
		HashMap<Point, Integer> board = new HashMap<>(40);
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				if (cBoard[i][j] != '.'){
					board.put(new Point(j, i), (cBoard[i][j] == 'B') ? 0:1);
				}
			}
		}
		return board;
	}
	
}
//class Board
//{
//}