package ru.yamakarov.graph;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphTest {

    @Test
    public void testAddVertex() {
        Graph<Long> graph = new Graph<>();
        graph.addVertex(1L);
        Assertions.assertEquals(1, graph.countVertices());
    }

    @Test
    public void testAddEdge() {
        Graph<Long> graph = new Graph<>();
        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addEdge(new Edge<>(1L, 2L));
    }

    @Test
    public void testConnectedPath() {
        Graph<Long> graph = new Graph<>();
        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addEdge(new Edge<>(1L, 2L));
        Assertions.assertEquals(Collections.singletonList(new Edge<>(1L, 2L)), graph.getPath(1L, 2L));
    }

    @Test
    public void testNotConnectedVertices() {
        Graph<Long> graph = new Graph<>();
        graph.addVertex(1L);
        graph.addVertex(2L);
        Assertions.assertNull(graph.getPath(1L, 2L));
    }

    @Test
    public void testDeadEnd() {
        Graph<Long> graph = new Graph<>();
        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addVertex(3L);
        graph.addVertex(4L);

        graph.addEdge(new Edge<>(1L, 2L));
        graph.addEdge(new Edge<>(1L, 3L));
        graph.addEdge(new Edge<>(3L, 4L));

        Assertions.assertEquals(Arrays.asList(new Edge<>(1L, 3L), new Edge<>(3L, 4L)), graph.getPath(1L, 4L));
    }

    @Test
    public void testNotOrientedCycle() {
        Graph<Long> graph = new Graph<>(false);
        graph.addVertex(1L);
        graph.addVertex(2L);
        graph.addVertex(3L);
        graph.addVertex(4L);
        graph.addVertex(5L);

        graph.addEdge(new Edge<>(1L, 2L));
        graph.addEdge(new Edge<>(1L, 3L));
        graph.addEdge(new Edge<>(3L, 4L));
        graph.addEdge(new Edge<>(4L, 1L));
        graph.addEdge(new Edge<>(5L, 4L));

        Assertions.assertEquals(Arrays.asList(new Edge<>(1L, 3L), new Edge<>(3L, 4L), new Edge<>(4L, 5L)), graph.getPath(1L, 5L));
    }

}
