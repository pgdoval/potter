package books;

import java.util.Map;
import java.util.Set;

/**
 * Created by pablo on 30/10/16.
 */
public class BookSet {

    private Set<Book> books;

    public BookSet(Set<Book> books) {
        this.books = books;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return books.stream().map((Book book)-> book.getName()).reduce((String a, String b)-> a+b).orElseGet(() -> "--");
    }

    public Double getPrice(Map<Integer, Double> discounts){
        int numBooks = books.size();

        double discount = discounts.getOrDefault(numBooks, 1.0);

        if(numBooks==0)
            return 0.0;
        //ugly way to get the price of one book (they're all the same)
        else
            return numBooks*books.iterator().next().getPrice()*discount;
    }

}
