package cn.ifengkou.dao.repository;

import cn.ifengkou.dao.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String username);

    Page<UserEntity> findByUsernameLike(String username, Pageable page);

    boolean existsByUsername(String username);
}
