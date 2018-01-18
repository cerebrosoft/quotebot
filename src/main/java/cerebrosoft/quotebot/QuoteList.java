package cerebrosoft.quotebot;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QuoteList {

    private List<String> quoteList = new ArrayList<>();

    public List<String> getQuoteList() {
        return quoteList;
    }

    public void setQuoteList(List<String> quoteList) {
        this.quoteList = quoteList;
    }

    public static void main(String[] args) throws Exception {
        QuoteList list = new QuoteList();
        ObjectMapper mapper = new ObjectMapper();
        list.getQuoteList().add("quote 1");
        list.getQuoteList().add("quote 2");
        String value = mapper.writeValueAsString(list);
        System.out.println(value);
    }

}
