/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow strict-local
 */

import React, {Component} from 'react';
import {
  StyleSheet,
  View,
  Text,
  Button
} from 'react-native';

import zoomsdk from 'zoom-sdk';

const zoomUserType = 2;

const config = {
  zoom: {
    sdkKey: "",
    sdkSecret: "",
    domain: "zoom.us",
    jwtToken:''
    //meetingNo = "74009434385";
  }
};

type Props = {};
export default class App extends Component<Props> {
  meetingNo = '74009434385'; 

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

    // TODO recieve user's details from zoom API? WOUT: webinar user is different
    const userId = 'Admin'; // NOTE: no need for userId when using zakToken
    const userType = zoomUserType;
    const zoomToken = 'token'; // NOTE: no need for userId when using zakToken

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
    const displayName = 'Test student';

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

  render() {
    return (
      <View style={styles.container}>
        <Button
          title="Initialize Zoom"
          onPress={()=> this.componentDidMount()}
        />
        <Text>------</Text>
        <Button
          onPress={() => this.start()}
          title="Start example meeting"
        />
        <Text>-------</Text>
        <Button
          onPress={() => this.join()}
          title="Join example meeting"
        />
        <Text>-------</Text>

        <Text>{config.zoom.sdkKey}</Text>
        <Text>-------</Text>
        <Text>{config.zoom.sdkSecret}</Text>
        <Text>-------</Text>
        <Text>{config.zoom.domain}</Text>
        <Text>-------</Text>
        <Text>{config.zoom.jwtTokenRaw}</Text>

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