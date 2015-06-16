package konsole;

import java.io.IOException;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import Functionality.*;
import Backbone.*;

public class KonsolenSpiele {

	// 01 abcdefgh
	// a-0ba-1fb-0gb-1cc-0ac-1cd-0cd-1ge-0he-1ff-0cf-1gg-0gg-1eh-0gh-1c a c
	// 01 AB A-0AA-1AA-1BB-1B A B
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		Scanner scanner2 = new Scanner(System.in);
		ArrayList<Automaton> automaten = new ArrayList<Automaton>();
		//ArrayList<AutomatonTidy> automaten = new ArrayList<AutomatonTidy>();
		ArrayList<String> automatenNamen = new ArrayList<String>();
		String eingabe;
		int eingabeInt = 0;
		System.out.println("Automaten basteln leicht gemacht.");
		boolean work = true;
		while (work) {
			System.out.println("_N_eu/ _L_iste/ _W_orttest / _E_nde");
			eingabe = scanner.nextLine();
			switch (eingabe.toUpperCase()) {
			case "N":
				System.out.println("Wie soll der Automat heißen?");
				boolean correctName = false;
				while (!correctName) {
					eingabe = scanner.nextLine();
					if (!automatenNamen.contains(eingabe)) {
						automatenNamen.add(eingabe);
						correctName = true;
					} else {
						System.out
								.println("Name existiert bereits. Nochmal bitte!");
					}
				}
				ArrayList<String> states = new ArrayList<String>();
				ArrayList<String> alphabet = new ArrayList<String>();
				HashSet<String[]> paths = new HashSet<String[]>();
				HashSet<String> initial = new HashSet<String>();
				HashSet<String> accepting = new HashSet<String>();
				System.out.println("Zustände.");
				while (true) {
					System.out.println("Zustand eingeben (# zum Beeenden)");
					eingabe = scanner.nextLine();
					if (eingabe.equals("#"))
						break;
					if (states.contains(eingabe)) {
						System.out.println("Zustand existiert schon");
					} else {
						states.add(eingabe);
						System.out.println("Bisherige Zustände: " + states);
					}
				}
				System.out
						.println("Welcher der Zustände soll ein Startzustand sein? ");
				while (true) {
					if (initial.isEmpty()) {
						System.out.print("Zustand eingeben: ");
						eingabe = scanner.nextLine();
						if (eingabe.equals("#"))
							continue;
					} else {
						System.out
								.print("Weiteren Startzustand eingeben (Eingabe beenden mit #): ");
						eingabe = scanner.nextLine();
						if (eingabe.equals("#"))
							break;
					}
					if (states.contains(eingabe)) {
						initial.add(eingabe);
					} else {
						System.out
								.println(eingabe
										+ " ist (noch) kein Zustand. Hinzufügen? (j/N)");
						eingabe = scanner.nextLine();
						if (!eingabe.toLowerCase().equals("n")) {
							states.add(eingabe);
							initial.add(eingabe);
						}
					}
				}
				System.out
						.println("Welcher der Zustände soll ein Endzustand sein? ");
				while (true) {
					System.out
							.print("Endzustand eingeben (Eingabe beenden mit #): ");
					eingabe = scanner.nextLine();
					if (eingabe.equals("#"))
						break;
					if (states.contains(eingabe)) {
						accepting.add(eingabe);
					} else {
						System.out
								.println(eingabe
										+ " ist (noch) kein Zustand. Hinzufügen? (j/N)");
						eingabe = scanner.nextLine();
						if (!eingabe.toLowerCase().equals("n")) {
							states.add(eingabe);
							accepting.add(eingabe);
						}
					}
				}

				System.out.println("Welche Buchstaben hat Sigma? ");
				while (true) {
					if (alphabet.isEmpty()) {
						System.out.print("Symbol eingeben: ");
					} else {
						System.out.print("Symbol (Eingabe beenden mit #): ");
					}
					eingabe = scanner.nextLine();
					if (eingabe.equals("#"))
						break;
					if (!alphabet.contains(eingabe)) {
						alphabet.add(eingabe);
						System.out.println("Bisherige Symbole: " + alphabet);
					}
				}
				System.out
						.println("Zum Abschluss kümmern wir uns um die Pfade. Unser Automat hat bisher");
				System.out.println("die Zustände " + states + "(Start: "
						+ initial + ", Ende: " + accepting + ")");
				System.out.println("und das Alphabet " + alphabet);
				while (true) {
//					String source;
//					String symbol;
//					String target;
					System.out.print("Ausgangszustand (Eingabe beenden mit #): ");
					String source = scanner.nextLine();
					if (source.equals("#"))
						break;
					if (!states.contains(source)) {
						states.add(source);
					}
					System.out.print("Übergangssymbol (Eingabe beenden mit #): ");
					String symbol = scanner.nextLine();
					if (symbol.equals("#"))
						break;
					if (!alphabet.contains(symbol)) {
						alphabet.add(symbol);
					}
					System.out.print("Zielzustand (Eingabe beenden mit #): ");
					String target = scanner.nextLine();
					if (source.equals("#"))
						break;
					if (!states.contains(target)) {
						states.add(target);
					}
					String[] path=new String[3];
					path[0]=source; path[1]=symbol; path[2]=target;
					paths.add(path);
				}
				automaten.add(new Automaton(states, alphabet, paths, initial,
						accepting));
				System.out.println("Automat "
						+ automatenNamen.get(automatenNamen.size() - 1)
						+ " hinzugefügt.");
				automaten.get(automaten.size() - 1).automatAusgabeKonsole();
				;
				break;
			case "L":
				if (automatenNamen.isEmpty()) {
					System.out
							.println("Es befindet sich noch kein Automat in der Liste.");
					break;
				} else {
					for (String s : automatenNamen) {
						System.out.println((automatenNamen.indexOf(s) + 1)
								+ "- " + s);
					}
				}
				boolean eingabeKorrekt = false;
				while (!eingabeKorrekt) {
					System.out.println("Welchen Automat anzeigen? (1-"
							+ automatenNamen.size() + ", 0 zurck zum Menue)");
					try {
						eingabeInt = scanner2.nextInt();
						eingabeKorrekt = true;
					} catch (InputMismatchException e) {
						eingabeKorrekt = false;
					}
					if (!((eingabeInt >= 0) && (eingabeInt <= automatenNamen
							.size())))
						eingabeKorrekt = false;
				}
				if (eingabeInt == 0)
					break;
				Automaton show = automaten.get(eingabeInt - 1);
				show.automatAusgabeKonsole();
				if (show.getMinimal())
					break;
				if (show.getDeterministic()) {
					System.out
							.println("Automat ist noch nicht minimiert. Jetzt minimieren? („j/N“)");
					eingabe = scanner.nextLine();
					if (!(eingabe.equals("J") || eingabe.equals("j")))
						break;
					show.minimize();
					System.out.println("Minimierter Automat:");
					show.automatAusgabeKonsole();
					break;
				}
				System.out
						.println("Der Automat ist weder deterministisch noch minimiert. Wollen wir dagegen was tun?? („d/m/N“)");
				eingabe = scanner.nextLine();
				if (!(eingabe.toLowerCase().equals("d") || eingabe
						.toLowerCase().equals("m")))
					break;
				if (!eingabe.toLowerCase().equals("m")) {
					show.determinize();
					System.out.println("Deterministischer Automat:");
					show.automatAusgabeKonsole();
					break;
				}
				show.minimize();
				System.out.println("Minimierter Automat:");
				show.automatAusgabeKonsole();
				break;
			case "W":
				if (automatenNamen.isEmpty()) {
					System.out
							.println("Es befindet sich noch kein Automat in der Liste.");
					break;
				} else {
					int i = 0;
					for (String s : automatenNamen) {
						i++;
						System.out.println(i + "- " + s + "  (Alphabet="
								+ automaten.get(i - 1).sigmaToString() + ")");
					}
				}
				eingabeKorrekt = false;
				while (!eingabeKorrekt) {
					System.out.println("Welcher Automat soll getestet werden?");
					try {
						eingabeInt = scanner2.nextInt();
						eingabeKorrekt = true;
					} catch (InputMismatchException e) {
						eingabeKorrekt = false;
					}
					if (!((eingabeInt >= 0) && (eingabeInt <= automatenNamen
							.size())))
						eingabeKorrekt = false;
				}
				if (eingabeInt == 0)
					break;

				boolean testen = true;
				while (testen) {
					System.out.println("Wort zum Testen eingeben");
					eingabe = scanner.nextLine();
					System.out.println(automaten.get(eingabeInt - 1).wordTest(
							eingabe));
					System.out.println("Nochmal? („J/n“)");
					eingabe = scanner.nextLine();
					if (!(eingabe.equals("J") || eingabe.equals("j")))
						testen = false;
				}
				break;
			case "E":
				System.out.println("I'll be back!");
				work = false;
				break;
			default:
				System.out.println("Eingabe nicht erkannt.");
			}
		}
	}

}
