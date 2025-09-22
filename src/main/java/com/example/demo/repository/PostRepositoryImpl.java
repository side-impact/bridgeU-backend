package com.example.demo.repository;

import com.example.demo.dto.posts.BlockRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

@Repository
public class PostRepositoryImpl {
    
    private static final Logger logger = LoggerFactory.getLogger(PostRepositoryImpl.class);
    
    private final JdbcTemplate jdbcTemplate;
    
    public PostRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public boolean userExists(Long userId) {
        String sql = "SELECT 1 FROM users WHERE id = ?";
        List<Integer> result = jdbcTemplate.queryForList(sql, Integer.class, userId);
        return !result.isEmpty();
    }
    
    public List<Object[]> validateTags(List<Integer> tagIds) {
        if (tagIds.isEmpty()) {
            return List.of();
        }
        
        String sql = """
            SELECT id, is_user_selectable, "group"
            FROM tags
            WHERE id = ANY(?)
            """;
        
        Integer[] tagArray = tagIds.toArray(new Integer[0]);
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Object[]{
            rs.getInt("id"),
            rs.getBoolean("is_user_selectable"),
            rs.getString("group")
        }, (Object) tagArray);
    }
    
    /**
     * 게시물 생성
     * 
     * @param authorId 작성자 ID
     * @param lang 언어 코드
     * @param title 제목
     * @param tagIds 태그 ID 목록
     * @return 생성된 게시물 ID
     * @throws SQLException SQL 실행 오류
     */
    public Long createPost(Long authorId, String lang, String title, List<Integer> tagIds) throws SQLException {
        String sql = """
            INSERT INTO posts(author_id, title, tags)
            VALUES (?, ?, ?)
            RETURNING id
            """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, authorId);
                ps.setString(2, title);
                
                // Integer[] 배열을 PostgreSQL array로 변환
                Integer[] tagArray = tagIds.toArray(new Integer[0]);
                java.sql.Array sqlArray = connection.createArrayOf("integer", tagArray);
                ps.setArray(3, sqlArray);
                
                return ps;
            }, keyHolder);
            
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new SQLException("Failed to get generated post ID");
            }
            
            return key.longValue();
            
        } catch (Exception e) {
            logger.error("게시물 생성 실패: authorId={}", authorId, e);
            throw e;
        }
    }
    
    /**
     * 게시물 블록들 배치 삽입
     * 
     * @param postId 게시물 ID
     * @param blocks 블록 목록
     * @throws SQLException SQL 실행 오류
     */
    public void createPostBlocks(Long postId, List<BlockRequest> blocks) throws SQLException {
        String sql = """
            INSERT INTO post_blocks(post_id, idx, type, text, image_url)
            VALUES (?, ?, ?::post_block_type, ?, ?)
            """;
        
        jdbcTemplate.batchUpdate(sql, blocks, blocks.size(), (ps, block) -> {
            ps.setLong(1, postId);
            ps.setInt(2, block.getIdx());
            ps.setString(3, block.getType());
            
            if ("paragraph".equals(block.getType())) {
                ps.setString(4, block.getText());
                ps.setNull(5, Types.VARCHAR);
            } else if ("image".equals(block.getType())) {
                ps.setNull(4, Types.VARCHAR);
                ps.setString(5, block.getImage_url());
            }
        });
    }
    
    /**
     * 게시물 작성자 ID 조회 (권한 검증용)
     * 
     * @param postId 게시물 ID
     * @return 작성자 ID (게시물이 없으면 null)
     */
    public Long getPostAuthorId(Long postId) {
        String sql = "SELECT author_id FROM posts WHERE id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, postId);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 게시물 업데이트 (덮어쓰기 방식)
     * 1. 기존 post_blocks 삭제
     * 2. posts 테이블 업데이트 
     * 3. 새로운 post_blocks 삽입
     * 
     * @param postId 게시물 ID
     * @param lang 언어 코드
     * @param title 제목
     * @param tagIds 태그 ID 목록
     * @param blocks 블록 목록
     * @throws SQLException SQL 실행 오류
     */
    public void updatePost(Long postId, String lang, String title, List<Integer> tagIds, 
                          List<BlockRequest> blocks) throws SQLException {
        try {
            // 기존 블록 삭제
            String deleteBlocksSql = "DELETE FROM post_blocks WHERE post_id = ?";
            jdbcTemplate.update(deleteBlocksSql, postId);
            
            String updatePostSql = """
                UPDATE posts 
                SET title = ?, tags = ?
                WHERE id = ?
                """;
            
            Integer[] tagArray = tagIds.toArray(new Integer[0]);
            java.sql.Array sqlArray = jdbcTemplate.getDataSource().getConnection().createArrayOf("integer", tagArray);
            
            jdbcTemplate.update(updatePostSql, title, sqlArray, postId);
            
            // 새로운 블록 생성
            createPostBlocks(postId, blocks);
            
        } catch (Exception e) {
            logger.error("게시물 업데이트 실패: postId={}", postId, e);
            throw e;
        }
    }
    
    /**
     * 게시물 삭제 (블록들도 함께 삭제됨 - CASCADE)
     * 
     * @param postId 삭제할 게시물 ID
     * @throws SQLException SQL 실행 오류
     */
    public void deletePost(Long postId) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        
        try {
            int deletedCount = jdbcTemplate.update(sql, postId);
            if (deletedCount == 0) {
                logger.warn("삭제할 게시물을 찾을 수 없음: postId={}", postId);
            }
        } catch (Exception e) {
            logger.error("게시물 삭제 실패: postId={}", postId, e);
            throw e;
        }
    }
}
