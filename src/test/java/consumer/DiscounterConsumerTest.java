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

    }

    @Test
    public void getMinPrice() throws Exception {
        /*
        * We test the whole process, with several cases:
        * * empty case
        * * easy cases
        * * tricky combination cases
        * * non-tricky combination cases
        * * cases with some cart elements set to zero
        * * cases with a lot of elements in the cart
        * */

        HashMap<Book, Integer> cart = new HashMap<Book, Integer>();
        assert(DiscounterConsumer.getMinPrice(cart)==0.0);

        cart.put(b1,1);
        assert(DiscounterConsumer.getMinPrice(cart)==8.0);

        cart.put(b1,2);
        assert(DiscounterConsumer.getMinPrice(cart)==16.0);

        //All nodes
        cart.put(b1,1);
        cart.put(b2,1);
        cart.put(b3,1);
        cart.put(b4,1);
        cart.put(b5,1);
        assert(DiscounterConsumer.getMinPrice(cart)==30.0);


        //Group 4 and 2
        cart.put(b1,2);
        cart.put(b2,1);
        cart.put(b3,2);
        cart.put(b4,1);
        cart.put(b5,0);
        assert(DiscounterConsumer.getMinPrice(cart)==40.8);

        //The tricky case
        cart.put(b1,2);
        cart.put(b2,2);
        cart.put(b3,2);
        cart.put(b4,1);
        cart.put(b5,1);
        assert(DiscounterConsumer.getMinPrice(cart)==51.2);

        //The long case
        cart.put(b1,5);
        cart.put(b2,5);
        cart.put(b3,4);
        cart.put(b4,5);
        cart.put(b5,4);
        assert(DiscounterConsumer.getMinPrice(cart)==(3*5*8*0.75) + (2*4*8*0.8));

        //The longest case
        cart.put(b1,29);
        cart.put(b2,29);
        cart.put(b3,28);
        cart.put(b4,28);
        cart.put(b5,29);
        assert(DiscounterConsumer.getMinPrice(cart)==(27*5*8*0.75) + (2*4*8*0.8));

    }

}