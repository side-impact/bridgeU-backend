package com.example.demo.service;

import com.example.demo.dto.hot.HotPostDto;
import com.example.demo.dto.post.BlockDto;
import com.example.demo.dto.post.PostDetailDto;
import com.example.demo.dto.posts.PostItemDto.AuthorDto;
import com.example.demo.dto.posts.PostResponseDto;
import com.example.demo.dto.posts.CreatePostRequest;
import com.example.demo.dto.posts.CreatePostResponse;
import com.example.demo.dto.posts.UpdatePostRequest;
import com.example.demo.dto.posts.UpdatePostResponse;
import com.example.demo.dto.posts.BlockRequest;
import com.example.demo.model.Author;
import com.example.demo.model.Cursor;
import com.example.demo.model.Post;
import com.example.demo.model.PostQuery;
import com.example.demo.repository.PostProjection;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.PostRepositoryImpl;
import com.example.demo.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PostService {
    
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    
    private final PostRepository postRepository;
    private final PostRepositoryImpl postRepositoryImpl;
    
    public PostService(PostRepository postRepository, PostRepositoryImpl postRepositoryImpl) {
        this.postRepository = postRepository;
        this.postRepositoryImpl = postRepositoryImpl;
    }
    
    /**
     * 게시물 조회
     * 
     * @param q 검색어 (선택사항)
     * @param tagId 태그 ID (선택사항)
     * @param limit 조회 개수 (기본 20, 최대 50)
     * @param cursor 커서 (선택사항)
     * @return 게시물 응답 DTO
     * @throws IllegalArgumentException 잘못된 쿼리 파라미터인 경우
     */
    public PostResponseDto getPosts(String q, Integer tagId, Integer limit, String cursor) {
        Optional<Cursor> cursorOpt = CursorCodec.decode(cursor);
        
        PostQuery query = new PostQuery(q, tagId, limit, cursorOpt.orElse(null));
        query.validate();
        
        List<PostProjection> projections = executeQuery(query);
        
        List<Post> posts = projections.stream()
            .map(this::toPost)
            .collect(Collectors.toList());
        
        return PostResponseDto.from(posts, query.getLimit());
    }
    
    private List<PostProjection> executeQuery(PostQuery query) {
        Cursor cursor = query.getCursor();
        
        if (query.isSearchQuery()) {
            return postRepository.findPostSearch(
                query.getSearchPattern(),
                cursor != null ? cursor.getCreatedAt() : null,
                cursor != null ? cursor.getId() : null,
                query.getHardLimit()
            );
        } else if (query.isTagFilterQuery()) {
            return postRepository.findPostByTag(
                query.getTagId(),
                cursor != null ? cursor.getCreatedAt() : null,
                cursor != null ? cursor.getId() : null,
                query.getHardLimit()
            );
        } else {
            return postRepository.findPostAll(
                cursor != null ? cursor.getCreatedAt() : null,
                cursor != null ? cursor.getId() : null,
                query.getHardLimit()
            );
        }
    }
    
    private Post toPost(PostProjection projection) {
        Author author = null;
        if (projection.getUserId() != null) {
            author = new Author(projection.getUserId(), projection.getUsername());
        }
        List<Integer> tags = safeConvertTags(projection.getTags());
        
        return new Post(
            projection.getPostId(),
            tags,
            projection.getTitle(),
            projection.getContentText(),
            projection.getFirstImageUrl(),
            projection.getCreatedAt(),
            projection.getViewCount(),
            author
        );
    }
    
    private List<Integer> safeConvertTags(Integer[] tagsArray) {
        if (tagsArray == null) {
            return List.of();
        }
        
        try {
            return Arrays.asList(tagsArray);
        } catch (Exception e) {
            logger.warn("Tags 배열 변환 실패: {}", e.getMessage());
            return List.of();
        }
    }
    
    public List<HotPostDto> getTopHotPosts(int limit) {
        List<Object[]> results = postRepository.findTopHot(limit);
        return results.stream()
                .map(this::mapToHotPostDto)
                .collect(Collectors.toList());
    }

    private HotPostDto mapToHotPostDto(Object[] row) {
        Long postId = ((Number) row[0]).longValue();
        List<Integer> tags = toIntList(row[1]);
        String title = row[2] != null ? row[2].toString() : "";
        Long viewCount = ((Number) row[3]).longValue();
        
        return new HotPostDto(postId, tags, title, viewCount);
    }
    
    private static List<Integer> toIntList(Object col) {
        if (col == null) {
            return List.of();
        }
        try {
            if (col instanceof Integer[] x) {
                return Arrays.asList(x);
            }
            if (col instanceof int[] x) {
                return Arrays.stream(x).boxed().collect(Collectors.toList());
            }
            if (col instanceof java.sql.Array a) {
                Object arr = a.getArray();
                if (arr instanceof Integer[] x) {
                    return Arrays.asList(x);
                }
                if (arr instanceof int[] x) {
                    return Arrays.stream(x).boxed().collect(Collectors.toList());
                }
                if (arr instanceof Object[] x) {
                    return Arrays.stream(x).map(v -> Integer.valueOf(v.toString())).collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
        }
        return List.of();
    }
    
    /**
     * 게시물 상세 정보 조회
     * 
     * @param postId 조회할 게시물 ID
     * @param currentUserId 현재 사용자 ID (선택)
     * @return 게시물 상세 정보 DTO
     * @throws PostNotFoundException
     */
    @Transactional
    public PostDetailDto getPostDetail(Long postId, Long currentUserId) {
        Object[] postRow = postRepository.findPostDetailRow(postId);
        if (postRow == null) {
            throw new PostNotFoundException("Post not found");
        }
        postRepository.incrementView(postId);
        List<Object[]> blockRows = postRepository.findBlocksByPost(postId);
    
        return mapToPostDetailDto(postRow, blockRows, currentUserId);
    }
    

    private PostDetailDto mapToPostDetailDto(Object[] postRow, List<Object[]> blockRows, Long currentUserId) {
        Object[] actualData;
        if (postRow.length > 0 && postRow[0] instanceof Object[]) {
            actualData = (Object[]) postRow[0];
        } else {
            actualData = postRow;
        }
        
        if (actualData.length == 0) {
            throw new PostNotFoundException("Post data is empty");
        }
        
        Long postId = ((Number) actualData[0]).longValue();
        List<Integer> tags = toIntList(actualData[1]);
        String title = actualData[2] != null ? actualData[2].toString() : "";
        String lang = actualData[3] != null ? actualData[3].toString() : "en";
        Long viewCount = ((Number) actualData[4]).longValue() + 1; // 증가된 조회수 반영
        String contentText = actualData[5] != null ? actualData[5].toString() : "";
        
        String createdAt = DateTimeUtil.formatToFeed(toInstant(actualData[6]));
        String updatedAt = DateTimeUtil.formatToFeed(toInstant(actualData[7]));
        
        AuthorDto author = null;
        if (actualData[8] != null) {
            Long userId = ((Number) actualData[8]).longValue();
            String username = actualData[9] != null ? actualData[9].toString() : "";
            author = new AuthorDto(userId, username);
        }
        
        List<BlockDto> blocks = blockRows.stream()
                .map(this::mapToBlockDto)
                .collect(Collectors.toList());
        
        Boolean isOwner = false;
        if (currentUserId != null && author != null) {
            isOwner = currentUserId.equals(author.getUserId());
        }
        
        return new PostDetailDto(
                postId, tags, title, author, createdAt, updatedAt,
                lang, viewCount, blocks, contentText, isOwner
        );
    }
    
    private BlockDto mapToBlockDto(Object[] blockRow) {
        Integer idx = ((Number) blockRow[0]).intValue();
        String type = blockRow[1] != null ? blockRow[1].toString() : "";
        String text = blockRow[2] != null ? blockRow[2].toString() : null;
        String imageUrl = blockRow[3] != null ? blockRow[3].toString() : null;
        
        return new BlockDto(idx, type, text, imageUrl);
    }
    
    private static Instant toInstant(Object timeValue) {
        if (timeValue == null) return null;
        
        try {
            if (timeValue instanceof Instant) {
                return (Instant) timeValue;
            } else if (timeValue instanceof Timestamp) {
                return ((Timestamp) timeValue).toInstant();
            } else if (timeValue instanceof OffsetDateTime) {
                return ((OffsetDateTime) timeValue).toInstant();
            } else if (timeValue instanceof LocalDateTime) {
                return ((LocalDateTime) timeValue).atZone(java.time.ZoneOffset.UTC).toInstant();
            }
        } catch (Exception e) {
            // 시간 변환 실패 시 null 반환
        }
        
        return null;
    }
    
    /**
     * 게시물 생성
     * 
     * @param currentUserId 현재 사용자 ID
     * @param request 게시물 생성 요청
     * @return 생성된 게시물 ID
     * @throws InvalidUserException
     * @throws IllegalArgumentException
     * @throws DatabaseConstraintException
     */
    @Transactional
    public CreatePostResponse createPost(Long currentUserId, CreatePostRequest request) {
        try {
            checkUserExists(currentUserId);
            validateCreatePostRequest(request);
            validateTags(request.getTags());
            validateBlocks(request.getBlocks());
            
            try {
                Long postId = postRepositoryImpl.createPost(
                    currentUserId, 
                    request.getLang(), 
                    request.getTitle(), 
                    request.getTags()
                );
                
                postRepositoryImpl.createPostBlocks(postId, request.getBlocks());
                
                logger.info("게시물 생성 완료: postId={}", postId);
                return new CreatePostResponse(postId);
                
            } catch (SQLException e) {
                logger.error("게시물 생성 실패: userId={}, SQLState={}", currentUserId, e.getSQLState(), e);
                if ("23514".equals(e.getSQLState())) {
                    throw new DatabaseConstraintException(e.getMessage());
                }
                throw new RuntimeException("Database error during post creation", e);
            }
            
        } catch (Exception e) {
            logger.error("게시물 생성 실패: userId={}", currentUserId, e);
            throw e;
        }
    }
    
    private void checkUserExists(Long userId) {
        boolean exists = postRepositoryImpl.userExists(userId);
        if (!exists) {
            throw new InvalidUserException("User not found: " + userId);
        }
    }
    
    private void validateCreatePostRequest(CreatePostRequest request) {
        String title = request.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (title.length() > 60) {
            throw new IllegalArgumentException("Title must be 60 characters or less");
        }
        
        String lang = request.getLang();
        if (!Arrays.asList("en", "ja", "zh-CN", "ko").contains(lang)) {
            throw new IllegalArgumentException("Invalid language code: " + lang);
        }
        
        if (request.getBlocks() == null || request.getBlocks().isEmpty()) {
            throw new IllegalArgumentException("Blocks are required");
        }
    }
    
    private void validateTags(List<Integer> tagIds) {
        if (tagIds.isEmpty()) {
            throw new IllegalArgumentException("At least one tag is required");
        }
        
        List<Object[]> tagResults = postRepositoryImpl.validateTags(tagIds);
        
        if (tagResults.size() != tagIds.size()) {
            throw new IllegalArgumentException("Some tags do not exist");
        }
        
        for (Object[] tagRow : tagResults) {
            Boolean isUserSelectable = (Boolean) tagRow[1];
            String group = (String) tagRow[2];
            
            if (!isUserSelectable || "notice".equals(group)) {
                throw new IllegalArgumentException("Tag with ID " + tagRow[0] + " is not user selectable");
            }
        }
    }
    

    private void validateBlocks(List<BlockRequest> blocks) {
        // 블록 개수 최대 20
        if (blocks.size() > 20) {
            throw new IllegalArgumentException("Too many blocks. Maximum is 20.");
        }
        
        //이미지 최대 6개
        long imageBlockCount = blocks.stream()
            .filter(block -> "image".equals(block.getType()))
            .count();
        if (imageBlockCount > 6) {
            throw new IllegalArgumentException("Too many image blocks. Maximum is 6.");
        }
        
        //idx가 0~N-1
        Set<Integer> indices = blocks.stream()
            .map(BlockRequest::getIdx)
            .collect(Collectors.toSet());
        
        Set<Integer> expectedIndices = IntStream.range(0, blocks.size())
            .boxed()
            .collect(Collectors.toSet());
        
        if (!indices.equals(expectedIndices)) {
            throw new IllegalArgumentException("Block indices must be unique and contiguous starting from 0");
        }
        
        for (BlockRequest block : blocks) {
            validateBlock(block);
        }
    }
    
    private void validateBlock(BlockRequest block) {
        String type = block.getType();
        String text = block.getText();
        String imageUrl = block.getImage_url();
        
        if ("paragraph".equals(type)) {
            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Paragraph block must have non-empty text");
            }
            if (imageUrl != null) {
                throw new IllegalArgumentException("Paragraph block must not have image_url");
            }
        } else if ("image".equals(type)) {
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                throw new IllegalArgumentException("Image block must have image_url");
            }
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                throw new IllegalArgumentException("Image URL must start with http:// or https://");
            }
            if (text != null) {
                throw new IllegalArgumentException("Image block must not have text");
            }
        } else {
            throw new IllegalArgumentException("Invalid block type: " + type);
        }
    }
    
    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(String message) {
            super(message);
        }
    }
    
    public static class InvalidUserException extends RuntimeException {
        public InvalidUserException(String message) {
            super(message);
        }
    }
    
    public static class DatabaseConstraintException extends RuntimeException {
        public DatabaseConstraintException(String message) {
            super(message);
        }
    }
    
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    
    /**
     * 게시물 작성자 권한 확인
     * 
     * @param postId
     * @param currentUserId
     * @throws PostNotFoundException
     * @throws UnauthorizedException
     */
    private void validatePostOwnership(Long postId, Long currentUserId) {
        Long authorId = postRepositoryImpl.getPostAuthorId(postId);
        
        if (authorId == null) {
            throw new PostNotFoundException("Post not found: " + postId);
        }
        
        if (!authorId.equals(currentUserId)) {
            throw new UnauthorizedException("You are not the owner of this post");
        }
    }
    
    /**
     * 게시물 업데이트 (MVP 덮어쓰기 방식) -> TODO 차이만 업데이트트
     * 
     * @param postId
     * @param currentUserId
     * @param request 업데이트 요청 데이터
     * @return 업데이트 응답
     * @throws PostNotFoundException
     * @throws UnauthorizedException
     * @throws IllegalArgumentException
     * @throws DatabaseConstraintException
     */
    @Transactional
    public UpdatePostResponse updatePost(Long postId, Long currentUserId, UpdatePostRequest request) {
        try {
            validatePostOwnership(postId, currentUserId);
            validateCreatePostRequest(request);
            validateTags(request.getTags());
            validateBlocks(request.getBlocks());
            
            try {
                postRepositoryImpl.updatePost(
                    postId,
                    request.getLang(),
                    request.getTitle(),
                    request.getTags(),
                    request.getBlocks()
                );
                
                logger.info("게시물 업데이트 완료: postId={}", postId);
                return new UpdatePostResponse(postId);
                
            } catch (SQLException e) {
                logger.error("게시물 업데이트 실패: postId={}, SQLState={}", postId, e.getSQLState(), e);
                if ("23514".equals(e.getSQLState())) {
                    throw new DatabaseConstraintException(e.getMessage());
                }
                throw new RuntimeException("Database error during post update", e);
            }
            
        } catch (Exception e) {
            logger.error("게시물 업데이트 실패: postId={}, userId={}", postId, currentUserId, e);
            throw e;
        }
    }
    
    /**
     * 게시물 삭제
     * 
     * @param postId 
     * @param currentUserId
     * @throws PostNotFoundException
     * @throws UnauthorizedException
     */
    @Transactional
    public void deletePost(Long postId, Long currentUserId) {
        try {
            validatePostOwnership(postId, currentUserId);
            
            try {
                postRepositoryImpl.deletePost(postId);
                logger.info("게시물 삭제 완료: postId={}", postId);
                
            } catch (SQLException e) {
                logger.error("게시물 삭제 실패: postId={}, SQLState={}", postId, e.getSQLState(), e);
                throw new RuntimeException("Database error during post deletion", e);
            }
            
        } catch (Exception e) {
            logger.error("게시물 삭제 실패: postId={}, userId={}", postId, currentUserId, e);
            throw e;
        }
    }
    

    private void validateCreatePostRequest(UpdatePostRequest request) {
        String title = request.getTitle();
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (title.length() > 60) {
            throw new IllegalArgumentException("Title must be 60 characters or less");
        }
        
        String lang = request.getLang();
        if (!Arrays.asList("en", "ja", "zh-CN", "ko").contains(lang)) {
            throw new IllegalArgumentException("Invalid language code: " + lang);
        }
        
        if (request.getBlocks() == null || request.getBlocks().isEmpty()) {
            throw new IllegalArgumentException("Blocks are required");
        }
    }
}
