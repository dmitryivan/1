import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		String filename = "cipher.txt";
		int numFail = 2; // number of task in which failed to open treasure-box
		// encrypted message
		String msg = "_èî8_0è2ã0â9ðòî9_.òå.2è_åÍ1êò7ìð3ñ,1îà_ï9óåö._8ã9";
		// key sequence
		int[] keySeq = buidMagicSquare(filename, msg.length(), numFail);
		
		System.out.println(decrypt(msg,keySeq));

	}
	
	public static String decrypt(String msg, int[] keySeq){
		String decrypted = "";
		char[] msgArr = msg.toCharArray();
		HashMap<Integer, Character> map = new HashMap<>();
		
		for (int i = 0; i < msg.length(); i++) {
			map.put(keySeq[i], msgArr[i]);
		}
		Arrays.sort(keySeq); 
		for(int k: keySeq){
			decrypted += map.get(k);
		}
		return decrypted;
	}

	public static int[] buidMagicSquare(String filename, int size, int startNum) {
		int[] result = new int[size];
		int i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String str = "";
			while ((str = br.readLine()) != null && i < size) {
				if (!str.matches("^\\s*$")) {
					if (i == 0) {
						result[i] = evalString(str, startNum);
						i++;
					} else {
						result[i] = evalString(str, result[i - 1]);
						i++;
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading file!");
		}

		if (i < size) {
			System.out
					.println("Error building magic square, the size of input is not enough");
			return null;
		} else {
			return result;
		}
	}

	public static int evalString(String str, int num) {
		String unaryOp = "~+-";
		String binaryOp = "&|^";
		char op = (char) 0;
		boolean buildNum2 = false;
		int num2 = 0;

		for (char c : str.toCharArray()) {
			if (buildNum2) {
				switch (c) {
				case '$':
					num2++;
					break;
				case '*':
					num2 += 10;
					break;
				case ')':
					if (op != (char) 0 && num2 >= 0) {
						num = evalSingle(op, num, num2);
					} else {
						System.out.println("Error in binary operation");
					}
					op = (char) 0;
					num2 = 0;
					buildNum2 = false;
					break;
				}
			} else {
				if (binaryOp.contains(c + "")) {
					op = c;
					buildNum2 = true;
				} else if (unaryOp.contains(c + "")) {
					num = evalSingle(c, num);
				}
			}
		}
		return num;
	}

	public static int evalSingle(char op, int num) {
		int result = 0;
		switch (op) {
		case '~':
			result = ~num;
			break;
		case '+':
			result = num + 1;
			break;
		case '-':
			result = num - 1;
			break;
		}
		return result;
	}

	public static int evalSingle(char op, int num, int num2) {
		int result = 0;
		switch (op) {
		case '|':
			result = num | num2;
			break;
		case '&':
			result = num & num2;
			break;
		case '^':
			result = num ^ num2;
			break;
		}
		return result;
	}
}