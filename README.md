# 仿IOS风格的ViewPager导航条

![静态图片](https://github.com/HaoFeiWang/IndicatorFromIos/blob/master/screen/screen.jpg)
![动态图片](https://github.com/HaoFeiWang/IndicatorFromIos/blob/master/screen/screen.gif)

## 添加依赖
- 在`Project`的`build.gradle`中加入`maven`地址

```
allprojects {
    repositories {
        jcenter()
        //加入的地址
        maven{ url 'https://dl.bintray.com/devhaofei/maven'}
    }
}
```

- 在`Moudle`的`build.gradle`中添加依赖
```
compile 'com.whf:IndicatorFromIos:1.0.3'
```

## 属性介绍

|属性|说明|
|--|--|
|unSelectedBackground|未被选中的Item的背景色|
|selectedBackground|被选中的Item的背景色|
|unSelectedTextColor|未被选中的Item中的字体颜色|
|selectedTextColor|被选中的Item中的字体颜色|
|lineColor|线条的颜色|
|textSize|字体的大小|
|lineWidth|线的宽度|
|radius|圆角的半径|

## 设置标题数组
```
mIndicatorView.setTitleArray(new String[]{"新闻","娱乐","游戏"});
```

## 与ViewPager配合使用
- 给指示器设置监听
```java
mIndicatorView.setOnItemClickListener(new IndicatorView.OnIndicatorItemClickListener() {
            @Override
            public void onIndicatorItemClick(int position) {
                //当点击指示器的Item时，切换ViewPager
                mViewPager.setCurrentItem(position);
            }
        });
```
- 当ViewPager滑动时，改变指示器的显示
```java
mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            
            @Override
            public void onPageSelected(int position) {
                //当ViewPager的页面改变时，更改指示器的位置
                mIndicatorView.setCurIndex(position);
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
```
