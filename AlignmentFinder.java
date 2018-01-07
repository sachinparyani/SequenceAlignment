import java.util.*;

abstract class AlignmentFinder
{
	String seq1, seq2;
	String charSet;
	int penalty;
	HashMap<Character, Integer> charIndexMap; 
	int scoreMatrix[][];
	int seq1endingpoint;
	int seq2endingpoint;
	int seq1startingpoint;
	int seq2startingpoint;
	String seq1id;
	String seq2id;
	int finalscore; 
	String seq1subsequence;
	String seq2subsequence;
	
	public int score(Character char1, Character char2){
		if(char1==null || char2 == null){
			return penalty;
		}
		
		else{
			//System.out.println(" "+charIndexMap.get(char1)+" "+char2+" "+charIndexMap.get(char2));
			return scoreMatrix[charIndexMap.get((char)(char1-32))][charIndexMap.get((char)(char2-32))];
		}	
	}
	
	public ArrayList<String> getSequences(){
		ArrayList<String> sequenceList = new ArrayList<String>();
		sequenceList.add(seq1subsequence);
		sequenceList.add(seq2subsequence);
		return sequenceList;
	}
	
	public ArrayList<Integer> getStartingPoints(){
		ArrayList<Integer> startingPoints = new ArrayList<Integer>();
		startingPoints.add(seq1startingpoint);
		startingPoints.add(seq2startingpoint);
		return startingPoints;
	}
	
	public ArrayList<String> getIds(){
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(seq1id);
		ids.add(seq2id);
		return ids;
	}
	
	public int getScore(){
		return finalscore;
	}
	
	public abstract void helper();
}