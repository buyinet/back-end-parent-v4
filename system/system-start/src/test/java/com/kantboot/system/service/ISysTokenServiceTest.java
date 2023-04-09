package com.kantboot.system.service;

import com.kantboot.system.start.SystemStartApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles()
@SpringBootTest(classes = {SystemStartApplication.class})
public class ISysTokenServiceTest {

    @Resource
    private ISysTokenService service;

    @Test
    public void create(){
        service.createToken(405036384620549l);
    }


}
