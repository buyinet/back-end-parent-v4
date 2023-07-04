package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;

/**
 * 帖子VO
 */
@Getter
@Setter
@Accessors(chain = true)
public class BusOvoPostVO extends BusOvoPost {




    // 其他属性和方法

}
