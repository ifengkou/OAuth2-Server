package cn.ifengkou.service;

import cn.ifengkou.dao.entity.AuthScopeEntity;
import cn.ifengkou.dao.repository.AuthScopeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Service
public class AuthScopeService {
    @Autowired
    AuthScopeRepository authScopeRepository;

    public AuthScopeEntity findByScope(String scope) {
        AuthScopeEntity scopeEntity = authScopeRepository.findByScope(scope);
        return scopeEntity;
    }
}
