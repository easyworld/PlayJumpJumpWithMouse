# PlayJumpJumpWithMouse
用鼠标玩微信跳一跳
---
## 缘起
微信刚刚更新了一个版本，主推了一个叫跳一跳的小程序。无奈手残，于是想办法提高自己的好友排名，于是有了这个。

## 原理
用usb调试安卓手机，用adb截图并用鼠标测量距离，然后计算按压时间后模拟按压。
```
adb shell input swipe <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen) # 模拟长按
adb shell screencap <filename> # 保存截屏到手机
adb pull /sdcard/screen.png # 下载截屏文件到本地
```

## 使用方法 
**当前最新代码版本为0.0.5**
1. 在电脑上下载好adb，并安装JDK7及以上版本以及配置好java环境变量
2. 打开安卓手机的usb调试模式并授权连接的电脑
   >  如果是小米手机，在USB调试下方有``USB调试（安全设置）``打开允许模拟点击 感谢[@wotermelon](https://github.com/wotermelon)
3. 打开微信跳一跳，并**点击开始**
4. 用终端打开adb，并执行一下adb shell，确认adb已经连接上手机后输入exit离开adb shell
   > 如果没有连接上，请通过搜索引擎查找原因
5. 输入命令
```
java -jar playJumpJumpWithMouse.jar -a <your adb path>
```
以windows为例：
```
java -jar playJumpJumpWithMouse.jar -a "C:\Users\Home\Desktop\platform-tools\adb.exe"
```
   > 如果想调整其他参数可以使用 java -jar playJumpJumpWithMouse.jar -h 查看更多选项
6. 直接点击想要跳跃地点即可，跳跃完成后，需稍稍等一会儿

## 半自动以及全自动模式

```
-m 1: manual-mode , 2: semi-mode(default) , 3: auto-mode 
```
### 说明
* 手动模式(manual-mode):弹出的窗口中先点击小人底部适当位置，然后再点想要跳的箱子的中心位置即可完成.
* 半自动模式(semi-mode):只需要点击一次鼠标,即只需要点击终点的中心点就可以了.(默认模式)
* 全自动模式(auto-mode):也就是挂机模式,不需要人工操作,启动后既可以自动识别算法自动帮你玩跳一跳.

## 一些需要注意的点
* 默认分辨率比例为：**675x1200**，如果你的手机分辨率不是该比例则必须设置-s参数后手动和半自动才能计算准确。比如mate10 pro的分辨率是1080x2160，则设置为**-s 675x1350**.

## 运行截图
![这是一个截图](https://github.com/easyworld/PlayJumpJumpWithMouse/raw/master/screenshot.png)

## 推荐的adb下载地址
* [SDK Platform-Tools for Windows](https://dl.google.com/android/repository/platform-tools-latest-windows.zip)
* [SDK Platform-Tools for Mac](https://dl.google.com/android/repository/platform-tools-latest-darwin.zip)
* [SDK Platform-Tools for Linux](https://dl.google.com/android/repository/platform-tools-latest-linux.zip)

## 感谢

* [@xenv](https://github.com/xenv)的文件流截图法
* [@hxzqlh](https://github.com/hxzqlh)的常量命名和操作系统判断
* [@kokutoukiritsugu](https://github.com/kokutoukiritsugu)的批处理脚本
* [@lrz0](https://github.com/lrz0)的修改建议
* [@bambootang](https://github.com/bambootang)的自动/半自动跳跃代码
