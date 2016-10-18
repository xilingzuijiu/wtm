/**
 * Created by Administrator on 2016/10/18 0018.
 */
$(function(){

    initilizePage()
    if ($.cookie("birth")==null||$.cookie("birth")==undefined) {
        var calendar = new lCalendar();
        calendar.init({
            'trigger': '#demo1',
            'type': 'date'
        });//日期插件
    }


//            var inviteheight=$(window).height();
//            var invitewidth=$(window).width();
//            var wayheight=$(".myinviteway").height();
//            var waywidth=$(".myinviteway").width();
//            $(".myinviteway").css("top",inviteheight-wayheight);
//            $(".myinviteway").css("left",invitewidth/2-waywidth/2);
//            $("#imgaccount").click(function(){
//                $("#cover").css("display","block");
//                $(".myinviteway").css("display","block");
//                $(".nogo").css("height",inviteheight);
//                $(".nogo").css("overflow","hidden");
//                $("#demo1").attr("disabled","disabled");
//                $("select").attr("disabled","disabled");
//            })
//            $("#myinvitecancle").click(function(){
//                $("#cover").css("display","none");
//                $(".myinviteway").css("display","none");
//                $(".nogo").css("overflow","auto");
//                $('#demo1').removeAttr("disabled");
//                $('select').removeAttr("disabled");
//            })
//            var obj=document.getElementById("cover");
//            obj.addEventListener("touchstart",function(e){
//                $("#cover").css("display","none");
//                $(".myinviteway").css("display","none");
//                $(".nogo").css("overflow","auto");
//                $(".time").click(function(){
//                    $('#demo1').removeAttr("disabled");
//                })
//                $(".sexmi").click(function(){
//                    $('select').removeAttr("disabled");
//                })
//            },false)
    $(".mywtm").click(function(){
        location.href="../invite.html?memberId="+ $.cookie('memberId');
    })
    $(".exit").click(function(){
        var result=confirm("退出当前账号后不会删除任何历史数据，下次登录依然可以使用本账号。");
        if(result){
            location.href="/frontPage/wap/login.html";
        }
    })
    //商家平台
//            if($.cookie("officialAccountList")!=null){
//                $(".openbusy").css("display","none");
//                $(".shop").css("display","block");
//                $("#one").click(function(){
//                    location.href="#";
//                })
//                $("#two").click(function(){
//                    location.href="#";
//                })
//                $("#three").click(function(){
//                    location.href="#";
//                })
//            }

    $("#demo1").on("blur", function () {
        if ($.cookie("birth")==null||$.cookie("birth")==undefined) {
            modifyBirth();
        }
    })
    $("#wechat").click(function(){
        if ($("#wechat>p").text()=="未绑定微信"){
            location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2282c57276f83fad&redirect_uri=http%3a%2f%2fwww.weitaomi.cn%2fblindWXOAuth%3fmemberId%3d"+ $.cookie("memberId")+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        }
    })
})
function modifyBirth(){
    var date =$("#demo1").val() ;
    date = date.replace(/-/g,'/');
    console.log("日期是"+date);
    var timestamp = new Date(date).getTime()/1000;
    $.ajax({
        url:"/pc/admin/member/modifyBirth",
        type:"post",
        data:{birth:timestamp},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success: function (params) {
            var data=eval(params)
            var errorCode=data.errorCode
            if (errorCode==0){
            } else if (errorCode==4){
                alert(data.message)
            }
        }
    })
}
function initilizePage(){
    if($.cookie("memberId")==null||$.cookie("memberId")==undefined){
        console.log("获取cookie内容失败:"+$.cookie("memberId"))
        alert("登录超时，请重新登录");
        location.href="/frontPage/wap/login.html"
    }
    $("#headimg").text(decodeURI($.cookie("memberName")))
    $(".accounttel>h6>span").text($.cookie("telephone"))
    $(".photoimg").attr("src",$.cookie("imageUrl"));
    $("#demo1").val(getLocalTime($.cookie("birth")));
    var sex=parseInt($.cookie("sex"));
//            $(".sexmi").find("option[value="+sex+"]").attr("selected",true);
    switch (sex){
        case 0:
            $(".sexmi").text("保密");
        case 1:
            $(".sexmi").text("男");
        case 2:
            $(".sexmi").text("女");
    }
    var thirdLogin=$.cookie("thirdLogin");
    if (thirdLogin==null||thirdLogin==undefined||thirdLogin.length<=0){
        $("#wechat>p").text("未绑定微信")
    } else{
        $("#wechat>p").text("已绑定微信")
    }
}

function getLocalTime(timestamp) {
    console.log(timestamp);
    function add0(y) {
        return y < 10 ? '0' + y : y
    }
    var time = new Date(timestamp * 1000);
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    return add0(y) + '-' +add0(m) + '-' + add0(d);
}//时间戳变换格式
//        var getLocalTime=function(nS){
//            return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
//        }
