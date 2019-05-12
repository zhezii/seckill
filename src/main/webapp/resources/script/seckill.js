//存放主要交互逻辑js代码
//javascript 模块化
var seckill ={
    //封装秒杀相关ajax的url
    URL : {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution : function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/excution';
        }
    },
    handleSeckillkill : function(seckillId,node){
        //获取秒杀地址,控制显示逻辑,执行秒杀
        node.hide().
            html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            //在回调函数中执行交互流程
            if (result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    console.log("md5:"+md5)
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl" + killUrl);
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求
                        $(this).addClass('disabled');
                        //发送秒杀请求
                        console.log(killUrl);
                        $.post(killUrl,{},function (result) {
                            if (result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>')
                            }
                        });
                    });
                    node.show();
                }else{
                    //未开启秒杀
                    var nowTime = result['now'];
                    var startTime = result['startTime'];
                    var endTime = result['endTime'];
                    //重新计算计时逻辑
                    seckill.countdown(seckillId, nowTime, startTime, endTime);
                }
            } else {
                console.log('result:' + result);
            }
        });
    },
    //验证手机号
    validatePhone: function(phone){
        if(phone && phone.length == 11 && !isNaN(phone)){
            return true;
        }else{
            return false;
        }
    },
    countdown:function(seckillId,nowTime,startTime,endTime){
        var seckillBox = $('#seckill-box');
        //时间判断
        if (nowTime > endTime){
            //秒杀结束
            seckillBox.html('秒杀结束!');
        }else if (nowTime < startTime) {
            //秒杀未开始,计时
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime,function (event) {
                //时间格式
                var format = event.strftime('秒杀倒计时: %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown',function () {

                seckill.handleSeckillkill(seckillId,seckillBox);
            });
        }else {
            //秒杀开始
            seckill.handleSeckillkill(seckillId,seckillBox);
        }
    },
    //详情页秒杀逻辑
    detail : {
        //详情页初始化
        init : function (params) {
            //用户手机验证和登录,即使交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定手机号
                //控制输出
                var killPhoneModel = $('#killPhoneModel');
                killPhoneModel.modal({
                    show: true,//显示弹出层
                    backdrop: 'static',//禁止位置关闭
                    keyboard: false//关闭键盘事件
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhone').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //电话写入cookie
                        $.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
                        //刷新页面
                        window.location.reload();
                    }else {
                        $('#killPhoneMessage').hide().html('<label class="label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(seckill.URL.now(),{},function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //时间判断,计时交互
                    seckill.countdown(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result:' + result);
                }
            });
        }
    }
}