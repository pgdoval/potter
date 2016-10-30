package dfs;

import books.Book;
import books.BookSet;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by pablo on 30/10/16.
 */
public class DfsDiscounter implements Discounter{

    private Map<Book, Integer> cart;
    private DfsNode bestSolution = null;

    private Stack<DfsNode> stack = new Stack<>();

    public DfsDiscounter(Map<Book, Integer> cart) {
        this.cart = cart;
    }

    public Map<Book, Integer> getCart() {
        return cart;
    }

    public void setCart(Map<Book, Integer> cart) {
        this.cart = cart;
    }

    public DfsDiscounter() {

    }

    public double getMinPrice(Map<Book, Integer> _cart) {
        /*
        * We are going to suppose that the discounts are compliant with the
        * rule that the best discount is contained into the set of discounts
        * with minimum depth (that is, the minimum number of booksets, which means
        * that each bookset has many books), which I feel is a supposition we can assume
        * because in real life it's what discounts are all about.
        * */

        cart = _cart;



    }

    public boolean isDone(Map<Book, Integer> cart)
    {
        return cart.values().stream().allMatch(v-> v==0);
    }

    public Map<Book, Integer> modifyCart(Map<Book, Integer> cart, Set<Book> books, boolean returning)
    {
        books.stream().forEach(
                book -> {
                    int current = cart.get(book);
                    if(returning)
                        current++;
                    else
                        current--;
                    cart.put(book,current);
                }
        );
        return cart;
    }

    public Map<Book, Integer> rollback(DfsNode node, Map<Book, Integer> cart, DfsNode hay)
    {
        while(node != hay){
            cart = modifyCart(cart, node.getSet().getBooks(), true);
            node = node.getFather();
        }

        return modifyCart(cart, node.getSet().getBooks(), true);

    }

    public void processNode(DfsNode currentNode){
        /*
        If this node's depth is bigger than or equals to the current solution's,
        we can stop and go back.
        If this node's depth is not bigger than current solution's, we check if
        it's a better solution.
        * */

        cart = modifyCart(cart, currentNode.getSet().getBooks(), false);
        if(isDone(cart))
        {
            if(bestSolution==null || bestSolution.getSpent() < currentNode.getSpent())
            {
                bestSolution = currentNode;
            }
            cart = rollback(currentNode, cart, stack.peek().getFather());
            return;
        }
        //Once we reached this point, we won't get any better in this branch
        if(bestSolution!=null && currentNode.getDepth() >= bestSolution.getDepth())
        {
            cart = rollback(currentNode, cart, stack.peek().getFather());
            return;
        }

        //Normal case: we generate the nodes and insert them into the stack
        generateNodes(currentNode, cart).forEach(node -> stack.push(node));

    }

    public List<DfsNode> generateNodes(DfsNode currentNode, Map<Book, Integer> cart)
    {
        List<Map.Entry<Book,Integer>> books = cart.entrySet().stream().filter(entry-> entry.getValue()>0).collect(Collectors.toList());

        Stream<DfsNode> unsortedResult = LongStream.range(1, (long) Math.pow(2.0, books.size())).mapToObj(num -> {
            Set<Book> res = new HashSet<Book>();
            int i = 0;
            while (num != 0) {
                if (num % 2 == 1) {
                    res.add(books.get(i).getKey());
                }
                num = num >> 1;
                i++;
            }
            return res;
        }).map(bookSet -> new BookSet(bookSet)).map(bookSet -> {
            return new DfsNode(currentNode, bookSet, currentNode.getSpent() + bookSet.getPrice(), currentNode.getDepth() + 1);
        });

        //We sort them reverse because we are going to insert them in a stack
        return unsortedResult.sorted((o1, o2) -> o2.getSet().getBooks().size()-o1.getSet().getBooks().size()).collect(Collectors.toList());



    }

}
