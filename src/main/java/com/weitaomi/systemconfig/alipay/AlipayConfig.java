package com.weitaomi.systemconfig.alipay;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.4
 *修改日期：2016-03-08
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

	public static String payCode_prefix="wtm";

	// 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String partner = "2088321045613085";
	//账户名
	public static String seller_name = "青岛匠人在线网络科技";

	// 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
	public static String seller_id = "9492011@qq.com";

	//商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPCCmMKCgNTio+if" +
			"ftPEiIyehQ+hGEpynSPn9ngzyqa1I/l2VpOhT13hL+m0cYHHvRv/mdEbGr9/p9R8" +
			"lHRuYENCmMDvdjeuVdXHcoePIu5RKpzI3SAa2vD8kA9Wt+vjpQVR9fDPDbiNSzc6" +
			"fKFAw9histbcfQUY0B5fwxNGGMJ9AgMBAAECgYEAm1vhDZR7YAje1D9jD6xegGdN" +
			"kKEBLeYHd9A5N8BM4Y8VI+m7VzXWLNBvwMlcoPbDQ7jCRX76skTlxq6KvwMljkx2" +
			"MRkoSBNyhjF/M/PJ7wj8WjPJyQxkVNNstdr+TKQ9Mv/x+juzwmIddYNWc6irgC4/" +
			"yt32WX6daSTDx7x28gkCQQD7mcNNZd3THOWzty2VwogC3z+UIbiJL+ZDUO6lG/tL" +
			"6wiAWxspyR/4FablTGSvfYFrL/1FpEmpF2tv9paAj/RvAkEA9Lcwh6EmCK5mWNq6" +
			"NuzVaRx0iPgKUicS2jsFHt47GjZ9qhxsgwRWqFfm2b384lwqLuQeHfTtYUmaKqHH" +
			"hmXl0wJAPC9a/Z+5gW+vLt9Jo0JmgLFgpxGt7/sqTL4GaC6aXV26qeGhTMyUviaH" +
			"xuAQUb5m//n5zvfuuukkRSMqe1R0NwJAVbAfih5fVwLaS5qrQbhEMf4IwD06P/H2" +
			"o/uum1RtlYwnDH3PcUD/pwrAhtYDyq9DBhBSi2LMDBjdprfnSaQ8iQJAZ7fWi9iq" +
			"wUuJ3z/UvKMMfnavCGl2A8b5KQ2Xe8nLr+1yK0HrbsUVnALhG3yjW6ffQ7p+xGFZ" +
			"S3/mZebWh9DUqg==";

	// 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
	public static String alipay_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDwgpjCgoDU4qPon37TxIiMnoUPoRhKcp0j5/Z4M8qmtSP5dlaToU9d4S/ptHGBx70b/5nRGxq/f6fUfJR0bmBDQpjA73Y3rlXVx3KHjyLuUSqcyN0gGtrw/JAPVrfr46UFUfXwzw24jUs3OnyhQMPYYrLW3H0FGNAeX8MTRhjCfQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//TODO
	public static String notify_url = "http://121.42.196.236:8018/weitaomi/pc/admin/paymemberCallBack/verifyAlipayNotify";
	//批量付款通知页面
	public static String batchPay_notify_url = "http://121.42.196.236:8018/weitaomi/pc/admin/paymemberCallBack/verifyAlipayNotify";

	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	//TODO
	public static String return_url = "http://商户网址/alipay.wap.create.direct.pay.by.user-JAVA-UTF-8/return_url.jsp";

	// 签名方式
	public static String sign_type = "RSA";

	// 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
	public static String log_path = "D:\\projectlogs\\weitaomi\\alipay";

	// 字符编码格式 目前支持utf-8
	public static String input_charset = "utf-8";

	// 支付类型 ，无需修改
	public static String payment_type = "1";

	// 调用的接口名，无需修改
	public static String service = "mobile.securitypay.pay";
	// 批量付款接口
	public static String batchPayservice = "batch_trans_notify";

}

