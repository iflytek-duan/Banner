[![](https://jitpack.io/v/iflytek-duan/Banner.svg)](https://jitpack.io/#iflytek-duan/Banner)
# Banner
使用ViewPager实现无限轮播效果
# 引入步骤
1. 在build.gradle(Project:xxx)中的repositories内添加：
```
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
2. 然后在build.gradle(Module:app)中dependencies内添加：
```
	dependencies {
	        ...
	        compile 'com.github.iflytek-duan:Banner:1.0.0'
	}
```
# 支持属性
> 目前仅支持自定义`isAutoLoop`属性，后期会陆续拓展。

isAutoLoop(标识是否打开/关闭无限轮播开关，默认为打开状态-true)
- 在代码中动态设置：
`banner.setAutoLoop(true);`
- 在xml中静态配置：
`app:autoLoop="true"`
# 设计思路
- 如何实现无限轮播效果？
在ViewPager的最左侧、最右侧各新增一个页卡，用来映射对应的尾页 / 首页并设置相同的图片内容，达到无缝对
接的效果，设计思路如下图：
 ![banner](/images/banner.png)