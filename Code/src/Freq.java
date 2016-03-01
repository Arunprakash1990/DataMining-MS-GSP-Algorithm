import java.util.HashSet;

/*
 * This class is eventually used to extract the SeedSet L, Frequent Patterns Fk and the Candidate Keys Ck
 */
public class Freq {
	public HashSet<Sequence> FrSeq = new HashSet<Sequence>();

	Freq() {
		FrSeq = new HashSet<Sequence>();
	}

	public void AddFreqsOutput(Freq fs) {
		FrSeq.addAll(fs.FrSeq);
	}

	Freq(HashSet<Sequence> sequence) {
		FrSeq.addAll(sequence);
	}

	public void AddListsToSequence(Sequence tran) {
		FrSeq.add(tran);
	}
}
