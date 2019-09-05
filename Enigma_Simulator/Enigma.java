
import java.io.*;
import java.util.Scanner;

public class Enigma {
	
	private static Machine enig;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		if (args == null || args[0] == null)
		{
			System.out.println("Please enter a setup file name in arguments.");
			System.exit(0);
		}
		else if (args.length > 2) {
			System.out.println("Too many arguments.");
			System.exit(0);
		}
		
		setUp(args[0]);
		
		Scanner scan;
		char[] plain;
		if (args.length > 1)
		{
			File tempfile = new File(args[1]);
			scan = new Scanner(tempfile);;
			plain = readFile(scan);
		}
		else
		{
			scan = new Scanner(System.in);
			plain = readInput(scan);
		}
		
		String endResult = cryptify(plain);
		System.out.println(endResult);
	}
	
	private static String cryptify(char[] plainText)
	{
		StringBuilder sb = new StringBuilder();
		for (char c : plainText) {
			if (c >= 65 && c <= 90)
			{
				sb.append(enig.encrypt(c));
			}
		}
		return sb.toString();
	}
	
	private static char[] readFile(Scanner kb)
	{
		String raw = "";
		while (kb.hasNextLine()){
			raw += kb.nextLine();
		}
		raw = raw.toUpperCase();
		char[] plainText = raw.toCharArray();
		return plainText;
	}
	
	private static char[] readInput(Scanner kb)
	{
		String raw = kb.nextLine();
		raw = raw.toUpperCase();
		char[] plainText = raw.toCharArray();
		return plainText;
	}
	
	private static void setUp(String fname) throws FileNotFoundException
	{
		File finsert = new File(fname);
		Scanner sc = new Scanner(finsert);
		
		String line1, line2, line3, line4, line5;
		// get lines
		line1 = sc.nextLine();
		line2 = sc.nextLine();
		line3 = sc.nextLine();
		line4 = sc.nextLine();
		line5 = sc.nextLine();
		
		// set up machine:
		
		// get rotor numbers
		String[] splitl1 = line1.split("\\s+");
		int rotNums[] = new int[3];
		for (int i = 0; i < 3; i++)
		{
			rotNums[i] = Integer.parseInt(splitl1[i]);
		}
		
		// get ring settings
		String[] splitl2 = line2.split("\\s+");
		int ringSets[] = new int[3];
		for (int i = 0; i < 3; i++)
		{
			ringSets[i] = Integer.parseInt(splitl2[i]) - 1;
		}
		
		// generate window characters
		char[] winLetters = line3.toUpperCase().toCharArray();
		
		// get reflector
		char refChoice = line4.charAt(0);
		
		// generate plugboard choices
		String[] pbc = line5.split("\\s+");
		char[][] pb = new char[10][2];
		for (int i = 0; i < 10; i++)
		{
			if (pbc[i].charAt(0) == 'A' || pbc[i].charAt(1) == 'A')
			{
				pb[i][0] = pbc[i].charAt(1);
				pb[i][1] = pbc[i].charAt(0);
			}
			else {
				pb[i][0] = pbc[i].charAt(0);
				pb[i][1] = pbc[i].charAt(1);
			}
		}
		
		enig = new Machine(rotNums, ringSets, winLetters, refChoice);
		enig.setPlugboard(pb);
	}
	
	
	
	private static class Machine
	{
		private Rotor rot1;
		private Rotor rot2;
		private Rotor rot3;
		private int[] plugboard;
		private int[] reflector;
		private String input;
		private String output;
		
		public Machine (int[] rotors, int[] rs, char[] wl, char refChoice)
		{
			switch (refChoice) {
				case 'B':
					int[] temp = {25,18,21,8,17,19,12,4,16,24,14,7,15,11,13,9,5,2,6,26,3,23,22,10,1,20};
					reflector = temp;
					break;
				case 'C': 
					int[] temp2 = {6,22,16,10,9,1,15,25,5,4,18,26,24,23,7,3,20,11,21,17,19,2,14,13,8,12};
					reflector = temp2;
					break;
			}
			reflector = correctArr(reflector);
			
			rot1 = new Rotor(rotors[0], rs[0], wl[0]);
			rot2 = new Rotor(rotors[1], rs[1], wl[1]);
			rot3 = new Rotor(rotors[2], rs[2], wl[2]);
		}
		
		public void setPlugboard(char[][] pb)
		{
			plugboard = new int[26];
			int l1, l2;
			for (int i = 0; i < 10; i++)
			{
				plugboard[pb[i][1] - 65] = pb[i][0] - 65;
				plugboard[pb[i][0] - 65] = pb[i][1] - 65;
			}
			for (int i = 0; i < plugboard.length; i++)
			{
				if (plugboard[i] == 0 && plugboard[0] != i) plugboard[i] = i;
			}
		}
		
		public char encrypt(char c)
		{
			checkRotate();
			int ec = c - 65;
			
			// plugboard
			ec = plugboard[ec];
			
			ec = rot3.rightLeft(ec);
			ec = rot2.rightLeft(ec);
			ec = rot1.rightLeft(ec);
			
			ec = reflector[ec];
			
			ec = rot1.leftRight(ec);
			ec = rot2.leftRight(ec);
			ec = rot3.leftRight(ec);
			
			ec = plugboard[ec];
			
			return (char)((int)ec + 65);
		}
		
		public void checkRotate() {
			if (rot2.winLet == rot2.notch) {
				rot2.rotate();
				rot1.rotate();
			}
			else if (rot3.winLet == rot3.notch)
			{
				rot2.rotate();
			}
			rot3.rotate();
		}
		
		private static int[] correctArr(int[] arr)
		{
			int[] ret = new int[arr.length];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = arr[i] - 1;
			}
			return ret;
		}
		
	}
	
	private static class Rotor
	{
		private int[] wiring;
		private int offset;
		private int ringSet;
		private int notch;
		private int winLet;
		
		public Rotor(int num, int rs, char wl)
		{
			wiring = null;
			ringSet = rs;
			winLet = wl - 65;
			setOffset();
			notch = 0;
			
			switch (num) {
				case 1: 
					int[] temp = {5,11,13,6,12,7,4,17,22,26,14,20,15,23,25,8,24,21,19,16,1,9,2,18,3,10};
					wiring = temp;
					notch = 16;
					break;
				case 2: 
					int[] temp2 = {1,10,4,11,19,9,18,21,24,2,12,8,23,20,13,3,17,7,26,14,16,25,6,22,15,5};
					wiring = temp2;
					notch = 4;
					break;
				case 3: 
					int[] temp3 = {2,4,6,8,10,12,3,16,18,20,24,22,26,14,25,5,9,23,7,1,11,13,21,19,17,15};
					wiring = temp3;
					notch = 21;
					break;
				case 4: 
					int[] temp4 = {5,19,15,22,16,26,10,1,25,17,21,9,18,8,24,12,14,6,20,7,11,4,3,13,23,2};
					wiring = temp4;
					notch = 9;
					break;
				case 5:
					int[] temp5 = {22,26,2,18,7,9,20,25,21,16,19,4,14,8,12,24,1,23,13,10,17,15,6,5,3,11};
					wiring = temp5;
					notch = 25;
					break;
				default:
					int[] temp6 = {-1};
					wiring = temp6;
					notch = -1;
					break;
			}
			wiring = correctArr(wiring);
			
		}
		
		public void setOffset()
		{
			offset = winLet - ringSet;
			if (offset > 25)
				offset -= 26;
			else if (offset < 0)
				offset += 26;
		}
		
		public int rightLeft(int ec)
		{
			int ret = (subtractOffset(wiring[(ec + offset)%26]));
			return ret;
		}
		
		public int leftRight(int ec)
		{
			int ret = -1;
			for (int i = 0; i < 26; i++) 
			{
				if (wiring[i] == (ec+offset)%26) ret = subtractOffset(i);
			}
			return ret;
		}
		
		public int subtractOffset(int ec) {
			int ret = ec - offset;
			if (ret < 0) ret += 26;
			return ret;
		}
		
		private static int[] correctArr(int[] arr)
		{
			int[] ret = new int[arr.length];
			for (int i = 0; i < ret.length; i++)
			{
				ret[i] = arr[i] - 1;
			}
			return ret;
		}
		
		public void rotate(){
			winLet++;
			if (winLet > 25) winLet -= 26;
			setOffset();
		}
	}
	
}
