package books;

import consumer.DiscounterConsumer;

import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by pablo on 30/10/16.
 */
public class BookSetTest {
    @org.junit.Test
    public void getPrice() throws Exception {
        /*
        * We just check that in all the combinations, the
        * calculated price is the same as the expected one.
        * This has been very useful when we gave entity to
        * discounts, externalizing them.
        * */

        Book b1 = new Book("book 1", 8.0);
        Book b2 = new Book("book 2", 8.0);
        Book b3 = new Book("book 3", 8.0);
        Book b4 = new Book("book 4", 8.0);
        Book b5 = new Book("book 5", 8.0);

        HashSet<Book> oneBook = new HashSet<>();
        HashSet<Book> twoBooks = new HashSet<>();
        HashSet<Book> threeBooks = new HashSet<>();
        HashSet<Book> fourBooks = new HashSet<>();
        HashSet<Book> fiveBooks = new HashSet<>();

        oneBook.add(b1);
        twoBooks.add(b1);
        threeBooks.add(b1);
        fourBooks.add(b1);
        fiveBooks.add(b1);

        twoBooks.add(b2);
        threeBooks.add(b2);
        fourBooks.add(b2);
        fiveBooks.add(b2);

        threeBooks.add(b3);
        fourBooks.add(b3);
        fiveBooks.add(b3);

        fourBooks.add(b4);
        fiveBooks.add(b4);

        fiveBooks.add(b5);

        BookSet bs1 = new BookSet(oneBook);
        BookSet bs2 = new BookSet(twoBooks);
        BookSet bs3 = new BookSet(threeBooks);
        BookSet bs4 = new BookSet(fourBooks);
        BookSet bs5 = new BookSet(fiveBooks);

        assert(bs1.getPrice(DiscounterConsumer.typicalDiscounts())==8.0);
        assert(bs2.getPrice(DiscounterConsumer.typicalDiscounts())==15.2);
        assert(bs3.getPrice(DiscounterConsumer.typicalDiscounts())==21.6);
        assert(bs4.getPrice(DiscounterConsumer.typicalDiscounts())==25.6);
        assert(bs5.getPrice(DiscounterConsumer.typicalDiscounts())==30.0);
    }

}