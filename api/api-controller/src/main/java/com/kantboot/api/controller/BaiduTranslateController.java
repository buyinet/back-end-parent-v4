package com.kantboot.api.controller;

import com.kantboot.api.service.IBaiduTranslateService;
import com.kantboot.system.service.IStateSuccessService;
import com.kantboot.util.common.result.RestResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 百度翻译控制器
 *
 * @author 方某方
 */
@RestController
@RequestMapping("/api/baiduTranslate")
public class BaiduTranslateController {

    @Resource
    private IBaiduTranslateService service;

    @Resource
    private IStateSuccessService stateSuccessService;

    /**
     * 通过百度翻译生成国际化字典
     *
     * @param q             待翻译的文本
     * @param from          源语言
     * @param dictGroupCode 字典分组编码
     * @param dictCode      字典子级编码
     * @return 结果
     */
    @RequestMapping("/generateDictI18n")
    public RestResult<String> generateDictI18n(@RequestParam("q") String q,
                                               @RequestParam("from") String from,
                                               @RequestParam("dictGroupCode") String dictGroupCode,
                                               @RequestParam("dictCode") String dictCode) {
        service.generateDictI18n(q, from, dictGroupCode, dictCode);
        return stateSuccessService.success(null, "generateSuccess");
    }

}
