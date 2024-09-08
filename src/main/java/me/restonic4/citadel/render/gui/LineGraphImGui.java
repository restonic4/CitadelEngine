package me.restonic4.citadel.render.gui;

import imgui.extension.implot.ImPlot;
import imgui.extension.implot.flag.ImPlotCol;
import imgui.extension.implot.flag.ImPlotCond;
import imgui.extension.implot.flag.ImPlotFlags;
import me.restonic4.citadel.util.ColorHelper;
import me.restonic4.citadel.util.StringBuilderHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LineGraphImGui {
    private String plotName;
    private int size;
    private int maxX, maxY;
    private List<GraphData> dataList;
    private float highestNumber = 0;
    private int width, height;

    public LineGraphImGui(String plotName, int dataSize, int width, int height, int maxX, int maxY) {
        this.plotName = plotName;
        this.size = dataSize;
        this.maxX = maxX;
        this.maxY = maxY;
        this.dataList = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public LineGraphImGui(String plotName, int dataSize, int width, int height, int maxX, boolean autoY) {
        this.plotName = plotName;
        this.size = dataSize;
        this.maxX = maxX;
        this.maxY = -1;
        this.dataList = new ArrayList<>();
        this.width = width;
        this.height = height;
    }

    public void addData(String name, int color) {
        dataList.add(new GraphData(name, this.size, color));
    }

    public void updateGraphData(String name, float newValue) {
        for (int i = 0; i < dataList.size(); i++) {
            GraphData graphData = dataList.get(i);

            if (Objects.equals(graphData.name, name)) {
                for (int j = 0; j < size - 1; j++) {
                    graphData.ys[j] = graphData.ys[j + 1];

                    if (graphData.ys[j] > highestNumber) {
                        highestNumber = graphData.ys[j] + 1;
                    }
                }

                graphData.ys[size - 1] = newValue;
                graphData.lastAdded = newValue;

                break;
            }
        }
    }

    public void render() {
        int desiredHeight = (this.maxY == -1) ? (int) highestNumber : this.maxY;

        ImPlot.setNextAxesLimits(0, this.maxX, 0, desiredHeight, ImPlotCond.Always);

        if (ImPlot.beginPlot(this.plotName, this.width, this.height, ImPlotFlags.NoInputs)) {
            for (int i = 0; i < dataList.size(); i++) {
                GraphData graphData = dataList.get(i);

                ImPlot.pushStyleColor(ImPlotCol.Line, ColorHelper.hexToARGB(graphData.color));
                ImPlot.plotLine(StringBuilderHelper.concatenate(graphData.name + ": ", graphData.lastAdded), graphData.xs, graphData.ys, size);
                ImPlot.popStyleColor();
            }

            ImPlot.endPlot();
        }

        highestNumber--;
    }

    public class GraphData {
        private String name;
        private float[] xs, ys;
        private float lastAdded = 0;
        private int color;

        public GraphData(String name, int size, int color) {
            this.name = name;
            this.xs = new float[size];
            this.ys = new float[size];
            this.color = color;

            for (int i = 0; i < size; i++) {
                xs[i] = i;
                ys[i] = 0;
            }
        }
    }
}
