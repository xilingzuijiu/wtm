/**
 * Created by yuguodong on 2016/10/11.
 */

var puser={
    purseDataDeal: function (data) {
        $(".pursebag>h6>span").text(data.memberScore/100)
        $(".pursebag>p>span").text(data.avaliableScore/100)
    },
    getMemberRequestHeaderMsg:function(XMLHttpRequest){
    var memberId= $.cookie("memberId");
    if (memberId==null||memberId == undefined){
        Showbo.Msg.confirm("登录已过期请重新登录", function () {
            location.href="/frontPage/wap/login.html"
        })
    }
    var password= $.cookie("password");
    XMLHttpRequest.setRequestHeader("memberId",memberId);
    XMLHttpRequest.setRequestHeader("from",2);
},
    updateMyscore:function(){
    $.ajax({
        url:"/pc/admin/member/updateMemberScore",
        type:"post",
        beforeSend: function (XMLHttpRequest) {
            puser.getMemberRequestHeaderMsg(XMLHttpRequest)
        } ,
        success: function (params) {
            var data=eval(params)
            var errorCode=data.errorCode
            if (errorCode==0){
                puser.purseDataDeal(data.data)
            } else if (errorCode==4){
                alert(data.message)
            }
        }
    })
},
    getFlowScoreDetail: function () {
        $.ajax({
            url:"/pc/admin/payment/getMemberWalletInfo",
            type:"get",
            beforeSend: function (XMLHttpRequest) {
                puser.getMemberRequestHeaderMsg(XMLHttpRequest)
            },
            success: function (params) {
                var data=eval(params)
                var errorCode=data.errorCode
                if (errorCode==0){
                    puser.showFlowScoreDetail(data.data)
                } else if (errorCode==4){
                    alert(data.message)
                }
            }
        })
    },
    showFlowScoreDetail: function (data) {
        data.list.forEach(function (memberScoreFlowDto) {
            var li = document.createElement("li");
            var div = document.createElement('div');
            div.className="cashname";
            var h4 = document.createElement('h4');
            h4.innerHTML = memberScoreFlowDto.typeName;
            var span = document.createElement('span');
            span.innerHTML = "（"+memberScoreFlowDto.typeDesc+"）";
            var p = document.createElement('p');
            p.innerHTML = puser.getLocalTime(memberScoreFlowDto.flowTime);
            var h6 = document.createElement('h6');
            h6.innerHTML = memberScoreFlowDto.flowCount/100+"元";
            var h5 = document.createElement('h5');
            if (memberScoreFlowDto.isFinished==0){
                h5.className="margin0";
                h5.innerHTML = "待审核";
            }
            li.appendChild(div);
            div.appendChild(h4);
            h4.appendChild(span);
            div.appendChild(p);
            li.appendChild(h6);
            li.appendChild(h5);
            //document.body.appendChild(li);
            document.getElementsByTagName('ul')[0].appendChild(li);
        })
    },
     getLocalTime:function(nS){
    return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}
}