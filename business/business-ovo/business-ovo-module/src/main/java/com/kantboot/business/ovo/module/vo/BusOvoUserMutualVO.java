package com.kantboot.business.ovo.module.vo;


import com.kantboot.business.ovo.module.entity.BusOvoUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Ovo用户互相关注VO
 * @author 方某方
 */
@Table(name="bus_ovo_user_mutual_vo")
@Getter
@Setter
@Accessors(chain = true)
public class BusOvoUserMutualVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户
     */
    private BusOvoUser ovoUser;


    private Date gmtCreate;

    private Date gmtModified;

}
