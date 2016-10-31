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
    private Map<Integer, Double> discounts;
    private DfsNode bestSolution = null;

    private Stack<DfsNode> stack = new Stack<>();

    public DfsDiscounter() {

    }

    public double getMinPrice(Map<Book, Integer> _cart, Map<Integer, Double> _discounts) {
        /*
        * We are going to suppose that the discounts are compliant with the
        * rule that the best discount is contained into the set of discounts
        * with minimum depth (that is, the minimum number of booksets, which means
        * that each bookset has many books), which I feel is a supposition we can assume
        * because in real life it's what discounts are all about.
        * */

        if(_cart==null || isDone(_cart))
            return 0.0;

        cart = _cart;
        discounts = _discounts;

        stack.push(new DfsNode(null, new BookSet(new HashSet<Book>()), 0.0, 0));

        while(!stack.isEmpty())
        {
            processNode(stack.pop());
        }

        if(bestSolution==null)
            return 0.0;
        else
            return bestSolution.getSpent();
    }


    public boolean isDone(Map<Book, Integer> cart)
    {
        if(cart.isEmpty())
            return true;
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

        return cart;

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
            if(bestSolution==null || bestSolution.getSpent() > currentNode.getSpent())
            {
                bestSolution = currentNode;
            }

            if(!stack.isEmpty())
                cart = rollback(currentNode, cart, stack.peek().getFather());

            return;
        }
        //Once we reached this point, we won't get any better in this branch
        if(bestSolution!=null && currentNode.getDepth() >= bestSolution.getDepth())
        {
            if(!stack.isEmpty())
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
            return new DfsNode(currentNode, bookSet, currentNode.getSpent() + bookSet.getPrice(discounts), currentNode.getDepth() + 1);
        });

        //We are assuming that given the nature of discounts, it's always better to get larger groups,
        //so we are discarding the smaller ones. Exactly, we get the largest half given the available books.
        unsortedResult = unsortedResult.filter(node -> node.getSet().getBooks().size() >= Math.ceil(((double)books.size()/2.0)));

        //We sort them reverse because we are going to insert them in a stack
        return unsortedResult.sorted((o1, o2) -> o1.getSet().getBooks().size()-o2.getSet().getBooks().size()).collect(Collectors.toList());



    }

}
