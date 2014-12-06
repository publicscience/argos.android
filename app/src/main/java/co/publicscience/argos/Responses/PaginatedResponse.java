package co.publicscience.argos.Responses;

import java.util.ArrayList;

public class PaginatedResponse<T> {
    private ArrayList<T> results;
    public ArrayList<T> getResults() {
        return results;
    }

    private Pagination pagination;
    public Pagination getPagination() {
        return pagination;
    }

    public static class Pagination {
        public Integer page;
        public Integer per_page;
        public Integer total_count;
    }
}
