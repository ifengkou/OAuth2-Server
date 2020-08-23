package cn.ifengkou.service;

import cn.ifengkou.dao.entity.AuthClientEntity;
import cn.ifengkou.dao.repository.AuthClientRepository;
import cn.ifengkou.model.exception.AlreadyExistsException;
import cn.ifengkou.model.AuthClient;
import cn.ifengkou.model.PageableResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Service
public class AuthClientService {

    @Autowired
    AuthClientRepository authClientRepository;

    /**
     * 根据clientId 查找AuthClient
     * @param clientId
     * @return
     */
    public AuthClient findByClientId(String clientId) {
        AuthClientEntity entity = authClientRepository.findByClientId(clientId);
        if (entity != null) {
            /*AuthClient client = AuthClient.builder()
                    .clientId(entity.getClientId())
                    .clientName(entity.getClientName())
                    .clientSecret(entity.getClientSecret())
                    .background(entity.getBackground())
                    .callbackUrl(entity.getCallbackUrl())
                    .homeUrl(entity.getHomeUrl())
                    .logo(entity.getLogo())
                    .logoutUrl(entity.getLogoutUrl())
                    .remarks(entity.getRemarks())
                    .status(entity.getStatus())
                    .build();*/

            AuthClient client = new AuthClient();
            BeanCopier beanCopier = BeanCopier.create(AuthClientEntity.class, AuthClient.class, false);
            beanCopier.copy(entity,client,null);
            return client;
        } else {
            return null;
        }
    }

    /**
     * 创建AuthClientEntity
     * @param oauthClient
     * @return
     * @throws AlreadyExistsException
     */
    public AuthClient create(AuthClient oauthClient) throws AlreadyExistsException {
        AuthClientEntity exist = authClientRepository.findByClientId(oauthClient.getClientId());
        if (exist != null) {
            throw new AlreadyExistsException(oauthClient.getClientId() + " already exists!");
        }
        AuthClientEntity entity = new AuthClientEntity();
        BeanCopier beanCopier = BeanCopier.create( AuthClient.class,AuthClientEntity.class, false);
        beanCopier.copy(oauthClient,entity,null);
        authClientRepository.save(entity);
        oauthClient.setId(entity.getId());
        return oauthClient;
    }

    /**
     * 分页获取AuthClient
     * @param pageNum
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @return
     */
    public PageableResponse<AuthClient> list(int pageNum, int pageSize, String sortField, String sortOrder) {
        PageableResponse<AuthClient> response = new PageableResponse<>();
        Sort sort;
        if (StringUtils.equalsIgnoreCase(sortOrder, "asc")) {
            sort = Sort.by(Sort.Direction.ASC, sortField);
        } else {
            sort = Sort.by(Sort.Direction.DESC, sortField);
        }
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<AuthClientEntity> page = authClientRepository.findAll(pageable);
        if (page.getContent() != null && page.getContent().size() > 0) {
            response.setTotalNum(page.getTotalElements());
            response.setFilteredNum(page.getTotalElements());
            BeanCopier beanCopier = BeanCopier.create(AuthClientEntity.class, AuthClient.class, false);
            page.getContent().forEach(u -> {
                AuthClient client = new AuthClient();
                beanCopier.copy(u,client,null);
                response.getData().add(client);
            });
        }
        return response;
    }

    /**
     * 变更AuthClientEntity status 字段
     * @param id
     * @param s
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, int s) {
        Optional<AuthClientEntity> ex = authClientRepository.findById(id);
        if(ex==null){
            throw new EntityNotFoundException();
        }
        AuthClientEntity e = ex.get();
        e.setStatus(s);
        authClientRepository.save(e);
    }
}
