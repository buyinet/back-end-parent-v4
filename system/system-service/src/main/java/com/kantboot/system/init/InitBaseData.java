package com.kantboot.system.init;

import com.alibaba.fastjson2.JSON;
import com.google.common.io.CharStreams;
import com.kantboot.system.module.entity.SysDict;
import com.kantboot.system.module.entity.SysDictI18n;
import com.kantboot.system.module.entity.SysException;
import com.kantboot.system.module.entity.SysLanguage;
import com.kantboot.system.repository.SysDictI18nRepository;
import com.kantboot.system.repository.SysDictRepository;
import com.kantboot.system.repository.SysExceptionRepository;
import com.kantboot.system.repository.SysLanguageRepository;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * 初始化类
 * 此类用来新建时初始化一些数据
 * @author 方某方
 */
@Component
@Log4j2
public class InitBaseData implements ApplicationListener<ContextRefreshedEvent> {


    @Resource
    private SysLanguageRepository languageRepository;

    @Resource
    private SysDictRepository dictRepository;

    @Resource
    private SysDictI18nRepository dictI18nRepository;

    @Resource
    private SysExceptionRepository exceptionRepository;

    @SneakyThrows
    private void initLanguage(){
        long count = languageRepository.count();
        if(count == 0){
            log.info("start:初始化数据库语言表");
            // 读取resources/init下的SysLanguage.json文件
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("init/SysLanguage.json");
            // 将resourceAsStream转换为字符串
            assert resourceAsStream != null;
            String jsonStr=CharStreams.toString(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
            // 将json文件转换为SysLanguage对象
            List<SysLanguage> sysLanguages = JSON.parseArray(jsonStr, SysLanguage.class);
            for (SysLanguage sysLanguage : sysLanguages) {
                sysLanguage.setId(null);
                sysLanguage.setGmtCreate(new Date());
                sysLanguage.setGmtModified(new Date());
            }

            // 保存到数据库
            languageRepository.saveAll(sysLanguages);
            log.info("end:初始化数据库语言表");
        }
    }


    @SneakyThrows
    private void initDict(){
        long count = dictI18nRepository.count();
        if(count == 0){
            log.info("start:初始化字典(sys_dict)表)");
            // 读取resources/init下的SysDictI18n.json文件
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("init/SysDict.json");
            // 将resourceAsStream转换为字符串
            assert resourceAsStream != null;
            String jsonStr=CharStreams.toString(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
            // 将json文件转换为SysDict对象集合
            List<SysDict> sysDicts = JSON.parseArray(jsonStr, SysDict.class);
            for (SysDict sysDict : sysDicts) {
                sysDict.setId(null);
                sysDict.setGmtCreate(new Date());
                sysDict.setGmtModified(new Date());
            }
            // 保存到数据库
            dictRepository.saveAll(sysDicts);

            log.info("end:初始化字典(sys_dict)表");
        }
    }

    @SneakyThrows
    private void initDictI18n(){
        long count = dictI18nRepository.count();
        if(count == 0){
            log.info("start:初始化字典国际化(sys_dict_i18n)表)");
            // 读取resources/init下的SysDictI18n.json文件
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("init/SysDictI18n.json");
            // 将resourceAsStream转换为字符串
            assert resourceAsStream != null;
            String jsonStr=CharStreams.toString(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
            // 将json文件转换为SysDictI18n对象集合
            List<SysDictI18n> sysDictI18ns = JSON.parseArray(jsonStr, SysDictI18n.class);
            for (SysDictI18n sysDictI18n : sysDictI18ns) {
                sysDictI18n.setId(null);
                sysDictI18n.setGmtCreate(new Date());
                sysDictI18n.setGmtModified(new Date());
            }

            // 保存到数据库
            dictI18nRepository.saveAll(sysDictI18ns);

            log.info("end:初始化字典国际化(sys_dict_i18n)表)");
        }
    }


    @SneakyThrows
    private void initException(){
        long count = exceptionRepository.count();
        if(count == 0){
            log.info("start:初始化异常(sys_exception)表)");
            // 读取resources/init下的SysDictI18n.json文件
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("init/SysException.json");
            // 将resourceAsStream转换为字符串
            assert resourceAsStream != null;
            String jsonStr=CharStreams.toString(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8));
            // 将json文件转换为SysException对象集合
            List<SysException> sysExceptions = JSON.parseArray(jsonStr, SysException.class);
            for (SysException sysException : sysExceptions) {
                sysException.setId(null);
                sysException.setGmtCreate(new Date());
                sysException.setGmtModified(new Date());
            }
            // 保存到数据库
            exceptionRepository.saveAll(sysExceptions);
            log.info("end:初始化异常(sys_exception)表)");
        }
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 防止重复执行
        if(event.getApplicationContext().getParent() == null){
            // 初始化语言表
            initLanguage();

            // 初始化字典表
            initDict();

            // 初始化字典国际化表
            initDictI18n();

            // 初始化异常表
            initException();
        }
    }
}
