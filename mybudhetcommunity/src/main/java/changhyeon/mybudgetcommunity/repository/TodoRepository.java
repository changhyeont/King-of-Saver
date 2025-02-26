package changhyeon.mybudgetcommunity.repository;

import changhyeon.mybudgetcommunity.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
        List<Todo> findByUserIdAndDateOrderByCreatedAtAsc(Long userId, LocalDate date);

        @Query("SELECT t FROM Todo t WHERE t.user.id = :userId " +
                        "AND t.date BETWEEN :startDate AND :endDate")
        List<Todo> findByUserIdAndDateBetween(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}