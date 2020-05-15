package com.mada;

import lombok.Data;

/*
 * 记录相似度索引
 *
 * */
@Data
public class index implements Comparable<index> {
    private int index;
    private double sim;
    public index(int index, double sim) {
        this.index = index;
        this.sim = sim;
    }

    @Override
    public int compareTo(index o) {
        if (this.sim-o.getSim()>0)
            return -1;
        else if(this.sim-o.getSim()<0)
            return 1;
        else
            return 0;
    }
}
