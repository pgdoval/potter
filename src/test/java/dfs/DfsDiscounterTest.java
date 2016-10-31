package dfs;

import books.Book;
import books.BookSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by pablo on 30/10/16.
 */
public class DfsDiscounterTest {

    DfsDiscounter discounter;

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

        discounter = new DfsDiscounter();
    }

    @Test
    public void getMinPrice() throws Exception {

    }

    @Test
    public void rollback() throws Exception {

        Set<Book> bs1 = new HashSet<>();
        bs1.add(b1);

        Set<Book> bs2 = new HashSet<>();
        bs2.add(b2);

        BookSet bookSet1 = new BookSet(bs1);
        BookSet bookSet2 = new BookSet(bs2);

        DfsNode stackPeekFather = new DfsNode(null,null,0.0,1);
        DfsNode node1 = new DfsNode(stackPeekFather,bookSet1,0.0,2);
        DfsNode node2 = new DfsNode(node1,bookSet2,0.0,3);

        Map<Book, Integer> cartFor1 = new HashMap<>();


        cartFor1.put(b1,1);
        cartFor1.put(b2,2);
        cartFor1.put(b3,2);

        Map<Book, Integer> cartFor2 = new HashMap<>();

        cartFor2.put(b1,1);
        cartFor2.put(b2,1);
        cartFor2.put(b3,2);

        Map<Book, Integer> res1 = discounter.rollback(node1,cartFor1,stackPeekFather);
        Map<Book, Integer> res2 = discounter.rollback(node2,cartFor2,stackPeekFather);

        assert(res1.get(b1)==2);
        assert(res1.get(b2)==2);
        assert(res1.get(b3)==2);

        assert(res2.get(b1)==2);
        assert(res2.get(b2)==2);
        assert(res2.get(b3)==2);

    }

    @Test
    public void generateNodes() throws Exception {

        Map<Book, Integer> cart1 = new HashMap<>();

        Map<Book, Integer>  cart2 = new HashMap<>();

        cart2.put(b1,2);
        cart2.put(b2,2);
        cart2.put(b3,2);

        Map<Book, Integer> cart3 = new HashMap<>();

        cart3.put(b1,2);
        cart3.put(b2,0);
        cart3.put(b3,2);
        DfsNode node = new DfsNode(null,null,1.0,1);

        List<DfsNode> nodes1 = discounter.generateNodes(node, cart1);
        List<DfsNode> nodes2 = discounter.generateNodes(node, cart2);
        List<DfsNode> nodes3 = discounter.generateNodes(node, cart3);


        assert(nodes1.isEmpty());


        assert(nodes2.size()==7);
        assert(nodes2.stream().filter(it -> it.getSet().getBooks().size()==3).count()==1);
        assert(nodes2.stream().filter(it -> it.getSet().getBooks().size()==2).count()==3);
        assert(nodes2.stream().filter(it -> it.getSet().getBooks().size()==1).count()==3);
        assert(nodes2.stream().filter(it -> it.getSet().getBooks().size()==0).count()==0);


        assert(nodes3.size()==3);
        assert(nodes3.stream().filter(it -> it.getSet().getBooks().size()==2).count()==1);
        assert(nodes3.stream().filter(it -> it.getSet().getBooks().size()==1).count()==2);
    }

}