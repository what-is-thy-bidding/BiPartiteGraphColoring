import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

public class RandomGraphs {
	
	
	public static void writeToFile(int graphNumber, int numberOfNodes, int numberOfEdges, boolean[][] graph) throws IOException {
		System.out.println("Graph Number " + graphNumber + " , Number Of Nodes " + numberOfNodes + " , Number Of Edges = " +numberOfEdges);
		int EdgesWritten=0;
		
		PrintStream  file= new PrintStream ("RandomGraph"+graphNumber+".txt");
		
		file.println("% Graph Number " + graphNumber + " , Number Of Nodes " + numberOfNodes + " , Number Of Edges = " +numberOfEdges);
		
		file.println(numberOfNodes + " "+ numberOfNodes + " " +numberOfEdges);
		
		for(int i=0;i<graph.length;i++) {
			for(int j=0;j<graph[0].length;j++) {
				if(i==j) {
					break;
				}else {
					if(graph[i][j]==true && graph[j][i]==true) {
						file.println( (i+1) + " "+(j+1));
						EdgesWritten++;
					}
				}
			}
		}
		file.close();
		System.out.println("Edges Written = "+EdgesWritten);
		
	}
	
	public static void createGraph() throws IOException {
		
		// # of nodes= 50 , 100, 200 ,300 ,400
	
		int []numberOfNodes= {25,50,100,200,400};
	
		for(int i=0;i<numberOfNodes.length;i++) {
		
			//System.out.println(numberOfNodes[i]);
		
			// divide the number of nodes by half/ 2 sets = U & V.
		
			boolean [][] Graph=new boolean[numberOfNodes[i]][numberOfNodes[i]];
			Random rand= new Random();
			int numberOfEdges=0;
			
			for(int j=0;j<Graph.length;j++) {
				for(int k=0;k<Graph[0].length;k++) {
					if(j!=k) {
						int answer=rand.nextInt(11) +1; // Random number between 1 and 11
														// P(odd) = 3/5, P(even)=2/5
						
						if(answer%2==0) { // If even, then edge exists
							Graph[j][k]=true;
							Graph[k][j]=true;
							numberOfEdges++;
						}
						
					}else {
						break;
					}
				}
			}
			
			writeToFile((i+1), numberOfNodes[i], numberOfEdges, Graph);		
		}
		
	}
	
	public static void print2Darray(boolean[][] array) {
		int row=0;
		System.out.println();
		for(int i=0;i<array[0].length;i++) {
			System.out.format("%10d", i+1);
		}
			System.out.println();
		for(boolean t[]: array) {
			
			System.out.format("%1d",row++ +1);
			for(boolean b:t) {
				System.out.format("%10b", b );
			}
			System.out.println();
		}
	}
	
	
	public static void main(String args[]) throws IOException {
		createGraph();
	}
}
