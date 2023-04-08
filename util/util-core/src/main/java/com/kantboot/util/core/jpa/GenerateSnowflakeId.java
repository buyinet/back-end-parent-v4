package com.kantboot.util.core.jpa;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.extra.spring.SpringUtil;
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
        return SpringUtil.getBean(Snowflake.class).nextId();
    }

    @Override
    public void configure(Map<String, Object> map) {
    }
}