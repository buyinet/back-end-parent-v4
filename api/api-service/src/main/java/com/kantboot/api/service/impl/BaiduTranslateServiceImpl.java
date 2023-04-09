package com.kantboot.api.service.impl;

import com.alibaba.fastjson2.JSON;
import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateParam;
import com.kantboot.api.util.baidu.translate.entity.BaiduTranslateResult;
import com.kantboot.api.util.baidu.translate.util.BaiduTranslateUtil;
import com.kantboot.api.service.IBaiduTranslateService;
import com.kantboot.system.module.entity.SysDictI18n;
import com.kantboot.system.module.entity.SysLanguage;
import com.kantboot.system.repository.SysDictI18nRepository;
import com.kantboot.system.repository.SysLanguageRepository;
import com.kantboot.system.service.ISysSettingService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 百度翻译的服务实现类
 *
 * @author 方某方
 */
@Service
public class BaiduTranslateServiceImpl implements IBaiduTranslateService {

    @Resource
    private ISysSettingService settingService;

    @Resource
    private SysLanguageRepository languageRepository;

    @Resource
    private SysDictI18nRepository dictI18nRepository;


    @Override
    public BaiduTranslateResult translate(String q, String from, String to) {
        Map<String, String> baiduTranslate = settingService.getMap("baiduTranslate");
        BaiduTranslateParam param = new BaiduTranslateParam();
        // 设置百度翻译的appid
        param.setAppid(baiduTranslate.get("appid"));
        // 设置百度翻译的密钥
        param.setSecret(baiduTranslate.get("secret"));
        param.setQ(q);
        param.setFrom(from);
        param.setTo(to);

        return BaiduTranslateUtil.translate(param);
    }

    @SneakyThrows
    @Override
    public void generateDictI18n(String q, String from,String dictGroupCode,String dictCode) {
        List<SysLanguage> allBySupportIsTrue = languageRepository.findAllBySupportIsTrue();
        // 获取所有baiduTranslateCode
        List<String> baiduTranslateCodeList= allBySupportIsTrue.stream().map(SysLanguage::getBaiduTranslateCode).toList();
        // 清除相同的baiduTranslateCode
        baiduTranslateCodeList=baiduTranslateCodeList.stream().distinct().toList();

        List<SysDictI18n> list=new ArrayList<>(100);

        HashMap<String, List<String>> dictMap = new HashMap<>(100);

        for(String to : baiduTranslateCodeList){
            List<SysLanguage> byBaiduTranslateCodeAndSupport
                    = languageRepository.findByBaiduTranslateCodeAndSupport(to, true);
            List<String> collect = byBaiduTranslateCodeAndSupport.stream().map(SysLanguage::getCode).collect(Collectors.toList());
            dictMap.put(to,collect);
        }

        for (String to : baiduTranslateCodeList) {
            BaiduTranslateResult translate = translate(q, from, to);
            List<String> strings = dictMap.get(to);
            System.out.println("=====================start:to:"+to);
            for (String string : strings) {
                SysDictI18n sysDictI18n = new SysDictI18n();
                sysDictI18n.setDictCode(dictCode);
                sysDictI18n.setDictGroupCode(dictGroupCode);
                sysDictI18n.setLanguageCode(string);
                sysDictI18n.setValue(translate.getDst());
                if("yue".equals(to)){
                    BaiduTranslateResult translate1 = translate(translate.getDst(), "zh", "cht");
                    sysDictI18n.setValue(translate1.getDst());
                }

                System.out.println(JSON.toJSONString(sysDictI18n));
                list.add(sysDictI18n);
            }
            System.out.println("======================end:to:"+to);
            Thread.sleep(1000);
        }

        dictI18nRepository.saveAll(list);

    }

}