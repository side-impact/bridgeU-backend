package com.example.demo.repository;

import com.example.demo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * 검색어로 게시물 조회 (title 또는 content_text에서 ILIKE 검색)
     * 
     * @param likePattern 검색 패턴 (%keyword% 형식)
     * @param cursorCreatedAt 커서의 created_at (null이면 처음부터)
     * @param cursorId 커서의 id (null이면 처음부터)
     * @param hardLimit 조회할 최대 개수 (hasMore 확인을 위해 limit+1)
     * @return 검색 결과 리스트
     */
    @Query(value = """
        SELECT 
            p.id as post_id,
            p.tags,
            p.title,
            p.content_text,
            p.created_at,
            p.view_count,
            u.id,
            u.name,
            (SELECT pb.image_url 
             FROM post_blocks pb 
             WHERE pb.post_id = p.id AND pb.type = 'image' 
             ORDER BY pb.idx LIMIT 1) as first_image_url
        FROM posts p
        LEFT JOIN users u ON p.author_id = u.id
        WHERE 
            (p.title ILIKE :likePattern OR p.content_text ILIKE :likePattern)
            AND (CAST(:cursorCreatedAt AS timestamptz) IS NULL 
                 OR (p.created_at < CAST(:cursorCreatedAt AS timestamptz)) 
                 OR (p.created_at = CAST(:cursorCreatedAt AS timestamptz) AND p.id < :cursorId))
        ORDER BY p.created_at DESC, p.id DESC
        LIMIT :hardLimit
        """, nativeQuery = true)
    List<PostProjection> findPostSearch(
        @Param("likePattern") String likePattern,
        @Param("cursorCreatedAt") Instant cursorCreatedAt,
        @Param("cursorId") Long cursorId,
        @Param("hardLimit") int hardLimit
    );
    
    /**
     * 특정 태그로 게시물 조회 (tags 배열에 tagId 포함)
     * 
     * @param tagId 필터링할 태그 ID
     * @param cursorCreatedAt 커서의 created_at (null이면 처음부터)
     * @param cursorId 커서의 id (null이면 처음부터)
     * @param hardLimit 조회할 최대 개수 (hasMore 확인을 위해 limit+1)
     * @return 태그 필터링 결과 리스트
     */
    @Query(value = """
        SELECT 
            p.id as post_id,
            p.tags,
            p.title,
            p.content_text,
            p.created_at,
            p.view_count,
            u.id,
            u.name,
            (SELECT pb.image_url 
             FROM post_blocks pb 
             WHERE pb.post_id = p.id AND pb.type = 'image' 
             ORDER BY pb.idx LIMIT 1) as first_image_url
        FROM posts p
        LEFT JOIN users u ON p.author_id = u.id
        WHERE 
            :tagId = ANY(p.tags)
            AND (CAST(:cursorCreatedAt AS timestamptz) IS NULL 
                 OR (p.created_at < CAST(:cursorCreatedAt AS timestamptz)) 
                 OR (p.created_at = CAST(:cursorCreatedAt AS timestamptz) AND p.id < :cursorId))
        ORDER BY p.created_at DESC, p.id DESC
        LIMIT :hardLimit
        """, nativeQuery = true)
    List<PostProjection> findPostByTag(
        @Param("tagId") int tagId,
        @Param("cursorCreatedAt") Instant cursorCreatedAt,
        @Param("cursorId") Long cursorId,
        @Param("hardLimit") int hardLimit
    );
    
    /**
     * 전체 게시물 조회 (필터 없음)
     * 
     * @param cursorCreatedAt 커서의 created_at (null이면 처음부터)
     * @param cursorId 커서의 id (null이면 처음부터)
     * @param hardLimit 조회할 최대 개수 (hasMore 확인을 위해 limit+1)
     * @return 전체 게시물 리스트
     */
    @Query(value = """
        SELECT 
            p.id as post_id,
            p.tags,
            p.title,
            p.content_text,
            p.created_at,
            p.view_count,
            u.id,
            u.name,
            (SELECT pb.image_url 
             FROM post_blocks pb 
             WHERE pb.post_id = p.id AND pb.type = 'image' 
             ORDER BY pb.idx LIMIT 1) as first_image_url
        FROM posts p
        LEFT JOIN users u ON p.author_id = u.id
        WHERE 
            (CAST(:cursorCreatedAt AS timestamptz) IS NULL 
             OR (p.created_at < CAST(:cursorCreatedAt AS timestamptz)) 
             OR (p.created_at = CAST(:cursorCreatedAt AS timestamptz) AND p.id < :cursorId))
        ORDER BY p.created_at DESC, p.id DESC
        LIMIT :hardLimit
        """, nativeQuery = true)
    List<PostProjection> findPostAll(
        @Param("cursorCreatedAt") Instant cursorCreatedAt,
        @Param("cursorId") Long cursorId,
        @Param("hardLimit") int hardLimit
    );
    
    /**
     * 조회수 기준 상위 핫 게시물 조회 ('notice' 태그 제외)
     * 
     * @param hardLimit 조회할 최대 개수
     * @return 핫 게시물 리스트 (post_id, tags, title, view_count)
     */
    @Query(value = """
        SELECT 
            p.id AS post_id,
            p.tags,
            p.title,
            p.view_count
        FROM posts p
        WHERE NOT EXISTS (
            SELECT 1
            FROM tags t
            WHERE t.id = ANY (p.tags) AND t."group" = 'notice'
        )
        ORDER BY p.view_count DESC, p.created_at DESC, p.id DESC
        LIMIT :hardLimit
        """, nativeQuery = true)
    List<Object[]> findTopHot(@Param("hardLimit") int hardLimit);
    
    /**
     * 조회수 원자적 증가 (UPDATE 문 활용)
     * 
     * @param postId 증가할 게시물 ID
     */
    @Modifying
    @Query(value = "UPDATE posts SET view_count = view_count + 1 WHERE id = :postId", nativeQuery = true)
    void incrementView(@Param("postId") Long postId);
    
    /**
     * 게시물 상세 정보 조회 (작성자 정보 포함)
     * 
     * @param postId 조회할 게시물 ID
     * @return 게시물 상세 정보 배열 (null이면 존재하지 않음)
     */
    @Query(value = """
        SELECT p.id AS post_id, p.tags, p.title, 'en' AS lang, p.view_count, p.content_text,
               p.created_at, p.updated_at,
               u.id, u.name
        FROM posts p
        LEFT JOIN users u ON u.id = p.author_id
        WHERE p.id = :postId
        """, nativeQuery = true)
    Object[] findPostDetailRow(@Param("postId") Long postId);
    
    /**
     * 게시물의 블록들 조회 (순서대로)
     * 
     * @param postId 조회할 게시물 ID
     * @return 블록 정보 리스트 (idx 순서대로 정렬)
     */
    @Query(value = """
        SELECT idx, type, text, image_url
        FROM post_blocks
        WHERE post_id = :postId
        ORDER BY idx ASC
        """, nativeQuery = true)
    List<Object[]> findBlocksByPost(@Param("postId") Long postId);
}
