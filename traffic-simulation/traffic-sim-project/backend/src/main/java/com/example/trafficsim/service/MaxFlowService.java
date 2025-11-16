package com.example.trafficsim.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MaxFlowService {

    // Node mapping: A=0, B=1, C=2, D=3, E=4, F=5, G=6, H=7, T=8
    private static final int N = 9;
    private static final int S = 0;
    private static final int T = 8;

    public static class Edge {
        int to, rev;
        int cap;
        public Edge(int to, int rev, int cap){
            this.to=to; this.rev=rev; this.cap=cap;
        }
    }

    // Build adjacency list from capacity matrix
    private List<Edge>[] buildGraph(int[][] cap){
        List<Edge>[] g = new ArrayList[N];
        for(int i=0;i<N;i++) g[i]= new ArrayList<>();
        for(int u=0;u<N;u++){
            for(int v=0;v<N;v++){
                if(cap[u][v]>0){
                    g[u].add(new Edge(v, g[v].size(), cap[u][v]));
                    g[v].add(new Edge(u, g[u].size()-1, 0));
                }
            }
        }
        return g;
    }

    // Edmonds-Karp (BFS-based Ford-Fulkerson)
    public int edmondsKarp(int[][] cap){
        int flow = 0;
        int[][] residual = new int[N][N];
        for(int i=0;i<N;i++) for(int j=0;j<N;j++) residual[i][j] = cap[i][j];
        while(true){
            int[] parent = new int[N];
            Arrays.fill(parent, -1);
            parent[S] = -2;
            int[] parentEdge = new int[N];
            Queue<Integer> q = new LinkedList<>();
            q.add(S);
            int[] bottleneck = new int[N];
            bottleneck[S] = Integer.MAX_VALUE;
            while(!q.isEmpty()){
                int u = q.poll();
                for(int v=0; v<N; v++){
                    if(parent[v]==-1 && residual[u][v]>0){
                        parent[v]=u;
                        bottleneck[v] = Math.min(bottleneck[u], residual[u][v]);
                        if(v==T) break;
                        q.add(v);
                    }
                }
            }
            if(parent[T]==-1) break;
            int aug = bottleneck[T];
            int v = T;
            while(v!=S){
                int u = parent[v];
                residual[u][v] -= aug;
                residual[v][u] += aug;
                v = u;
            }
            flow += aug;
        }
        return flow;
    }

    // Dinic
    public int dinic(int[][] cap){
        List<Edge>[] g = buildGraph(cap);
        int flow = 0;
        while(true){
            int[] dist = new int[N];
            Arrays.fill(dist, -1);
            Queue<Integer> q = new LinkedList<>();
            q.add(S); dist[S]=0;
            while(!q.isEmpty()){
                int u=q.poll();
                for(Edge e: g[u]){
                    if(e.cap>0 && dist[e.to]==-1){
                        dist[e.to]=dist[u]+1;
                        q.add(e.to);
                    }
                }
            }
            if(dist[T]==-1) break;
            int[] it = new int[N];
            int pushed;
            while((pushed = dfs(S, T, Integer.MAX_VALUE, g, it, dist))>0){
                flow += pushed;
            }
        }
        return flow;
    }

    private int dfs(int u, int t, int f, List<Edge>[] g, int[] it, int[] dist){
        if(u==t) return f;
        for(int i=it[u]; i<g[u].size(); i++, it[u]++){
            Edge e = g[u].get(i);
            if(e.cap>0 && dist[e.to]==dist[u]+1){
                int ret = dfs(e.to, t, Math.min(f, e.cap), g, it, dist);
                if(ret>0){
                    e.cap -= ret;
                    g[e.to].get(e.rev).cap += ret;
                    return ret;
                }
            }
        }
        return 0;
    }
}
