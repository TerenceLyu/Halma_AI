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
	public static void main(String[] args) throws Exception
	{
		BufferedReader input =
				new BufferedReader(new FileReader("input.txt"));
		String mode = input.readLine();
		String player = input.readLine();
		double timeLeft = Double.parseDouble(input.readLine());
		char[][] charBoard = new char[16][16];
		for (int i = 0; i < 16; i++)
		{
			charBoard[i] = input.readLine().toCharArray();
		}
		
		HashMap<Point, Integer> board = buildBoard(charBoard);
		
		
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
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				if (cBoard[i][j] != '.'){
					board.put(new Point(i, j), (cBoard[i][j] == 'B') ? 0:1);
				}
			}
		}
		return board;
	}
	private static int eval(char[][] board, char player)
	{
		
		
		return 0;
	}
}
//class Board
//{
//}