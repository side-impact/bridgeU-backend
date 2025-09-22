package com.example.demo.dto.posts;

import com.example.demo.model.Post;
import com.example.demo.service.CursorCodec;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;


public class PostResponseDto {
    
    private List<PostItemDto> items;
    
    @JsonProperty("next_cursor")
    private String nextCursor;
    
    private Integer limit;
    
    @JsonProperty("has_more")
    private Boolean hasMore;
    
    public PostResponseDto() {}
    
    public PostResponseDto(List<PostItemDto> items, String nextCursor, Integer limit, Boolean hasMore) {
        this.items = items;
        this.nextCursor = nextCursor;
        this.limit = limit;
        this.hasMore = hasMore;
    }
    
    /**
     * Post 리스트로부터 PostResponseDto 생성
     * 
     * @param posts 게시물 리스트 (limit+1 개까지 포함될 수 있음)
     * @param requestedLimit 요청된 limit 값
     * @return PostResponseDto 객체
     */
    public static PostResponseDto from(List<Post> posts, Integer requestedLimit) {
        if (posts == null) {
            return new PostResponseDto(List.of(), null, requestedLimit, false);
        }
        
        boolean hasMore = posts.size() > requestedLimit;
        
        List<Post> actualPosts = hasMore ? 
            posts.subList(0, requestedLimit) : 
            posts;
        
        List<PostItemDto> items = actualPosts.stream()
            .map(PostItemDto::from)
            .collect(Collectors.toList());
        
        String nextCursor = null;
        if (hasMore && !actualPosts.isEmpty()) {
            Post lastPost = actualPosts.get(actualPosts.size() - 1);
            nextCursor = CursorCodec.encode(lastPost.toCursor());
        }
        
        return new PostResponseDto(items, nextCursor, requestedLimit, hasMore);
    }
    
    public List<PostItemDto> getItems() {
        return items;
    }
    
    public void setItems(List<PostItemDto> items) {
        this.items = items;
    }
    
    public String getNextCursor() {
        return nextCursor;
    }
    
    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public Boolean getHasMore() {
        return hasMore;
    }
    
    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }
}
