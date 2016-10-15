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
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: '${appid}', // 必填，公众号的唯一标识
            timestamp: '${timestamp}', // 必填，生成签名的时间戳
            nonceStr: '${nonceStr}', // 必填，生成签名的随机串
            signature: '${signature}',// 必填，签名，见附录1
            jsApiList: ["chooseWXPay"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });
//        $(function () {
//            var height = $(".payway h3").height();
//            console.log(height);
//            //js制作单选框效果
//            $('label').click(function () {
//                var radioId = $(this).attr('name');
//                $('label').removeAttr('class') && $(this).attr('class', 'checked');
//                $('input[type="radio"]').removeAttr('checked') && $('#' + radioId).attr('checked', 'checked');
//            });
//            $("#pay").click(function () {
//                if (typeof WeixinJSBridge == "undefined") {
//                    if (document.addEventListener) {
//                        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
//                    } else if (document.attachEvent) {
//                        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
//                        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
//                    }
//                } else {
//                    getPayRequestParams()
//                }
//            })
//        })
        wx.ready(function () {
            wx.chooseWXPay({
                timestamp:'${pay_timestamp}', // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                nonceStr: '${nonceStrPay}', // 支付签名随机串，不长于 32 位
                package: 'prepay_id=${prepayid}', // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                paySign: '${paySign}', // 支付签名
                success: function (res) {
                    // 支付成功后的回调函数
                }
            });
        })
        <%--function getPayRequestParams(){--%>
            <%--var amount=$("#amount").val()--%>
            <%--var ip='${spbill_create_ip}';--%>
            <%--var requestParams=JSON.stringify(new Params(amount,4,ip))--%>
            <%--$.ajax({--%>
                <%--type: 'post',--%>
                <%--url: '/pc/admin/payment/getPaymentParams',--%>
                <%--contentType:"application/json",--%>
                <%--data:requestParams,--%>
                <%--beforeSend: function (XMLHttpRequest) {--%>
                    <%--getMemberRequestHeaderMsg(XMLHttpRequest)--%>
                <%--},--%>
                <%--success:function (params) {--%>
                    <%--var data=params.data--%>
                    <%--wx.chooseWXPay({--%>
                        <%--timestamp: data.timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符--%>
                        <%--nonceStr: data.noncestr, // 支付签名随机串，不长于 32 位--%>
                        <%--package: 'prepay_id='+data.prepayid, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）--%>
                        <%--signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'--%>
                        <%--paySign: data.paySign, // 支付签名--%>
                        <%--success: function (res) {--%>
                            <%--// 支付成功后的回调函数--%>
                        <%--}--%>
                    <%--});--%>
                <%--}--%>
            <%--});--%>
        <%--}--%>
//        var getMemberRequestHeaderMsg=function(XMLHttpRequest){
//            var memberId= $.cookie("memberId");
//            if (memberId==null||memberId == undefined){
//                Showbo.Msg.confirm("登录已过期请重新登录", function () {
//                    location.href="/frontPage/wap/login.html"
//                })
//            }
//            var password= $.cookie("password");
//            XMLHttpRequest.setRequestHeader("memberId",memberId);
//            XMLHttpRequest.setRequestHeader("from",2);
//        }
//        function onBridgeReady(appId,timestamp,nonceStr,prepay_id,paySign) {
//            WeixinJSBridge.invoke(
//                    'getBrandWCPayRequest', {
//                        "appId": appId,     //公众号名称，由商户传入
//                        "timeStamp":timestamp,         //时间戳，自1970年以来的秒数
//                        "nonceStr":nonceStr, //随机串
//                        "package": "prepay_id="+prepay_id,
//                        "signType": "MD5",         //微信签名方式：
//                        "paySign": paySign //微信签名
//                    },
//                    function (res) {
//                        if (res.err_msg == "get_brand_wcpay_request：ok") {
//                        }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
//                    }
//            );
//        }
//        function Params(amount,payType,ip){
//            this.amount=amount
//            this.payType=payType
//            this.spbill_create_ip=ip
//        }
    </script>
</head>
<body>
<%--<div class="payway">--%>
    <%--<h3>请选择充值方式</h3>--%>
    <%--<div class="bdnone">--%>
        <%--<label><input type="radio" id="wxpay" name="sport" value="微信充值"><img--%>
                <%--src="../../code/img/pay/wxlogot.png"/>微信充值<span>我</span></label>--%>
    <%--</div>--%>
<%--</div>--%>
<%--<div class="paymuch">--%>
    <%--<h3>请选择充值金额</h3>--%>
    <%--<div>充值金额￥--%>
        <%--<input id="amount" type="text" value="" placeholder="请输入金额" autofocus/>元--%>
    <%--</div>--%>
    <%--<ol class="paydetails">--%>
        <%--<li><span>*</span>到账时间最快当天，通常为第二天到账</li>--%>
        <%--<li><span>*</span><strong>周末、法定节假日不处理提现</strong>（周五周六顺延至周一，周日顺延至周二）</li>--%>
        <%--<li><span>*</span>所有账户充值均人工审核，审核完成后由系统自动充值</li>--%>
    <%--</ol>--%>
<%--</div>--%>
<%--<input id="pay" class="payconfirm" type="button" value="确认" />--%>

</body>
</html>