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
    //用户修改名称
    $("#headimg").click(function(){
        $("#cover").css("display","block");
        $(".changename").css("display","block");
    })
    var obj=document.getElementById("cover");
    obj.addEventListener("touchstart",function(e){
        $("#cover").css("display","none");
        $(".changename").css("display","none");
    },false)
    $("#changenamesave").click(function(){
        var originname=decodeURI($.cookie("memberName"))
        console.log(originname);
        var nowname=$("#changeaccountname").val();
        console.log(nowname);
        if(originname!=nowname){
            $.ajax({
                type:"post",
                url:"/pc/admin/member/modifyMemberName",
                data:{memberName:nowname},
                beforeSend: function (XMLHttpRequest) {
                    getMemberRequestHeaderMsg(XMLHttpRequest)
                },
                success:function(params){
                    var json=eval(params);
                    if(json!=null&&json.errorCode==0){
                        $.cookie("memberName", encodeURI(nowname), {expires: 30, path: "/frontPage/wap"});
                        $("#headimg").text(decodeURI($.cookie("memberName")));
                        $("#prompt").fadeIn(500);
                        $("#prompt").text("用户名修改成功");
                        $('#prompt').delay(600).fadeOut(350);
                        $("#cover").css("display","none");
                        $(".changename").css("display","none");
                    }else{
                        alert(json.message);
                    }
                }
            })
        }
    })
    $(".mywtm").click(function(){
        location.href="../invite.html?memberId="+ $.cookie('memberId');
    })
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
    $("#demo1").val(getLocalTime(($.cookie("birth")==null||$.cookie("birth")==undefined)?0:$.cookie("birth")));
    var sex=parseInt($.cookie("sex"));
    switch (sex){
        case 0:
            $(".sexmi").text("保密");
            break;
        case 1:
            $(".sexmi").text("男");
            break;
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
    var date = new Date(Math.floor(timestamp) * 1000);
    return [date.getFullYear(), date.getMonth()+1, date.getDate()].join('/');
}