/**
 * Created by yuguodong on 2016/10/11.
 */

var enchashment={
    getChange: function () {
        var requestData=JSON.stringify(enchashment.getRequestData())
        $.ajax({
            url:"/pc/admin/payment/generatorPayParams",
            type:"post",
            contentType:"application/json",
            data:requestData,
            beforeSend: function (XMLHttpRequest) {
                enchashment.getMemberRequestHeaderMsg(XMLHttpRequest)
            } ,
            success: function (params) {
                var data=eval(params)
                var errorCode=data.errorCode
                if (errorCode==0){
                    Showbo.Msg.alert("提现申请成功", function () {
                        location.reload()
                    })
                } else if (errorCode==4){
                    Showbo.Msg.alert(data.message, function () {
                        location.reload()
                    })
                }
            }
        })
    },
    getMemberRequestHeaderMsg:function(XMLHttpRequest) {
        var memberId = $.cookie("memberId");
        if (memberId == null || memberId == undefined) {
            Showbo.Msg.confirm("登录已过期请重新登录", function () {
                location.href = "/frontPage/wap/login.html"
            })
        }
        var password= $.cookie("password");
        XMLHttpRequest.setRequestHeader("memberId",memberId);
        XMLHttpRequest.setRequestHeader("from",2);
    },
    getRequestData:function() {
        var amount=$("select").find("option:selected").val()
        var payAccounts= decodeURI($.cookie("payList"));
        var thirdLogin= decodeURI($.cookie("thirdLogin"));
        if (thirdLogin==null||thirdLogin==undefined){
            Showbo.Msg.alert("未绑定微信或者登录已过期");
            return;
        }
        var thirdLogin= JSON.parse($.cookie("thirdLogin"));
        var payAccount;
        var nickname;
        if (payAccounts!=null && !payAccounts.length>0 ){
            var payAccounts= JSON.parse(payAccounts);
            payAccounts.forEach(function (payAcc) {
                if (payAcc.type==0){
                    payAccount=payAcc.payAccount
                    nickname=payAcc.nickname
                }
            })
        }else{
            payAccount=thirdLogin.openId
            nickname=thirdLogin.nickname
        }
        var remark=$(".wxcashremark").val()
        var memberId= $.cookie("memberId")
        var payType=2
        var approve=new Approve(payAccount,nickname,amount,payType,memberId,remark)
        return approve
    }

}

function Approve(accountNumber,accountName,amount,payType,memberId,remark){
    this.accountNumber=accountNumber
    this.accountName=accountName
    this.amount=amount
    this.payType=payType
    this.memberId=memberId
    this.remark=remark
}