package com.kantboot.api.util.wechat.pay;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 商户对resource对象进行解密后，得到的资源对象示例
 * @author 方某方
 */
@Data
public class PayNotifyResult {

    /**
     * 返回状态码
     * 错误码，SUCCESS为清算机构接收成功，其他错误码为失败。
     * 示例值：FAIL
     */
    @JSONField(name="code")
    private String code;

    /**
     * 返回信息
     * 返回信息，如非空，为错误原因。
     * 示例值：失败
     */
    @JSONField(name="message")
    private String message;


//    参数名	变量	类型[长度限制]	必填	描述
//    应用ID	appid	string[1,32]	是	直连商户申请的公众号或移动应用appid。
//    示例值：wxd678efh567hg6787
//    商户号	mchid	string[1,32]	是	商户的商户号，由微信支付生成并下发。
//    示例值：1230000109
//    商户订单号	out_trade_no	string[6,32]	是	商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
//    特殊规则：最小字符长度为6
//    示例值：1217752501201407033233368018
//    微信支付订单号	transaction_id	string[1,32]	是	微信支付系统生成的订单号。
//    示例值：1217752501201407033233368018
//    交易类型	trade_type	string[1,16]	是	交易类型，枚举值：
//    JSAPI：公众号支付
//    NATIVE：扫码支付
//    APP：APP支付
//    MICROPAY：付款码支付
//    MWEB：H5支付
//    FACEPAY：刷脸支付
//    示例值：MICROPAY
//    交易状态	trade_state	string[1,32]	是	交易状态，枚举值：
//    SUCCESS：支付成功
//    REFUND：转入退款
//    NOTPAY：未支付
//    CLOSED：已关闭
//    REVOKED：已撤销（付款码支付）
//    USERPAYING：用户支付中（付款码支付）
//    PAYERROR：支付失败(其他原因，如银行返回失败)
//    示例值：SUCCESS
//    交易状态描述	trade_state_desc	string[1,256]	是	交易状态描述
//    示例值：支付成功
//    付款银行	bank_type	string[1,32]	是	银行类型，采用字符串类型的银行标识。银行标识请参考《银行类型对照表》
//    示例值：CICBC_DEBIT
//    附加数据	attach	string[1,128]	否	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
//    示例值：自定义数据
//    支付完成时间	success_time	string[1,64]	是	支付完成时间，遵循rfc3339标准格式，格式为yyyy-MM-DDTHH:mm:ss+TIMEZONE，yyyy-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
//    示例值：2018-06-08T10:34:56+08:00
//            -支付者	payer	object	是	支付者信息
//    参数名	变量	类型[长度限制]	必填	描述
//    用户标识	openid	string[1,128]	是	用户在直连商户appid下的唯一标识。
//    示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
//-订单金额	amount	object	是	订单金额信息
//    参数名	变量	类型[长度限制]	必填	描述
//    总金额	total	int	是	订单总金额，单位为分。
//    示例值：100
//    用户支付金额	payer_total	int	是	用户支付金额，单位为分。
//    示例值：100
//    货币类型	currency	string[1,16]	是	CNY：人民币，境内商户号仅支持人民币。
//    示例值：CNY
//    用户支付币种	payer_currency	string[1,16]	是	用户支付币种
//    示例值：CNY
//-场景信息	scene_info	object	否	支付场景信息描述
//    参数名	变量	类型[长度限制]	必填	描述
//    商户端设备号	device_id	string[1,32]	否	终端设备号（门店号或收银设备ID）。
//    示例值：013467007045764
//            -优惠功能	promotion_detail	array	否	优惠功能，享受优惠时返回该字段。
//    参数名	变量	类型[长度限制]	必填	描述
//    券ID	coupon_id	string[1,32]	是	券ID
//    示例值：109519
//    优惠名称	name	string[1,64]	否	优惠名称
//    示例值：单品惠-6
//    优惠范围	scope	string[1,32]	否	GLOBAL：全场代金券
//    SINGLE：单品优惠
//    示例值：GLOBAL
//    优惠类型	type	string[1,32]	否	CASH：充值型代金券
//    NOCASH：免充值型代金券
//    示例值：CASH
//    优惠券面额	amount	int	是	优惠券面额
//    示例值：100
//    活动ID	stock_id	string[1,32]	否	活动ID
//    示例值：931386
//    微信出资	wechatpay_contribute	int	否	微信出资，单位为分
//    示例值：0
//    商户出资	merchant_contribute	int	否	商户出资，单位为分
//    示例值：0
//    其他出资	other_contribute	int	否	其他出资，单位为分
//    示例值：0
//    优惠币种	currency	string[1,16]	否	CNY：人民币，境内商户号仅支持人民币。
//    示例值：CNY
//-单品列表	goods_detail	array	否	单品列表信息
//    参数名	变量	类型[长度限制]	必填	描述
//    商品编码	goods_id
//    string[1,32]
//
//    是	商品编码
//    示例值：M1006
//    商品数量	quantity	int	是	用户购买的数量
//    示例值：1
//    商品单价	unit_price	int	是	商品单价，单位为分
//    示例值：100
//    商品优惠金额	discount_amount	int	是	商品优惠金额
//    示例值：0
//    商品备注	goods_remark	string[1,128]	否	商品备注信息
//    示例值：商品备注信息

