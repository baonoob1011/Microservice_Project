package ProductService.repository;

import ProductService.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    /**
     * Cập nhật stock của sản phẩm.
     * - quantity > 0: giảm stock (bán hàng)
     * - quantity < 0: tăng stock (rollback)
     * Tránh stock âm khi bán.
     *
     * @param productId id sản phẩm
     * @param quantity số lượng thay đổi (dương: trừ, âm: cộng)
     * @return số bản ghi bị ảnh hưởng
     */
    @Modifying
    @Transactional
    @Query("UPDATE Product p " +
            "SET p.quantity = CASE " +
            "WHEN :quantity > 0 AND p.quantity >= :quantity THEN p.quantity - :quantity " +
            "WHEN :quantity <= 0 THEN p.quantity - :quantity " +  // -(-N) = +N
            "ELSE p.quantity END " +
            "WHERE p.id = :productId")
    int updateStock(@Param("productId") Long productId, @Param("quantity") long quantity);
}
