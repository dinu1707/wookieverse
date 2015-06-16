package Backbone;

import java.util.ArrayList;
import java.util.HashSet;

import Functionality.State;

/**
 * Die AutoSyntax muss so aussehen:
 * Alphabet Zust�nde �berg�nge Anfangszustand Endzustand
 * Dabei m�ssen zwischen allen jeweils ein leerzeichen stehen.
 * Die �berg�nge haben die Form Zustand-AlphabetFolgezustand
 * 
 * @author Ayse
 *
 */
public interface AutoSyntax {
	
	
	public static String[] makeStringArr(String input){
		String[] splitResult = input.split(" ");
		
		return splitResult;
	}
	
	
	/**
	 * 
	 * Methoden um Einzelne Eintr�ge im AutoSyntax Array in extra char Arrays zuspeichern
	 * Dazu muss der eingebene String zun�chst mit der Methide Ausgabe in ein String array umgewandelt werden.
	 * 
	 */
	public static ArrayList<String> getAlphabet(String[] input){
		
		ArrayList<String> alphabet=new ArrayList<String>();
		for(String string:input[0].split("")) alphabet.add(string);
		return alphabet;
	}
	
	public static ArrayList<String> getStates(String[] input){

		ArrayList<String> states=new ArrayList<String>();
		for(String string:input[1].split("")) states.add(string);
		return states;
	}
	
	public static ArrayList<String[]> getDelta(String[] input){
		String[] paths=new String[input[2].length()];
		paths=input[2].split("");
		ArrayList<String[]> edges=new ArrayList<String[]>();
		for(int i=0; i<paths.length; i+=4) {
			String[] edge=new String[3];
			edge[0]=paths[i];
			edge[1]=paths[i+2];
			edge[2]=paths[i+3];
			edges.add(edge);
		}
		
		return edges;
	}
	
	public static HashSet<String> getInitial(String[] input){
		HashSet<String> initials=new HashSet<String>();
		for(String string:input[3].split("")) initials.add(string);
		return initials;
	}
	
	public static HashSet<String> getAccepting(String[] input){
		HashSet<String> acceptings=new HashSet<String>();
		for(String string:input[4].split("")) acceptings.add(string);
		return acceptings;
	}
	
	public static boolean nameAlreadyInList(String name, ArrayList<State> list) {
		for(State state:list) {
			if(name.equals(state.getName())) return true;
		}
		return false;
	}


	public static boolean isSomeWhereInHashSetList(State last,
			ArrayList<HashSet<State>> newStates){
		for(HashSet<State> set: newStates) {
			if(set.contains(last)) return true;
		}
		return false;
	}
}
