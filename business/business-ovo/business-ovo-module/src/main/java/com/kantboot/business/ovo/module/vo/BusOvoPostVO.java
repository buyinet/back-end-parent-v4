package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.system.module.entity.SysUserHasHide;
import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 帖子VO
 */
@Getter
@Setter
@Accessors(chain = true)
public class BusOvoPostVO {


}
