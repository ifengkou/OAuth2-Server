package cn.ifengkou.dao.repository;


import cn.ifengkou.dao.entity.AuthClientUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
public interface AuthClientUserRepository extends JpaRepository<AuthClientUserEntity,String> {
    AuthClientUserEntity findByClientIdAndOpenId(String clientId,String openId);
}
