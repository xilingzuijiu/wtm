/**
 * Created by Administrator on 2016/11/3.
 */
var count = 0;
    function blur(){
        $("#cleckmoney").blur(function (){
            var howmoney=parseInt(Math.ceil($("#cleckmoney").val()));
            $("#cleckmoney").val(howmoney);
            console.log("输入框是" + howmoney);
            if(howmoney>=50){
                if (howmoney!=null&&howmoney>0) {
                    var review = confirm("确定充值" + howmoney + "元？");
                    if (review){
                        $("#cleckmoney").css("border", "#ddd 1px solid");
                        $(".moneylist li").css("cursor", "default");
                        $(".moneylist li").css("color", "#666");
                        $(".moneylist li").css("border", "#ddd 1px solid");
                        $("#cleckmoney").attr("disabled", "disabled");
                        count = 1;
                        request(howmoney);
                    }
                }else {
                    $("#cleckmoney").val("");
                    alert("输入金额有误，请重新输入");
                }
            }else{
                alert("最小充值金额为50元");
                $("#cleckmoney").val("");
            }
        });
        cleckpaycash();
    }
function cleckpaycash(){
    console.log("count的值是"+count);
    $(".moneylist li").click(function(){
        if (count==0) {
            console.log("count的第二个值是" + count);
            count++;
            howmoney = $(this).find("span").text();
            console.log(howmoney);
            var review = confirm("确定充值" + howmoney + "元？");
            if (review){
                $(this).siblings().css("border", "#ddd 1px solid");
                $(this).siblings().css("color", "#666");
                $("#cleckmoney").css("border", "#ddd 1px solid");
                $("#cleckmoney").attr("disabled", "disabled");
                $(".moneylist li").css("cursor","default");
                request(howmoney);
            }
        }
    })
}

function request(howmoney){
    var obj=new Pay(5,howmoney);
    var requestParams=JSON.stringify(obj)
    $.ajax({
        type: 'post',
        contentType: "application/json",
        dataType:'json',
        url: '/pc/admin/payment/getPCPaymentParams',
        data: requestParams,
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function (params) {
            var json = eval(params);
            if (json.data != null && json.errorCode == 0) {
                $(".confirmOrder").css("display","block");
                var str = json.data.code_url;
                $("#qrcode").qrcode({
                    render: "canvas",
                    text: str,
                    width: 180,
                    height: 180,
                })
                $(".paycode").css("display","block");
                $(".saocode img").css("margin","0");
                $(".saocode img").css("float","left");
            } else {
                alert("获取数据失败");
            }
        }, error: function (data) {
            alert("页面加载错误，请重试");
        }
    })
}
function Pay(payType,amount){
    this.payType=payType
    this.amount=amount
}
function toUtf8(str) {
    var out, i, len, c;
    out = "";
    len = str.length;
    for(i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if ((c >= 0x0001) && (c <= 0x007F)) {
            out += str.charAt(i);
        } else if (c > 0x07FF) {
            out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
            out += String.fromCharCode(0x80 | ((c >>  6) & 0x3F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        } else {
            out += String.fromCharCode(0xC0 | ((c >>  6) & 0x1F));
            out += String.fromCharCode(0x80 | ((c >>  0) & 0x3F));
        }
    }
    return out;
}


var number=0
function getMemberRequestHeaderMsg(XMLHttpRequest) {
    var memberId = $.cookie("memberId");
    var password = $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId", memberId);
    XMLHttpRequest.setRequestHeader("from", 1);
}
