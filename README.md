﻿# QuickLogin
一个基于Minecraft Spigot的登录插件

用法
/register <密码> <确认密码>
/login <密码>
/exchangepassword <旧密码> <新密码> <确认新密码> 玩家修改自己的密码
/opexchange <玩家> <密码> 管理员修改指定玩家的密码
/ql add <玩家> <密码> 管理员添加玩家账号(此指令绕过密码要求检查)
/ql message join/leave <消息> 设置玩家登录/离开提示
/ql sound <声音> <音量> 设置登录音效
/ql kick <时间> 设置玩家登录时间，超过将踢出(0为没有时间限制)
/ql players 展示所有已注册的玩家
/ql skull 开/关头颅皮肤显示(对于players指令)
/ql auto <时间> 设置离线后自动登录的时间(以秒为单位，0为不开启该功能)
/ql password <次数> 玩家输入密码错误超过该次数就会被踢出(默认为5)
