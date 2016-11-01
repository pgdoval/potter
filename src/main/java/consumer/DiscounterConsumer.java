package consumer;

import books.Book;
import dfs.DfsDiscounter;
import dfs.Discounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pablo on 31/10/16.
 */
public class DiscounterConsumer {

    public static double getMinPrice(Map<Book,Integer> cart)
    {
        //We could create a factory class if we see a need for it,
        //i.e. if we find another strategy and want to test both.
        Discounter discounter = new DfsDiscounter();

        return discounter.getMinPrice(cart, typicalDiscounts());

    }

    //This method could read a file or something
    public static Map<Integer, Double> typicalDiscounts(){

        Map<Integer, Double> discounts = new HashMap<>();

        discounts.put(1, 1.0);
        discounts.put(2, 0.95);
        discounts.put(3, 0.9);
        discounts.put(4, 0.8);
        discounts.put(5, 0.75);

        return discounts;
    }
}
