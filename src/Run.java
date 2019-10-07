import java.io.*;

/**
 * Botong Lyu
 * botongly@usc.edu
 * Halma_AI
 * 2019/10/6
 */
public class Run
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader input =
				new BufferedReader(new FileReader("input.txt"));
		String mode = input.readLine();
		String player = input.readLine();
		double timeLeft = Double.parseDouble(input.readLine());
		char[][] board = new char[16][16];
		for (int i = 0; i < 16; i++)
		{
			board[i] = input.readLine().toCharArray();
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
		
	}
	private static int eval(char[][] board, char player)
	{
		
		
		return 0;
	}
}
