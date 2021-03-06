/*******************************************************************************
 * Copyright (C) 2015 Kristian Sloth Lauszus. All rights reserved.
 *
 * This software may be distributed and modified under the terms of the GNU
 * General Public License version 2 (GPL2) as published by the Free Software
 * Foundation and appearing in the file GPL2.TXT included in the packaging of
 * this file. Please note that GPL2 Section 2[b] requires that all works based
 * on this software must also be made publicly available under the terms of
 * the GPL2 ("Copyleft").
 *
 * Contact information
 * -------------------
 *
 * Kristian Sloth Lauszus
 * Web      :  http://www.lauszus.com
 * e-mail   :  lauszus@gmail.com
 ******************************************************************************/

package com.lauszus.launchpadflightcontrollerandroid.app;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BluetoothProtocol {
    // Debugging
    private static final String TAG = BluetoothProtocol.class.getSimpleName();
    private static final boolean D = LaunchPadFlightControllerActivity.D;

    static final byte SET_PID_ROLL_PITCH = 0;
    static final byte GET_PID_ROLL_PITCH = 1;
    static final byte SET_PID_YAW = 2;
    static final byte GET_PID_YAW = 3;
    static final byte SET_PID_SONAR_ALT_HOLD = 4;
    static final byte GET_PID_SONAR_ALT_HOLD = 5;
    static final byte SET_PID_BARO_ALT_HOLD = 6;
    static final byte GET_PID_BARO_ALT_HOLD = 7;
    static final byte SET_SETTINGS = 8;
    static final byte GET_SETTINGS = 9;
    static final byte SEND_ANGLES = 10;
    static final byte CAL_ACC = 11;
    static final byte CAL_MAG = 12;
    static final byte RESTORE_DEFAULTS = 13;

    static final String commandHeader = "$S>"; // Standard command header
    static final String responseHeader = "$S<"; // Standard response header
    static final String responseEnd = "\r\n";


    BluetoothChatService mChatService;
    private final Handler mHandler;

    public BluetoothProtocol(BluetoothChatService mChatService, Handler handler) {
        this.mChatService = mChatService;
        this.mHandler = handler;
    }

    private void sendCommand(byte output[]) {
        mChatService.write(commandHeader);
        mChatService.write(output);
        mChatService.write(getChecksum(output));
    }

    private void setPID(byte cmd, int Kp, int Ki, int Kd, int IntLimit) {
        byte output[] = {
                cmd, // Cmd
                8, // Length
                (byte) (Kp & 0xFF),
                (byte) (Kp >> 8),
                (byte) (Ki & 0xFF),
                (byte) (Ki >> 8),
                (byte) (Kd & 0xFF),
                (byte) (Kd >> 8),
                (byte) (IntLimit & 0xFF),
                (byte) (IntLimit >> 8),
        };
        sendCommand(output); // Set PID values
    }

    private void getPID(byte cmd) {
        byte output[] = {
                cmd, // Cmd
                0, // Length
        };
        sendCommand(output); // Send output
    }

    /**
     * Set PID values for roll and pitch.
     *
     * @param Kp Kp value.
     * @param Ki Ki value.
     * @param Kd Kd value.
     */
    public void setPIDRollPitch(int Kp, int Ki, int Kd, int IntLimit) {
        if (D)
            Log.i(TAG, "setPIDRollPitch: " + Kp + " " + Ki + " " + Kd + " " + IntLimit);

        setPID(SET_PID_ROLL_PITCH, Kp, Ki, Kd, IntLimit);
    }

    /**
     * Use this to request PID values for roll and pitch.
     */
    public void getPIDRollPitch() {
        if (D)
            Log.i(TAG, "getPIDRollPitch");

        getPID(GET_PID_ROLL_PITCH);
    }

    /**
     * Set PID values for yaw.
     *
     * @param Kp Kp value.
     * @param Ki Ki value.
     * @param Kd Kd value.
     */
    public void setPIDYaw(int Kp, int Ki, int Kd, int IntLimit) {
        if (D)
            Log.i(TAG, "setPIDYaw: " + Kp + " " + Ki + " " + Kd + " " + IntLimit);

        setPID(SET_PID_YAW, Kp, Ki, Kd, IntLimit);
    }

    /**
     * Use this to request PID values for yaw.
     */
    public void getPIDYaw() {
        if (D)
            Log.i(TAG, "getPIDYaw");

        getPID(GET_PID_YAW);
    }

    /**
     * Set PID values for sonar altitude hold.
     *
     * @param Kp Kp value.
     * @param Ki Ki value.
     * @param Kd Kd value.
     */
    public void setPIDSonarAltHold(int Kp, int Ki, int Kd, int IntLimit) {
        if (D)
            Log.i(TAG, "setPIDSonarAltHold: " + Kp + " " + Ki + " " + Kd + " " + IntLimit);

        setPID(SET_PID_SONAR_ALT_HOLD, Kp, Ki, Kd, IntLimit);
    }

    /**
     * Use this to request PID values for sonar altitude hold.
     */
    public void getPIDSonarAltHold() {
        if (D)
            Log.i(TAG, "getPIDSonarAltHold");

        getPID(GET_PID_SONAR_ALT_HOLD);
    }

    /**
     * Set PID values for baro altitude hold.
     *
     * @param Kp Kp value.
     * @param Ki Ki value.
     * @param Kd Kd value.
     */
    public void setPIDBaroAltHold(int Kp, int Ki, int Kd, int IntLimit) {
        if (D)
            Log.i(TAG, "setPIDBaroAltHold: " + Kp + " " + Ki + " " + Kd + " " + IntLimit);

        setPID(SET_PID_BARO_ALT_HOLD, Kp, Ki, Kd, IntLimit);
    }

    /**
     * Use this to request PID values for baro altitude hold.
     */
    public void getPIDBaroAltHold() {
        if (D)
            Log.i(TAG, "getPIDBaroAltHold");

        getPID(GET_PID_BARO_ALT_HOLD);
    }

    public void setSettings(int AngleKp, int HeadingKp, byte AngleMaxInc, byte AngleMaxIncSonar, int StickScalingRollPitch, int StickScalingYaw) {
        if (D)
            Log.i(TAG, "setSettings: " + AngleKp + " " + HeadingKp + " " + AngleMaxInc + " " + AngleMaxIncSonar + " " + StickScalingRollPitch + " " + StickScalingYaw);

        byte output[] = {
                SET_SETTINGS, // Cmd
                10, // Length
                (byte) (AngleKp & 0xFF),
                (byte) (AngleKp >> 8),
                (byte) (HeadingKp & 0xFF),
                (byte) (HeadingKp >> 8),
                AngleMaxInc,
                AngleMaxIncSonar,
                (byte) (StickScalingRollPitch & 0xFF),
                (byte) (StickScalingRollPitch >> 8),
                (byte) (StickScalingYaw & 0xFF),
                (byte) (StickScalingYaw >> 8),
        };
        sendCommand(output); // Send output
    }

    public void getSettings() {
        if (D)
            Log.i(TAG, "getSettings");

        byte output[] = {
                GET_SETTINGS, // Cmd
                0, // Length
        };
        sendCommand(output); // Send output
    }

    public void sendAngles(byte enable) {
        if (D)
            Log.i(TAG, "sendAngles: " + enable);

        byte output[] = {
                SEND_ANGLES, // Cmd
                1, // Length
                enable,
        };
        sendCommand(output); // Send output
    }

    public void calibrateAccelerometer() {
        if (D)
            Log.i(TAG, "calibrateAccelerometer");

        byte output[] = {
                CAL_ACC, // Cmd
                0, // Length
        };
        sendCommand(output); // Send output
    }

    public void calibrateMagnetometer() {
        if (D)
            Log.i(TAG, "calibrateMagnetometer");

        byte output[] = {
                CAL_MAG, // Cmd
                0, // Length
        };
        sendCommand(output); // Send output
    }

    public void restoreDefaults() {
        if (D)
            Log.i(TAG, "restoreDefaults");

        byte output[] = {
                RESTORE_DEFAULTS, // Cmd
                0, // Length
        };
        sendCommand(output); // Send output
    }

    private byte[] buffer = new byte[1024];

    public void parseData(byte msg[], int offset, int length) {
        System.arraycopy(msg, offset, buffer, 0, length);

        String readMessage = new String(buffer, 0, length);

        if (D)
            Log.d(TAG, "Received string: " + readMessage);

        int[] data = new int[length];
        for (int i = 0; i < length; i++) {
            data[i] = buffer[i] & 0xFF; // Cast to unsigned value
            if (D)
                Log.d(TAG, "Data[" + i + "]: " + data[i]);
        }

        if (length < responseHeader.length() + 2) { // We should have at least received the header, cmd and length
            if (D)
                Log.e(TAG, "String is too short!");
            return;
        }

        // TODO: Remove whitespace in front
        if (new String(buffer).startsWith(responseHeader)) {
            int cmd = data[responseHeader.length()];
            int msgLength = data[responseHeader.length() + 1];
            if (msgLength > (length - responseHeader.length() - 3)) { // Check if there is enough data - there needs to be the header, cmd, length, data and checksum
                if (D)
                    Log.e(TAG, "Not enough data!");
                return;
            }

            int input[] = new int[msgLength];
            int i;
            for (i = 0; i < msgLength; i++)
                input[i] = data[i + responseHeader.length() + 2];
            int checksum = data[i + responseHeader.length() + 2];

            int msgChecksum = cmd ^ msgLength ^ getChecksum(input);

            if (checksum == msgChecksum) {
                Message message = mHandler.obtainMessage(LaunchPadFlightControllerActivity.MESSAGE_READ); // Send message back to the UI Activity
                Bundle bundle = new Bundle();

                switch (cmd) {
                    case GET_PID_ROLL_PITCH:
                        int KpRollPitch = input[0] | (input[1] << 8);
                        int KiRollPitch = input[2] | (input[3] << 8);
                        int KdRollPitch = input[4] | (input[5] << 8);
                        int IntLimitRollPitch = input[6] | (input[7] << 8);

                        bundle.putInt(LaunchPadFlightControllerActivity.KP_ROLL_PITCH_VALUE, KpRollPitch);
                        bundle.putInt(LaunchPadFlightControllerActivity.KI_ROLL_PITCH_VALUE, KiRollPitch);
                        bundle.putInt(LaunchPadFlightControllerActivity.KD_ROLL_PITCH_VALUE, KdRollPitch);
                        bundle.putInt(LaunchPadFlightControllerActivity.INT_LIMIT_ROLL_PITCH_VALUE, IntLimitRollPitch);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.i(TAG, "Received PID roll & pitch: " + KpRollPitch + " " + KiRollPitch + " " + KdRollPitch + " " + IntLimitRollPitch);
                        break;

                    case GET_PID_YAW:
                        int KpYaw = input[0] | (input[1] << 8);
                        int KiYAw = input[2] | (input[3] << 8);
                        int KdYAw = input[4] | (input[5] << 8);
                        int IntLimitYaw = input[6] | (input[7] << 8);

                        bundle.putInt(LaunchPadFlightControllerActivity.KP_YAW_VALUE, KpYaw);
                        bundle.putInt(LaunchPadFlightControllerActivity.KI_YAW_VALUE, KiYAw);
                        bundle.putInt(LaunchPadFlightControllerActivity.KD_YAW_VALUE, KdYAw);
                        bundle.putInt(LaunchPadFlightControllerActivity.INT_LIMIT_YAW_VALUE, IntLimitYaw);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.i(TAG, "Received PID yaw: " + KpYaw + " " + KiYAw + " " + KdYAw + " " + IntLimitYaw);
                        break;

                    case GET_PID_SONAR_ALT_HOLD:
                        int KpSonarAltHold = input[0] | (input[1] << 8);
                        int KiSonarAltHold = input[2] | (input[3] << 8);
                        int KdSonarAltHold = input[4] | (input[5] << 8);
                        int IntLimitSonarAltHold = input[6] | (input[7] << 8);

                        bundle.putInt(LaunchPadFlightControllerActivity.KP_SONAR_ALT_HOLD_VALUE, KpSonarAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.KI_SONAR_ALT_HOLD_VALUE, KiSonarAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.KD_SONAR_ALT_HOLD_VALUE, KdSonarAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.INT_LIMIT_SONAR_ALT_HOLD_VALUE, IntLimitSonarAltHold);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.i(TAG, "Received PID sonar altitude hold: " + KpSonarAltHold + " " + KiSonarAltHold + " " + KdSonarAltHold + " " + IntLimitSonarAltHold);
                        break;

                    case GET_PID_BARO_ALT_HOLD:
                        int KpBaroAltHold = input[0] | (input[1] << 8);
                        int KiBaroAltHold = input[2] | (input[3] << 8);
                        int KdBaroAltHold = input[4] | (input[5] << 8);
                        int IntLimitBaroAltHold = input[6] | (input[7] << 8);

                        bundle.putInt(LaunchPadFlightControllerActivity.KP_BARO_ALT_HOLD_VALUE, KpBaroAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.KI_BARO_ALT_HOLD_VALUE, KiBaroAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.KD_BARO_ALT_HOLD_VALUE, KdBaroAltHold);
                        bundle.putInt(LaunchPadFlightControllerActivity.INT_LIMIT_BARO_ALT_HOLD_VALUE, IntLimitBaroAltHold);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.i(TAG, "Received PID baro altitude hold: " + KpBaroAltHold + " " + KiBaroAltHold + " " + KdBaroAltHold + " " + IntLimitBaroAltHold);
                        break;

                    case GET_SETTINGS:
                        int AngleKp = input[0] | (input[1] << 8);
                        int HeadingKp = input[2] | (input[3] << 8);
                        byte AngleMaxInc = (byte) input[4];
                        byte AngleMaxIncSonar = (byte) input[5];
                        int StickScalingRollPitch = input[6] | (input[7] << 8);
                        int StickScalingYaw = input[8] | (input[9] << 8);

                        bundle.putInt(LaunchPadFlightControllerActivity.ANGLE_KP_VALUE, AngleKp);
                        bundle.putInt(LaunchPadFlightControllerActivity.HEADING_KP_VALUE, HeadingKp);
                        bundle.putByte(LaunchPadFlightControllerActivity.ANGLE_MAX_INC_VALUE, AngleMaxInc);
                        bundle.putByte(LaunchPadFlightControllerActivity.ANGLE_MAX_INC_SONAR_VALUE, AngleMaxIncSonar);
                        bundle.putInt(LaunchPadFlightControllerActivity.STICK_SCALING_ROLL_PITCH_VALUE, StickScalingRollPitch);
                        bundle.putInt(LaunchPadFlightControllerActivity.STICK_SCALING_YAW_VALUE, StickScalingYaw);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.i(TAG, "Received settings: " + AngleKp + " " + HeadingKp + " " + AngleMaxInc + " " + AngleMaxIncSonar + " " + StickScalingRollPitch + " " + StickScalingYaw);
                        break;

                    case SEND_ANGLES:
                        int roll = input[0] | ((byte) input[1] << 8); // This can be negative as well
                        int pitch = input[2] | ((byte) input[3] << 8); // This can be negative as well
                        int yaw = input[4] | (input[5] << 8); // Heading is always positive

                        bundle.putFloat(LaunchPadFlightControllerActivity.ROLL_ANGLE, (float) roll / 100.0f);
                        bundle.putFloat(LaunchPadFlightControllerActivity.PITCH_ANGLE, (float) pitch / 100.0f);
                        bundle.putFloat(LaunchPadFlightControllerActivity.YAW_ANGLE, (float) yaw / 100.0f);

                        message.setData(bundle);
                        mHandler.sendMessage(message);

                        if (D)
                            Log.v(TAG, "Acc: " + roll + " Gyro: " + pitch + " Kalman: " + yaw);
                        break;

                    default:
                        if (D)
                            Log.e(TAG, "Unknown command: " + cmd);
                        break;
                }
            } else {
                if (D)
                    Log.e(TAG, "Checksum error! Got: " + checksum + " Expected: " + msgChecksum);
            }
        } else {
            if (D)
                Log.e(TAG, "Wrong header! " + readMessage);
        }
    }

    private byte getChecksum(byte data[]) {
        byte checksum = 0;
        for (byte val : data)
            checksum ^= val;
        return checksum;
    }

    private int getChecksum(int data[]) {
        int checksum = 0;
        for (int val : data)
            checksum ^= val;
        return checksum;
    }
}
