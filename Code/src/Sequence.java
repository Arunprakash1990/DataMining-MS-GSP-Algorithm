import java.util.ArrayList;
import java.util.HashSet;

/*
 * This is the class used to abstract Sequence.
 * Every Sequence will contains a list of Lists.
 */
public class Sequence {
	public ArrayList<List> Lists;
	public int count; // count the occurrence of the Sequence

Sequence() {
	Lists = new ArrayList<List>();
	count = 0;
}

/*
 * This function returns the number of the last item in the Sequence
 */
public Integer getLastItem() {
	ArrayList<Integer> item_Support_Count = Lists.get(Lists.size() - 1).item_Support_Count;
	return item_Support_Count.get(item_Support_Count.size() - 1);
}

// To maintain order of items based on their item numbers.
public int hashCode() {
	int result = 0;
	for (Integer it : this.getItem_Support_Count())
		result += it.intValue();
	return result;
}

/*
 * This function returns all the item_Support_Count contained in this
 * Sequence
 */
public ArrayList<Integer> getItem_Support_Count() {
	ArrayList<Integer> result = new ArrayList<Integer>();
	for (List i : Lists) {
		result.addAll(i.item_Support_Count);
	}
	return result;
}

public HashSet<Integer> getItem_Support_CountAsSet() {
	HashSet<Integer> result = new HashSet<Integer>();
	for (List i : Lists) {
		result.addAll(i.item_Support_Count);
	}
	return result;
}

/*
 * This function returns the id of the first item in the Sequence
 */
public Integer getFirstItem() {
	return Lists.get(0).item_Support_Count.get(0);
}

/*
 * This method is used to copy a Sequence
 */
public Sequence copy() {
	Sequence tran = new Sequence();
	for (int i = 0; i < this.Lists.size(); i++) {
		List is = new List();
		is.item_Support_Count
				.addAll(this.Lists.get(i).item_Support_Count);
		tran.Lists.add(is);
	}
	return tran;
}

/*
 * This method is used to check if this Sequence is contained in the
 * Sequence indicated by an para.
 */
public boolean containedIn(Sequence tran) {
	boolean result = true;
	int m = this.Lists.size();
	int n = tran.Lists.size();
	int i = 0, j = 0;
	for (i = 0; i < m; i++) {
		List is = this.Lists.get(i);
		do {
			if (m - i > n - j) { // The number of rest elements in current
// sequence
// is greater than the number of rest
// elements in tran
	result = false;
	break;
}
if (is.isSubset(tran.Lists.get(j))) { // find an element in tran
// which is the
// Support_Counter set
// of current element in
// current sequence
				j++;
				break;
			}
			j++;
		} while (j < n);
		if (result == false) {
			break;
		}
		if (i == m - 1 && j == n) {
			result = is.isSubset(tran.Lists.get(j - 1));
		}
	}
	return result;
}

/*
 * This method is used to create and return an Sequence which is an reverse
 * of this Sequence (also have to reverse the element/item set)
 */
public Sequence reverse() {
	Sequence rev = new Sequence();
	List revIS = new List();
	int i, j;
	for (i = this.Lists.size() - 1; i >= 0; i--) {
		List is = this.Lists.get(i);
		for (j = is.item_Support_Count.size() - 1; j >= 0; j--)
			revIS.item_Support_Count.add(new Integer(
					is.item_Support_Count.get(j).intValue()));
		rev.Lists.add(revIS);
		revIS = new List();
	}
	return rev;
}

/*
 * This method is responsible to perform the first step in the MS-Candidate
 * SPM function. Remove the second item in itemset s1 and the last item in
 * Itemset s2 and to compare if both of them are equal. If they are equal,
 * perform the Join operation.
 */
public boolean specialEqualTo(Sequence tran, int index1, int index2) {
	boolean result = false;
	int s1 = this.Lists.size();
	int s2 = tran.Lists.size();
	//Create new transactions to see the original one and the compared one
	Sequence current = new Sequence();
	Sequence compare = new Sequence();
	//Prevent modification of actual objects, use their copies to make changes. 
	current = this.copy();
	compare = tran.copy();
	int i = 0, j = 0, index;
	//Finds out item at index 1 
	for (index = 0; index < s1; index++) {
		List is = current.Lists.get(index);
		j = i;
		i += is.item_Support_Count.size();
		if (i > index1)
			break;
	}
	//Removes item at index 1
	current.Lists.get(index).item_Support_Count.remove(index1 - j);
	if (current.Lists.get(index).item_Support_Count.size() == 0)
		current.Lists.remove(index);
	i = 0;
	j = 0;
	//Finds item at index 2
	for (index = 0; index < s2; index++) {
		List is = compare.Lists.get(index);
		j = i;
		i += is.item_Support_Count.size();
		if (i > index2)
			break;
	}
	//Removes
	compare.Lists.get(index).item_Support_Count.remove(index2 - j);
	if (compare.Lists.get(index).item_Support_Count.size() == 0)
		compare.Lists.remove(index);
	int s12 = current.Lists.size();
	int s22 = compare.Lists.size();
	int l12 = current.getItem_Support_Count().size();
	int l22 = compare.getItem_Support_Count().size();
	if (current.containedIn(compare) && s12 == s22 && l12 == l22)
		result = true;
	return result;
}

/*
 * test whether the item with number of itemId has the lowest MIS in current
 * Sequence. Flag is the value to denote if the item is the firstItem found
 * or the LastItem found in the sequence.
 */
	public boolean isSmallest(Integer itemId, int flag) {
		boolean result = true;
		ArrayList<Integer> item_Support_Count = this.getItem_Support_Count();
		if (flag == 0)
			item_Support_Count.remove(0);
		else if (flag == 1)
			item_Support_Count.remove(item_Support_Count.size() - 1);
		for (Integer id : item_Support_Count) {
			if (MSGSPAlgorithm.MISValue_Of_Item.get(id).doubleValue() <= MSGSPAlgorithm.MISValue_Of_Item
					.get(itemId).doubleValue()) {
				result = false;
				break;
			}
		}
		return result;
	}



    public boolean equals(Object tr) {
            Sequence trans = (Sequence) tr;
            boolean result = false;
            if (this.containedIn(trans) && this.Lists.size() == trans.Lists.size() && this.getItem_Support_Count().size() == trans.getItem_Support_Count().size())
                    result = true;
            return result;
    }

}
