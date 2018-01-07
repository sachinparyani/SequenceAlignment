import java.util.*;

class GlobalAlignmentFinder extends AlignmentFinder
{
	
	public GlobalAlignmentFinder(String seq1, String seq2, String charSet, int penalty, int scoreMatrix[][], String id1, String id2){
		this.seq1 = seq1;
		this.seq2 = seq2;
		this.charSet = charSet;
		this.penalty = penalty;
		this.scoreMatrix = scoreMatrix;
		seq1id = id1;
		seq2id = id2;
	}
	

	public void helper()
	{
		HashMap<Character, Integer> charIndexMap_ = new HashMap<Character, Integer>();
		
		charIndexMap = charIndexMap_;
		
		for(int i =0; i<charSet.length();i++)
		{
			charIndexMap_.put(charSet.charAt(i),i);
		}
		
		
		int matrix[][] = new int[seq2.length()+1][seq1.length()+1];
		Character BacktrackingMatrix[][]= new Character[seq2.length()+1][seq1.length()+1];
			
		
		for(int i =0; i<seq1.length()+1;i++)
		{
			matrix[0][i] = i*penalty;
		}
		
		
		for(int i =1; i<seq2.length()+1;i++)
		{
			matrix[i][0] = i*penalty;
		}
		
		int left, top, diagonal;

		for(int i =0; i <seq1.length()+1; i++)
		{
			BacktrackingMatrix[0][i] = 'L';
		}
		
		for(int i =0; i <seq2.length()+1; i++)
		{
			BacktrackingMatrix[i][0] = 'T';
		}
		
		for(int i =1; i < seq2.length()+1; i++)
		{
			for(int j =1; j <seq1.length()+1; j++)
			{
				left = matrix[i][j-1] + score(null, seq2.charAt(i-1));
				top = matrix[i-1][j] + score(seq1.charAt(j-1), null);
				diagonal = matrix[i-1][j-1] + score(seq1.charAt(j-1), seq2.charAt(i-1));
			
				
				if((left >= top) && (left >= diagonal)){
					matrix[i][j] =left;
					BacktrackingMatrix[i][j]='L';
				}
				else if((top >= left) && (top >= diagonal))
				{
					matrix[i][j] =top;
					BacktrackingMatrix[i][j]='T';
				}
				else
				{
					matrix[i][j] =diagonal;
					BacktrackingMatrix[i][j]='D';
				}
				
				//if(i==3 && j ==2)
					//System.out.println(left+" " + top+ " "+ diagonal + " "+ matrix[i][j]);
					
 			}
		}
		
		//printMatrix(matrix);
		finalscore = matrix[seq2.length()][seq1.length()];
		
		int x = seq1.length();
		int y = seq2.length();
		Stack<Character> seq1stack = new Stack<Character>();
		Stack<Character> seq2stack = new Stack<Character>();
		
		//System.out.println(BacktrackingMatrix[1][0]);
		while(x!=0 || y!=0 )
		{
			//System.out.println(x+" "+y);
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
		
		seq1startingpoint =x+1;
		seq2startingpoint =y+1;	
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
	}	
		
		
}