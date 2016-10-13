/**
 * Created by Administrator on 2016/10/13 0013.
 */
var hashMap = {
    Set : function(key,value){this[key] = value},
    Get : function(key){return this[key]},
    Contains : function(key){return this.Get(key) == null?false:true},
    Remove : function(key){delete this[key]}
}
var memberId= $.cookie('memberId');
$(function(){
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
                json.data.forEach(function (task) {
                    if(task.memberTask.id!=10){
                        var li=document.createElement('li');
                        li.id=task.memberTask.id;
                        li.setAttribute("onClick", "taskSubmit(this)");
                        hashMap.Set(li.id, task.url);
                        var h6=document.createElement('h6');
                        h6.innerHTML=task.memberTask.taskName;
                        var p=document.createElement('p');
                        p.innerHTML=task.memberTask.pointCount+"米";
                        li.appendChild(h6);
                        li.appendChild(p);
                        document.body.appendChild(li);
                        if(task.isFinisherToday==0){
                            document.getElementsByTagName('ul')[0].appendChild(li);
                        }else{
                            $(".undotask").css("display","block");
                            li.setAttribute("onClick", "");
                            document.getElementsByTagName('ul')[1].appendChild(li);
                        }
                    }
                })
            } else
            {
                alert("获取数据失败")
            }
        },error:function(data){
            alert("页面加载错误，请重试");
        }
    })
    $("#signday").click(function(){
        $(".daysignbox").show();
    })
})
var taskSubmit=function(obj){
    var taskId=parseInt(obj.getAttribute("id"));
    console.log(taskId);
    switch(taskId){
        case 5:
            $(".daysignbox").show();
            $(".signbutton").click(function(){
                $.ajax({
                    type: 'get',
                    contentType: "application/json",
                    dataType: 'json',
                    url: '/pc/admin/memberTask/addDailyTask',
                    data:{memberId:memberId,taskId:taskId},
                    beforeSend: function (XMLHttpRequest) {
                        getMemberRequestHeaderMsg(XMLHttpRequest)
                    },
                    success:function(params){
                        var json=eval(params);
                        if(json!=null&&json.errorCode==0){
                            if(json.data){
                                $(this).attr("src","../../code/img/MainViewImgae/signtwo.png");
                                $(this).css("opacity","1");
                                $("#prompt").css("display", "block");
                                $("#prompt").text(json.message);
                                $('#prompt').delay(600).fadeOut(350);
                                $(".daysignbox").delay(1000).hide(50);
                                location.reload();
                            }
                        }else{
                            alert("签到失败")
                        }
                    },
                    error:function(data){
                        alert("页面加载错误，请重试");
                    }
                })
            })
        case 10:

        case 2:

        case 4:
    }
}