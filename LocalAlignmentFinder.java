import java.util.*;

class LocalAlignmentFinder extends AlignmentFinder
{
	
	public LocalAlignmentFinder(String seq1, String seq2, String charSet, int penalty, int scoreMatrix[][], String id1, String id2){
		this.seq1 = seq1;
		this.seq2 = seq2;
		this.charSet = charSet;
		this.penalty = penalty;
		this.scoreMatrix = scoreMatrix;
		seq1id = id1;
		seq2id = id2;
	}

	
	private void findMaxAndEP(int matrix[][], Character backTrackingMatrix[][])
	{
		int max=0;
		seq1endingpoint=0;
		seq1endingpoint=0;
		for(int i =0; i < matrix.length; i++)
		{
			for(int j = 0; j < matrix[0].length;j++){
				
				if(matrix[i][j] > max){
					max = matrix[i][j];
					seq1endingpoint=j;
					seq2endingpoint=i;
				}
			}
			
			//System.out.println();
		}
		
		finalscore = max;
	}
	
	
		
	public void helper()
	{
		HashMap<Character, Integer> charIndexMap_ = new HashMap<Character, Integer>();
		
		charIndexMap = charIndexMap_;
		
		//System.out.println(charSet);
		
		for(int i =0; i<charSet.length();i++)
		{
			charIndexMap_.put(charSet.charAt(i),i);
		}
	
		
		int matrix[][] = new int[seq2.length()+1][seq1.length()+1];
		Character BacktrackingMatrix[][]= new Character[seq2.length()+1][seq1.length()+1];
			
		/* Filling the first row with 0s */
		for(int i =0; i<seq1.length()+1;i++)
		{
			matrix[0][i]=0;
		}
		
		/* Filling the first column with 0s */
		for(int i =1; i<seq2.length()+1;i++)
		{
			matrix[i][0]=0;
		}
		
		int left, top, diagonal;
		
		for(int i =1; i < seq2.length()+1; i++)
		{
			for(int j =1; j <seq1.length()+1; j++)
			{
				left = matrix[i][j-1] + score(null, seq2.charAt(i-1));
				top = matrix[i-1][j] + score(seq1.charAt(j-1), null);
				diagonal = matrix[i-1][j-1] + score(seq1.charAt(j-1), seq2.charAt(i-1));
			
				
				if((left >= top) && (left >= diagonal) && (left >= 0)){
					matrix[i][j] =left;
					BacktrackingMatrix[i][j]='L';
				}
				else if((top >= left) && (top >= diagonal) && (top >= 0))
				{
					matrix[i][j] =top;
					BacktrackingMatrix[i][j]='T';
				}
				else if((diagonal >= left) && (diagonal >= top) && (diagonal >= 0))
				{
					matrix[i][j] =diagonal;
					BacktrackingMatrix[i][j]='D';
				}
				else{
					matrix[i][j] =0;
					BacktrackingMatrix[i][j]='Z';
				}
				
				//if(i==3 && j ==2)
					//System.out.println(left+" " + top+ " "+ diagonal + " "+ matrix[i][j]);
					
 			}
		}
		
		
		findMaxAndEP(matrix, BacktrackingMatrix);
		
		//System.out.println(BacktrackingMatrix[seq][]
		
		int x = seq1endingpoint, y= seq2endingpoint;
		Stack<Character> seq1stack = new Stack<Character>();
		Stack<Character> seq2stack = new Stack<Character>();
		
		
		while(matrix[y][x] != 0)
		{
			if(BacktrackingMatrix[y][x] =='L'){
					seq1stack.push(seq1.charAt(x-1));
					seq2stack.push('.');
					x -= 1;		
				}
				
				else if(BacktrackingMatrix[y][x] =='T'){
					seq1stack.push('.');
					seq2stack.push(seq2.charAt(y-1));
					y -= 1;		
				}
				
				else{
					seq1stack.push(seq1.charAt(x-1));
					seq2stack.push(seq2.charAt(y-1));
					x -= 1;	
					y -= 1;
				}
		}
		
		seq1startingpoint = x+1;
		seq2startingpoint = y+1;
		seq1subsequence ="";
		seq2subsequence ="";
		
		//System.out.print(seq1startingpoint+" ");
		while(!seq1stack.empty())
		{
			//System.out.print(seq1stack.pop());
			char ch = seq1stack.pop();
			
			if(ch!='.')
				seq1subsequence += Character.toUpperCase(ch);
		
			else
				seq1subsequence += ch;
		}
		
		//System.out.print(seq2startingpoint+" ");
		
		while(!seq2stack.empty())
		{
			//System.out.print(seq2stack.pop());
			
			char ch = seq2stack.pop();
			
			if(ch!='.')
				seq2subsequence += Character.toUpperCase(ch);
		
			else
				seq2subsequence += ch;
		}
		
		//System.out.println(seq1subsequence+" "+seq2subsequence);
		
	}
}	

