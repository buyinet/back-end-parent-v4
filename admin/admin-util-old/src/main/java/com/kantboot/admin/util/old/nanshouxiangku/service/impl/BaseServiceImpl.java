package com.kantboot.admin.util.old.nanshouxiangku.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.kantboot.admin.util.service.entity.CommonEntity;
import com.kantboot.admin.util.service.entity.CommonEntityPageParam;
import com.kantboot.admin.util.service.exception.UpdateException;
import com.kantboot.admin.util.service.service.IBaseService;
import com.kantboot.admin.util.service.util.FindCommonUtil;
import com.kantboot.util.common.exception.BaseException;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class BaseServiceImpl<T, ID> implements IBaseService<T, ID> {

    @Resource
    EntityManagerFactory entityManagerFactory;

    /**
     * 用来查询的方法
     *
     * @param commonEntity
     * @param entityManager
     * @param transaction
     * @return
     */
    private Specification<T> findCommon(CommonEntity<T> commonEntity, EntityManager entityManager, EntityTransaction transaction) {
        transaction.begin();
        List<T> andEq = commonEntity.getAnd().getEq();
        String andEqJSONString = JSON.toJSONString(andEq);
        List<HashMap> andEqMaps = JSON.parseArray(andEqJSONString, HashMap.class);

        List<T> andLike = commonEntity.getAnd().getLike();
        String andLikeJSONString = JSON.toJSONString(andLike);
        List<HashMap> andLikeMaps = JSON.parseArray(andLikeJSONString, HashMap.class);

        List<T> andVague = commonEntity.getAnd().getVague();
        String andVagueJSONString = JSON.toJSONString(andVague);
        List<HashMap> andVagueMaps = JSON.parseArray(andVagueJSONString, HashMap.class);

        List<T> andGt = commonEntity.getAnd().getGt();
        String andGtJSONString = JSON.toJSONString(andGt);
        List<HashMap> andGtMaps = JSON.parseArray(andGtJSONString, HashMap.class);

        List<T> andLt = commonEntity.getAnd().getLt();
        String andLtJSONString = JSON.toJSONString(andLt);
        List<HashMap> andLtMaps = JSON.parseArray(andLtJSONString, HashMap.class);

        List<T> andGe = commonEntity.getAnd().getGe();
        String andGeJSONString = JSON.toJSONString(andGe);
        List<HashMap> andGeMaps = JSON.parseArray(andGeJSONString, HashMap.class);

        List<T> andLe = commonEntity.getAnd().getLe();
        String andLeJSONString = JSON.toJSONString(andLe);
        List<HashMap> andLeMaps = JSON.parseArray(andLeJSONString, HashMap.class);

        ///////
        List<T> orEq = commonEntity.getOr().getEq();
        String orEqJSONString = JSON.toJSONString(orEq);
        List<HashMap> orEqMaps = JSON.parseArray(orEqJSONString, HashMap.class);

        List<T> orLike = commonEntity.getOr().getLike();
        String orLikeJSONString = JSON.toJSONString(orLike);
        List<HashMap> orLikeMaps = JSON.parseArray(orLikeJSONString, HashMap.class);

        List<T> orVague = commonEntity.getOr().getVague();
        String orVagueJSONString = JSON.toJSONString(orVague);
        List<HashMap> orVagueMaps = JSON.parseArray(orVagueJSONString, HashMap.class);

        List<T> orGt = commonEntity.getOr().getGt();
        String orGtJSONString = JSON.toJSONString(orGt);
        List<HashMap> orGtMaps = JSON.parseArray(orGtJSONString, HashMap.class);

        List<T> orLt = commonEntity.getOr().getLt();
        String orLtJSONString = JSON.toJSONString(orLt);
        List<HashMap> orLtMaps = JSON.parseArray(orLtJSONString, HashMap.class);

        List<T> orGe = commonEntity.getOr().getGe();
        String orGeJSONString = JSON.toJSONString(orGe);
        List<HashMap> orGeMaps = JSON.parseArray(orGeJSONString, HashMap.class);

        List<T> orLe = commonEntity.getAnd().getLe();
        String orLeJSONString = JSON.toJSONString(orLe);
        List<HashMap> orLeMaps = JSON.parseArray(orLeJSONString, HashMap.class);

        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicatesByAnd = new ArrayList<Predicate>();

                //查询所有非空的数据
                for (String s : commonEntity.getNotNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNotNull(root.get(s)));
                }


                // 查询所有为空的数据
                for (String s : commonEntity.getIsNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNull(root.get(s)));
                }

                //查询所有 and 下的 = 条件
                for (HashMap map : andEqMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {

                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .equal(root.get(s), map.get(s)));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .equal(root.join(s).get(s2), hashMap.get(s2)));
                                    }
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 % 条件
                for (HashMap map : andLikeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .like(root.get(s), map.get(s) + ""));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), hashMap.get(s2) + ""));
                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : andVagueMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .like(root.get(s), "%" + map.get(s) + "%"));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), "%" + hashMap.get(s2) + "%"));
                                    }
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 > 条件
                for (HashMap map : andGtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .gt(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .gt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 < 条件
                for (HashMap map : andLtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lt(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 >= 条件
                for (HashMap map : andGeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .ge(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .ge(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                //查询所有 and 下的 <= 条件
                for (HashMap map : andLeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .le(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .le(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByAnd.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                Predicate[] predicateArrByAnd = new Predicate[predicatesByAnd.size()];
                Predicate and = criteriaBuilder.and(predicatesByAnd.toArray(predicateArrByAnd));

                ////////////////////////
                List<Predicate> predicatesByOr = new ArrayList<Predicate>();

                for (HashMap map : orEqMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .equal(root.get(s), map.get(s)));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .equal(root.join(s).get(s2), hashMap.get(s2)));
                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLikeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .like(root.get(s), map.get(s) + ""));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), hashMap.get(s2) + ""));
                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orVagueMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {
                            if (!(map.get(s) instanceof JSONArray)) {
                                predicatesByOr.add(
                                        criteriaBuilder
                                                .like(root.get(s), "%" + map.get(s) + "%"));
                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .like(root.join(s).get(s2), "%" + hashMap.get(s2) + "%"));
                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orGtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .gt(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .gt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLtMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lt(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lt(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orGeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .ge(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .greaterThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .ge(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                for (HashMap map : orLeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        if (!StringUtils.isEmpty(map.get(s))) {

                            if (!(map.get(s) instanceof JSONArray)) {

                                try {
                                    Number number = (Number) map.get(s);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .le(root.get(s), (Number) map.get(s)));
                                } catch (IllegalArgumentException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));

                                } catch (ClassCastException ex) {
                                    Date date = new Date((Long) map.get(s));
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String format = sdf.format(date);
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .lessThanOrEqualTo(root.get(s).as(String.class), format));
                                }


                            } else {
                                JSONArray array = (JSONArray) map.get(s);
                                for (Object o : array) {
                                    String s1 = JSON.toJSONString(o);
                                    HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                                    Set<String> set1 = hashMap.keySet();
                                    for (String s2 : set1) {

                                        try {
                                            Number number = (Number) hashMap.get(s2);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .le(root.join(s).get(s2), (Number) hashMap.get(s2)));
                                        } catch (IllegalArgumentException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        } catch (ClassCastException ex) {
                                            Date date = new Date((Long) hashMap.get(s2));
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            String format = sdf.format(date);
                                            predicatesByOr.add(
                                                    criteriaBuilder
                                                            .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                Predicate[] predicateArrByOr = new Predicate[predicatesByOr.size()];
                Predicate or = criteriaBuilder.or(predicatesByOr.toArray(predicateArrByOr));

                if (predicateArrByAnd.length == 0 && predicateArrByOr.length == 0) {
                    return criteriaQuery.where(and).getRestriction();
                }
                if (predicateArrByAnd.length == 0) {
                    return criteriaQuery.where(or).getRestriction();
                }
                if (predicateArrByOr.length == 0) {
                    return criteriaQuery.where(and).getRestriction();
                }
                return criteriaQuery.where(and, or).getRestriction();
            }
        };
        return specification;
    }

    @Override
    public List<T> findCommonByList(CommonEntity<T> commonEntity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class) type.getActualTypeArguments()[0];//<T>
//            Class<T> aClass = (Class<T>) commonEntity.getEntity().getClass();
            Class<T> aClass = entityClass;
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
            Specification<T> specification = findCommon(commonEntity, entityManager, transaction);
            List<T> all = jpaRepository.findAll(specification);
            transaction.commit();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        throw new BaseException(3000, "查询失败");
    }

    //    @Override
//    public T findById(T entity) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);
//        Optional<T> byId = jpaRepository.findById((ID) getId(entity));
//        entityManager.close();
//        return byId.get();
//    }
    @Override
    public T findById(ID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
        Class entityClass = (Class) type.getActualTypeArguments()[0];//<T>
//            Class<T> aClass = (Class<T>) commonEntity.getEntity().getClass();
        Class<T> aClass = entityClass;
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
        Optional<T> byId = jpaRepository.findById(id);
        entityManager.close();
        return byId.get();
    }


    @Override
    public Page<T> findCommonByPage(CommonEntityPageParam<T> pageParam) {
        CommonEntity<T> commonEntity = pageParam.getData();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            //返回表示此类型实际类型参数的 Type 对象的数组(),赋值给this.classt
            Class entityClass = (Class) type.getActualTypeArguments()[0];//<T>
//            Class<T> aClass = (Class<T>) commonEntity.getEntity().getClass();
            Class<T> aClass = entityClass;
            SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>(aClass, entityManager);
            Specification<T> specification = findCommon(commonEntity, entityManager, transaction);
            Page<T> all = jpaRepository.findAll(specification, pageParam.getPageable());
            transaction.commit();
            entityManager.close();
            return all;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        throw new BaseException(3000, "查询失败");
    }

    /**
     * 根据参数中的id删除
     *
     * @param entity
     * @return 删除成功 或者 删除失败
     */
    @Override
    public void remove(T entity) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);
        transaction.begin();
        Object o = entityManager.find(entity.getClass(), getId(entity));
        try {
            jpaRepository.delete((T) o);
        } catch (Exception e) {
            entityManager.close();
            throw new UpdateException().setMessage("删除失败");
        }
        transaction.commit();
        entityManager.close();
    }

    @Resource
    FindCommonUtil<T> findCommonUtil;

    @Override
    public T save(T entity) {
        //判断是否为添加，true为添加，false为修改
        boolean isInsert = findCommonUtil.getId(entity) == null;

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        SimpleJpaRepository<T, ID> jpaRepository = new SimpleJpaRepository<T, ID>((Class<T>) entity.getClass(), entityManager);

        if (!isInsert) {
            try {
                Optional<T> byId = jpaRepository.findById((ID) findCommonUtil.getId(entity));
                T entityByInsert = byId.get();
                BeanUtils.copyProperties(entity, entityByInsert, findCommonUtil.getNullPropertyNames(entity));
                jpaRepository.save(entityByInsert);
                return entityByInsert;
            } catch (Exception e) {
                e.printStackTrace();
                entityManager.close();
                throw new UpdateException().setMessage("修改失败");
            } finally {
                transaction.commit();
                entityManager.close();
            }
        }

        try {
            T save = jpaRepository.save(entity);
            return save;

        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException().setMessage("添加失败");
        } finally {
            transaction.commit();
            entityManager.close();
        }
    }


    /**
     * 这个方法会将一段文本注入到某个类中添加了@Autowired注解的方法中，并将实例对象返回
     */
    public Object getId(Object object) {
        // 从Class对象中获取Demo中声明方法对应的Method对象
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 判断方法是否被加上了@Autowired这个注解
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                try {
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获得所有不为空的字段
     *
     * @param source
     * @return
     */
    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