    /**
     * 微信支付订单号
     * 微信支付系统生成的订单号。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name="transaction_id")
    private String transactionId;

    /**
     * 订单金额
     * 订单金额信息。
     */
    @JSONField(name="amount")
    private Amount amount;

    @Data
    public static class Amount{

        /**
         * 用户支付金额
         * 用户支付金额
         * 示例值：100
         */
        @JSONField(name="payer_total")
        private Integer payerTotal;

        /**
         * 总金额
         * 订单总金额，单位为分。
         * 示例值：100
         */
        @JSONField(name="total")
        private Integer total;

        /**
         * 货币类型
         * CNY：人民币，境内商户号仅支持人民币。
         * 示例值：CNY
         */
        @JSONField(name="currency")
        private String currency;

        /**
         * 用户支付币种
         * 用户支付币种
         * 示例值：CNY
         */
        @JSONField(name="payer_currency")
        private String payerCurrency;
    }

    /**
     * 商户号
     * 直连商户的商户号，由微信支付生成并下发。
     * 示例值：1230000109
     */
    @JSONField(name="mchid")
    private String mchid;

    /**
     * 交易状态
     * 交易状态，枚举值：
     * SUCCESS：支付成功
     * REFUND：转入退款
     * NOTPAY：未支付
     * CLOSED：已关闭
     * REVOKED：已撤销（付款码支付）
     * USERPAYING：用户支付中（付款码支付）
     * PAYERROR：支付失败(其他原因，如银行返回失败)
     * 示例值：SUCCESS
     */
    @JSONField(name="trade_state")
    private String tradeState;

    /**
     * 付款银行
     * 银行类型，采用字符串类型的银行标识。
     * 银行表示请参考《银行类型对照表》
     * 银行类型对照表 https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6
     * 示例值：CMC
     */
    @JSONField(name="bank_type")
    private String bankType;

    /**
     * 优惠功能
     * 优惠功能，享受优惠时返回该字段。
     */
    @JSONField(name="promotion_detail")
    private PromotionDetail[] promotionDetail;

    @Data
    public static class PromotionDetail{
        /**
         * 优惠券面额
         * 优惠券面额
         * 示例值：100
         */
        @JSONField(name="amount")
        private Integer amount;

        /**
         * 微信出资
         * 微信出资，单位为分
         * 示例值：0
         */
        @JSONField(name="wechatpay_contribute")
        private Integer wechatpayContribute;

        /**
         * 券ID
         * 券ID
         * 示例值：109519
         */
        @JSONField(name="coupon_id")
        private String couponId;

        /**
         * 优惠范围
         * GLOBAL：全场代金券
         * SINGLE：单品优惠
         * 示例值：SINGLE
         */
        @JSONField(name="scope")
        private String scope;

        /**
         * 商户出资
         * 商户出资，单位为分
         * 示例值：0
         */
        @JSONField(name="merchant_contribute")
        private Integer merchantContribute;

        /**
         * 优惠名称
         * 优惠名称
         * 示例值：单品惠-6
         */
        @JSONField(name="name")
        private String name;

        /**
         * 其他出资
         * 其他出资，单位为分
         * 示例值：0
         */
        @JSONField(name="other_contribute")
        private Integer otherContribute;

        /**
         * 优惠币种
         * CNY：人民币，境内商户号仅支持人民币。
         * 示例值：CNY
         */
        @JSONField(name="currency")
        private String currency;

        /**
         * 活动ID
         * 活动ID
         * 示例值：931386
         */
        @JSONField(name="stock_id")
        private String stockId;

        /**
         * 单品列表
         * 单品列表信息
         */
        @JSONField(name="goods_detail")
        private GoodsDetail[] goodsDetail;

        @Data
        public static class GoodsDetail{
            @JSONField(name="goods_remark")
            private String goodsRemark;
            @JSONField(name="quantity")
            private Integer quantity;
            @JSONField(name="discount_amount")
            private Integer discountAmount;
            @JSONField(name="goods_id")
            private String goodsId;
            @JSONField(name="unit_price")
            private Integer unitPrice;
        }

    }

    @JSONField(name="success_time")
    private String successTime;

    @JSONField(name="payer")
    private Payer payer;

    @Data
    public static class Payer{
        @JSONField(name="openid")
        private String openid;
    }

    @JSONField(name="out_trade_no")
    private String outTradeNo;

    @JSONField(name="appid")
    private String appid;

    @JSONField(name="trade_state_desc")
    private String tradeStateDesc;

    @JSONField(name="trade_type")
    private String tradeType;

    @JSONField(name="attach")
    private String attach;

    @JSONField(name="scene_info")
    private SceneInfo sceneInfo;

    @Data
    public static class SceneInfo{
        @JSONField(name="device_id")
        private String deviceId;
    }



}
