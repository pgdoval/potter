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

        //Little optimization: if we find a cart with some element set to zero, we delete it right away
        //It's something we won't be able to do afterwards, because it would impact the rollback
        cart = _cart.entrySet().stream().filter(entry -> entry.getValue()>0).collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        discounts = _discounts;

        //The first node we include in the stack may be empty or not, depending on the cart
        stack.push(advanceInitialNodes());

        while(!stack.isEmpty())
        {
            processNode(stack.pop());
        }

        if(bestSolution==null)
            return 0.0;
        else
            return bestSolution.getSpent();
    }

    private DfsNode advanceInitialNodes(){
        /*
        * In this method we are assuming that if our cart consists of different quantities of n different books,
        * we can get packs of n books (one of each type) at least until the book from which we are buying less
        * units is n-1. We could have set that threshold to a lower value, say Math.ceil(n/2), but in this case
        * it's not bad to be a little bit more conservative.
        * */
        DfsNode node = new DfsNode(null, new BookSet(new HashSet<Book>()), 0.0, 0);

        long differentBooks = cart.entrySet().stream().count();

        if(differentBooks==0)
            return node;

        int minNumberOfBooks = cart.entrySet().stream().min((o1, o2) -> o1.getValue()-o2.getValue()).get().getValue();

        Set<Book> allBooks = cart.entrySet().stream().map(it -> it.getKey()).collect(Collectors.toSet());

        BookSet bookSet = new BookSet(allBooks);

        while(minNumberOfBooks>= differentBooks)
        {
            node = node.generateNext(bookSet,discounts);
            cart = modifyCart(cart, allBooks, false);
            minNumberOfBooks--;
        }

        node = node.generateNext(new BookSet(new HashSet<Book>()),discounts);

        return node;
    }

    public boolean isDone(Map<Book, Integer> cart)
    {
        if(cart.isEmpty())
            return true;
        return cart.values().stream().allMatch(v-> v<=0);
    }

    public Map<Book, Integer> modifyCart(Map<Book, Integer> cart, Set<Book> books, boolean returning)
    {
        /*
        * We add or subtract from the cart one existence of every book in the set,
        * depending on the returning value. This means:
         * if we are withdrawing elements because we are creating a node, returning will be false
         * if we are rolling back, then returning is true and we are adding those elements back
        * */
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

        /*
        * Instead of using function rollback we could have simply included a "current cart"
        * value on each node, but we would have had to clone the cart, which is a very expensive
        * operation. It's better to add and subtract elements, given that the problem allows us
        * to do that.
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
        generateNodes(currentNode, cart, discounts).forEach(node -> stack.push(node));

    }

    public List<DfsNode> generateNodes(DfsNode currentNode, Map<Book, Integer> cart, Map<Integer, Double> discounts)
    {
        //For generating all the possibilities, we only want to take into account the books
        //for which we still have existences in our cart
        List<Map.Entry<Book,Integer>> books = cart.entrySet().stream().filter(entry-> entry.getValue()>0).collect(Collectors.toList());


        //To explore all the possibilities, we generate an integer with the number of possibilities we have,
        //and then treat all the numbers as chunks of bits, with the i bit meaning "is book number i in this set?"
        //Then we generate the sets of books and then the nodes.
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
        })//We are assuming that given the nature of discounts, it's always better to get larger groups,
                //so we are discarding the smaller ones. Exactly, we get the largest half given the available books.
                .filter(set -> set.size() >= Math.ceil(((double)books.size()/2.0)))
                .map(bookSet -> new BookSet(bookSet)).map(bookSet -> {
                    return currentNode.generateNext(bookSet, discounts);
        });

        //We sort them reverse because we are going to insert them in a stack
        return unsortedResult.sorted((o1, o2) -> o1.getSet().getBooks().size()-o2.getSet().getBooks().size()).collect(Collectors.toList());



    }

}
