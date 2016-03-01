import java.util.ArrayList;

public class GenerateCandidates {
	
	//Method to generate the candidate keys for k> 2
	public Freq GenerateCandidateKeys(Freq P) {
		Freq C = new Freq();
		for (Sequence tr1 : P.FrSeq) {
			for (Sequence tr2 : P.FrSeq) {
				Sequence s1 = tr1.copy();
				Sequence s2 = tr2.copy();
				int CheckConditionResult = checkCheckConditionResult(s1, s2);
				if (CheckConditionResult != 0) {
					C.AddFreqsOutput(joinFrSeq(s1, s2, CheckConditionResult));
				}
			}
		}
		// The prune step checks if the F(k-1) Lists are frequent. If not found
// to be frequent, the sequences are pruned.
	return prune(C, P);
}

/*
 * This method is used to generate the possible combinations for Level 2
 * Candidate keys and then performing pruning togenerate the two frequent
 * item set sequence.
 */
public Freq level2GenerateCandidateKeys(ArrayList<Integer> L) {
	Freq Level2CandidateKeys = new Freq();
	/*
 * In Generating the candidate keys, see if the item Support_Countport
 * count is greater or equal to the Support_Countport count, add that
 * item to the candidate keys, and then later compare remaining
 * item_Support_Count with current MIS value.
 */
System.out.println(L);
for (int i = 0; i < L.size(); i++) {
	if (MSGSPAlgorithm.Support_Count.get(L.get(i)) * 1.0
			/ MSGSPAlgorithm.N < MSGSPAlgorithm.MISValue_Of_Item.get(
			L.get(i)).floatValue())
		continue;
	else {
		for (int j = i; j < L.size(); j++) {
			if (MSGSPAlgorithm.Support_Count.get(L.get(j)) * 1.0
					/ MSGSPAlgorithm.N >= MSGSPAlgorithm.MISValue_Of_Item
					.get(L.get(i)).floatValue()) {
				/*
 * Now that the Support_Count count is satisfied, to
 * generate the possible candidate sequences of the form
 * {a, b} or {a} {b} or {b} {a} and later to check if
 * the order is maintained.
 */
if (Math.abs(MSGSPAlgorithm.Support_Count.get(L.get(i))
		.intValue()
		- MSGSPAlgorithm.Support_Count.get(L.get(j))
				.intValue()) <= MSGSPAlgorithm.Support_Diff_Constraint
		* MSGSPAlgorithm.N) {
	List i1 = new List();
	// Create a new List and add the above generated
// sequence to the possible set of candidate keys.
if (L.get(i) <= L.get(j)) {
	i1.item_Support_Count.add(L.get(i));
	i1.item_Support_Count.add(L.get(j));
} else {
	i1.item_Support_Count.add(L.get(j));
	i1.item_Support_Count.add(L.get(i));
}
if (!L.get(i).equals(L.get(j))) {
	Sequence tr1 = new Sequence();
	tr1.Lists.add(i1);
	Level2CandidateKeys.AddListsToSequence(tr1); // Add
							// the
							// sequence
							// {a,
							// b}
							// to
							// the
							// frequent
							// pattern
							// List
}
List i2 = new List();
i2.item_Support_Count.add(L.get(i));
List i3 = new List();
i3.item_Support_Count.add(L.get(j));
Sequence tr2 = new Sequence();
tr2.Lists.add(i2);
tr2.Lists.add(i3);
Level2CandidateKeys.AddListsToSequence(tr2); // Next
						// set
						// of
						// possible
						// combination
						// -
						// {a}
						// {b}
						// to
						// the
						// possible
						// set
						// of
						// candidate
						// keys.
List i4 = new List();
i4.item_Support_Count.add(L.get(j));
List i5 = new List();
i5.item_Support_Count.add(L.get(i));
if (!L.get(i).equals(L.get(j))) {
	Sequence tr3 = new Sequence();
	tr3.Lists.add(i4);
	tr3.Lists.add(i5);
	// Add the final possible set of combinations to
// see that {b} {a} is also added to the
// Candidate keys
							Level2CandidateKeys.AddListsToSequence(tr3);
						}
					}
				}
			}
		}
	}

	return Level2CandidateKeys;
}

/*
 * Method to check if the Transactions s1 and s2 can be joined to generate a
 * candidate key.
 */
private int checkCheckConditionResult(Sequence tran1, Sequence tran2) {
	int i = 0;
	int result = 0;
	Integer firstItem = tran1.getFirstItem();
	Integer lastItem = tran1.getLastItem();
	//Check if the MIS value of the first item is smallest or the l
	if (tran1.isSmallest(firstItem, 0))
		i = 1;
	else if (tran1.isSmallest(lastItem, 1))
		i = 2;

	if (testPair(tran1, tran2, i))
		result = i;
	else if (testPair(tran1, tran2, 3))
		result = 3;
	return result;
}

/*
 * This method finds the corresponding join FrSeq for s1, s2, and s3.
 * Parameter tran is the Sequence in s1, s2, or s3, which will be found a
 * pair for. Parameter i indicates which of the three, s1, s2, and s3, does
 * tran belong to.
 * 
 * The below method will test
 */
private boolean testPair(Sequence tran, Sequence tr, int i) {
	boolean result = false;
	switch (i) {
	case 1:
		if (tran.specialEqualTo(tr, 1,
				tr.getItem_Support_Count().size() - 1)//mis(last_s2)>mis(first_s1)
				&& MSGSPAlgorithm.MISValue_Of_Item
						.get(tran.getFirstItem()).doubleValue() < MSGSPAlgorithm.MISValue_Of_Item
						.get(tr.getLastItem()).doubleValue()
				&& Math.abs(MSGSPAlgorithm.Support_Count.get(
						tran.getItem_Support_Count().get(1)).intValue()
						- MSGSPAlgorithm.Support_Count
								.get(tr.getLastItem()).intValue()) <= MSGSPAlgorithm.Support_Diff_Constraint
						* MSGSPAlgorithm.N)
			result = true;
		break;
	case 2:
		if (tran.specialEqualTo(tr,
				tran.getItem_Support_Count().size() - 2, 0)
				&& MSGSPAlgorithm.MISValue_Of_Item.get(tr.getFirstItem())
						.doubleValue() > MSGSPAlgorithm.MISValue_Of_Item
						.get(tran.getLastItem()).doubleValue()
				&& Math.abs(MSGSPAlgorithm.Support_Count.get(
						tran.getItem_Support_Count().get(
								tran.getItem_Support_Count().size() - 2))
						.intValue()
						- MSGSPAlgorithm.Support_Count.get(
								tr.getFirstItem()).intValue()) <= MSGSPAlgorithm.Support_Diff_Constraint
						* MSGSPAlgorithm.N)
			result = true;
		break;
	case 3:
		if (tran.specialEqualTo(tr, 0,
				tr.getItem_Support_Count().size() - 1)
				&& Math.abs(MSGSPAlgorithm.Support_Count.get(
						tran.getFirstItem()).intValue()
						- MSGSPAlgorithm.Support_Count
								.get(tr.getLastItem()).intValue()) <= MSGSPAlgorithm.Support_Diff_Constraint
						* MSGSPAlgorithm.N)
			result = true;
		break;
	}
	return result;
}

/*
 * 
 * This method is responsible for generating k-length frequent item-set
 * mining patterns.
 */
private Freq joinFrSeq(Sequence txn, Sequence seq, int i) {
	Freq freqSet = new Freq();
	Sequence CndSet = new Sequence();
	switch (i) {
	case 1:
		Sequence trans = txn.copy();
		if (seq.Lists.get(seq.Lists.size() - 1).item_Support_Count.size() == 1) {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.Lists);
			CndSet.Lists.add(seq.Lists.get(seq.Lists.size() - 1));
			freqSet.AddListsToSequence(CndSet);
			if (trans.Lists.size() == 2
					&& trans.getItem_Support_Count().size() == 2
					&& trans.getLastItem().toString()
							.compareTo(seq.getLastItem().toString()) < 0) {
				CndSet = new Sequence();
				CndSet.Lists.addAll(trans.copy().Lists);
				CndSet.Lists.get(CndSet.Lists.size() - 1).item_Support_Count
						.add(seq.copy().getLastItem());
				freqSet.AddListsToSequence(CndSet);
			}
		} else if (trans.getItem_Support_Count().size() > 2
				|| (trans.Lists.size() == 1
						&& trans.getItem_Support_Count().size() == 2 && trans
						.getLastItem().toString()
						.compareTo(seq.getLastItem().toString()) < 0)) {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.Lists);
			CndSet.Lists.get(CndSet.Lists.size() - 1).item_Support_Count
					.add(seq.getLastItem());
			freqSet.AddListsToSequence(CndSet);
		}

