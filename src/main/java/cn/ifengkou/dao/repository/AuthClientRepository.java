package cn.ifengkou.dao.repository;


import cn.ifengkou.dao.entity.AuthClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
public interface AuthClientRepository extends JpaRepository<AuthClientEntity,Long> {
    AuthClientEntity findByClientId(String clientId);
}
