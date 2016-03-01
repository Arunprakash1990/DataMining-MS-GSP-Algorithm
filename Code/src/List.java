import java.util.ArrayList;

/*
 This class is responsible for holding ItemSets. An ItemSet here will be categorized as an ArrayList of Integers, denoting item numbers. 
 */
public class List {
	/*
 * Need to use ArrayList instead of HashSet Order of item_Support_Count
 * need to be maintained during MISValue_Of_ItemCandidate-Gen
 */
public ArrayList<Integer> item_Support_Count;

public List() {
	item_Support_Count = new ArrayList<Integer>();
}

/*
 * Test whether current List is a subset of the List given as the para
 */
public boolean isSubset(List list) {
	// To see if the parameter list contains all the items in the
// transaction.
		return list.item_Support_Count.containsAll(this.item_Support_Count);
	}
}
