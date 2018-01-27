[ ![Download](https://api.bintray.com/packages/ymex/maven/popup-dialog/images/download.svg) ](https://bintray.com/ymex/maven/notice-dialog/_latestVersion)

# popup-dialog

继承自PopupWindow 实现灵活易用的Dialog ，并实现Dialog 的管理功能。Inherited from PopupWindow flexible and easy to use Dialog, and to achieve the management of Dialog function.

## 介绍
Android 及 support 库的AlertDialog 组件样式 常常满足不了日常的开发使用，定义起来又烦琐。
随着项目的需求增长，活动弹窗,升级弹窗，升级公告弹窗，乱七八糟的弹窗越来越多，引入dialog 管理就显的十分必要。
<br>
popup-dialog 实现自 PopupWindow,使用上更灵活，组件复用更便捷。解决了PopupWindow返回键取消的问题，
并引入Dialog管理机制。
<br>


## 引入
```
compile 'cn.ymex:popup-dialog:1.2.7'
```


## 使用

为了方便 popup-dialog 默认实现的 AlertController(类似系统的AlertDialog)与 ProgressController(类似系统的ProgressDialog)。
其自定义功能能满足你的任何dialog 需求。

### 1、AlertController

```
PopupDialog.create(this)
        .controller(AlertController.build()
                .message("提醒")
                .message("登录后才能评论。")
                .negativeButton("取消", null)
                .positiveButton("确定", null))
        .show();
```
AlertController在保持在系统AlertDialog样式的基础。<br>
![AlertController](https://github.com/ymex/popup-dialog/blob/master/arts/alert_controller.png)

### 2、ProgressController


```
PopupDialog.create(this)
        .controller(ProgressController.build().message("loading ... "))
        .show();
```

ProgressController 除了默认为系统风格的样式外还定义了其他两种样式,使用`model(ProgressController.MODE_CIRCLE)`
方法去设置。
![ProgressController](https://github.com/ymex/popup-dialog/blob/master/arts/default_progress.gif)


### 3、自定义DialogController


通过上面两种DialogController的使用相信你已经看出，这种设计模式。
像你看到的那样,`PopupDialog`只作为你要展示 view 的容器，它做的工作仅仅是把你的view展示到最上层和纳入可控的管理中（这个需要主动请求）。
定义DialogController是为了方便管理View（或xml布局文件），只需要实现`DialogControlable`接口。如 ProgressController

```
class CustomDialogController implements DialogControllable {
        @Override
        public View createView(Context cotext, ViewGroup parent) {
            return LayoutInflater.from(cotext).inflate(R.layout.dialog_view, parent, false);
        }

        @Override
        public PopupDialog.OnBindViewListener bindView() {



            return new PopupDialog.OnBindViewListener() {
                @Override
                public void onCreated(PopupDialog dialog,final View layout) {

                    dialog.backPressedHide(true);
                    dialog.outsideTouchHide(false);

                    dialog.backgroundDrawable(new ColorDrawable(Color.parseColor("#66000000")));

                    layout.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditText etNumber = layout.findViewById(R.id.et_number);
                            Toast.makeText(MainActivity.this, "兑换码为：" + etNumber.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            };
        }
}

```
就像上面所说的那样，定义DialogController是为了方便管理View（或xml布局文件），popup-dialog并不强制你
实现DialogController,毕竟我们是自由的，你可以用以下方式使用它。
```
PopupDialog.create(this)
        //设置使用的view xml
        .view(R.layout.dialog_view, new PopupDialog.OnBindViewListener() {
            @Override
            public void onCreated(PopupDialog dialog,View layout) {
                //初始化控件
                EditText etNumber = layout.findViewById(R.id.et_number);
                etNumber.setText("100866");
            }
        })
        //点击事件
        .click(R.id.btn_submit, new PopupDialog.OnClickListener() {
            @Override
            public void onClick(PopupDialog dialog,View layout, View view) {

                EditText etNumber = layout.findViewById(R.id.et_number);
                Toast.makeText(MainActivity.this, "兑换码为：" + etNumber.getText(), Toast.LENGTH_SHORT).show();
            }
        })
        .show();
```

![自定义DialogController](https://github.com/ymex/popup-dialog/blob/master/arts/custom_view.png)


### 4、管理我的Dialog



popup-dialog 实现了dialog管理，目前就实现一种管理方式，按优先级管理，这种管理方式只默认显示一个dialog，
(1:)若新到来的dialog级别小于当前dialog则级别小的不显示。
(2:)若新到来的dialog级别大于当前dialog则关闭当前dialog，显示级别大的dialog。
使用起来相当简单，如下：
```
 DialogManager manager = new DialogManager();
 PopupDialog.create(this)
         .manageMe(manager)//使用 DialogManager 管理这个PopupDialog
         .priority(SECOND_DIALOG)//优先级
         .controller(ProgressController.build().message("loading ... "));
         
 manager.show(SECOND_DIALOG);//使用manager 显示此级别dialog
```

注意：相同优先级的dialog 将被最后加入管理的与其相同优先级的dialog替换。

![管理我的Dialog](https://github.com/ymex/popup-dialog/blob/master/arts/low_p.gif)


### 5、管理已经存在的Dialog
popup-dialog 编写之初就考虑到这个问题，所以把它抽离出来作为接口。 只要实现`Priority`的类。你便可以放心的加入 到
DialogManage中接收管理。



## 最近版本变更

v1.2.7

增加不遮挡虚拟导航的compatShow

OnClickListener增加dialog 参数

v1.2.6

修复动画重复问题

重载相关方法

增加 Toast 样式弹框

增加定时消失方法 dismissTime(ms)

## 已知问题：

1、popdialog 中的EditText无法进行复制与粘贴。




License
-------

    Copyright 2017 ymex.cn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.