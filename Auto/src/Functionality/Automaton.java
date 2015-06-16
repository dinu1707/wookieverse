package Functionality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import Backbone.*;

public class Automaton implements AutoSyntax, RegularLanguage {
	private ArrayList<String> alphabet;
	private ArrayList<State> states;

	public Automaton(ArrayList<String> states, ArrayList<String> symbols,
			HashSet<String[]> delta, HashSet<String> initialStates,
			HashSet<String> acceptingStates) {
		this.alphabet = symbols;
		this.states = new ArrayList<State>();
		for (String state : states) {
			boolean init = initialStates.contains(state);
			boolean accept = acceptingStates.contains(state);
			this.states.add(new State(state, init, accept, this.alphabet));
		}
		for (String[] paths : delta)
			addEdge(paths[0], paths[1], paths[2]);
	}

	public Automaton(String auto) {
		String[] split = AutoSyntax.makeStringArr(auto);
		this.alphabet = AutoSyntax.getAlphabet(split);
		this.states = new ArrayList<State>();
		HashSet<String> initialStates = AutoSyntax.getInitial(split);
		HashSet<String> acceptingStates = AutoSyntax.getAccepting(split);
		for (String state : AutoSyntax.getStates(split)) {
			boolean init = initialStates.contains(state);
			boolean accept = acceptingStates.contains(state);
			this.states.add(new State(state, init, accept, this.alphabet));
		}
		for (String[] paths : AutoSyntax.getDelta(split))
			addEdge(paths[0], paths[1], paths[2]);

	}

	public ArrayList<State> getStates() {
		// TODO Auto-generated method stub
		return this.states;
	}

	public ArrayList<String> getAlphabet() {
		return this.alphabet;
	}

	public State getState(String name) {
		for (State state : this.states) {
			if (state.getName().equals(name))
				return state;
		}
		return null;
	}

	public HashSet<State> getInitialStates() {
		HashSet<State> initialStates = new HashSet<State>();
		for (State state : this.states) {
			if (state.isInitial())
				initialStates.add(state);
		}
		return initialStates;
	}

	public HashSet<State> getAcceptingStates() {
		HashSet<State> acceptingStates = new HashSet<State>();
		for (State state : this.states) {
			if (state.isAccepting())
				acceptingStates.add(state);
		}
		return acceptingStates;
	}

	public int getSymbolID(String symbol) {
		return this.alphabet.indexOf(symbol);
	}

	public void addEdge(String startName, String symbol, String targetName) {
		getState(startName).addEdge(getSymbolID(symbol), getState(targetName));
	}

	public void automatAusgabeKonsole() {
		if (isMinimal())
			System.out.print("Minimierter DFA ");
		else if (isDeterministic())
			System.out.print("DFA ");
		else
			System.out.print("NFA ");
		System.out.print("über das Alphabet ");
		System.out.print(this.alphabet);
		System.out.print(" mit den Zuständen ");
		System.out.println(this.states);

		for (State state : this.states)
			System.out.println(state.printRow());
	}

	@Override
	public boolean wordTest(String word) {
		for (State state : getInitialStates()) {
			if (state.wordTest(word))
				return true;
		}
		return false;

	}

	public boolean isDeterministic() {
		if (getInitialStates().size() != 1)
			return false;
		for (State state : this.states)
			if (!state.isDeterministic())
				return false;
		return true;
	}

	public void determinize() {
		if (!isDeterministic()) {
			ArrayList<HashSet<State>> newStates = new ArrayList<HashSet<State>>();
			ArrayList<ArrayList<HashSet<State>>> newDelta = new ArrayList<ArrayList<HashSet<State>>>();
			ArrayList<HashSet<State>> todo = new ArrayList<HashSet<State>>();
			todo.add(getInitialStates());
			boolean sinkStateNeeded = false;
			while (!todo.isEmpty()) {
				HashSet<State> currentStates = todo.get(0);
				ArrayList<HashSet<State>> newRow = new ArrayList<HashSet<State>>();
				for (String symbol : this.alphabet) {
					HashSet<State> set = new HashSet<State>();
					for (State state : currentStates) {
						set.addAll(state.targets(symbol));
					}
					newRow.add(set);
					/**
					 * Wenn wir das neue Set nicht schon entweder in unserer
					 * todo-Liste oder den bereits bearbeiteten Zustaenden
					 * haben, fuegen wir es der todo Liste hinzu.
					 **/
					if (!set.isEmpty() && !todo.contains(set)
							&& !newStates.contains(set))
						todo.add(set);
					if (set.isEmpty())
						sinkStateNeeded = true;
				}
				newDelta.add(newRow);
				newStates.add(currentStates);
				todo.remove(0);
			}
			/**
			 * Hier haben wir jetzt quasi die Zustandstabelle, daraus wollen wir
			 * jetzt neue States machen.
			 */
			ArrayList<State> newStateList = new ArrayList<State>();
			int x = 0;
			for (HashSet<State> set : newStates) {
				String name = "";
				// Zustände sind keine Startzustände
				boolean init = false;
				// Es sei denn, es handelt sich um den ersten Zustand in der
				// Liste
				if (x == 0)
					init = true;
				x++;
				boolean accept = false;
				for (State state : set) {
					name += state.getName();
					accept = accept || state.isAccepting();
				}
				int i = 0;
				String tempName = name;
				while (AutoSyntax.nameAlreadyInList(tempName, newStateList)) {
					tempName = name + i;
					i++;
				}
				newStateList.add(new State(tempName, init, accept,
						this.alphabet));
			}
			// Wenn ein Sinkstate gebraucht wird, bauen wir uns einen und packen
			// ihn in unsere neue Zustandsliste ein
			State sink = null;
			if (sinkStateNeeded) {
				sink = new State("Sink", false, false, this.alphabet);
				sink.addSinkState(sink);
				newStateList.add(sink);
			}
			// Jetzt bauen wir uns aus der Tabelle die wir ermittelt haben die
			// neuen Zustände
			for (int i = 0; i < newDelta.size(); i++) {
				ArrayList<HashSet<State>> row = newDelta.get(i);
				State state = newStateList.get(i);
				for (int s = 0; s < this.alphabet.size(); s++) {
					HashSet<State> setOfTargets = row.get(s);
					if (!setOfTargets.isEmpty()) {
						state.addEdge(s, newStateList.get(newStates
								.indexOf(setOfTargets)));
					}
					if (sinkStateNeeded)
						state.addSinkState(sink);
				}
			}
			this.states = newStateList;
		}
	}

