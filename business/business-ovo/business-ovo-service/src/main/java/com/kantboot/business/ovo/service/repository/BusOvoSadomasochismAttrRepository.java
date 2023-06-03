package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoSadomasochismAttr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * Ovo性虐属性表仓库
 * @author 方某方
 */
public interface BusOvoSadomasochismAttrRepository
extends Repository<BusOvoSadomasochismAttr, Long>, JpaRepository<BusOvoSadomasochismAttr, Long>
{

        /**
        * 根据性虐属性编码获取性虐属性
        * @param code 性虐属性编码
        * @return 性虐属性
        */
        BusOvoSadomasochismAttr findByCode(String code);

}
