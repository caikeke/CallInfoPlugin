# CallInfoPlugin
获取Android通话记录：
添加权限插件，判断权限
$ ionic cordova plugin add cordova-plugin-android-permissions
$ npm install --save @ionic-native/android-permissions
 this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.READ_CALL_LOG).then(
      success => {
        if (success.hasPermission) {
          cordova.plugins.CallInfoPlugin.getCallInfo("",(msg)=>{
           alert(JSON.stringify(msg));
          },null);
        } else {
          this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.READ_CALL_LOG).then(
            success => {
              if (success.hasPermission) {
                cordova.plugins.CallInfoPlugin.getCallInfo("",(msg)=>{
                  alert(JSON.stringify(msg));
                },null);
              }
            }
          );
        }
      }, null);
