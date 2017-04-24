package clzola.lightsout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Model {
	public short[][] MATRIX;
	public short[][] SAVE_MATRIX;
	public short M;
	public short N;
	public short[] solutionVector;
	public short best;
	
	public static short SIZE = 48;
	public static short GAP = 4;
	
	public Rectangle[][] MRect;
	
	public boolean solve = false;
	
	public Model(short M, short N) {
		MATRIX = new short [M][N];
		solutionVector = null;
		
		this.M = M;
		this.N = N;
		
		GenerateSolvableMatrix(M, N);
	}
	
	private void GenerateSolvableMatrix(short M, short N) {
		Random random = new Random();
		SAVE_MATRIX = new short [M][N];
		MRect = new Rectangle[M][N];
		
		for(int i=0; i<M; i++) 
			for(int j=0; j<N; j++) 
				MRect[i][j] = new Rectangle(0, 0, SIZE, SIZE);
		
		do {
			solutionVector = null;
			int brojac = random.nextInt(M*N-M)+M;
			
			for(int i=0; i<M; i++) {
				for(int j=0; j<N; j++) {
					MATRIX[i][j] = 0;
					SAVE_MATRIX[i][j] = 0;
				}
			}
			
			while( brojac > 0 ) {
				int iRow = random.nextInt(M);
				int iCol = random.nextInt(N);
				
				if( MATRIX[iRow][iCol] == 0 ) {
					MATRIX[iRow][iCol] = 1;
					SAVE_MATRIX[iRow][iCol] = 1;
					brojac--;
				}
			}
			
			FindSolution();
		} while ( solutionVector == null );
	}
	
	private short[] GenerateToggleVector(short M, short N, short i, short j) {
		short[][] TMatrix = new short[M][N];
	    
	    TMatrix[i][j] = 1;
	    if( i > 0 )   TMatrix[i-1][j] = 1;
	    if( i < M-1 ) TMatrix[i+1][j] = 1;
	    if( j > 0 )   TMatrix[i][j-1] = 1;
	    if( j < N-1 ) TMatrix[i][j+1] = 1;
	    
	    short[] resultVector = new short [M*N];
	    for(int ii=0; ii<M; ii++) {
	        for(int jj=0; jj<N; jj++)
	            resultVector[ii*N+jj] = TMatrix[ii][jj];
	    }
	    
	    return resultVector;
	}
	
	private short[][] MakeToggleMatrix(short M, short N) {
		short[][] result = new short [M*N][M*N];
	    
	    for(short i=0; i<M; i++) {
	        for(short j=0; j<N; j++) {
	            short[] toggleVector = GenerateToggleVector(M, N, i, j);
	            
	            for(int k=0; k<M*N; k++)
	                result[k][i*N+j] = toggleVector[k];
	            
	        }
	    }
	    
	    return result; 
	}
	
	private short[] getConfigVector(short[][] g, short M, short N) {
	    short[] resultVector = new short [M*N];
	    for(int i=0; i<M; i++)
	        for(int j=0; j<N; j++)
	            resultVector[i*N+j] = g[i][j];
	    return resultVector;
	}
	
	private short FindPivot(short[][] matrix, short M, short N, short startRow, short column)  {
	    for(short row = startRow; row < M*N; row++)
	        if(matrix[row][column] == 1)
	            return row;
	    return -1;
	}
	
	private void SwapRows(short[][] matrix, short M, short N, short row1, short row2)  {
	    for( short col=0; col<M*N; col++ ) {
	        short temp = matrix[row1][col];
	        matrix[row1][col] = matrix[row2][col];
	        matrix[row2][col] = temp;
	    }
	}
	
	private void PerformGaussianElimination(short[][] toggle, short[] puzzle, short M, short N) {
	    short nextFreeRow = 0;
	    for(short col = 0; col < M*N; col++) {
	        short pivotRow = FindPivot(toggle, M, N, nextFreeRow, col);
	        if( pivotRow == -1 )
	            continue;
	        
	        SwapRows(toggle, M, N, nextFreeRow, pivotRow);
	        
	        short v = puzzle[pivotRow];
	        puzzle[pivotRow] = puzzle[nextFreeRow];
	        puzzle[nextFreeRow] = v;
	        
	        for( short row = (short) (pivotRow + 1); row < M*N; row++ ) {
	            if( toggle[row][col] == 1 ) {
	                for(short j = 0; j<M*N; j++)
	                    toggle[row][j] = (short) ((toggle[nextFreeRow][j] + toggle[row][j]) % 2);
	                puzzle[row] = (short) ((puzzle[nextFreeRow] + puzzle[row]) % 2);
	            }
	        }
	        
	        nextFreeRow++;
	    }
	}
	
	short[] BackSubstitue(short[][] toggle, short[] puzzle, short M, short N) {
		short[] resultVector = new short [M*N];
	    for(short i=0; i<M*M; i++)
	        resultVector[i] = 0;
	        
	    for(short row = (short) (M*N); row-- != 0; ) {
	    	short pivot = -1;
	        
	        for(short col = 0; col < M*N; col++) {
	            if( toggle[row][col] == 1 ) {
	                pivot = col;
	                break;
	            }
	        }
	        
	        if( pivot == -1 ) 
	            if(puzzle[row] == 1)
	                return null;
	                
	        resultVector[row] = puzzle[row];
	        for(short col = (short) (pivot + 1); col < M*N; col++)
	            resultVector[row] = (short) ((resultVector[row] != (toggle[row][col] & resultVector[col])) == true ? 1 : 0);
	    }
	    
	    return resultVector;
	}
	
	public void FindSolution() {
		
		short[][] toggle = MakeToggleMatrix(M, N);
		short[] puzzleVector = getConfigVector(SAVE_MATRIX, M, N);
		
		PerformGaussianElimination(toggle, puzzleVector, M, N);
		this.solutionVector = BackSubstitue(toggle, puzzleVector, M, N);
		
		if(solutionVector != null) {
			best = 0;
			for(int i=0; i<solutionVector.length; i++)
				if( solutionVector[i] == 1 )
					best++;
		}
	}
	
	public void reset() {
		for(int i=0; i<M; i++)
			for(int j=0; j<N; j++)
				MATRIX[i][j] = SAVE_MATRIX[i][j];
		solve = false;
	}

	public void draw(Graphics g, int startx, int starty) {
		for(int i=0; i<M; i++)
			for(int j=0; j<N; j++) {
				if(MATRIX[i][j] == 1)
					g.setColor(Color.CYAN);
				else g.setColor(Color.BLUE);
				
				if( solve == true && solutionVector[i*N+j] == 1 )
					g.setColor(Color.RED);
				
				g.fillRect(startx + i*SIZE+i*GAP, starty + j*SIZE+j*GAP, SIZE, SIZE);
				MRect[i][j].width = MRect[i][j].height = SIZE;
				MRect[i][j].x = startx + i*SIZE+i*GAP;
				MRect[i][j].y = starty + j*SIZE+j*GAP;
			}
	}
	
}
