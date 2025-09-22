package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.hot.HotPostDto;
import com.example.demo.dto.post.PostDetailDto;
import com.example.demo.dto.posts.PostResponseDto;
import com.example.demo.dto.posts.CreatePostRequest;
import com.example.demo.dto.posts.CreatePostResponse;
import com.example.demo.dto.posts.UpdatePostRequest;
import com.example.demo.dto.posts.UpdatePostResponse;
import com.example.demo.model.User;
import com.example.demo.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 게시물 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api")
public class PostController {
    
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final PostService postService;
    
    public PostController(PostService postService) {
        this.postService = postService;
    }
    
    /**
     * 게시물 조회 API
     * GET /api/posts
     * 
     * @param q 검색어 (선택사항, title 또는 content_text에서 ILIKE 검색)
     * @param tagId 태그 ID (선택사항, 특정 태그로 필터링)
     * @param limit 조회 개수 (선택사항, 기본 20, 최대 50)
     * @param cursor 커서 (선택사항, 페이지네이션용 Base64 인코딩된 문자열)
     * @return 게시물 응답 데이터
     */
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPosts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer tagId,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) String cursor) {
        
        try {
            logger.info("API 호출: getPosts(q={}, tagId={}, limit={}, cursor={})", q, tagId, limit, cursor);
            PostResponseDto response = postService.getPosts(q, tagId, limit, cursor);
            logger.info("API 성공: 응답 데이터 반환");
            return ResponseEntity.ok(ApiResponse.success("Posts", response));
            
        } catch (IllegalArgumentException e) {
            // 도메인 로직에서 발생한 검증 오류
            logger.error("도메인 검증 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(e.getMessage()));
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.internalError("Failed to retrieve posts"));
        }
    }
    
    /**
     * 핫 게시물 조회 API
     * GET /api/posts/hot
     * 
     * @return 조회수 기준 상위 5개 핫 게시물 ('notice' 태그 제외)
     */
    @GetMapping("/posts/hot")
    public ResponseEntity<ApiResponse<List<HotPostDto>>> getHotPosts() {
        try {
            logger.info("API 호출: getHotPosts()");
            List<HotPostDto> hotPosts = postService.getTopHotPosts(5);
            logger.info("API 성공: {} 개의 핫 게시물 반환", hotPosts.size());
            return ResponseEntity.ok(ApiResponse.success("Top 5 hot posts", hotPosts));
            
        } catch (Exception e) {
            // 예상치 못한 오류
            logger.error("핫 게시물 조회 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.internalError("Failed to load hot posts"));
        }
    }
    
    /**
     * 게시물 상세 조회 API (조회수 증가 포함)
     * GET /api/posts/{post_id}
     * 
     * @param postId 조회할 게시물 ID
     * @param currentUser 현재 인증된 사용자 (JWT에서 추출)
     * @return 게시물 상세 정보 (블록, 작성자 정보, is_owner 포함)
     */
    @GetMapping("/posts/{post_id}")
    public ResponseEntity<ApiResponse<PostDetailDto>> getPostDetail(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal User currentUser) {
        try {
            Long currentUserId = currentUser != null ? currentUser.getId() : null;
            logger.info("API 호출: getPostDetail(postId={}, currentUserId={})", postId, currentUserId);
            PostDetailDto postDetail = postService.getPostDetail(postId, currentUserId);
            logger.info("API 성공: 게시물 상세 정보 반환 (postId={}, viewCount={}, isOwner={})", 
                postId, postDetail.getViewCount(), postDetail.getIsOwner());
            return ResponseEntity.ok(ApiResponse.success("Post detail", postDetail));
            
        } catch (PostService.PostNotFoundException e) {
            // 게시물을 찾을 수 없는 경우
            logger.warn("게시물을 찾을 수 없음: postId={}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "Post not found"));
                
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            logger.error("게시물 상세 조회 중 오류 발생: postId={}, error={}", postId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.internalError("Failed to load post detail"));
        }
    }
    
    /**
     * 게시물 생성 API
     * POST /api/posts
     * 
     * @param currentUser 현재 인증된 사용자 (JWT에서 추출)
     * @param request 게시물 생성 요청 데이터
     * @return 생성된 게시물 ID
     */
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreatePostRequest request) {
        
        Long currentUserId = currentUser.getId();
        logger.info("API 호출: createPost(userId={}, title={})", currentUserId, request.getTitle());
        
        CreatePostResponse response = postService.createPost(currentUserId, request);
        
        logger.info("API 성공: 게시물 생성 완료 (postId={})", response.getPost_id());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(201, "Post created", response));
    }
    
    /**
     * 게시물 수정 API (덮어쓰기 방식)
     * PUT /api/posts/{post_id}
     * 
     * @param postId 수정할 게시물 ID
     * @param currentUser 현재 인증된 사용자 (JWT에서 추출)
     * @param request 게시물 수정 요청 데이터
     * @return 수정된 게시물 응답
     */
    @PutMapping("/posts/{post_id}")
    public ResponseEntity<ApiResponse<UpdatePostResponse>> updatePost(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UpdatePostRequest request) {
        
        Long currentUserId = currentUser.getId();
        try {
            logger.info("API 호출: updatePost(postId={}, userId={}, title={})", 
                postId, currentUserId, request.getTitle());
            
            UpdatePostResponse response = postService.updatePost(postId, currentUserId, request);
            
            logger.info("API 성공: 게시물 수정 완료 (postId={})", postId);
            return ResponseEntity.ok(ApiResponse.success("Post updated", response));
            
        } catch (PostService.PostNotFoundException e) {
            // 게시물을 찾을 수 없는 경우
            logger.warn("게시물을 찾을 수 없음: postId={}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "Post not found"));
                
        } catch (PostService.UnauthorizedException e) {
            // 권한이 없는 경우
            logger.warn("권한 없음: postId={}, userId={}", postId, currentUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "You are not the owner of this post"));
                
        } catch (IllegalArgumentException e) {
            // 도메인 로직에서 발생한 검증 오류
            logger.error("도메인 검증 오류: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(e.getMessage()));
                
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            logger.error("게시물 수정 중 오류 발생: postId={}, userId={}, error={}", 
                postId, currentUserId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.internalError("Failed to update post"));
        }
    }
    
    /**
     * 게시물 삭제 API
     * DELETE /api/posts/{post_id}
     * 
     * @param postId 삭제할 게시물 ID
     * @param currentUser 현재 인증된 사용자 (JWT에서 추출)
     * @return 삭제 결과
     */
    @DeleteMapping("/posts/{post_id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("post_id") Long postId,
            @AuthenticationPrincipal User currentUser) {
        
        Long currentUserId = currentUser.getId();
        try {
            logger.info("API 호출: deletePost(postId={}, userId={})", postId, currentUserId);
            
            postService.deletePost(postId, currentUserId);
            
            logger.info("API 성공: 게시물 삭제 완료 (postId={})", postId);
            return ResponseEntity.ok(ApiResponse.success("Post deleted", null));
            
        } catch (PostService.PostNotFoundException e) {
            // 게시물을 찾을 수 없는 경우
            logger.warn("게시물을 찾을 수 없음: postId={}", postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, "Post not found"));
                
        } catch (PostService.UnauthorizedException e) {
            // 권한이 없는 경우
            logger.warn("권한 없음: postId={}, userId={}", postId, currentUserId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "You are not the owner of this post"));
                
        } catch (Exception e) {
            // 기타 예상치 못한 오류
            logger.error("게시물 삭제 중 오류 발생: postId={}, userId={}, error={}", 
                postId, currentUserId, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body(ApiResponse.internalError("Failed to delete post"));
        }
    }
}
