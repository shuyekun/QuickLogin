QuickLogin
==
一个基于Minecraft Spigot的玩家登录插件

命令
--
/register <密码> <确认密码><br>
/login <密码><br>
/exchangepassword <旧密码> <新密码> <确认新密码> 玩家修改自己的密码<br>
/opexchange <玩家> <密码> 管理员修改指定玩家的密码<br>
/ql add <玩家> <密码> 管理员添加玩家账号(此指令绕过密码要求检查)<br>
/ql message join/leave <消息> 设置玩家登录/离开提示<br>
/ql sound <声音> <音量> 设置登录音效<br>
/ql kick <时间> 设置玩家登录时间，超过将踢出(0为没有时间限制)<br>
/ql players 展示所有已注册的玩家<br>
/ql skull 开/关头颅皮肤显示(对于players指令)<br>
/ql auto <时间> 设置离线后自动登录的时间(以秒为单位，0为不开启该功能)<br>
/ql password <次数> 玩家输入密码错误超过该次数就会被踢出(默认为5)<br>
