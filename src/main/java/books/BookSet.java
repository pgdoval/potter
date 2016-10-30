package books;

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

    public Double getPrice(){
        int numBooks = books.size();

        if(numBooks==0)
            return 0.0;
            else return numBooks*books.iterator().next().getPrice()*getBookDiscount(numBooks);
    }

    private double getBookDiscount(int numBooks) {
        switch (numBooks){
            case 1:
                return 1.0;
            case 2:
                return 0.95;
            case 3:
                return 0.9;
            case 4:
                return 0.8;
            case 5:
                return 0.75;
            default:
                return -1.0;
        }
    }
}
