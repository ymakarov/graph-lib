package ru.yamakarov.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    private final Map<T, List<T>> vertices = new HashMap<>();
    private final boolean oriented;

    public Graph() {
        this(true);
    }

    public Graph(boolean oriented) {
        this.oriented = oriented;
    }

    public void addVertex(T v) {
        if (v == null) {
            throw new IllegalArgumentException("Null vertices are not supported");
        }
        vertices.putIfAbsent(v, new ArrayList<>());
    }

    public int countVertices() {
        return vertices.size();
    }

    public void addEdge(Edge<T> edge) {
        T from = edge.from;
        T to = edge.to;
        if (from == null || to == null) {
            throw new IllegalArgumentException("Null vertices are not supported");
        }
        if (!vertices.containsKey(to)) {
            throw new IllegalArgumentException(String.format("Vertex %s is not found", to.toString()));
        }
        doAddEdge(from, to);

        if (!oriented) {
            doAddEdge(to, from);
        }
    }

    private void doAddEdge(T from, T to) {
        if (vertices.computeIfPresent(from,
                (key, value) -> {
                    value.add(to);
                    return value;
                }) == null) {
            throw new IllegalArgumentException(
                    String.format("Vertex %s is not found", from.toString()));
        }
    }

    /**
     * @return List of connected vertices. Null if path doesn't exist.
     */
    public List<Edge<T>> getPath(T v1, T v2) {
        if (!vertices.containsKey(v1)) {
            throw new IllegalArgumentException(String.format("Vertex %s is not found", v1));
        }
        if (!vertices.containsKey(v2)) {
            throw new IllegalArgumentException(String.format("Vertex %s is not found", v2));
        }
        Set<T> visited = new HashSet<>();
        ArrayList<T> currentPath = new ArrayList<>();
        currentPath.add(v1);
        visited.add(v1);
        while (visited.size() <= vertices.size()) {
            T last = currentPath.get(currentPath.size() - 1);
            boolean hasNotVisited = false;
            for (T v : vertices.getOrDefault(last, new ArrayList<>())) {
                if (!visited.contains(v)) {
                    if (v == v2) {
                        currentPath.add(v);
                        return toEdges(currentPath);
                    }
                    visited.add(v);
                    currentPath.add(v);
                    hasNotVisited = true;
                    break;
                }
            }
            if (!hasNotVisited) {
                currentPath.remove(currentPath.size() - 1);
                if (currentPath.size() == 0) {
                    return null;
                }
            }
        }
        return null;
    }

    private List<Edge<T>> toEdges(List<T> vertices) {
        List<Edge<T>> edges = new ArrayList<>();
        for (int i = 1, size = vertices.size(); i < size; i++) {
            edges.add(new Edge<>(vertices.get(i - 1), vertices.get(i)));
        }
        return edges;
    }
}
