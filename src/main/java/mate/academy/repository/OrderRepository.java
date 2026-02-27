package mate.academy.repository;

import mate.academy.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems"})
    Page<Order> findByUserEmail(String userEmail, Pageable pageable);

    @EntityGraph(attributePaths = {"orderItems"})
    Order findOrderByUserEmailAndId(String userEmail, Long id);
}
