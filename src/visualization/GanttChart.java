package visualization;

import graph.GraphController;
import main.Main;

import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Node;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import solutiontreecreator.data.Processor;

import javax.swing.*;

public class GanttChart extends JFrame {

    private static final long serialVersionUID = 1L;

    public GanttChart(String title) {
        super(title);

        IntervalCategoryDataset dataset = getCategoryDataset();

        JFreeChart chart = ChartFactory.createGanttChart(
                "Gantt Chart of the different processes",
                "Time",
                "Processors",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);

        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add(panel, BorderLayout.NORTH);

        JFrame frame = new JFrame();
        frame.add(jPanel4);
        frame.pack();
        frame.setVisible(true);

    }

    private IntervalCategoryDataset getCategoryDataset() {

        TaskSeries series1 = new TaskSeries("Execution Time");
        Processor[] processors = Main.getController().getSolution().getProcessors();
        int counter;
        Task initialTask = null;
        List<Task> subTasks;

        for (int i = 0; i < processors.length; i++) {
            counter = 0;
            String processorName = "Processor " + i;
            subTasks = new ArrayList<>();
            for (Node n: processors[i].mapOfTasksAndStartTimes.keySet()) {
                if (counter == 0) {
                    long startTime = processors[i].mapOfTasksAndStartTimes.get(n).longValue();
                    long endTime = processors[i].mapOfTasksAndStartTimes.get(n).longValue() + (long) GraphController.getNodeWeight(n.getId(), Main.getController().getGraph());                    initialTask = new Task(processorName, new SimpleTimePeriod(startTime, endTime));
                    counter++;
                } else {
                    long startTime = processors[i].mapOfTasksAndStartTimes.get(n).longValue();
                    long endTime = processors[i].mapOfTasksAndStartTimes.get(n).longValue() + (long) GraphController.getNodeWeight(n.getId(), Main.getController().getGraph());
                    subTasks.add(new Task(processorName + 1, new SimpleTimePeriod(startTime, endTime)));
                }
            }

            for (Task st: subTasks) {
                if (initialTask != null) {
                    initialTask.addSubtask(st);
                }
            }

            series1.add(initialTask);

        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        dataset.add(series1);

        return dataset;

    }

}
