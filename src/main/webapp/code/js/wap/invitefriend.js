/**
 * Created by Administrator on 2016/10/15 0015.
 */
$(function(){
    var a=$("#Notwo .personimg").offsetWidth;
    $("#Notwo .personimg").css("height",a);
    console.log("图像宽度是"+a);
    var inviteheight=$(window).height();
    var eventheight=$(".eventintroduce").height();
    var payconfirmheight=Math.ceil($(".payconfirm").height());
    $(".nullbox").css("height",payconfirmheight*1.5);
    var nulltop=$('.payconfirm')[0].offsetTop;
    $(".nullbox").css("top",nulltop);
    $(".eventintroduce").css("top",inviteheight/2-eventheight/2);
    //弹窗遮罩效果
    $(".btonshare").click(function(){
        $("#cover").css("display","block");
        $(".eventintroduce").css("display","block");
        $(".nogo").css("height",inviteheight);
        $(".nogo").css("overflow","hidden");
        $(".nullbox").css("display","block");
        $(".payconfirm").attr("disabled","disabled");
    })
    var obj=document.getElementById("cover");
    obj.addEventListener("touchstart",function(e){
        $("#cover").css("display","none");
        $(".eventintroduce").css("display","none");
        $(".nogo").css("overflow","auto");
        $(".nullbox").click(function(){
            $(".nullbox").css("display","none");
            $('.payconfirm').removeAttr("disabled");
        })
    },false);
    $(".payconfirm").click(function(){
        location.href="../invite.html?memberId="+ $.cookie('memberId');
    });
    $.cookie("memberId");
    $.ajax({
        type:'post',
        dataType:'json',
        url:'/pc/admin/memberInvited/getInvitedParamsDto',
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params){
            var json=eval(params);
            if(json.data!=null&&json.errorCode==0){
                $(".invitedScore").text(json.data.invitedScore);
                $("#totalScore").text(json.data.totalScore);
                $("#invitecode").text(json.data.invitedCode);
            }else
            {
                alert("获取数据失败")
            }
        },
        error:function (data) {
            alert("页面加载错误，请重试");
        }
    })
    $.ajax({
        type:'post',
        dataType:'json',
        url:'/pc/admin/memberInvited/getTotalSharedMsg',
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params){
            var json=eval(params);
            if(json.data!=null&&json.errorCode==0){
                var length=json.data.length;
                if(length==1){
                    $("#Notwo").css("display","inline-block");
                    $("#Notwo .personimg").attr("src",json.data[0].imageUrl);
                    $("#Notwo p strong").text(json.data[0].shareCounts+"米");
                    $("#Notwo h6").text(json.data[0].memberName);
                }else if(length==2){
                    $(".inviterank li:not('#Nothree')").css("display","inline-block");
                    for(var i=0;i<2;i++){
                        $(".inviterank li").eq(i).find('.personimg').attr("src",json.data[1-i].imageUrl);
                        $(".inviterank li").eq(i).find('strong').text(json.data[1-i].shareCounts+"米");
                        $(".inviterank li").eq(i).find('h6').text(json.data[1-i].memberName);
                    }
                }else{
                    $(".inviterank li").css("display","inline-block");
                    for(var i=0;i<3;i++){
                        $("#Noone .personimg").attr("src",json.data[1].imageUrl);
                        $("#Noone p strong").text(json.data[1].shareCounts+"米");
                        $("#Noone h6").text(json.data[1].memberName);
                        $("#Notwo .personimg").attr("src",json.data[0].imageUrl);
                        $("#Notwo p strong").text(json.data[0].shareCounts+"米");
                        $("#Notwo h6").text(json.data[0].memberName);
                        $("#Nothree .personimg").attr("src",json.data[2].imageUrl);
                        $("#Nothree p strong").text(json.data[2].shareCounts+"米");
                        $("#Nothree h6").text(json.data[2].memberName);
                    }
                    for(var j=3;j<length;j++){
                        var li=document.createElement('li');
                        var p=document.createElement('p');
                        var img=document.createElement('img');
                        var h5=document.createElement('h5');
                        var h6=document.createElement('h6');
                        li.appendChild(p);
                        li.appendChild(img);
                        li.appendChild(h5);
                        li.appendChild(h6);
                        document.body.appendChild(li);
                        document.getElementsByTagName('ul')[1].appendChild(li);
                        if(j==9){
                            p.innerHTML=j+1;
                        }else{p.innerHTML="0"+(j+1);}
                        img.setAttribute('src',json.data[j].imageUrl);
                        h6.innerHTML=json.data[j].shareCounts;
                        h5.innerHTML=json.data[j].memberName;
                    }
                }
            }else
            {
                alert("获取数据失败")
            }
        },
        error:function (data) {
            alert("页面加载错误，请重试");
        }
    })
    $("#inviterecord").click(function(){
        location.href="/frontPage/wap/inviterecord.html";
    })
})