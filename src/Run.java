import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Botong Lyu
 * botongly@usc.edu
 * Halma_AI
 * 2019/10/6
 */
public class Run
{
	public final static int SIZE = 16;
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
	public static void main(String[] args) throws Exception
	{
		BufferedReader input =
				new BufferedReader(new FileReader("input.txt"));
		String mode = input.readLine();
		String player = input.readLine();
		double timeLeft = Double.parseDouble(input.readLine());
		char[][] charBoard = new char[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
		{
			charBoard[i] = input.readLine().toCharArray();
		}
		
		HashMap<Point, Integer> board = buildBoard(charBoard);
		System.out.println(eval(board, 'B'));
		System.out.println(eval(board, 'W'));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		
//		for (Map.Entry<Point, Integer> e:board.entrySet())
//		{
//			writer.write(e.getKey().toString() + e.getValue() + "\n");
//		}
		writer.close();
	}
	private static HashMap<Point, Integer> buildBoard(char[][] cBoard)
	{
		HashMap<Point, Integer> board = new HashMap<>(40);
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				if (cBoard[i][j] != '.'){
					board.put(new Point(i, j), (cBoard[i][j] == 'B') ? 0:1);
				}
			}
		}
		return board;
	}
	private static int eval(HashMap<Point, Integer> board, char player)
	{
		Point[] base = (player=='B') ? bBase:wBase;
		int score = 0;
		for (int i = 0; i < 19; i++)
		{
			if (board.containsKey(base[i]))
			{
				score += 10;
			}
		}
		int x = (player=='B') ? 15:0;
		int y = x;
		int p = (player=='B') ? 0:1;
		for (Map.Entry<Point, Integer> e:board.entrySet())
		{
			if (e.getValue().equals(p))
			{
				score += Math.abs(x-e.getKey().x) + Math.abs(y-e.getKey().y);
			}
		}
		return score-50;
	}
}
//class Board
//{
//}