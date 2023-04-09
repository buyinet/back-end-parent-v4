package com.kantboot.util.core.jpa;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.extra.spring.SpringUtil;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.spi.Configurable;
import java.io.Serializable;
import java.util.Map;

/**
 * 雪花算法id生成器
 * @author 方某方
 */
public class GenerateSnowflakeId implements IdentifierGenerator, Configurable {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // 创建 IdGeneratorOptions 对象，请在构造函数中输入 WorkerId：
        IdGeneratorOptions options = new IdGeneratorOptions();
        YitIdHelper.setIdGenerator(options);
        return YitIdHelper.nextId();
    }

    @Override
    public void configure(Map<String, Object> map) {
    }


}