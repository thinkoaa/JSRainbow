# JSRainbow
BurpSuite插件，调用外部js处理payload，可以有效解决前端js对某些字段加密的问题，以及做fuzz时，处理逻辑可以在js文件中定义，不必写python、java等代码，灵活高效，你值得拥有。

使用时，只需要 把js逻辑写进一个js文件中，然后在该文件中定义好方法，该方法的入参是BurpSuite传入的payload，出参是加工好的新payload，出参可以是：

1.一个字符串 如burp传来 payload=123，可以在js中加工、加密成任意字符newPayload后 直接return newPayload

2.多个出参的组合 如burp传来payload=123，经过加工之后，可以return ?passwd=newpayload&t=Math.random()&x=1&y=2等等，还可以任意调整参数的顺序

3.多个入参组合，如burp传来payload=abc&123&456或payload=lucy:abc等等有格式的payload,那么可以在js中按照自己的想法分割入参，然后任意处理，再按照自己的想法return newpayload即可。

导入：可以修改编译源码自己导入插件，也可以直接下载jar包，然后导入进burp

其余的大家任意发挥，核心思想就是：1.得到burp传来的payload--->2.js中任意逻辑处理payload---->3.return 处理之后的payload 用来爆破、用来fuzz各种 有问题发邮箱：mrlixiaopeng@qq.com
