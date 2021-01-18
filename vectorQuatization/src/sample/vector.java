package sample;

import java.util.ArrayList;

public class vector {
    int[][] data;
    int n, m;
    vector() {
        data = null;
        n = m = 0;
    }
    vector(int[][] data,int n,int m){
        this.data = data;
        this.n = n;
        this.m = m;
    }
    vector(int n,int m){
        this.n = n;
        this.m = m;
        this.data = new int[n][m];
    }
    int getElement(int i,int j){
        return this.data[i][j];
    }

    void setElement(int i,int j,int val){
        this.data[i][j] = val;
    }

    int getDistance(vector v){
        int distance = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int d = this.data[i][j] - v.getElement(i,j);
                distance += d*d;
            }
        }
        return distance;
    }

    public void setData(int[][] data) {
        this.data = data;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public int[][] getData() {
        return data;
    }

}
