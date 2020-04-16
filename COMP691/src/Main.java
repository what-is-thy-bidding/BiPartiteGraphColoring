import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {
	
	public static boolean[][] AdjacencyMatrix=null;
	
	
	public static String FileName = "RandomGraph4.txt";
	
	
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
    VAM-PH input model
 * 
 * 
 * readGraph()
 * makeBipartite(G,method): The Duplicating Method and The Random Balanced Partition Method
 * FirstFit(G, sigma)
 * CBIP(G,sigma)
 * MyAlgorithm(G,sigma)
 * */
	
	public static void CBIP() {
		
		int colors=0;
		
		int[] vertexColor= new int[Order.length];
		
		for(int i=0;i<Order.length;i++) {
			//System.out.print("vertex = "+Order[i]);
			if(i==0) { // Base Case assigning a color to the 1st vertex
				colors++;
				vertexColor[Order[i]-1]=colors;
				//System.out.println(" , color= "+vertexColor[Order[i]-1] );
			}else {
				
				boolean[]vertexVisited= new boolean[Order.length]; // set of vertices from their index values for BFS to find out if they have been visited or not.
				
				boolean[][] ColorsNotEligible=new boolean[2][colors]; // set of colors used by the 2 sets of vertices - (U & V) -  Bipartition
																	  // The indices represent the color values.
				
				//System.out.println("ColorsNotEligible[# of colors] = " +ColorsNotEligible[0].length );
				
				int currentVertex=Order[i]; // The current vertex 
				
				vertexVisited[currentVertex-1]= true; // We have visited this vertex , and marked it as true for BFS 
				
				ArrayList<Integer> queue= new ArrayList<Integer>(); // A Queue, for a set of adjacent vertices for BFS
				
				//System.out.print(" Adjacent vertices = ");
				
				for(int j=0;j<i;j++) {
					
					if(AdjacencyMatrix[currentVertex-1][Order[j]-1]) { // if an edge exists between the currentVertex and the previous arrived vertices in the Order
						
						queue.add(Order[j]); // adjacentVertex has been added to the queue
										
						vertexVisited[Order[j]-1]= true;// adjacentVertex has been marked visited
						
						ColorsNotEligible[0][vertexColor[Order[j]-1] - 1]=true; // U vertex set -> Since these vertices are IMMIDEATE neighbors of currentVertex.
						
						//System.out.print(Order[j]+ " , ");
						
					}
				}
				
				if(queue.isEmpty()) { // Vertex Is independent
					
					vertexColor[currentVertex-1]=1;

					/*System.out.println("Vertex has no neighbours ");
					System.out.println(" , color= "+1 );*/
				
				}else {
					
					//System.out.print( " // ");
					
					
					boolean part_of_V=true; // To check if the vertices belong to U or V, flips sign alternatively.
					
					
					while(!queue.isEmpty()) {// Runs till the queue is empty/ no more new vertices are encountered.
						
						ArrayList<Integer>tempQueue= new ArrayList<>(queue);// Copying the original queue to a temporary queue
						
						queue.clear(); // Emptying the original queue, to keep the original queue only for new vertices
						
						while(!tempQueue.isEmpty()) { // running this until we find out all the nth distance neighbors from the currentVertex

							int adjVertex=tempQueue.get(0);// Getting the top of the queue
							
							tempQueue.remove(0); // Polling the top of the queue
							
							// Find the immediate neighbors of the current AdjacentVertex.
							//The immediate neighbor have to come from the list of vertices that have arrived before the currentVertex and NOT include the currentVertex.	
							
							for(int j=0;j<i;j++) {
									// An edge exists						     // In case the same vertices are not repeated
								if(AdjacencyMatrix[adjVertex-1][Order[j]-1]==true  && vertexVisited[Order[j]-1]==false) {
									
									queue.add(Order[j]); //Immediate neighbor of the adjacent Vertex has been added to the queue 
									
									//System.out.print(Order[j]+ " , ");
									
									vertexVisited[Order[j]-1]= true; // Immediate neighbor/Order[j] has been marked visited will not be repeated
									
									if(part_of_V) { // V vertex set
										ColorsNotEligible[1][vertexColor[Order[j]-1] -1]=true; // Its color value is set true and cannot be taken for the currentVertex

									}else { // U vertex set
										ColorsNotEligible[0][vertexColor[Order[j]-1] -1]=true;// Its color value is set true and can be taken for the currentVertex
									}
								}
									
							}
							
							
							
						
						}
						
						
						
						part_of_V=!part_of_V; // Flip the boolean to make sure only alternating depth node colors are added
						
					}
					
					//System.out.println();
					
					//print2Darray(ColorsNotEligible);// Printing the colors - U & V
					
					//System.out.println();
					
					boolean ColorAssigned=false;
					for(int j=0;j<colors;j++) {
							// U vertices						// V vertices
						if(ColorsNotEligible[0][j]==false && ColorsNotEligible[1][j]==true) {
							vertexColor[currentVertex-1]=j+1; // Assigning minimum legal color
							ColorAssigned=true;
							//System.out.println(" , color= "+(j+1) );

						}
					}
					
					if(ColorAssigned==false) {
						colors++;
						vertexColor[currentVertex-1]=colors;
						//System.out.println(" , color= "+colors );

					}
									
					
					
				}	
				
				
				}
			//System.out.println("*******************");

				
		}
		System.out.println("CBIP : Total Number of Colors = " + colors);
	}
	
	
	public static void FirstFit() {
		
		int colors=0;
		
		HashMap<Integer,Integer> vertexColor= new HashMap<Integer, Integer>(Order.length); // Key= vertexNumber, Value=colorNumber
		
		for(int i=0;i<Order.length;i++) {
			int currentVertex=Order[i];
			/*
			 * check if there have been previously known vertices have edges with the current vertex and what color they are
			 * */
			boolean colorsUsed[]=new boolean[colors];
			
			for(int j=0;j<i;j++) {
				int previousVertex=Order[j];
				if(AdjacencyMatrix[currentVertex-1][previousVertex-1]) { // An edge exists			
					int previousVertexColor=vertexColor.get(previousVertex);
					colorsUsed[previousVertexColor-1]= true;
					
				}
			}
			
			boolean colorAssigned=false;
			
			for(int j=0;j<colorsUsed.length;j++) {			
				if(colorsUsed[j]==false) {
					colorAssigned=true;
					vertexColor.put(currentVertex, j+1);
				}
			}
			
			if(colorAssigned==false) {
				colors++;
				vertexColor.put(currentVertex,colors);
			}
			//System.out.println("Current Vertex " + Order[i] + " and its' color = " + vertexColor.get(Order[i]));

			
		}
		
		System.out.println("FirstFit : Total Number of colors = " + colors);
		//printHashMap(vertexColor);
	}
	
	
	public static void printHashMap(HashMap<Integer,Integer> vertexColor) {
		for(int i=0;i<Order.length;i++) {
			System.out.println("Vertex Number = "+(i+1) + "  --> Vertex Color = "+vertexColor.get(i+1));
		}
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
	
	
	public static void readGraph() throws IOException {
		
		int numberOfVertices=0;
		
		File file= new File(FileName);
		
		BufferedReader br= new BufferedReader(new FileReader(file));
			String st="";
			while((st=br.readLine())!=null) {
				if(!st.contains("%")) {
					String array[]=st.split(" ");
					
					if(array.length==3) {
						numberOfVertices=Integer.parseInt(array[0]);
						System.out.println("Number of Vertices = "+ numberOfVertices);
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
		
		System.out.println();
		
		RandomInputOrder();
		
		System.out.println();
		
		/*
		 *Graph2.txt
		Order= new int[6];
		Order[0]= 1;
		Order[1]= 4;
		Order[2]= 2;
		Order[3]= 5;
		Order[4]= 3;
		Order[5]= 6;*/
		
		
		
		/*
		 *Graph3.txt 
		Order= new int[5];
		Order[0]= 1;
		Order[1]= 2;
		Order[2]= 3;
		Order[3]= 4;
		Order[4]= 5;*/		
		
		FirstFit();
		
		CBIP();
	}

}