		break;
	case 2:
		trans = txn.copy();
		if (seq.reverse().Lists.get(seq.reverse().Lists.size() - 1).item_Support_Count
				.size() == 1) {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.reverse().Lists);
			CndSet.Lists.add(seq.reverse().Lists.get(seq.reverse().Lists
					.size() - 1));
			freqSet.AddListsToSequence(CndSet.reverse());
			if (trans.reverse().Lists.size() == 2
					&& trans.reverse().getItem_Support_Count().size() == 2
					&& trans.reverse()
							.getLastItem()
							.toString()
							.compareTo(
									seq.reverse().getLastItem().toString()) < 0) {
				CndSet = new Sequence();
				CndSet.Lists.addAll(trans.copy().reverse().Lists);
				CndSet.Lists.get(CndSet.Lists.size() - 1).item_Support_Count
						.add(seq.copy().reverse().getLastItem());
				freqSet.AddListsToSequence(CndSet.reverse());
			}
		} else if (trans.reverse().getItem_Support_Count().size() > 2
				|| (trans.reverse().Lists.size() == 1
						&& trans.reverse().getItem_Support_Count().size() == 2 && trans
						.reverse().getLastItem().toString()
						.compareTo(seq.reverse().getLastItem().toString()) < 0)) {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.reverse().Lists);
			CndSet.Lists.get(CndSet.Lists.size() - 1).item_Support_Count
					.add(seq.reverse().getLastItem());
			freqSet.AddListsToSequence(CndSet.reverse());
		}
		break;
	case 3:
		trans = txn.copy();
		if (seq.Lists.get(seq.Lists.size() - 1).item_Support_Count.size() == 1) {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.Lists);
			CndSet.Lists.add(seq.Lists.get(seq.Lists.size() - 1));
			freqSet.AddListsToSequence(CndSet);
		} else {
			CndSet = new Sequence();
			CndSet.Lists.addAll(trans.Lists);
			CndSet.Lists.get(CndSet.Lists.size() - 1).item_Support_Count
					.add(seq.getLastItem());
			freqSet.AddListsToSequence(CndSet);
		}

		break;
	}
	return freqSet;
}

