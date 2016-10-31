package dfs;

import books.Book;

import java.util.Map;

/**
 * Created by pablo on 30/10/16.
 */
public interface Discounter {

    double getMinPrice(Map<Book,Integer> cart,  Map<Integer, Double> discounts);
}
