package dfs;

import books.BookSet;

import java.util.Map;

/**
 * Created by pablo on 30/10/16.
 */
public class DfsNode {

    private DfsNode father;
    private BookSet set;
    private double spent;
    private int depth;

    public DfsNode generateNext(BookSet set, Map<Integer, Double> discounts)
    {
        return new DfsNode(this, set, spent + set.getPrice(discounts), depth + 1);
    }

    public DfsNode(DfsNode father, BookSet set, double spent, int depth) {
        this.father = father;
        this.set = set;
        this.spent = spent;
        this.depth = depth;
    }

    public DfsNode getFather() {
        return father;
    }

    public void setFather(DfsNode father) {
        this.father = father;
    }

    public BookSet getSet() {
        return set;
    }

    public void setSet(BookSet set) {
        this.set = set;
    }

    public double getSpent() {
        return spent;
    }

    public void setSpent(double spent) {
        this.spent = spent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
