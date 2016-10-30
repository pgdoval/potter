package books;

/**
 * Created by pablo on 30/10/16.
 */
public class Book {

    private String name;
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Book(String name, Double price) {
        this.name = name;
        this.price = price;
    }

}
