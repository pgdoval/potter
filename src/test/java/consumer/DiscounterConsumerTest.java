package consumer;

import books.Book;
import dfs.DfsDiscounter;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by pablo on 31/10/16.
 */
public class DiscounterConsumerTest {

    DiscounterConsumer consumer;

    Book b1;
    Book b2;
    Book b3;
    Book b4;
    Book b5;


    @Before
    public void setUp()
    {

        b1 = new Book("b1",8.0);
        b2 = new Book("b2",8.0);
        b3 = new Book("b3",8.0);
        b4 = new Book("b4",8.0);
        b5 = new Book("b5",8.0);

        consumer = new DiscounterConsumer();
    }

    @Test
    public void getMinPrice() throws Exception {

        HashMap<Book, Integer> cart = new HashMap<Book, Integer>();
        assert(consumer.getMinPrice(cart)==0.0);

        cart.put(b1,1);
        assert(consumer.getMinPrice(cart)==8.0);

        cart.put(b1,2);
        assert(consumer.getMinPrice(cart)==16.0);

        //All nodes
        cart.put(b1,1);
        cart.put(b2,1);
        cart.put(b3,1);
        cart.put(b4,1);
        cart.put(b5,1);
        assert(consumer.getMinPrice(cart)==30.0);


        //Group 4 and 2
        cart.put(b1,2);
        cart.put(b2,1);
        cart.put(b3,2);
        cart.put(b4,1);
        cart.put(b5,0);
        assert(consumer.getMinPrice(cart)==40.8);

        //The tricky case
        cart.put(b1,2);
        cart.put(b2,2);
        cart.put(b3,2);
        cart.put(b4,1);
        cart.put(b5,1);
        assert(consumer.getMinPrice(cart)==51.2);

        //The long case
        cart.put(b1,5);
        cart.put(b2,5);
        cart.put(b3,4);
        cart.put(b4,5);
        cart.put(b5,4);
        assert(consumer.getMinPrice(cart)==(3*5*8*0.75) + (2*4*8*0.8));

        //The longest case
        cart.put(b1,6);
        cart.put(b2,6);
        cart.put(b3,5);
        cart.put(b4,6);
        cart.put(b5,5);
        assert(consumer.getMinPrice(cart)==(4*5*8*0.75) + (2*4*8*0.8));

    }

}