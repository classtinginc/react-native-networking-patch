/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.modules.network;

import androidx.annotation.Nullable;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter;
import java.net.SocketTimeoutException;

/** Util methods to send network responses to JS. */
public class ResponseUtil {
  public static void sendEvent(
    @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, String eventName, WritableArray args) {
    if (eventEmitter == null) {
      return;
    }

    WritableMap eventArgs = Arguments.createMap();
    eventArgs.putString("eventName", eventName);
    eventArgs.putArray("args", args);

    eventEmitter.emit("events", eventArgs);
  }
  
  public static void onDataSend(
      @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, long progress, long total) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushInt((int) progress);
    args.pushInt((int) total);

    sendEvent(eventEmitter, requestId, "didSendNetworkData", args);
  }

  public static void onIncrementalDataReceived(
      @Nullable RCTDeviceEventEmitter eventEmitter,
      int requestId,
      String data,
      long progress,
      long total) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushString(data);
    args.pushInt((int) progress);
    args.pushInt((int) total);

    sendEvent(eventEmitter, requestId, "didReceiveNetworkIncrementalData", args);
  }

  public static void onDataReceivedProgress(
      @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, long progress, long total) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushInt((int) progress);
    args.pushInt((int) total);

    sendEvent(eventEmitter, requestId, "didReceiveNetworkDataProgress", args);
  }

  public static void onDataReceived(
      @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, String data) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushString(data);

    sendEvent(eventEmitter, requestId, "didReceiveNetworkData", args);
  }

  public static void onDataReceived(
      @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, WritableMap data) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushMap(data);

    sendEvent(eventEmitter, requestId, "didReceiveNetworkData", args);
  }

  public static void onRequestError(
      @Nullable RCTDeviceEventEmitter eventEmitter, int requestId, String error, Throwable e) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushString(error);

    if ((e != null) && (e.getClass() == SocketTimeoutException.class)) {
      args.pushBoolean(true); // last argument is a time out boolean
    }

    sendEvent(eventEmitter, requestId, "didCompleteNetworkResponse", args);
  }

  public static void onRequestSuccess(@Nullable RCTDeviceEventEmitter eventEmitter, int requestId) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushNull();

    sendEvent(eventEmitter, requestId, "didCompleteNetworkResponse", args);
  }

  public static void onResponseReceived(
      @Nullable RCTDeviceEventEmitter eventEmitter,
      int requestId,
      int statusCode,
      WritableMap headers,
      String url) {
    WritableArray args = Arguments.createArray();
    args.pushInt(requestId);
    args.pushInt(statusCode);
    args.pushMap(headers);
    args.pushString(url);

    sendEvent(eventEmitter, requestId, "didReceiveNetworkResponse", args);
  }
}
