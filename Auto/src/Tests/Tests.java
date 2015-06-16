package Tests;

import java.util.ArrayList;
import java.util.HashSet;

import Functionality.Automaton;
import Functionality.State;
public class Tests {
	public static void main(String[] args) {
		Automaton miniTest = new Automaton("01" + " " + "abcdefgh" + " "
				+ "a-0ba-1f" + "b-0gb-1c" + "c-0ac-1c" + "d-0cd-1g"
				+ "e-0he-1f" + "f-0cf-1g" + "g-0gg-1e" + "h-0gh-1c" + " " + "a"
				+ " " + "c");
		Automaton test = new Automaton("ab"
				+ " "
				+ "12345" 
				+ " "
				+ "1-a1" 
				+ "1-a4"
				+ "1-b1"
				+ "1-b2"
				+ "2-a3"
				+ "4-a4"
				+ "4-b4"
				+ "4-b5"
				+ "5-a5"
				+ "5-b5"
				+ "3-b5" 
				+ " "
				+ "1"
				+ " "
				+ "5");
		Automaton t= new Automaton("01 ab a-0b a b");
		System.out.println(miniTest.wordTest("01"));
		System.out.println(miniTest.wordTest("110"));
		System.out.println(miniTest.isDeterministic());
//		ArrayList<String> x= new ArrayList<String>();
//		x.add("a");
//		State a=new State("x", true, true, x);
//		State b=new State("y", true, true, x);
//		HashSet<State> s1=new HashSet<State>();
//		s1.add(b);s1.add(a);
//		HashSet<State> s2=new HashSet<State>();
//		s2.add(a);s2.add(b);
//		System.out.println();
//		System.out.println(s1.equals(s2));
		miniTest.minimize();
		System.out.println(miniTest.isMinimal());
		miniTest.automatAusgabeKonsole();
	}
}
