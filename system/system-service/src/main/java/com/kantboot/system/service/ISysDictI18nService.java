package com.kantboot.system.service;


import java.util.Map;

/**
 * <p>
 *     基于字典表的国际化服务接口
 * </p>
 * @author 方某方
 */
public interface ISysDictI18nService {

    /**
     * 根据是语言、父级编码查询
     * @param languageCode 语言编码
     * @param dictGroupCode 字典分组编码
     * @return 方便调用的字典表，非直接数组形式，而采用键值对形式，如{"key":"value"}
     */
    Map<String,String> getMap(String languageCode, String dictGroupCode);

    /**
     * 根据是父级编码查询
     * @param dictGroupCode 字典分组编码
     * @return 方便调用的字典表，非直接数组形式，而采用键值对形式，如{"key":"value"}
     */
    Map<String,String> getMap(String dictGroupCode);

    /**
     * 根据语言、分组编码、子级编码查询
     * @param languageCode 语言编码
     * @param dictGroupCode 字典分组编码
     * @param dictCode 字典子级编码
     * @return 结果
     */
    String getValue(String languageCode, String dictGroupCode, String dictCode);

    /**
     * 根据分组编码、编码查询
     * @param dictGroupCode 字典分组编码
     * @param dictCode 字典子级编码
     * @return 结果
     */
    String getValue(String dictGroupCode, String dictCode);



}
