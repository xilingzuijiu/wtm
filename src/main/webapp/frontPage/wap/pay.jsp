<%@ page import="com.weitaomi.systemconfig.util.IpUtils" %><%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/10/14
  Time: 9:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0" name="viewport"/>
    <title>申请充值</title>
    <link href="../../code/css/base.css" rel="stylesheet" type="text/css"/>
    <link href="../../code/css/showBo.css" rel="stylesheet" type="text/css"/>
    <link href="../../code/css/wapstyle.css" rel="stylesheet" type="text/css"/>
    <script src="../../code/js/jquery.min.js"></script>
    <script src="../../code/js/jquery.cookie.js"></script>
    <script src="../../code/js/showBo.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script>
        <%--wx.ready(function () {--%>
            <%--wx.chooseWXPay({--%>
                <%--timestamp:"${pay_timestamp}", // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符--%>
                <%--nonceStr: "${nonceStrPay}", // 支付签名随机串，不长于 32 位--%>
                <%--package: "prepay_id=${prepayid}", // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）--%>
                <%--signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'--%>
                <%--paySign: '${paySign}', // 支付签名--%>
                <%--success: function (res) {--%>
                    <%--if (res.errMsg == "chooseWXPay:ok") {--%>
                        <%--alert("支付成功");--%>
                        <%--// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。--%>
                        <%--location.href="/frontPage/wap/pay.html"--%>
                    <%--}--%>
                    <%--else if (res.errMsg == "chooseWXPay:cancel") {--%>
                        <%--alert("cancel");--%>
                        <%--// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。--%>
                    <%--}--%>
                    <%--else if (res.errMsg == "chooseWXPay:fail") {--%>
                        <%--alert("fail");--%>
                        <%--// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。--%>
                    <%--}--%>
                <%--}--%>
            <%--});--%>
        <%--})--%>
        <%--wx.config({--%>
            <%--debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。--%>
            <%--appId: '${appid}', // 必填，公众号的唯一标识--%>
            <%--timestamp: '${timestamp}', // 必填，生成签名的时间戳--%>
            <%--nonceStr: '${nonceStr}', // 必填，生成签名的随机串--%>
            <%--signature: '${signature}',// 必填，签名，见附录1--%>
            <%--jsApiList: ["chooseWXPay"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2--%>
        <%--});--%>

        window.onload=function()
        {
            if (typeof WeixinJSBridge == "undefined"){
                if( document.addEventListener ){
                    document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
                }else if (document.attachEvent){
                    document.attachEvent('WeixinJSBridgeReady', jsApiCall);
                    document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
                }
            }else{
                jsApiCall();
            }
        }


        function jsApiCall() {
            WeixinJSBridge.invoke(
                    'getBrandWCPayRequest', {
                        "appId": '${appid}',     //公众号名称，由商户传入
                        "timeStamp":'${pay_timestamp}',         //时间戳，自1970年以来的秒数
                        "nonceStr":'${nonceStrPay}', //随机串
                        "package": 'prepay_id=${prepayid}',
                        "signType": "MD5",         //微信签名方式：
                        "paySign": '${paySign}' //微信签名
                    },
                    function (res) {
                        if (res.err_msg == "get_brand_wcpay_request：ok") {
                            alert("支付成功")
                        }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                        if (res.err_msg == "get_brand_wcpay_request：cancel") {
                            alert("取消支付")
                        }
                        if (res.err_msg == "get_brand_wcpay_request：fail") {
                            alert("支付失败")
                        }
                        location.href="/frontPage/wap/pay.html"
                    }
            );
        }
    </script>
</head>
<body>

</body>
</html>