package com.poct.android.utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;

import com.poct.android.manager.BluetoothSetManager;
import com.poct.android.net.NetUtils;


public class MyBluetoothGattCallback extends BluetoothGattCallback {

    public Handler handler;

    public MyBluetoothGattCallback(Handler handler) {
        super();
        this.handler = handler;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                        int newState) {

        String intentAction;
        // GATT Server connected
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            System.out.println("---------------------------->已经连接");
//                intentAction = ACTION_GATT_CONNECTED;
//                mConnectionState = STATE_CONNECTED;
//                broadcastConnectionUpdate(intentAction);
            BluetoothSetManager.getInstance().mBluetoothGatt.discoverServices();

        }
        // GATT Server disconnected
        else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            System.out.println("---------------------------->连接断开");
//                intentAction = ACTION_GATT_DISCONNECTED;
//                mConnectionState = STATE_DISCONNECTED;
//                broadcastConnectionUpdate(intentAction);

        }
        // GATT Server disconnected
        else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
            System.out.println("---------------------------->正在连接");
//                intentAction = ACTION_GATT_DISCONNECTING;
//                mConnectionState = STATE_DISCONNECTING;
//                broadcastConnectionUpdate(intentAction);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        // GATT Services discovered
        //发现新的服务
        if (status == BluetoothGatt.GATT_SUCCESS) {
            System.out.println("---------------------------->发现服务");
//                broadcastConnectionUpdate(ACTION_GATT_SERVICES_DISCOVERED);
        } else {

        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                  int status) {


        if (status == BluetoothGatt.GATT_SUCCESS) {
            System.out.println("onDescriptorWrite GATT_SUCCESS------------------->SUCCESS");
        } else if (status == BluetoothGatt.GATT_FAILURE){
            System.out.println("onDescriptorWrite GATT_FAIL------------------->FAIL");
//                Intent intent = new Intent(ACTION_GATT_DESCRIPTORWRITE_RESULT);
//                intent.putExtra(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT, status);
//                mContext.sendBroadcast(intent);
        }

    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                 int status) {


        System.out.println("onDescriptorRead ------------------->GATT_SUCC");

        if (status == BluetoothGatt.GATT_SUCCESS) {
//                UUID descriptorUUID = descriptor.getUuid();
//                final Intent intent = new Intent(ACTION_DATA_AVAILABLE);
//                Bundle mBundle = new Bundle();
//                // Putting the byte value read for GATT Db
//                mBundle.putByteArray(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE,
//                        descriptor.getValue());
//
//
//                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_UUID,
//                        descriptor.getUuid().toString());
//                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID,
//                        descriptor.getCharacteristic().getUuid().toString());
//                if (descriptorUUID.equals(UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG)) {
//                    String valueReceived = DescriptorParser
//                            .getClientCharacteristicConfiguration(descriptor);
//                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, valueReceived);
//                }
//                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_EXTENDED_PROPERTIES)) {
//                    HashMap<String, String> receivedValuesMap = DescriptorParser
//                            .getCharacteristicExtendedProperties(descriptor);
//                    String reliableWriteStatus = receivedValuesMap.get(Constants.firstBitValueKey);
//                    String writeAuxillaryStatus = receivedValuesMap.get(Constants.secondBitValueKey);
//                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, reliableWriteStatus + "\n"
//                            + writeAuxillaryStatus);
//                }
//                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_USER_DESCRIPTION)) {
//                    String description = DescriptorParser
//                            .getCharacteristicUserDescription(descriptor);
//                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, description);
//                }
//                if (descriptorUUID.equals(UUIDDatabase.UUID_SERVER_CHARACTERISTIC_CONFIGURATION)) {
//                    String broadcastStatus = DescriptorParser.
//                            getServerCharacteristicConfiguration(descriptor);
//                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, broadcastStatus);
//                }
//                if (descriptorUUID.equals(UUIDDatabase.UUID_REPORT_REFERENCE)) {
//                    ArrayList<String> reportReferencealues = DescriptorParser.getReportReference(descriptor);
//                    String reportReference;
//                    String reportReferenceType;
//                    if (reportReferencealues.size() == 2) {
//                        reportReference = reportReferencealues.get(0);
//                        reportReferenceType = reportReferencealues.get(1);
//                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_ID, reportReference);
//                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_TYPE, reportReferenceType);
//                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, reportReference + "\n" +
//                                reportReferenceType);
//                    }
//
//                }
//                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_PRESENTATION_FORMAT)) {
//                    String value = DescriptorParser.getCharacteristicPresentationFormat(descriptor);
//                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
//                            value);
//                }
//                intent.putExtras(mBundle);
//                /**
//                 * Sending the broad cast so that it can be received on
//                 * registered receivers
//                 */
//                mContext.sendBroadcast(intent);
        } else {
            System.out.println("onDescriptorRead ------------------->GATT_FAIL");
        }

    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic
            characteristic, int status) {
        //write操作会调用此方法
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                System.out.println("onCharacteristicWrite ------------------->write success");
//                Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_WRITE_SUCCESS);
//                mContext.sendBroadcast(intent);
//            } else {
//                Intent intent = new Intent(ACTION_GATT_CHARACTERISTIC_ERROR);
//                intent.putExtra(Constants.EXTRA_CHARACTERISTIC_ERROR_MESSAGE, "" + status);
//                mContext.sendBroadcast(intent);
//            }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic, int status) {

        System.out.println("onCharacteristicWrite ------------------->read");
        // GATT Characteristic read (读操作会调用该方法)
//            if (status == BluetoothGatt.GATT_SUCCESS) {
//                UUID charUuid = characteristic.getUuid();
//                final Intent intent = new Intent(ACTION_DATA_AVAILABLE);
//                Bundle mBundle = new Bundle();
//                // Putting the byte value read for GATT Db
//                mBundle.putByteArray(Constants.EXTRA_BYTE_VALUE,
//                        characteristic.getValue());
//                mBundle.putString(Constants.EXTRA_BYTE_UUID_VALUE,
//                        characteristic.getUuid().toString());
//
//                System.out.println("onCharacteristicRead------------------->GATT_SUCC");
//
//                // Body sensor location read value
//                if (charUuid.equals(UUIDDatabase.UUID_BODY_SENSOR_LOCATION)) {
//                    mBundle.putString(Constants.EXTRA_BSL_VALUE,
//                            HRMParser.getBodySensorLocation(characteristic));
//                }
//                // Manufacture name read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_MANUFATURE_NAME_STRING)) {
//                    mBundle.putString(Constants.EXTRA_MNS_VALUE,
//                            Utils.getManufacturerNameString(characteristic));
//                }
//                // Model number read value
//                else if (charUuid.equals(UUIDDatabase.UUID_MODEL_NUMBER_STRING)) {
//                    mBundle.putString(Constants.EXTRA_MONS_VALUE,
//                            Utils.getModelNumberString(characteristic));
//                }
//                // Serial number read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_SERIAL_NUMBER_STRING)) {
//                    mBundle.putString(Constants.EXTRA_SNS_VALUE,
//                            Utils.getSerialNumberString(characteristic));
//                }
//                // Hardware revision read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_HARDWARE_REVISION_STRING)) {
//                    mBundle.putString(Constants.EXTRA_HRS_VALUE,
//                            Utils.getHardwareRevisionString(characteristic));
//                }
//                // Firmware revision read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_FIRWARE_REVISION_STRING)) {
//                    mBundle.putString(Constants.EXTRA_FRS_VALUE,
//                            Utils.getFirmwareRevisionString(characteristic));
//                }
//                // Software revision read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_SOFTWARE_REVISION_STRING)) {
//                    mBundle.putString(Constants.EXTRA_SRS_VALUE,
//                            Utils.getSoftwareRevisionString(characteristic));
//                }
//                // Battery level read value
//                else if (charUuid.equals(UUIDDatabase.UUID_BATTERY_LEVEL)) {
//                    mBundle.putString(Constants.EXTRA_BTL_VALUE,
//                            Utils.getBatteryLevel(characteristic));
//                }
//                // PNP ID read value
//                else if (charUuid.equals(UUIDDatabase.UUID_PNP_ID)) {
//                    mBundle.putString(Constants.EXTRA_PNP_VALUE,
//                            Utils.getPNPID(characteristic));
//                }
//                // System ID read value
//                else if (charUuid.equals(UUIDDatabase.UUID_SYSTEM_ID)) {
//                    mBundle.putString(Constants.EXTRA_SID_VALUE,
//                            Utils.getSYSID(characteristic));
//                }
//                // Regulatory data read value
//                else if (charUuid.equals(UUIDDatabase.UUID_IEEE)) {
//                    mBundle.putString(Constants.EXTRA_RCDL_VALUE,
//                            Utils.ByteArraytoHex(characteristic.getValue()));
//                }
//                // Health thermometer sensor location read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_HEALTH_THERMOMETER_SENSOR_LOCATION)) {
//                    mBundle.putString(Constants.EXTRA_HSL_VALUE, HTMParser
//                            .getHealthThermoSensorLocation(characteristic));
//                }
//                // CapSense proximity read value
//                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_PROXIMITY) ||
//                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_PROXIMITY_CUSTOM)) {
//                    mBundle.putInt(Constants.EXTRA_CAPPROX_VALUE,
//                            CapSenseParser.getCapSenseProximity(characteristic));
//                }
//                // CapSense slider read value
//                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_SLIDER) ||
//                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_SLIDER_CUSTOM)) {
//                    mBundle.putInt(Constants.EXTRA_CAPSLIDER_VALUE,
//                            CapSenseParser.getCapSenseSlider(characteristic));
//                }
//                // CapSense buttons read value
//                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_BUTTONS) ||
//                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_BUTTONS_CUSTOM)) {
//                    mBundle.putIntegerArrayList(
//                            Constants.EXTRA_CAPBUTTONS_VALUE,
//                            CapSenseParser.getCapSenseButtons(characteristic));
//                }
//                // Alert level read value
//                else if (charUuid.equals(UUIDDatabase.UUID_ALERT_LEVEL)) {
//                    mBundle.putString(Constants.EXTRA_ALERT_VALUE,
//                            Utils.getAlertLevel(characteristic));
//                }
//                // Transmission power level read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_TRANSMISSION_POWER_LEVEL)) {
//                    mBundle.putInt(Constants.EXTRA_POWER_VALUE,
//                            Utils.getTransmissionPower(characteristic));
//                }
//                // RGB Led read value
//                else if (charUuid.equals(UUIDDatabase.UUID_RGB_LED) ||
//                        charUuid.equals(UUIDDatabase.UUID_RGB_LED_CUSTOM)) {
//                    mBundle.putString(Constants.EXTRA_RGB_VALUE,
//                            RGBParser.getRGBValue(characteristic));
//                }
//                // Glucose read value
//                else if (charUuid.equals(UUIDDatabase.UUID_GLUCOSE)) {
//                    mBundle.putStringArrayList(Constants.EXTRA_GLUCOSE_VALUE,
//                            GlucoseParser.getGlucoseHealth(characteristic));
//                }
//                // Running speed read value
//                else if (charUuid.equals(UUIDDatabase.UUID_RSC_MEASURE)) {
//                    mBundle.putStringArrayList(Constants.EXTRA_RSC_VALUE,
//                            RSCParser.getRunningSpeednCadence(characteristic));
//                }
//                // Accelerometer X read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_X)) {
//                    mBundle.putInt(Constants.EXTRA_ACCX_VALUE, SensorHubParser
//                            .getAcceleroMeterXYZReading(characteristic));
//                }
//                // Accelerometer Y read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_Y)) {
//                    mBundle.putInt(Constants.EXTRA_ACCY_VALUE, SensorHubParser
//                            .getAcceleroMeterXYZReading(characteristic));
//                }
//                // Accelerometer Z read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_Z)) {
//                    mBundle.putInt(Constants.EXTRA_ACCZ_VALUE, SensorHubParser
//                            .getAcceleroMeterXYZReading(characteristic));
//                }
//                // Temperature read value
//                else if (charUuid.equals(UUIDDatabase.UUID_TEMPERATURE_READING)) {
//                    mBundle.putFloat(Constants.EXTRA_STEMP_VALUE,
//                            SensorHubParser
//                                    .getThermometerReading(characteristic));
//                }
//                // Barometer read value
//                else if (charUuid.equals(UUIDDatabase.UUID_BAROMETER_READING)) {
//                    mBundle.putInt(Constants.EXTRA_SPRESSURE_VALUE,
//                            SensorHubParser.getBarometerReading(characteristic));
//                }
//                // Accelerometer scan interval read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_SENSOR_SCAN_INTERVAL)) {
//                    mBundle.putInt(
//                            Constants.EXTRA_ACC_SENSOR_SCAN_VALUE,
//                            SensorHubParser
//                                    .getSensorScanIntervalReading(characteristic));
//                }
//                // Accelerometer analog sensor read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_ANALOG_SENSOR)) {
//                    mBundle.putInt(Constants.EXTRA_ACC_SENSOR_TYPE_VALUE,
//                            SensorHubParser
//                                    .getSensorTypeReading(characteristic));
//                }
//                // Accelerometer data accumulation read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_ACCELEROMETER_DATA_ACCUMULATION)) {
//                    mBundle.putInt(Constants.EXTRA_ACC_FILTER_VALUE,
//                            SensorHubParser
//                                    .getFilterConfiguration(characteristic));
//                }
//                // Temperature sensor scan read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_TEMPERATURE_SENSOR_SCAN_INTERVAL)) {
//                    mBundle.putInt(
//                            Constants.EXTRA_STEMP_SENSOR_SCAN_VALUE,
//                            SensorHubParser
//                                    .getSensorScanIntervalReading(characteristic));
//                }
//                // Temperature analog sensor read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_TEMPERATURE_ANALOG_SENSOR)) {
//                    mBundle.putInt(Constants.EXTRA_STEMP_SENSOR_TYPE_VALUE,
//                            SensorHubParser
//                                    .getSensorTypeReading(characteristic));
//                }
//                // Barometer sensor scan interval read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_BAROMETER_SENSOR_SCAN_INTERVAL)) {
//                    mBundle.putInt(
//                            Constants.EXTRA_SPRESSURE_SENSOR_SCAN_VALUE,
//                            SensorHubParser
//                                    .getSensorScanIntervalReading(characteristic));
//                }
//                // Barometer digital sensor
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_BAROMETER_DIGITAL_SENSOR)) {
//                    mBundle.putInt(Constants.EXTRA_SPRESSURE_SENSOR_TYPE_VALUE,
//                            SensorHubParser
//                                    .getSensorTypeReading(characteristic));
//                }
//                // Barometer threshold for indication read value
//                else if (charUuid
//                        .equals(UUIDDatabase.UUID_BAROMETER_THRESHOLD_FOR_INDICATION)) {
//                    mBundle.putInt(Constants.EXTRA_SPRESSURE_THRESHOLD_VALUE,
//                            SensorHubParser.getThresholdValue(characteristic));
//                }
//                intent.putExtras(mBundle);
//
//                /**
//                 * Sending the broad cast so that it can be received on
//                 * registered receivers
//                 */
//
//                mContext.sendBroadcast(intent);
//
//            } else {
//
//            }
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        /**
         * Android 底层貌似做了限制只能接受20个字节
         * There are four basic operations for moving data in BLE: read, write, notify, and indicate.
         * The BLE protocol specification requires that the maximum data payload size for these
         * operations is 20 bytes, or in the case of read operations, 22 bytes.
         * BLE is built for low power consumption, for infrequent short-burst data transmissions.
         * Sending lots of data is possible, but usually ends up being less efficient than classic
         * Bluetooth when trying to achieve maximum throughput.
         */
//            System.out.println("onCharacteristicChanged -------------------> changed");
//            //notify 会回调用此方法
//            broadcastNotifyUpdate(characteristic);
    }
}
