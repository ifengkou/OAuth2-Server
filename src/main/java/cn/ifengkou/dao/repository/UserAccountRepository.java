package cn.ifengkou.dao.repository;

import cn.ifengkou.dao.entity.UserAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public interface UserAccountRepository extends JpaRepository<UserAccountEntity,Long> {
    UserAccountEntity findByUsername(String username);

    Page<UserAccountEntity> findByUsernameLike(String username, Pageable page);

    boolean existsByUsername(String username);
}
