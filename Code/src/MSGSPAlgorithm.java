
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class MSGSPAlgorithm {

	
	// HashMap to score item number and subsequent MIS values as per transaction
public static HashMap<Integer, Float> MISValue_Of_Item;

// Total number of Transactions as per in the data.txt file
public static int N;

/*
 * The variable seq is used to store the transactions read from the data.txt
 * file. Each element in a Sequence corresponds to one Transaction in each
 * file.
 */
public ArrayList<Sequence> seq;

// Support Difference Constraint for eliminating very high and very low MIS
// values occuring in the same Frequent
// Pattern itemSet
public static double Support_Diff_Constraint;
/*
 * HashMap to store the different items with their corresponding support
 * counts for later stage comparison
 */
public static HashMap<Integer, Integer> Support_Count = new HashMap<Integer, Integer>();

/*
 * MSGSP Algorithm Class Constructor - Reads and initializes para.txt and
 * data.txt filename
 */
MSGSPAlgorithm(String paraFileName, String dataFileName) {
	MISValue_Of_Item = ReadParaFile(paraFileName);
	seq = readData(dataFileName);
}

public void main() {

	LinkedList<Integer> M = sort(MISValue_Of_Item); // according to
// MIS(i)'s stored
// in
// MISValue_Of_Item

ArrayList<Integer> L = initPass(M); // make the first over S

// ArrayList to store the union of all Frequent ItemSets
ArrayList<Freq> F = new ArrayList<Freq>();

F.add(new Freq());

F.add(initPrune(L)); // obtain F1 from L

GenerateCandidates gen = new GenerateCandidates(); // Define a new
// instance of
// GenerateCandidates
// class

Freq Fk_1;
for (int k = 2; !(Fk_1 = F.get(k - 1)).FrSeq.isEmpty(); k++) {
	Freq Ck;
	if (k == 2)
		Ck = gen.level2GenerateCandidateKeys(L);
	else
		Ck = gen.GenerateCandidateKeys(Fk_1);

	for (Sequence s : seq)//iterate over the initial transaction
		for (Sequence c : Ck.FrSeq) {//iterate over the previously generated candidate set
			if (c.containedIn(s))//check if the candidate set is present in the initial transaction
				c.count++;//If present,increment the count
		}

	Freq Fk = new Freq();

	for (Sequence c : Ck.FrSeq) {
		ArrayList<Integer> item_Support_Count = c
				.getItem_Support_Count();
		int item = 0;
		for (int i = 1; i < item_Support_Count.size(); i++) {
			if (MSGSPAlgorithm.MISValue_Of_Item
					.get(item_Support_Count.get(i)) < MSGSPAlgorithm.MISValue_Of_Item
					.get(item_Support_Count.get(item)))
				item = i;
		}
		int minItem = item;
		if (c.count >= MISValue_Of_Item.get(c.getItem_Support_Count()
				.get(minItem)) * N)
			Fk.FrSeq.add(c);
	}
	F.add(Fk);
}
F.remove(F.size() - 1);

// Print F
int k = 0;
while (++k < F.size()) {
	Freq Fk = F.get(k);
	System.out.println("The number of length " + k + "-Sequential patterns is  "+ Fk.FrSeq.size());
System.out.println();
for (Sequence tran : Fk.FrSeq) {
	int i;
	System.out.print("              Pattern:<");
for (List is : tran.Lists) {
	System.out.print("{");
for (i = 0; i < is.item_Support_Count.size() - 1; i++) { // print
				// an
				// element
				// except
				// the
				// last
				// item
System.out.print(is.item_Support_Count.get(i) + ",");
}
System.out.print(is.item_Support_Count.get(i) + "}"); // print
			// the
			// last
			// item
			// in
			// an
			// element
}
System.out.println("> count: " + tran.count);
}
System.out.println();

		System.out.println();
	}

	
writeToOutputFile(F);
}

/*
 * InitPass method as explained in book. Reads the list of items from
 * previous methods. Generates SeedSet L by comparing first item to satisfy
 * supportCount /n >= MIS Value. Subsequent items to follow conditions that
 * MIS value(item) >= MIS value of first item.
 */
public ArrayList<Integer> initPass(LinkedList<Integer> SortedList) {
	ArrayList<Integer> L = new ArrayList<Integer>();
	Iterator<Integer> it = SortedList.iterator(); // get the iterator of
// MISValue_Of_Item's
// key set
for (Integer i : MISValue_Of_Item.keySet()) { // initialize
// Support_Count, set
// each item's
// Support_Countport
// count to 0
	Support_Count.put(i, new Integer(0));
}

for (Sequence tran : seq) {
	// Read Transaction from file
N++;
HashSet<Integer> item_Support_Count = tran
		.getItem_Support_CountAsSet(); // get all the
// item_Support_Count
// contained in current
// Sequence
for (Integer id : item_Support_Count) { // add 1 to the
// Support_Countport
// count for each item
		Integer count = Support_Count.get(id);
		Support_Count.put(id, new Integer(count.intValue() + 1));
	}
}
// List of steps to find the first item which satisfies condition
// item.SupportCount/n >=MIS(item)
Integer minId = null; // used to store the id of the first item who
// meets its MIS
while (it.hasNext()) { // find the first item who meets its MIS
Integer itemId = it.next();
if (Support_Count.get(itemId) * 1.0 / N >= MISValue_Of_Item.get(
		itemId).floatValue()) {
	minId = itemId;
	L.add(itemId);
	// Break the loop once the condition is satisfied
		break;
	}
}
while (it.hasNext()) {
	// Generate the remaining of SeedSet by comparing
// item.SupportCount/n >= MIS value of item found in
// previous statement.
		Integer itemId = it.next();
		if (Support_Count.get(itemId) * 1.0 / N >= MISValue_Of_Item.get(
				minId).floatValue()) {
			L.add(itemId);
		}
	}
	return L;
}

/*
 * After reading para.txt file MIS values need to be sorted in ascending
 * order. This method performs the sorting operation.
 */
public LinkedList<Integer> sort(HashMap<Integer, Float> MISValue_Of_Item) {
	LinkedList<Integer> M = new LinkedList<Integer>();
	for (Integer itemID : MISValue_Of_Item.keySet()) {
		if (M.size() == 0) // Initial check to see if the linked list is empty
			M.add(itemID);// add first item to to linked from hashmap.
		else {
			int i = 0;
			while (i < M.size() //First element has an MIS value less than all other elements.
					&& MISValue_Of_Item.get(M.get(i)) < MISValue_Of_Item
							.get(itemID))
				i++;
			M.add(i, itemID); // i is the index at which itemID is inserted.
		}
	}
	return M;
}

/*
 * Once SeedSet L is generated, perform Prune to Generate One ItemSet
 * Frequent Patterns Check item.supportCount/ N >= MIS(item)
 */
public Freq initPrune(ArrayList<Integer> L) {
	Freq FreqSet1 = new Freq();
	for (Integer itemId : L) {
		if (Support_Count.get(itemId) * 1.0 / N >= MISValue_Of_Item.get(
				itemId).floatValue()) {
			//Create a new ItemsSet variable to store the current item
			List temp = new List();
			//Add it to the ItemSet created
			temp.item_Support_Count.add(itemId);
			Sequence tran = new Sequence();
			tran.Lists.add(temp);
			//Get the support count of item from the original HashMap
			tran.count = Support_Count.get(itemId);
			FreqSet1.AddListsToSequence(tran);
		}
	}
	return FreqSet1;
}

// Main function to provide parameters of data.txt and Para.txt files
public static void main(String[] args) {
	//new MSGSPAlgorithm("F:/Data Mining Project/test-data/large-data-2/para2-1.txt", "F:/Data Mining Project/test-data/large-data-2/data2.txt").main();
	new MSGSPAlgorithm("F:/Data Mining Project/test-data/large-data-2/para2-2.txt", "F:/Data Mining Project/test-data/large-data-2/data2.txt").main();
	//new MSGSPAlgorithm("F:/Data Mining Project/test-data/small-data-1/para1-2.txt", "F:/Data Mining Project/test-data/small-data-1/data-1.txt").main();
	//new MSGSPAlgorithm("F:/Data Mining Project/test-data/small-data-1/para1-1.txt", "F:/Data Mining Project/test-data/small-data-1/data-1.txt").main();
}


public static void writeToOutputFile(ArrayList<Freq> F)
{
	
	try{
		
	File file = new File("F:/output.txt");
	FileWriter fw = new FileWriter(file.getAbsoluteFile());
	BufferedWriter bw = new BufferedWriter(fw);
	
	int k = 0;
	while (++k < F.size()) {
		Freq Fk = F.get(k);
		bw.write("\n\n");
		bw.write("The number of length " + k + "-Sequential patterns is  "+ Fk.FrSeq.size());
	bw.write("\n");
	for (Sequence tran : Fk.FrSeq) {
		int i;
		bw.write("  Pattern :<");
	for (List is : tran.Lists) {
		bw.write("{");
	for (i = 0; i < is.item_Support_Count.size() - 1; i++) { // print
					// an
					// element
					// except
					// the
					// last
					// item
		bw.write(is.item_Support_Count.get(i) + ",");
	}
	bw.write(is.item_Support_Count.get(i) + "}"); // print
				// the
				// last
				// item
				// in
				// an
				// element
	}
	bw.write("> count: " + tran.count);
	bw.write("\n");
	}
	
	
	
		}
	bw.close();

	}catch(Exception e)
	{
		e.printStackTrace();
	}
	
}
/*
 * Method to read content from data.txt, reads all transactions and stores
 * them in an ArrayList to subsequently generate SeedSet L
 */
public static ArrayList<Sequence> readData(String filename) {
	ArrayList<Sequence> seq = new ArrayList<Sequence>();
	try {
		File file = new File(filename);
		BufferedReader f = new BufferedReader(new FileReader(file));
		String record = f.readLine();
		while (record != null) {
			// Create a new Sequence for each line in the data.txt file
Sequence s = new Sequence();

// Record index positions of open and closed brackets to read
// itemsets
int openParen = record.indexOf('{');
int closedParen = record.indexOf('}');

while (openParen < record.length() - 1) {
	List ls = new List(); // create an new
// item_Support_Countet for this
// transaction
// find occurence of comma for the given open brackets
int comma = record.indexOf(',', openParen);

// Next number occurs after a comma - record index position
// of the next number.
int start = openParen + 1;
// index indicating the ending position of next number
// within current parentheses. The character on this
// position is either a '}' or ','.
int end = 0;
if (comma < 0)
	end = closedParen;
else {
	// Indicates that the sequence is ending and there is no
// comma.
if (closedParen < comma)
	end = closedParen;

// If comma exists then, traverse for next number.
	else
		end = comma;
}

// To extract the items between the comma and the closed
// brackets
while (end <= closedParen) {
	ls.item_Support_Count.add(Integer.valueOf(record
			.substring(start, end).trim())); // add an item
				// to the
				// item_Support_Countet

// Reached the end of an itemset
if (end == closedParen)
	break;
// locate beginning and ending positions for next item
// in the current transaction
start = comma + 2;
comma = record.indexOf(',', start);

	if (comma < 0)
		end = closedParen;
	else {
		if (closedParen < comma)
			end = closedParen;
		else
			end = comma;
	}
}

// locate beginning and ending positions for next item in
// the current transaction
openParen = closedParen + 1;
closedParen = record.indexOf('}', openParen);
// item number extracted. Add to the List of Items
	s.Lists.add(ls);

}

seq.add(s);
// Add the transaction

			record = f.readLine();
		}
		return seq;
	} catch (IOException e) {
		e.printStackTrace();
	}
	return null;
}

/*
 * Method to read from para file and to store items and MIS values in a
 * HashMap
 */
public static HashMap<Integer, Float> ReadParaFile(String filename) {
	HashMap<Integer, Float> mis = new HashMap<Integer, Float>();
	try {
		Scanner f = new Scanner(new File(filename));
		while (true) {
			// read each line in parameter-file.txt
String record = f.nextLine();
if (f.hasNextLine()) {
	// Find index of open and closed parenthesis to leave only
// itemID
Integer item = Integer.valueOf(record.substring(
		record.indexOf('(') + 1, record.indexOf(')')));

// Once item number is found, delete remaining characters to
// extract MIS value.
Float itemMIS = Float.valueOf(record.substring(record
		.indexOf('=') + 2));
	mis.put(item, itemMIS);
} else {

	// Read final line value from SDC.
MSGSPAlgorithm.Support_Diff_Constraint = Float
		.valueOf(record.substring(record.indexOf('=') + 2));
					break;
				}

			}
			return mis;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


}
