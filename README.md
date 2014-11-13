JavaDictionary
==============

A dictionary with UI designed by Java.

【English】<br>
This is a java program to design a dictionary which can translate english to chinese using the wordlist file.<br>

【Chinese】<br>
【目的】<br>
在学习Java 语言的基础语法与简单的UI设计及事件处理后，便具备了自主设计一个具有良好用户图形界面的电子词典的能力。这份代码就是借助于Swing 类库进行UI 设计，加入事件处理机制，并使用高效的搜索算法实现一个优秀的电子词典应用程序。首先根据需求抽象出各个功能也即目标，然后将各功能进一步抽象为具有某一/某些功能的合理封装的类，进而有序的组合成最终的程序框架。<br>
【功能】<br>
1. 使用Swing 类库画出基本的电子词典框架，包括输入框、联想框、主显区。<br>
2. 在基本的UI 上进行修饰优化，使得界面更加友好。<br>
3. 读入存储字典文件, 使用查找高效的数据结构存储(字典树及BK树)。<br>
4. 正确的绑定事件处理，重载事件处理类。<br>
5. 调试可正确运行, 增加程序的鲁棒性。<br>
6. 设计高效的算法，并且提供纠错能力(使用BK树结构，利用编辑距离的概念进行纠错)。<br>
7. 在version2中增加单词添加、句子翻译功能。
