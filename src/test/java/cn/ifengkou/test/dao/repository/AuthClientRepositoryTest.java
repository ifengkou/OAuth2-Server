package cn.ifengkou.test.dao.repository;

import cn.ifengkou.dao.entity.AuthClientEntity;
import cn.ifengkou.dao.repository.AuthClientRepository;
import com.sun.tools.javac.util.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = {"classpath:application.properties"})
@SpringBootTest
public class AuthClientRepositoryTest {
    @Autowired
    protected AuthClientRepository authClientRepository;

    @Test
    @Ignore
    public void testFindByClientId(){
        AuthClientEntity entity = authClientRepository.findByClientId("SampleClientId");
        Assert.check(entity!=null);
    }

}