/*
 * Prune Step to remove infrequent itemsets from the result
 */
private Freq prune(Freq freqSet, Freq FreqPattern) {
	Integer minItem; // Item that has the minimum MIS in a sequence
Freq PrunedResult = new Freq(); // Frequent sequence set after prune
// step
for (Sequence t : freqSet.FrSeq) {
	ArrayList<Integer> item_Support_Count = t.getItem_Support_Count();
	int item1 = 0;
	for (int i = 1; i < item_Support_Count.size(); i++) {
		if (MSGSPAlgorithm.MISValue_Of_Item.get(item_Support_Count
				.get(i)) < MSGSPAlgorithm.MISValue_Of_Item
				.get(item_Support_Count.get(item1)))
			item1 = i;
	}
	minItem = item1;
	// To mark if an item is frequent or not
boolean frequent = true;
for (int i = 0; i < t.Lists.size(); i++) {
	// For Loop to traverse through the ItemSets
if (t.Lists.get(i).item_Support_Count.contains(minItem)) { // if
							// the
							// List
							// contains
							// the
							// item
							// with
							// min
							// MIS
Sequence copy = t.copy();
for (int k = 0; k < t.Lists.size(); k++) {
	if (!frequent)
		break;
	for (Integer item : t.Lists.get(k).item_Support_Count) {
		if (!(k == i && item == minItem)) {
			// Do not compare the item with minimum MIS
// value
copy.Lists.get(k).item_Support_Count
		.remove(item); // generate a k-1
	// subsequence
if (!frequent(copy, FreqPattern)) {// if this
				// subsequence
				// is not
				// frequent,
				// then the
				// sequence
				// is not
				// frequent
				// either.
				frequent = false;
				break;
			}
		}
	}
}
// If found out that the item is already infrequent, reduce
// number of iterations by breaking the loop.
		if (!frequent)
			break;
	}
}
// Add obtained result to the Frequent Pattern Set
		if (frequent)
			PrunedResult.FrSeq.add(t);
	}
	return PrunedResult;
}

/*
 * After finding candidate keys, to compare them to see if they're frequent
 * or not.
 */
private boolean frequent(Sequence t, Freq fk_1) {
	boolean frequent = false;
	for (Sequence freq : fk_1.FrSeq)
		if (t.containedIn(freq)) {
			frequent = true; // there is one sequence in F(k-1) includes the
// subsequence
				break;
			}
		return frequent;
	}

}
