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
import solutiontreecreator.data.Processor;

import java.awt.*;


public class GraphViewController {
    private Graph graph = new DefaultGraph(Main.getController().getGraphFilename());
    private Viewer viewer;

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

    public GraphViewController(){
        //Initialise the propertiers of the graph.
        //System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        graph = GraphController.defaultGraph(graph,Main.getController().getGraphFilename());
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.stylesheet",styleSheet);
        setGraphColours(graph);
    }

    public SwingNode viewGraph(){
        //GraphController.viewGraph(graph);
        viewer = new Viewer(graph,Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
        
        viewer.enableAutoLayout();

        ViewerPipe fromViewer = viewer.newViewerPipe();

        SwingNode swingNode = new SwingNode();
        view.setPreferredSize(new Dimension(750,600));
        swingNode.setContent(view);

        return swingNode;
    }

    public void setGraphColours(Graph graph){

        String[] processorColor = {"red","blue","yellow","green","brown","indigo","cyan","hotpink"};

        Processor[] processors = Main.getController().getSolution().getProcessors();

        for(int i = 0; i< processors.length;i++){
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                graph.getNode(n.getId()).setAttribute("ui.style", "fill-color:"+processorColor[i]+";");
                graph.getNode(n.getId()).addAttribute("ui.label",n.getId());
            }
        }
    }


}
