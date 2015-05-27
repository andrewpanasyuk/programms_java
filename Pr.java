package cademika.com;

public class Pr {

	public static void main(String[] args) {
		System.out.println("Hello world!!!");
		int a = 5;
		int b = 25;
		System.out.println(a*b);
		
//		while (true) {
//			System.out.println(generation());
//			
//		}
	}

	static int generation() {
		int a = 1 + (int) (Math.random() * ((5 - 1) + 1));
		return a;
	}
}