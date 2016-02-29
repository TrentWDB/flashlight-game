package com.aetherpass.utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

// a really shitty code segment I found online...
public class Triangulator {
    private ArrayList<Point> m_points = new ArrayList<Point>();

    public Triangulator(ArrayList<Point> points) {
        m_points = points;
    }

    public ArrayList<Integer> triangulate() {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        int n = m_points.size();
        if (n < 3) {
            return indices;
        }

        int[] v = new int[n];
        if (area() > 0) {
            for (int v2 = 0; v2 < n; v2++) {
                v[v2] = v2;
            }
        } else {
            for (int v2 = 0; v2 < n; v2++) {
                v[v2] = (n - 1) - v2;
            }
        }

        int nv = n;
        int count = 2 * nv;
        int m = 0;
        for (int v2 = nv - 1; nv > 2; ) {
            if ((count--) <= 0) {
                return indices;
            }

            int u = v2;
            if (nv <= u) {
                u = 0;
            }
            v2 = u + 1;
            if (nv <= v2) {
                v2 = 0;
            }
            int w = v2 + 1;
            if (nv <= w) {
                w = 0;
            }

            if (snip(u, v2, w, nv, v)) {
                int a = 0;
                int b = 0;
                int c = 0;
                int s = 0;
                int t = 0;
                a = v[u];
                b = v[v2];
                c = v[w];
                indices.add(a);
                indices.add(b);
                indices.add(c);
                m++;
                s = v2;
                for (t = v2 + 1; t < nv; t++) {
                    v[s] = v[t];
                    s++;
                }
                nv--;
                count = 2 * nv;
            }
        }

        Collections.reverse(indices);
        return indices;
    }

    private double area() {
        int n = m_points.size();
        float a = 0;
        int q = 0;
        for (int p = n - 1; q < n; p = q++) {
            Point pval = m_points.get(p);
            Point qval = m_points.get(q);
            a += pval.x * qval.y - qval.x * pval.y;
        }

        return a * 0.5;
    }

    private boolean snip(int u, int v, int w, int n, int[] V) {
        int p = 0;
        Point A = m_points.get(V[u]);
        Point B = m_points.get(V[v]);
        Point C = m_points.get(V[w]);
        if (MathUtils.EPSILON > (((B.x - A.x) * (C.y - A.y)) - ((B.y - A.y) * (C.x - A.x)))) {
            return false;
        }
        for (p = 0; p < n; p++) {
            if ((p == u) || (p == v) || (p == w)) {
                continue;
            }
            Point P = m_points.get(V[p]);

            if (insideTriangle(A, B, C, P)) {
                return false;
            }
        }
        return true;
    }

    private boolean insideTriangle(Point A, Point B, Point C, Point P) {
        float ax = 0;
        float ay = 0;
        float bx = 0;
        float by = 0;
        float cx = 0;
        float cy = 0;
        float apx = 0;
        float apy = 0;
        float bpx = 0;
        float bpy = 0;
        float cpx = 0;
        float cpy = 0;
        float cCROSSap = 0;
        float bCROSScp = 0;
        float aCROSSbp = 0;

        ax = C.x - B.x;
        ay = C.y - B.y;
        bx = A.x - C.x;
        by = A.y - C.y;
        cx = B.x - A.x;
        cy = B.y - A.y;
        apx = P.x - A.x;
        apy = P.y - A.y;
        bpx = P.x - B.x;
        bpy = P.y - B.y;
        cpx = P.x - C.x;
        cpy = P.y - C.y;

        aCROSSbp = ax * bpy - ay * bpx;
        cCROSSap = cx * apy - cy * apx;
        bCROSScp = bx * cpy - by * cpx;

        return ((aCROSSbp >= 0.0) && (bCROSScp >= 0.0) && (cCROSSap >= 0.0));
    }
}