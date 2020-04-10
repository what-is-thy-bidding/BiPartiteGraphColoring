import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Main {
	
	public static boolean[][] AdjacencyMatrix=null;
	public static int Order[]= null;

	
	/*
    
	Undirected Graph Formats
    % comment l i n e 1
	% comment l i n e 2
	n n m
	u1 v1
	u2 v2
	...
	um vm
	
	More specifically, any line starting with % is a comment and should be ignored. The first
	line that does not start with % contains 3 integers n, n, m. The integer n is repeated twice
	and stands for the number of vertices |V |. The integer m stands for the number of edges |E|.
	The next m lines contain description of edges as pairs of vertices.
 * 
 * 
 * 	For example, the following
	example shows how a graph with 4 nodes and 6 edges {1, 2}, {1, 3}, {2, 3}, {1, 4}, {2, 4}, {3, 4}
	can be represented as follows:
 	
 	%MatrixMarket matrix c o o r d i n a t e p a t t e r n symmetric
	4 4 6
	2 1
	3 1
	3 2
	4 1
	4 2
	4 3
	
 * */
	
	
	
	
/*
 * VAM-PH input model
 * 
 * 
 * readGraph()
 * makeBipartite(G,method): The Duplicating Method and The Random Balanced Partition Method
 * FirstFit(G, sigma)
 * CBIP(G,sigma)
 * MyAlgorithm(G,sigma)
 * */
		
	public static void SimpleGreedy() {
		
	}
	
	public static void RandomInputOrder() {
		
		Order= new int[AdjacencyMatrix.length];
		
		Random rand= new Random();
		
		for(int i=0;i<AdjacencyMatrix.length;i++) {
			Order[i]= i+1;
		}
		
		//printArray(Order);
		
		for(int i=0;i<AdjacencyMatrix.length;i++) {
			int randomIndexToSwap=rand.nextInt(Order.length);
			int temp= Order[randomIndexToSwap];
			Order[randomIndexToSwap]=Order[i];
			Order[i]=temp;
			
		}
		
		printArray(Order);
	}

	public static void printArray(int[]array) {
		for(int i:array) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	public static void print2Darray(boolean[][] array) {
		int row=0;
		System.out.println();
		for(int i=0;i<array.length;i++) {
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
	
	public static void readGraph() throws IOException {
		
		int numberOfVertices=0, numberOfEdges=0;
		
		File file= new File("graph.txt");
		
		BufferedReader br= new BufferedReader(new FileReader(file));
			String st="";
			while((st=br.readLine())!=null) {
				if(!st.contains("%")) {
					String array[]=st.split(" ");
					
					if(array.length==3) {
						numberOfVertices=Integer.parseInt(array[0]);
						numberOfEdges=Integer.parseInt(array[2]);
						System.out.println("Number of Vertices = "+ numberOfVertices);
						System.out.println("Number of Edges = "+ numberOfEdges);
						AdjacencyMatrix=new boolean[numberOfVertices][numberOfVertices];

					}else {
						System.out.println(array[0]+ " -- > " + array[1]);
						AdjacencyMatrix[Integer.parseInt(array[0])-1][Integer.parseInt(array[1])-1]=true;
						AdjacencyMatrix[Integer.parseInt(array[1])-1][Integer.parseInt(array[0])-1]=true;

					}
				}
				
			}
		br.close();
		
		print2Darray(AdjacencyMatrix);
		
		
	}
	
	
	public static void main(String[] args) {
		try {
			readGraph();
		} catch (IOException e) {
			System.out.println("File Not Found ");
			e.printStackTrace();
		}
		
		RandomInputOrder();
	}

}
