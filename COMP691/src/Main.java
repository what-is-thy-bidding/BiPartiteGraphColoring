import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Main {
	
	public static boolean[][] AdjacencyMatrix=null;
	
	public static String FileName = "";
	
	
	public static int Order[]= null;

	
	public static int numberOfVertices=0;
	
	public static double NetworkGraphData[][]= new double[3][5];
	
	public static double RandomGraphData[][]= new double[3][5];
	
	public static int Iterations=1;// Keep the number of iterations as 1 AT LEAST 
		
	
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
	
	public static void ReadNetworkRepoGraphs() {
		System.out.println("\n **************************** \n Network Repository Graphs\n **************************** \n");	
		try {
			
			for(int j=0;j<Iterations;j++) {
				for(int i=1;i<=5 ;i++) {
					FileName="NetworkRepoGraph"+i+".txt";
					readGraph(FileName);
					
					//System.out.println();
					
					RandomInputOrder();
					
					System.out.println();
					
					NetworkGraphData[0][i-1]+=FirstFit();
					
					NetworkGraphData[1][i-1]+=CBIP();
					
					NetworkGraphData[2][i-1]+=MyAlgorithm();

				}
				System.out.println("\n **************************** \n");
			}
				
					
				
		} catch (IOException e) {
			System.out.println("File Not Found ");
			e.printStackTrace();
		}
		
		//System.out.println("\n **************************** \n");
		
		
		
	}
	
	
	public static void ReadRandomGraphs() {
		System.out.println("\n **************************** \n     Random Graphs\n **************************** \n");
		try {
			
			for(int j=0;j<Iterations;j++) {
				for(int i=1;i<=5 ;i++) {
					FileName="RandomGraph"+i+".txt";
					readGraph(FileName);
					
					//System.out.println();
					
					RandomInputOrder();
					
					System.out.println();
					
					RandomGraphData[0][i-1]+=FirstFit();
					
					RandomGraphData[1][i-1]+=CBIP();
					
					RandomGraphData[2][i-1]+=MyAlgorithm();
				}
			
			}
			
				
		} catch (IOException e) {
			System.out.println("File Not Found ");
			e.printStackTrace();
		}
		
		
		System.out.println("\n **************************** \n");
		
		

	}
	
	
	public static int CBIP() {
		
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
				
				//printVertexColors(vertexColor);
				
				}
			//System.out.println("*******************");

				
		}
		System.out.println("CBIP : Total Number of Colors = " + colors);
		return colors;
	}
	
	
	public static void printVertexColors(int[] array) {
		for(int i=0;i<array.length;i++) {
			System.out.println("Vertex: "+ (i+1) + " -- > Color: "+array[i] );
		}
	}
	
	
	public static int FirstFit() {
		
		int colors=0;
		
		int[] vertexColor= new int[Order.length]; // Key= vertexNumber, Value=colorNumber
		
		for(int i=0;i<Order.length;i++) {
			int currentVertex=Order[i];
			/*
			 * check if there have been previously known vertices have edges with the current vertex and what color they are
			 * */
			boolean colorsUsed[]=new boolean[colors];
			
			for(int j=0;j<i;j++) {
				int previousVertex=Order[j];
				if(AdjacencyMatrix[currentVertex-1][previousVertex-1]) { // An edge exists			
					int previousVertexColor=vertexColor[previousVertex-1];
					colorsUsed[previousVertexColor-1]= true;
					
				}
			}
			
			boolean colorAssigned=false;
			
			for(int j=0;j<colorsUsed.length;j++) {			
				if(colorsUsed[j]==false) { // The minimum color NOT used
					colorAssigned=true;
					vertexColor[currentVertex-1]= j+1;
				}
			}
			
			if(colorAssigned==false) {
				colors++;
				vertexColor[currentVertex-1]=colors;
			}
			//System.out.println("Current Vertex " + Order[i] + " and its' color = " + vertexColor.get(Order[i]));

			
		}
		
		System.out.println("FirstFit : Total Number of colors = " + colors);
		
		
		return colors;
	}
	
	
	public static int MyAlgorithm() {
		
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
					
					ArrayList<Integer> ListOfEligibleColors= new ArrayList<Integer>(); 
					for(int j=0;j<colors;j++) {
							// U vertices						// V vertices
						if(ColorsNotEligible[0][j]==false && ColorsNotEligible[1][j]==true) {
							ListOfEligibleColors.add(j+1); // j+1 is the color number
							//System.out.println(" , color= "+(j+1) );

						}
					}
					
					if(ListOfEligibleColors.size()==0) {
						colors++;
						vertexColor[currentVertex-1]=colors;
						//System.out.println(" , color= "+colors );

					}else {
						Random Rand= new Random();
						
						int indexChoice= Rand.nextInt(ListOfEligibleColors.size()); //[0,List.size-1] 
																					//So shouldn't go out of bounds
						
						vertexColor[currentVertex-1]= ListOfEligibleColors.get(indexChoice);//Assigns color
					}
									
					
					
				}	
				
				//printVertexColors(vertexColor);
				
				}
			//System.out.println("*******************");

				
		}
		System.out.println("MyAlgorithm(RandomCBIP) : Total Number of Colors = " + colors);
		return colors;
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
		
		//printArray(Order);
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
	
	
	public static void readGraph(String FileName) throws IOException {
		
		
		File file= new File(FileName);
		
		BufferedReader br= new BufferedReader(new FileReader(file));
			String st="";
			while((st=br.readLine())!=null) {
				if(!st.contains("%")) {
					String array[]=st.split(" ");
					
					if(array.length==3) {
						numberOfVertices=Integer.parseInt(array[0]);
						System.out.println("\n Number of Nodes = "+ numberOfVertices);
						AdjacencyMatrix=new boolean[numberOfVertices][numberOfVertices];

					}else {
						//System.out.println(array[0]+ " -- > " + array[1]);
						
						if(array[0].compareTo(array[1])!=0) { // No self loops
							AdjacencyMatrix[Integer.parseInt(array[0])-1][Integer.parseInt(array[1])-1]=true;
							AdjacencyMatrix[Integer.parseInt(array[1])-1][Integer.parseInt(array[0])-1]=true;

						}
							
					}
				}
				
			}
		br.close();
		
		//print2Darray(AdjacencyMatrix);
		
		
	}
	
	
	public static void Data() {
		System.out.println("\n **************************** \n   Data after "+Iterations+ " Iteration(s)\n **************************** \n");

		System.out.println("\n **************************** \n   Network Repository Graphs DATA\n **************************** \n");
		
		
		for(int i=0;i<NetworkGraphData.length;i++) {
			if(i==0) {
				System.out.print("FirstFit      :");
			}else if(i==1) {
				System.out.print("CBIP          :");
			}else if(i==2) {
				System.out.print("MyAlgorithm   :");
			}
			for(double j:NetworkGraphData[i]) {
				System.out.print( (j/Iterations) + " , ");
			}
			System.out.println();
		}
		
		System.out.println("\n **************************** \n     Random Graphs DATA\n **************************** \n");
		
		
		for(int i=0;i<RandomGraphData.length;i++) {
			if(i==0) {
				System.out.print("FirstFit     :");
			}else if(i==1) {
				System.out.print("CBIP         :");
			}else if(i==2) {
				System.out.print("MyAlgorithm  :");
			}
			
			for(double j:RandomGraphData[i]) {
				System.out.print( (j/Iterations) + " , ");
			}
			System.out.println();
		}
	}
	
	public static void execute() {
		
		ReadNetworkRepoGraphs();
		
		
		
		ReadRandomGraphs();
		
		
		Data();
		
	}
	
		
	public static void main(String[] args) {
		execute();
	}

}
