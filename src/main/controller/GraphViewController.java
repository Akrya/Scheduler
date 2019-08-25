package main.controller;

import graph.GraphController;
import javafx.embed.swing.SwingNode;
import main.Main;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerPipe;
import solutionfinder.data.Processor;
import solutionfinder.data.Solution;

import java.awt.*;
import java.util.*;
import java.util.List;


public class GraphViewController {
    public Graph graph = new DefaultGraph(Main.getController().getGraphFilename());
    private Viewer viewer;
    private List<String> processorColours = new ArrayList<>();

    /**
     * Styles the graph so that it is easy to read.
     */
    protected String styleSheet = "graph {" +
            "fill-color:white;" +
            "}" +
            "node {" +
            "size: 40px;" +
            "stroke-mode: plain;" +
            "stroke-color: black;" +
            "text-background-mode:plain;" +
            "text-background-color:white;" +
            "text-style:bold;" +
            "text-alignment:under;" +
            "text-size:30px;" +
            "}" +
            "edge {" +
            "shape: cubic-curve;" +
            "}";

    /**
     * Initialises the properties of the graph.
     */
    public GraphViewController(){
        graph = GraphController.defaultGraph(graph,Main.getController().getGraphFilename());
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.stylesheet",styleSheet);
        for (Node n: graph.getNodeSet()) {
            graph.getNode(n.getId()).addAttribute("ui.label",n.getId());
        }
//        setGraphColours(graph);
    }

    /**
     * Displays the graph.
     * @return swingNode used to embed the graph visual into the JavaFX scene.
     */
    public SwingNode viewGraph(){
        viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
        
        viewer.enableAutoLayout();

        ViewerPipe fromViewer = viewer.newViewerPipe();

        // SwingNode is used to convert the Swing component view into a JavaFX component.
        SwingNode swingNode = new SwingNode();
        view.setPreferredSize(new Dimension(750,600));
        swingNode.setContent(view);

        return swingNode;
    }

    /**
     * Assigns the colors to be used to represent the different processors used in the schedule.
     * @param numOfProcessors
     */
    public void setProcessorColours(int numOfProcessors) {

        List<String> processorColor = new ArrayList<>(Arrays.asList("red","blue","yellow","green","brown","indigo","cyan","hotpink"));
        Random rand = new Random();
        int randomNumber = rand.nextInt(processorColor.size() - 1);

        // For each processor, randomly assign it a colour from the processorColor list.
        for (int i = 0; i < numOfProcessors; i++) {
            processorColours.add(processorColor.get(randomNumber));
            processorColor.remove(randomNumber);
            randomNumber = rand.nextInt(processorColor.size() - 1);
        }

    }

    public void setGraphColours(Graph graph, Solution solution){

        Processor[] processors = solution.getProcessors();

        try{
            for(int i = 0; i < processors.length;i++){
                for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                    graph.getNode(n.getId()).setAttribute("ui.style", "fill-color:"+processorColours.get(i)+";");
                    graph.getNode(n.getId()).addAttribute("ui.label",n.getId());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public Graph getGraph(){
        return this.graph;
    }

}
