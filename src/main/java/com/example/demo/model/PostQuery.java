package com.example.demo.model;


public class PostQuery {
    
    private final String searchKeyword;
    private final Integer tagId;
    private final Integer limit;
    private final Cursor cursor;
    
    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 50;
    
    public PostQuery(String searchKeyword, Integer tagId, Integer limit, Cursor cursor) {
        this.searchKeyword = searchKeyword;
        this.tagId = tagId;
        this.limit = limit != null ? limit : DEFAULT_LIMIT;
        this.cursor = cursor;
    }
    

    public void validate() {
        // q와 tagId 동시 사용 불가
        if (searchKeyword != null && tagId != null) {
            throw new IllegalArgumentException("Use either q or tagId (not both)");
        }
        
        // limit 범위 검증
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new IllegalArgumentException("Limit must be between 1 and " + MAX_LIMIT);
        }
    }
    
    public boolean isSearchQuery() {
        return searchKeyword != null && !searchKeyword.trim().isEmpty();
    }
    
    public boolean isTagFilterQuery() {
        return tagId != null;
    }
    
    public boolean isAllPostQuery() {
        return !isSearchQuery() && !isTagFilterQuery();
    }
    
    public String getSearchPattern() {
        if (!isSearchQuery()) {
            return null;
        }
        return "%" + searchKeyword.trim() + "%";
    }
    
    public int getHardLimit() {
        return limit + 1;
    }
    
    public String getSearchKeyword() {
        return searchKeyword;
    }
    
    public Integer getTagId() {
        return tagId;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public Cursor getCursor() {
        return cursor;
    }
    
    @Override
    public String toString() {
        return "PostQuery{" +
                "searchKeyword='" + searchKeyword + '\'' +
                ", tagId=" + tagId +
                ", limit=" + limit +
                ", cursor=" + cursor +
                '}';
    }
}
