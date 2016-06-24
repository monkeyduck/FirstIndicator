import com.alibaba.fastjson.JSON;
import com.llc.model.Log;
import com.llc.service.LogService;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import javax.annotation.Resource;

/**
 * Created by llc on 16/6/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})

public class Test {
    private static Logger logger = Logger.getLogger(Test.class);

    @Resource
    private LogService userService;

    @org.junit.Test
    public void test() {
        Log log = userService.getLog(2877);
        // System.out.println(user.getUserName());
        // logger.info("值："+user.getUserName());
        logger.info(JSON.toJSONString(log));
        System.out.println(log.getDevice_id());
    }

}
