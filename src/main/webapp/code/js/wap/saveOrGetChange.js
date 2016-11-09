/**
 * Created by yuguodong on 2016/10/11.
 */
function cleckhowcash(){
    $.ajax({
        url:"/common/admin/keyValue/getKeyValueDtoList",
        type:"get",
        //contentType:"application/json",
        data:{mapKey:'deposit:amount',id:null},
        beforeSend: function (XMLHttpRequest) {
            enchashment.getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (params) {
            var data=eval(params);
            var errorCode=data.errorCode;
            if (data!=null&&errorCode==0){
                var i=0;
                data.data.forEach(function(){
                    $("#cleckhowcash").append("<option value='Value'>"+data.data[i++].value+"</option>");
                })
            } else{
                alert(data.message);
            }
        }
    })
}


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
                    alert("提现申请成功");
                        location.reload()
                } else if (errorCode==4){
                    alert(data.message);
                        location.reload()
                }
            }
        })
    },
    getMemberRequestHeaderMsg:function(XMLHttpRequest) {
        var memberId = $.cookie("memberId");
        if (memberId == null || memberId == undefined) {
            var result=confirm("登录已过期请重新登录");
            if(result){
                location.href = "/frontPage/wap/login.html"
            }
        }
        var password= $.cookie("password");
        XMLHttpRequest.setRequestHeader("memberId",memberId);
        if(isAndroid){
            XMLHttpRequest.setRequestHeader("from",6);
        }else if(isiOS){
            XMLHttpRequest.setRequestHeader("from",7);
        }else {
            XMLHttpRequest.setRequestHeader("from",2);
        }
    },
    getRequestData:function() {
        var index=$("select").get(0).selectedIndex;
        if(index!=0){
            var amount=$("select").find("option:selected").text();
        console.log(amount);
        var payAccounts= decodeURI($.cookie("payList"));
        var thirdLogin= decodeURI($.cookie("thirdLogin"));
        if (thirdLogin==null||thirdLogin==undefined){
            alert("未绑定微信或者登录已过期");
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
        }else {
            alert("金额输入错误")
        }
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

function initilizePage(){
    $.ajax({
        url:"/pc/admin/member/getAvaliableScoreAndWxInfo",
        type:"post",
        beforeSend: function (XMLHttpRequest) {
            enchashment.getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (params) {
            var data=eval(params)
            var errorCode=data.errorCode
            if (errorCode==0){
                $("#cashon span").text((data.data.avaliableScore/100).toFixed(2))
                $("#cashaccount").text(data.data.nickname)
            } else if (errorCode==4){
                alert(data.message);
            }
        }
    })
}