package com.kantboot.admin.util.old.nanshouxiangku.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.admin.util.old.nanshouxiangku.entity.CommonParam;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Transient;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class FindCommonUtil<T, ID> {

    /**
     * 用来通用查询的方法
     *
     * @param commonParam
     * @param entityManager
     * @param transaction
     * @return Specification<T>
     */
    public Specification<T> findCommon(CommonParam<T> commonParam, EntityManager entityManager, EntityTransaction transaction) {
        transaction.begin();


        List<T> andLe = commonParam.getAnd().getLe();
        String andLeJSONString = JSON.toJSONString(andLe);
        List<HashMap> andLeMaps = JSON.parseArray(andLeJSONString, HashMap.class);

        ///////
        List<T> orEq = commonParam.getOr().getEq();
        String orEqJSONString = JSON.toJSONString(orEq);
        List<HashMap> orEqMaps = JSON.parseArray(orEqJSONString, HashMap.class);


        List<T> orLike = commonParam.getOr().getLike();
        String orLikeJSONString = JSON.toJSONString(orLike);
        List<HashMap> orLikeMaps = JSON.parseArray(orLikeJSONString, HashMap.class);


        List<T> orVague = commonParam.getOr().getVague();
        String orVagueJSONString = JSON.toJSONString(orVague);
        List<HashMap> orVagueMaps = JSON.parseArray(orVagueJSONString, HashMap.class);


        List<T> orGt = commonParam.getOr().getGt();
        String orGtJSONString = JSON.toJSONString(orGt);
        List<HashMap> orGtMaps = JSON.parseArray(orGtJSONString, HashMap.class);

        List<T> orLt = commonParam.getOr().getLt();
        String orLtJSONString = JSON.toJSONString(orLt);
        List<HashMap> orLtMaps = JSON.parseArray(orLtJSONString, HashMap.class);


        List<T> orGe = commonParam.getOr().getGe();
        String orGeJSONString = JSON.toJSONString(orGe);
        List<HashMap> orGeMaps = JSON.parseArray(orGeJSONString, HashMap.class);

        List<T> orLe = commonParam.getAnd().getLe();
        String orLeJSONString = JSON.toJSONString(orLe);
        List<HashMap> orLeMaps = JSON.parseArray(orLeJSONString, HashMap.class);

        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicatesByAnd = new ArrayList<Predicate>();

                //查询所有非空的数据
                for (String s : commonParam.getNotNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNotNull(root.get(s)));
                }


                // 查询所有为空的数据
                for (String s : commonParam.getIsNull()) {
                    predicatesByAnd.add(
                            criteriaBuilder
                                    .isNull(root.get(s)));
                }

                //查询所有 and 下的 = 条件
                FindCommonUtil.findCommonAndEq(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 % 条件
                FindCommonUtil.findCommonAndLike(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 模糊 条件
                FindCommonUtil.findCommonAndVague(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 > 条件
                FindCommonUtil.findCommonAndGt(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 < 条件
                FindCommonUtil.findCommonAndLt(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 >= 条件
                FindCommonUtil.findCommonAndGe(commonParam, predicatesByAnd, criteriaBuilder, root);

                //查询所有 and 下的 <= 条件
                for (HashMap map : andLeMaps) {
                    Set<String> set = map.keySet();
                    for (String s : set) {
                        boolean bool = true;
                        if (FindCommonUtil.isHasTransient(commonParam.getEntity(), s)) {
                            bool = false;
                        }
                        if (bool) {

                        } else if (!(null != map.get(s)) && !("".equals(map.get(s)))) {

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


                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number = (Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .le(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    } catch (IllegalArgumentException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    } catch (ClassCastException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
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
                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .equal(root.get(s).get(s2), jsonObject.get(s2)));
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
                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .equal(root.get(s).get(s2), jsonObject.get(s2)));
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
                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {
                                    predicatesByOr.add(
                                            criteriaBuilder
                                                    .like(root.get(s).get(s2), "%" + jsonObject.get(s2) + "%"));
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


                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number = (Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .gt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    } catch (IllegalArgumentException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    } catch (ClassCastException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
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


                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number = (Number) jsonObject.get(s2);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    } catch (IllegalArgumentException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    } catch (ClassCastException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByAnd.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
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


                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number = (Number) jsonObject.get(s2);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .ge(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    } catch (IllegalArgumentException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    } catch (ClassCastException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
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


                            } else if (map.get(s) instanceof JSONArray) {
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
                            } else if (map.get(s) instanceof JSONObject) {
                                JSONObject jsonObject = (JSONObject) map.get(s);
                                Set<String> set1 = jsonObject.keySet();
                                for (String s2 : set1) {

                                    try {
                                        Number number = (Number) jsonObject.get(s2);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .le(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                                    } catch (IllegalArgumentException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                                    } catch (ClassCastException ex) {
                                        Date date = new Date((Long) jsonObject.get(s2));
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String format = sdf.format(date);
                                        predicatesByOr.add(
                                                criteriaBuilder
                                                        .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
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

    /**
     * 查询所有 and 下的 = 条件
     */
    public static void findCommonAndEq(
            CommonParam commonParam,
            List<Predicate> predicatesByAnd,
            CriteriaBuilder criteriaBuilder,
            Root root) {
        List andEq = commonParam.getAnd().getEq();
        String andEqJSONString = JSON.toJSONString(andEq);
        List<HashMap> andEqMaps = JSON.parseArray(andEqJSONString, HashMap.class);

        for (HashMap map : andEqMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {
                if (FindCommonUtil.isHasTransientByFiled(commonParam.getEntity(), s)) {
                } else if (!StringUtils.isEmpty(map.get(s))) {
                    if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject)) {

                        predicatesByAnd.add(
                                criteriaBuilder
                                        .equal(root.get(s), map.get(s)));
                    }
                    //当对一对多或者多对多查询时
                    else if (map.get(s) instanceof JSONArray) {

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
                    //当对一对一查询时
                    else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {

                            predicatesByAnd.add(
                                    criteriaBuilder
                                            .equal(root.get(s).get(s2), jsonObject.get(s2)));

                        }
                    }
                }
            }
        }


    }

    /**
     * 查询所有 and 下的 like 条件
     */
    public static void findCommonAndLike(CommonParam commonParam,
                                         List<Predicate> predicatesByAnd,
                                         CriteriaBuilder criteriaBuilder,
                                         Root root) {
        List andLike = commonParam.getAnd().getLike();
        String andLikeJSONString = JSON.toJSONString(andLike);
        List<HashMap> andLikeMaps = JSON.parseArray(andLikeJSONString, HashMap.class);

        for (HashMap map : andLikeMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {
                if (FindCommonUtil.isHasTransientByFiled(commonParam.getEntity(), s)) {
                } else if (!StringUtils.isEmpty(map.get(s))) {
                    if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject)) {
                        predicatesByAnd.add(
                                criteriaBuilder
                                        .like(root.get(s), map.get(s) + ""));
                    } else if (map.get(s) instanceof JSONArray) {
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
                    } else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {
                            predicatesByAnd.add(
                                    criteriaBuilder
                                            .like(root.get(s).get(s2), jsonObject.get(s2) + ""));
                        }
                    }
                }
            }
        }

    }

    public static void findCommonAndVague(CommonParam commonParam,
                                          List<Predicate> predicatesByAnd,
                                          CriteriaBuilder criteriaBuilder,
                                          Root root) {

        List andVague = commonParam.getAnd().getVague();
        String andVagueJSONString = JSON.toJSONString(andVague);
        List<HashMap> andVagueMaps = JSON.parseArray(andVagueJSONString, HashMap.class);
        for (HashMap map : andVagueMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {

                if (FindCommonUtil.isHasTransientByFiled(commonParam.getEntity(), s)) {
                } else if (!StringUtils.isEmpty(map.get(s))) {
                    if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject)) {
                        predicatesByAnd.add(
                                criteriaBuilder
                                        .like(root.get(s), "%" + map.get(s) + "%"));
                    } else if (map.get(s) instanceof JSONArray) {
                        JSONArray array = (JSONArray) map.get(s);
                        for (Object o : array) {
                            String s1 = JSON.toJSONString(o);
                            HashMap<String, Object> hashMap = JSON.parseObject(s1, HashMap.class);
                            Set<String> set1 = hashMap.keySet();
                            for (String s2 : set1) {
                                if (hashMap.get(s2) instanceof String) {
                                    predicatesByAnd.add(
                                            criteriaBuilder
                                                    .like(root.join(s).get(s2), "%" + hashMap.get(s2) + "%"));
                                }
                            }
                        }
                    } else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {
                            if (!StringUtils.isEmpty(jsonObject.get(s2))) {
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .like(root.get(s).get(s2), "%" + jsonObject.get(s2) + "%"));
                            }
                        }
                    }
                }
            }
        }

    }

    public static void findCommonAndGt(CommonParam commonParam,
                                       List<Predicate> predicatesByAnd,
                                       CriteriaBuilder criteriaBuilder,
                                       Root root) {
        List andGt = commonParam.getAnd().getGt();
        String andGtJSONString = JSON.toJSONString(andGt);
        List<HashMap> andGtMaps = JSON.parseArray(andGtJSONString, HashMap.class);

        for (HashMap map : andGtMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {
                boolean bool = true;
                if (isHasTransient(commonParam.getEntity(), s)) {
                    bool = false;
                }
                if (bool) {

                } else if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject)) {

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


                    } else if (map.get(s) instanceof JSONArray) {
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
                    } else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {

                            try {
                                Number number = (Number) jsonObject.get(s2);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .gt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                            } catch (IllegalArgumentException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                            } catch (ClassCastException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                            }

                        }
                    }
                }
            }
        }

    }

    public static void findCommonAndLt(CommonParam commonParam,
                                       List<Predicate> predicatesByAnd,
                                       CriteriaBuilder criteriaBuilder,
                                       Root root) {

        List andLt = commonParam.getAnd().getLt();
        String andLtJSONString = JSON.toJSONString(andLt);
        List<HashMap> andLtMaps = JSON.parseArray(andLtJSONString, HashMap.class);
        for (HashMap map : andLtMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {

                if (FindCommonUtil.isHasTransientByFiled(commonParam.getEntity(), s)) {
                } else if (!StringUtils.isEmpty(map.get(s))) {

                    if (!(map.get(s) instanceof JSONArray) && !(map.get(s) instanceof JSONObject)) {

                        try {
                            Number number = (Number) map.get(s);
                            predicatesByAnd.add(
                                    criteriaBuilder
                                            .lt(root.get(s), number));
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


                    } else if (map.get(s) instanceof JSONArray) {
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
                    } else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {

                            try {
                                Number number = (Number) jsonObject.get(s2);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .lt(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                            } catch (IllegalArgumentException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .lessThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                            } catch (ClassCastException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .lessThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                            }

                        }
                    }
                }
            }
        }
    }

    public static void findCommonAndGe(CommonParam commonParam,
                                       List<Predicate> predicatesByAnd,
                                       CriteriaBuilder criteriaBuilder,
                                       Root root) {
        List andGe = commonParam.getAnd().getGe();
        String andGeJSONString = JSON.toJSONString(andGe);
        List<HashMap> andGeMaps = JSON.parseArray(andGeJSONString, HashMap.class);

        for (HashMap map : andGeMaps) {
            Set<String> set = map.keySet();
            for (String s : set) {

                if (FindCommonUtil.isHasTransientByFiled(commonParam.getEntity(), s)) {
                } else if (!StringUtils.isEmpty(map.get(s))) {

                    if ((!(map.get(s) instanceof JSONArray)) && (!(map.get(s) instanceof JSONObject))) {

                        try {
                            Number number = (Number) map.get(s);
                            predicatesByAnd.add(
                                    criteriaBuilder
                                            .ge(root.get(s), number));
                            log.info("findCommonAndGe -> key:value -> " + s + ":" + number);

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


                    } else if (map.get(s) instanceof JSONArray) {
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
                    } else if (map.get(s) instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) map.get(s);
                        Set<String> set1 = jsonObject.keySet();
                        for (String s2 : set1) {

                            try {
                                Number number = (Number) jsonObject.get(s2);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .ge(root.get(s).get(s2), (Number) jsonObject.get(s2)));
                            } catch (IllegalArgumentException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .greaterThanOrEqualTo(root.join(s).get(s2).as(String.class), format));
                            } catch (ClassCastException ex) {
                                Date date = new Date((Long) jsonObject.get(s2));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String format = sdf.format(date);
                                predicatesByAnd.add(
                                        criteriaBuilder
                                                .greaterThanOrEqualTo(root.get(s).get(s2).as(String.class), format));
                            }

                        }
                    }
                }
            }
        }

    }

    @Resource
    EntityManagerFactory entityManagerFactory;

    /**
     * 这个方法会将一段文本注入到某个类中添加了@Autowired注解的方法中，并将实例对象返回
     */
    public ID getId(T entity) {
        return (ID) entityManagerFactory.getPersistenceUnitUtil().getIdentifier(entity);
    }

    public static Boolean isHasTransient(Object object, String fieldName) {
        if (isHasTransientByMethod(object, fieldName)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有@Transient注解
     *
     * @param object
     * @param fieldName
     * @return
     */
    public static Boolean isHasTransientByFiled(Object object, String fieldName) {
        // 从Class对象中获取Demo中声明方法对应的Method对象
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return true;
        }

        // 判断方法是否被加上了@Transient这个注解
        if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
            // 删除了访问权限检查

            return true;
        }
        return false;
    }

    public static Boolean isHasTransientByMethod(Object object, String fieldName) {
        Method method = null;
        Boolean ab = false;
        try {
            String methodName = "get" + StrUtil.upperFirst(fieldName);
            method = object.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
            ab = true;
        }


        Method method1 = null;
        Boolean bb = false;
        try {
            String methodName = "is" + StrUtil.upperFirst(fieldName);
            method1 = object.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException ex) {
//                throw new RuntimeException(ex);
            bb = true;
        }

        if (ab && bb) {
            return true;
        }

        if (!ab) {
            // 判断方法是否被加上了@Transient这个注解
            if (method.isAnnotationPresent(Transient.class) || method.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
                // 删除了访问检查
                return true;
            }

        }
        if (!bb) {
            // 判断方法是否被加上了@Transient这个注解
            if (method1.isAnnotationPresent(Transient.class) || method1.isAnnotationPresent(org.springframework.data.annotation.Transient.class)) {
                // 删除了访问所有方法
                return true;
            }
        }
        return false;

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
