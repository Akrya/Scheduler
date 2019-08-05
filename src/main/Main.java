package main;

public class Main {

    public static void main(String args[]) throws Exception {
    	args = new String[]{ "INPUT.dot", "3", "-p", "3", "-o", "ligma", "-v" };
    	
        System.out.println(args.length);

        if (args.length < 2) {
            printInputArgumentsError();
        }
        
        // check first two args
        
        	// check for following input.DOT format
        
        	// check for integer representing P
        
        for(int i = 2; i < args.length; i++) {
        	if(args[i].equals("-p")) {
        		// Check if argument after -p is a number
        		String parallelArg = args[i++];
        		try {
        			Integer.parseInt(parallelArg);
        			System.out.println("Number parsed: "+parallelArg);
        		} catch (NumberFormatException e) {
        			System.out.println("Argument after -p must be a positive integer!");
        		}
        	} else if (args[i].equals("-v")) {
        		// Enable visualization
        		System.out.println("Visualization enabled!");
        		
        	} else if (args[i].equals("-o")) {
        		// Check if argument after -o is a valid file name
        		String outputArg = args[i++];
        		System.out.println("Output argument is "+outputArg);
        	} else {
        		throw new Exception();
        	}
        }
    }

    private static void printInputArgumentsError() {
        System.out.println("Invalid input");
        System.out.println("Please run the jar file using the following interface ->\n");
        System.out.println("java−jar scheduler . jar INPUT.dot P [OPTION]");
        System.out.println("INPUT. dot   a task graph with integer weights in dot format");
        System.out.println("P            number of  processors  to  schedule  the INPUT graph on");
        System.out.println("Optional:");
        System.out.println("−p N use N cores for execution in parallel (default  is  sequential)");
        System.out.println("−v visualise the search");
        System.out.println("−o OUTPUT   output file  is named OUTPUT (default  is INPUT−output.dot)");
        
        
    }

}
