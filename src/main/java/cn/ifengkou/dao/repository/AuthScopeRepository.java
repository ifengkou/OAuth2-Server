package cn.ifengkou.dao.repository;

import cn.ifengkou.dao.entity.AuthScopeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public interface AuthScopeRepository extends JpaRepository<AuthScopeEntity,Long> {
    AuthScopeEntity findByScope(String scope);
}
