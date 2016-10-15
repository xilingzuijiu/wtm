/**
 * Created by Administrator on 2016/10/14 0014.
 */
$(function(){
    var memberId= $.cookie('memberId');
    var signheight=$(window).height();
    var signwidth=$(window).width();
    $(".daysign").css("height",signheight);
    $(".daysign").css("width",signwidth);
    $.ajax({
        type: 'post',
        contentType: "application/json",
        dataType: 'json',
        url: '/pc/admin/memberTask/getMemberDailyTask',
        data:{memberId:memberId},
        beforeSend: function (XMLHttpRequest) {
            getMemberRequestHeaderMsg(XMLHttpRequest)
        },
        success:function(params){
            var json=eval(params);
            if(json!=null&&json.errorCode==0){
                if(json.data[0].isFinisherToday==0){
                    $(".signday").click(function(){
                        $(".daysignbox").show();
                        $(".signbutton").click(function(){
                            $.ajax({
                                type: 'get',
                                contentType: "application/json",
                                dataType: 'json',
                                url: '/pc/admin/memberTask/addDailyTask',
                                data:{memberId:memberId,taskId:5},
                                beforeSend: function (XMLHttpRequest) {
                                    getMemberRequestHeaderMsg(XMLHttpRequest)
                                },
                                success:function(params){
                                    var json=eval(params);
                                    if(json!=null&&json.errorCode==0) {
                                        $(this).attr("src", "../../code/img/MainViewImgae/signtwo.png");
                                        $(this).css("opacity", "1");
                                        $("#prompt").css("display", "block");
                                        $("#prompt").text(json.message);
                                        $('#prompt').delay(600).fadeOut(350);
                                        $(".daysignbox").delay(1000).hide(50);
                                        location.reload();
                                    }else{
                                        alert("签到失败")
                                    }
                                },
                                error:function(data){
                                    alert("页面加载错误，请重试");
                                }
                            })
                        })
                    })
                }else{
                    $(".signday").click(function(){
                        alert("今日签到已完成")
                    })
                }
            } else
            {
                alert("获取数据失败")
            }
        },error:function(data){
            alert("页面加载错误，请重试");
        }
    })
})


