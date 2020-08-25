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
   
   meetingNo = ''; //TODO
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
           onPress={() => this.joinMeetingWithPassword()}
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