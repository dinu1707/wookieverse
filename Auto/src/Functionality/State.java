package Functionality;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class State {
	private static SecureRandom random=new SecureRandom();
	private String name;
	private String id;
	private boolean initial;
	private boolean accepting;
	private ArrayList<String> alphabet;
	private ArrayList<HashSet<State>> edges;

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof State) || (other == null))
			return false;
		State otherState = (State) other;
		return this.id.equals(otherState.id);
	}

	@Override
	public int hashCode() {
		return 42 * this.id.hashCode();
	}

	public State(String stateName, boolean init, boolean accept,
			ArrayList<String> alphabet) {
		this.name = stateName;
		this.id=new BigInteger(42,random).toString(32);
		this.initial = init;
		this.accepting = accept;
		this.alphabet = alphabet;
		this.edges = new ArrayList<HashSet<State>>();
		for (int i = 0; i < this.alphabet.size(); i++) {
			this.edges.add(new HashSet<State>());
		}
	}
	
	public void setName(String name) {
		this.name=name;
	}
	public String getName() {
		return this.name;
	}

	public boolean isInitial() {
		return this.initial;
	}

	public boolean isAccepting() {
		return this.accepting;
	}

	public void addEdge(int symbolID, State state) {
		this.edges.get(symbolID).add(state);
	}
	
	public void addSinkState(State sinkState) {
		HashSet<State> sink=new HashSet<State>();
		sink.add(sinkState);
		for(int i=0; i<this.edges.size(); i++) {
			if(this.edges.get(i).isEmpty()) this.edges.set(i, sink);
		}
	}

	public String getTargetNames(int symbolID) {
		String output = "{";
		Iterator<State> it = this.edges.get(symbolID).iterator();
		while (it.hasNext()) {
			output += it.next().getName();
			if (it.hasNext())
				output += ", ";
		}
		return output + "}";
	}

	public String getTargetNames(String symbol) {
		String output = "{";
		Iterator<State> it = this.edges.get(this.alphabet.indexOf(symbol))
				.iterator();
		while (it.hasNext()) {
			output += it.next().getName();
			if (it.hasNext())
				output += ", ";
		}
		return output + "}";
	}

	public HashSet<State> targets(String symbol) {
		return this.edges.get(this.alphabet.indexOf(symbol));
	}
	
	public State getTarget(String symbol) {
		Iterator<State> it=targets(symbol).iterator();
		return it.next();
	}

	public boolean wordTest(String word) {
		if (word.isEmpty()) {
			return isAccepting();
		}
		String firstLetter = word.substring(0, 1);
		if(!this.alphabet.contains(firstLetter)) return false;
		HashSet<State> targets = targets(firstLetter);
		for (State state : targets) {
			if (state.wordTest(word.substring(1)))
				return true;
		}
		return false;
	}

	public String toString() {
		return this.name;
	}
	
	public boolean isDeterministic() {
		for(HashSet<State> set:this.edges) {
			if(set.size()!=1) return false;
		}
		return true;
	}

	public String printRow() {
		String output = "";
		if (this.isInitial())
			output += "-> ";
		else
			output += "   ";
		output += String.format("%25s", this.name);
		if (this.isAccepting())
			output += " #";
		else
			output += "  ";
		output += "|";
		for(HashSet<State> set: this.edges) {
			output += String.format("%25s", set+" || ");
		}
		return output;

	}

	public void setAccepting(boolean b) {
		this.accepting=b;
		
	}
	
	

}
