package cn.ifengkou.service;

import cn.ifengkou.dao.entity.AuthClientUserEntity;
import cn.ifengkou.dao.entity.UserAccountEntity;
import cn.ifengkou.dao.repository.AuthClientUserRepository;
import cn.ifengkou.dao.repository.UserAccountRepository;
import cn.ifengkou.model.exception.NotExistsException;
import cn.ifengkou.model.UserAccount;
import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Service
public class AuthClientUserService {
    @Autowired
    AuthClientUserRepository authClientUserRepository;

    @Autowired
    UserAccountRepository userRepository;

    @Autowired
    Mapper dozerMapper;

    UserAccount findUserByClientIdAndOpenId(String clientId, String openId){
        AuthClientUserEntity entity = authClientUserRepository.findByClientIdAndOpenId(clientId,openId);
        if(entity == null){
            throw new NotExistsException("clientId="+clientId+",openId="+openId +" 的记录不存在");
        }
        Optional<UserAccountEntity> userEntityOptional = userRepository.findById(entity.getUnionId());
        if(userEntityOptional == null){
            throw new NotExistsException("clientId="+clientId+",openId="+openId +" 的用户不存在");
        }
        UserAccountEntity user = userEntityOptional.get();
        return dozerMapper.map(user,UserAccount.class);
    }

}
