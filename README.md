# React-Native-Zoom-SDK

**PULL REQUEST ARE ALWAYS APPRECIATED**

![Kotlin build](https://img.shields.io/badge/Kotlin%20Build-Success-brightgreen)<br>
![Java build](https://img.shields.io/badge/Java%20Build-Success-brightgreen)<br>

## Tested On
```
Npm             :v6.14.7
Node            :v14.7.0
ReactNativeCli  :v2.0.1
ReactNative     :v0.63.2
React           :v16.13.1
ZoomCommonlib   :v5.0.24433.0616
ZoomMobileRTC   :v5.0.24437.0708
JavaOpenJDK     :v11.0.8

AndroidMinSDK   : 22
CompileSDK      : 30
BuildToolsVer   :v30.0.1
Kotlin-version  :v1.4.0
GradleBuildTool :v4.0.1
```
*for more checkout the build.gradle*<br>
[Requrements](https://github.com/zoom/zoom-sdk-android#prerequisites)

## Test App
Test app is in ZoomBridge branch of this repo.

## Features


## Installation

**Clone this repo in the root directory of your project**<br>
If you don't have existing project create one then follow along.

**Java**
```
git clone https://github.com/abughalib/zoom-sdk.git -b master
```
**Kotlin**
```
git clone https://github.com/abughalib/zoom-sdk.git -b kotlin
```

### Configure Package.json
Add this to your package.json of your project in dependencies section<br>
```json
"zoom-sdk": "file:zoom-sdk"
```
**Example**
```json
...
  "dependencies": {
    "react": "16.13.1",
    "react-native": "0.63.2",
    "zoom-sdk": "file:zoom-sdk"
  },
...
```
 **DO**
 ```npm
 npm install
 ```
### Constants
```JavaScript
const config = {
  zoom: {
    sdkKey: "", //TODO
    sdkSecret: "", //TODO
    domain: "zoom.us",
    jwtToken:'token' //JWT-Token not required when sdkKey, SdkSecret is used
  }
};
```

### Uses

**Initialization**
```JavaScript
async componentDidMount() {
    try {
      const initializeResult = await zoomsdk.initialize(
        config.zoom.sdkKey,
        config.zoom.sdkSecret,
        config.zoom.domain,
        //config.zoom.jwtTokenRaw
      );
      console.warn({ initializeResult });
    } catch(e) {
      console.warn({ e });
    }
  }
```
**start Meeting**
```JavaScript
  meetingNo = ''; //TODO
  async start() {
    
    const displayName = 'Test';

    const userId = 'ABC'; 
    const userType = zoomUserType;
    const zoomToken = 'token';

    const zoomAccessToken = "jwtToken";

    try {
      const startMeetingResult = await zoomsdk.startMeeting(
        displayName,
        this.meetingNo,
        userId,
        userType,
        zoomAccessToken,
        zoomToken
      );
      console.warn({ startMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }
  ```
**Join Meeting**
```JavaScript
async join() {
    const displayName = 'Test User';

    try {
      const joinMeetingResult = await zoomsdk.joinMeeting(
        displayName,
        this.meetingNo
      );
      console.warn({ joinMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }
```
**Join Meeting With password**
```JavaScript
async joinWithPass() {
    const displayName = 'Test User';
    const password = '';
    try {
      const joinMeetingResult = await zoomsdk.joinMeetingWithPassword(
        displayName,
        this.meetingNo,
        password
      );
      console.warn({ joinMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }
```
## Example
```JavaScript
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

 //NOTE: Don't keep any value to null.

import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button, TextInput
} from 'react-native';

import zoomsdk from 'zoom-sdk';

const zoomUserType = 2;


const config = {
  zoom: {
    sdkKey: "", //TODO
    sdkSecret: "", //TODO
    domain: "zoom.us",
    jwtToken:'token' //JWT-Token not required when sdkKey, SdkSecret is used
  }
};

type Props = {};
export default class App extends Component<Props> {

  meetingNo = ''; //TODO
  
  async componentDidMount() {
    try {
      const initializeResult = await zoomsdk.initialize(
        config.zoom.sdkKey,
        config.zoom.sdkSecret,
        config.zoom.domain,
        //config.zoom.jwtTokenRaw
      );
      console.warn({ initializeResult });
    } catch(e) {
      console.warn({ e });
    }
  }

  async start() {
    
    const displayName = 'Test';
    const userId = 'XYZ'; // NOTE: no need for userId when using JwtToken
    const userType = zoomUserType;
    const zoomToken = 'token';

    const zoomAccessToken = "jwtToken";

    try {
      const startMeetingResult = await zoomsdk.startMeeting(
        displayName,
        this.meetingNo,
        userId,
        userType,
        zoomAccessToken,
        zoomToken
      );
      console.warn({ startMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }

  async join() {
    const displayName = 'Test User';

    try {
      const joinMeetingResult = await zoomsdk.joinMeeting(
        displayName,
        this.meetingNo
      );
      console.warn({ joinMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }
  async joinWithPass() {
    const displayName = 'Test User';
    const password = '';
    try {
      const joinMeetingResult = await zoomsdk.joinMeetingWithPassword(
        displayName,
        this.meetingNo,
        password
      );
      console.warn({ joinMeetingResult });
    } catch(e) {
      console.warn({ e });
    }
  }
  

  render() {
    return (
      <View style={styles.container}>
        <Button
          onPress={() => this.start()}
          title="Start meeting"
        />
        <Text>-------</Text>
        <Button
          onPress={() => this.join()}
          title="Join meeting"
        />
        <Text>-------</Text>
        <Button
          onPress={() => this.joinWithPass()}
          title="Join meeting with Password"
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
});
```
# Documentation

[Zoom-SDK-Android](https://marketplace.zoom.us/docs/sdk/native-sdks/android)<br>

[Zoom-SDK Github](https://github.com/zoom/zoom-sdk-android)

**Make sure you agree to Zoom Licence before Use** [Licence](https://github.com/zoom/zoom-sdk-android/blob/master/LICENSE.md)

This SDK is the minimal version of Zoom Android SDK [Zoom-SDK-Android](https://github.com/zoom/zoom-sdk-android)

## Note:
**Do not fill any value with null or keep empty it may cause some issue.**