	private boolean[][] miniHelper() {
		int size = this.states.size() - 1;
		boolean[][] miniTable = new boolean[size][size];
		// Paare von Start und
		for (int row = 0; row < size; row++) {
			for (int col = 0; col <= row; col++) {
				boolean accept1 = this.states.get(row + 1).isAccepting();
				boolean accept2 = this.states.get(col).isAccepting();
				miniTable[row][col] = accept1 ^ accept2;
			}
		}
		boolean hasChanged = true;
		while (hasChanged) {
			hasChanged = false;
			for (int row = 0; row < size; row++) {
				State rowState = this.states.get(row + 1);
				for (int col = 0; col <= row; col++) {
					State colState = this.states.get(col);
					if (!miniTable[row][col]) {
						for (String symbol : this.alphabet) {
							State rowTarget = rowState.getTarget(symbol);
							int rowTargetID = this.states.indexOf(rowTarget);
							State colTarget = colState.getTarget(symbol);
							int colTargetID = this.states.indexOf(colTarget);
							int min = Math.min(rowTargetID, colTargetID);
							int max = Math.max(rowTargetID, colTargetID);
							if ((!rowTarget.equals(colTarget))
									&& miniTable[max - 1][min]) {
								miniTable[row][col] = true;
								break;
							}

						}
					}
				}
			}
		}
		return miniTable;
	}

	public boolean isMinimal() {
		boolean[][] mini = miniHelper();
		for (int i = 0; i < this.states.size() - 1; i++) {
			for (int j = 0; j <= i; j++) {
				if (!mini[i][j])
					return false;
			}
		}
		return true;
	}

	public void minimize() {
		if (!isDeterministic())
			determinize();
		if (!isMinimal()) {
			int size = this.states.size() - 1;
			boolean[][] mini = miniHelper();
			ArrayList<HashSet<State>> newStates = new ArrayList<HashSet<State>>();
			HashSet<State> visited = new HashSet<State>();
			for (int col = 0; col < size; col++) {
				State state = this.states.get(col);
				if (!visited.contains(state)) {
					HashSet<State> set = new HashSet<State>();
					set.add(state);
					visited.add(state);
					for (int row = col; row < size; row++) {
						if (!mini[row][col]) {
							State stateToAdd = this.states.get(row + 1);
							set.add(stateToAdd);
							visited.add(stateToAdd);
						}
					}
					newStates.add(set);
				}
			}
			State last = this.states.get(size);
			if (!visited.contains(last)) {
				HashSet<State> set = new HashSet<State>();
				set.add(last);
				newStates.add(set);
			}
			// Neue States basteln
			ArrayList<State> newStateList = new ArrayList<State>();
			for (HashSet<State> set : newStates) {
				String name = "";
				boolean initial = false;
				boolean accepting = false;
				for (State state : set) {
					name += state.getName();
					initial |= state.isInitial();
					accepting |= state.isAccepting();
				}
				int x = 0;
				String tempName = name;
				while (AutoSyntax.nameAlreadyInList(tempName, newStateList)) {
					tempName += name + x;
					x++;
				}
				newStateList.add(new State(tempName, initial, accepting,
						this.alphabet));
			}
			System.out.println("x" + newStateList.size());
			System.out.println("x" + newStates.size());
			for (int row = 0; row < newStateList.size(); row++) {
				HashSet<State> set = newStates.get(row);
				for (int col = 0; col < this.alphabet.size(); col++) {
					// Ein Zustand aus der aktuellen Menge wird genommen. Da
					// alle gleich sind, reicht es wenn wir schauen wo der
					// hinführt
					State state = set.iterator().next();
					State target = state.getTarget(this.alphabet.get(col));
					int i = 0;
					for (HashSet<State> s : newStates) {
						if (s.contains(target))
							break;
						i++;
					}
					newStateList.get(row).addEdge(col, newStateList.get(i));
				}
			}
			this.states = newStateList;
			for (State s : newStateList)
				System.out.println(s);
			for (HashSet<State> s : newStates)
				System.out.println(s);
		}

	}

	// Vertauscht Start und Endzustände
	// Dreht die Kanten um.
//	public void reverse() {
//		HashSet<State> initial = getInitialStates();
//		HashSet<State> accepting = getAcceptingStates();
//		return;
//	}

	// Vertauscht Endzustände und Nicht-Endzustände
	public void complement() {
		for (State state : this.states) {
			state.setAccepting(!state.isAccepting());
		}
	}

}
