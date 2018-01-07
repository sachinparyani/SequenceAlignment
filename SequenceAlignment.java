import java.io.*;
import java.util.*;

class SequenceAlignment{
	
	//static TreeSet<DovetailAlignmentFinder> kMaxAlignments;
	
	public static void writeScore(int gld, int score, int k) throws IOException{
		FileWriter fileWriter;
		if(gld == 1)
			fileWriter = new FileWriter(new File("global"+Integer.toString(k)+"score.txt"), true);
		
		else if(gld == 2)
			fileWriter = new FileWriter(new File("local"+Integer.toString(k)+"score.txt"), true);
		else 
			fileWriter = new FileWriter(new File("dovetail"+Integer.toString(k)+"score.txt"), true);
		
		PrintWriter printWriter = new PrintWriter(fileWriter);
		
		printWriter.println(Integer.toString(score));
		
		
		printWriter.close();
	}
	
	public static void writeSequenceLengthRunningTime(int gld, TreeMap<Integer,Long> seqLengthRunningTimeMap) throws IOException{
		
		FileWriter fileWriter;
		PrintWriter printWriter; 
		
		if(gld == 1)
				fileWriter = new FileWriter(new File("global_running_time.txt"), true);
		
		else if(gld == 2)
			fileWriter = new FileWriter(new File("local_running_time.txt"), true);
			
		else 
			fileWriter = new FileWriter(new File("dovetail_running_time.txt"), true);
			
		printWriter = new PrintWriter(fileWriter);	
		for(Map.Entry<Integer,Long> entry : seqLengthRunningTimeMap.entrySet()) {
			
			int seqlength = entry.getKey();
			long runningTime = entry.getValue();
			//System.out.println(key + " => " + value);				
		
			
			printWriter.println(Integer.toString(seqlength)+" "+Long.toString(runningTime) );
			
		}
		
		printWriter.close();
		
	}
	public static void main(String args[]){
		
		int gld=  Integer.parseInt(args[0]);
		String queryfilename= args[1];
		String dbfilename = args[2];
		String alphabetfilename = args[3];
		String scorematrixfilename = args[4];
		int k = Integer.parseInt(args[5]);
		int penalty = Integer.parseInt(args[6]);
		
		String charSet="";
		File file = new File(alphabetfilename+".txt");
		try {

			Scanner sc = new Scanner(file);
			charSet = sc.next();
			sc.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	
		int scoreMatrix[][] = new int[charSet.length()][charSet.length()];
		
		file = new File(scorematrixfilename+".txt");
		
		try{
			Scanner sc = new Scanner(file);
			for(int i =0; i< charSet.length();i++)
			{
				for(int j =0; j < charSet.length(); j++)
				{
					scoreMatrix[i][j] = sc.nextInt();
				}
			}
			sc.close();	
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		ArrayList<FastaSequence> seq1List = new ArrayList<FastaSequence>();
		ArrayList<FastaSequence> seq2List = new ArrayList<FastaSequence>();
		
		
		for(int var = 0; var <2 ;var++)
		{
			
			file = new File(queryfilename+".txt");
		
			if(var == 1)
				file = new File(dbfilename+".txt");
		
			try{
				Scanner sc = new Scanner(file);
				String id ="";
				String seq ="";
				while(sc.hasNextLine()){
					//System.out.println("Reading Line");
					String line = sc.nextLine();
					
					if(line.charAt(0)=='>'){
						
						if(!seq.isEmpty() && !id.isEmpty())
						{
							if(var == 0 )
								seq1List.add(new FastaSequence(id,seq ));
						
							else 
								seq2List.add(new FastaSequence(id,seq ));
						}
						
						seq = "";
						id ="";
						
						char ch = line.charAt(0);
						int i =0;
						while(ch!=' '){
							
							//System.out.println("Reading character "+ ch);
							if(Character.isDigit(ch))
							{
								id+= ch;
							}
							i++;
							ch = line.charAt(i);
						}
						
						
					}
					
					else{
						seq+=line;
					}
				}
				
				if(var ==0 )
					seq1List.add(new FastaSequence(id,seq ));
						
				else 
					seq2List.add(new FastaSequence(id,seq ));
				
				sc.close();	
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		}
		
		/*
		for(int i =0; i<seq1List.size(); i++)
		{
			System.out.println(seq1List.get(i).id+" " + seq1List.get(i).seq);
		}*/
		
		
		
	
		AlignmentFinder finder;
		TreeSet<AlignmentFinder> kMaxAlignments;
		kMaxAlignments = new TreeSet<AlignmentFinder>(new AlignmentComp());
		
		/*if(gld == 1)
			kMaxAlignments = new TreeSet<GlobalAlignmentFinder>(new AlignmentComp());
		
		else if(gld == 2)
			kMaxAlignments = new TreeSet<LocalAlignmentFinder>(new AlignmentComp());
		
		else
			kMaxAlignments = new TreeSet<DovetailAlignmentFinder>(new AlignmentComp());
		*/
		//TreeSet<DovetailAlignmentFinder> kMaxAlignments = new TreeSet<DovetailAlignmentFinder>(new AlignmentComp());
		
		//System.out.println(charSet);
		
		TreeMap<Integer,Long> seqLengthRunningTimeMap = new TreeMap<Integer, Long>();
		
		
		for(int i = 0; i < seq1List.size(); i++){
			
			long startTime, endTime, totalTime=0;
			
			for(int j =0; j < seq2List.size();j++){
				
				startTime = System.currentTimeMillis();
				if(gld == 1)
					finder = new GlobalAlignmentFinder(seq1List.get(i).seq, seq2List.get(j).seq, charSet, penalty, scoreMatrix, seq1List.get(i).id, seq2List.get(j).id);
				else if(gld == 2)
					finder = new LocalAlignmentFinder(seq1List.get(i).seq, seq2List.get(j).seq, charSet, penalty, scoreMatrix, seq1List.get(i).id, seq2List.get(j).id);
				else
					finder = new DovetailAlignmentFinder(seq1List.get(i).seq, seq2List.get(j).seq, charSet, penalty, scoreMatrix, seq1List.get(i).id, seq2List.get(j).id);
				
				finder.helper();
				
				
				endTime = System.currentTimeMillis();
				totalTime += endTime - startTime;
				
				if(kMaxAlignments.size() != k)
				{
					kMaxAlignments.add(finder);
				}
				
				else
				{
					if(kMaxAlignments.last().getScore() < finder.getScore()){
						kMaxAlignments.pollLast();
						
						kMaxAlignments.add(finder);
					}
					
				}
			}
			
			seqLengthRunningTimeMap.put(seq1List.get(i).seq.length(),totalTime);
			
		}
		try{
			writeSequenceLengthRunningTime(gld, seqLengthRunningTimeMap);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
		ArrayList<String> sequenceList = new ArrayList<String>();
		ArrayList<Integer> startingPoints = new ArrayList<Integer>();
		ArrayList<String> idList = new ArrayList<String>();
		int score;
		
		while(!kMaxAlignments.isEmpty()){
			finder = kMaxAlignments.pollFirst();
			sequenceList = finder.getSequences();
			startingPoints = finder.getStartingPoints();
			score = finder.getScore();
			idList = finder.getIds();
			
			try{
				writeScore(gld, score, k);
			}
			
			catch (IOException e){
				e.printStackTrace();
			}
			
			
		}
	
	}
}

class AlignmentComp implements Comparator<AlignmentFinder>{
    public int compare(AlignmentFinder finder1, AlignmentFinder finder2) {
        if(finder1.getScore() < finder2.getScore()){
            return 1;
        } else {
            return -1;
        }
    }
}

class FastaSequence{
	String id;
	String seq;
	
	FastaSequence(String id, String seq)
	{
		this.id = id;
		this.seq = seq;
	}
}