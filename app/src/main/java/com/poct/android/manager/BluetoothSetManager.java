package com.poct.android.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.poct.android.entity.EquipmentItem;
import com.poct.android.entity.MService;
import com.poct.android.prase.BloodPressureParser;
import com.poct.android.prase.CSCParser;
import com.poct.android.prase.CapSenseParser;
import com.poct.android.prase.DescriptorParser;
import com.poct.android.prase.GlucoseParser;
import com.poct.android.prase.HRMParser;
import com.poct.android.prase.HTMParser;
import com.poct.android.prase.RGBParser;
import com.poct.android.prase.RSCParser;
import com.poct.android.prase.SensorHubParser;
import com.poct.android.receiver.GattUpdateReceiver;
import com.poct.android.thread.BleConnectThread;
import com.poct.android.utils.Constants;
import com.poct.android.utils.GattAttributes;
import com.poct.android.utils.UUIDDatabase;
import com.poct.android.utils.Utils;
import com.poct.android.view.PoctApplication;
import com.poct.android.view.activity.LoginActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BluetoothSetManager {

    public static final String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public static final String ACTION_DEVICE_CONNECTED = "ACTION_DEVICE_CONNECTED";
    public static final String ACTION_DEVICE_DISCONNECT = "ACTION_DEVICE_DISCONNECT";
    public static final String ACTION_DEVICE_CONNECTING = "ACTION_DEVICE_CONNECTING";
    public static final String ACTION_DEVICE_READY= "ACTION_DEVICE_READY";
    public static final String ACTION_DEVICE_FADA_GET= "ACTION_DEVICE_FADA_GET";
    public static final String ACTION_DEVICE_CHECK_FADA= "ACTION_DEVICE_CHECK_FADA";
    public static final String ACTION_DEVICE_POTC_GET= "ACTION_DEVICE_POTC_GET";
    public static final String ACTION_DEVICE_POTC_CONNECTED= "ACTION_DEVICE_POTC_CONNECTED";
    public static final String ACTION_DEVICE_POTC_UNCONNECTED= "ACTION_DEVICE_POTC_UNCONNECTED";
    public static final String ACTION_DEVICE_CHECK_POTC= "ACTION_DEVICE_CHECK_POTC";
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public int mConnectionState = STATE_DISCONNECTED;
    public BluetoothManager mBluetoothManager;
    public static BluetoothSetManager mBluetoothSetManager;
    public BluetoothAdapter blueToothAdapter;
    public boolean enabled = true;
    public BluetoothDevice netBluetoothDevice;
    public BluetoothGatt mBluetoothGatt;
    public MService mPadaService;
    public BluetoothGattCharacteristic mPadacharacteristic;
    public static UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//    public UUID MY_UUID_SECURE;
    public BluetoothSetManager()
    {
        mBluetoothManager = (BluetoothManager) PoctApplication.mApp.getSystemService(Context.BLUETOOTH_SERVICE);
        blueToothAdapter = mBluetoothManager.getAdapter();
    }

    public static synchronized BluetoothSetManager getInstance() {
        if (mBluetoothSetManager == null) {
            mBluetoothSetManager = new BluetoothSetManager();
        }
        return mBluetoothSetManager;
    }

    public void doDiscovery() {
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        PoctApplication.mApp.sendBroadcast(new Intent(LoginActivity.ACTION_END_DISCOVER_DEVICE));
        blueToothAdapter.startDiscovery();
    }

    public void stopDisvery() {
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        blueToothAdapter.cancelDiscovery();
    }


    public final static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                System.out.println("---------------------------->已经连接");
                Intent intent = new Intent(ACTION_DEVICE_CONNECTED);
                BluetoothSetManager.getInstance().mConnectionState = STATE_CONNECTED;
                PoctApplication.mApp.sendBroadcast(intent);
                BluetoothSetManager.getInstance().mBluetoothGatt.discoverServices();

            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                System.out.println("---------------------------->连接断开");
                Intent intent = new Intent(ACTION_DEVICE_DISCONNECT);
                BluetoothSetManager.getInstance().mConnectionState = STATE_DISCONNECTED;
                PoctApplication.mApp.sendBroadcast(intent);
                BluetoothSetManager.getInstance().mBluetoothGatt.close();
                BluetoothSetManager.getInstance().mBluetoothGatt = null;
//                BluetoothSetManager.getInstance().mBluetoothGatt.discoverServices();
            }
            // GATT Server disconnected
            else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                System.out.println("---------------------------->正在连接");
                Intent intent = new Intent(ACTION_DEVICE_CONNECTING);
                BluetoothSetManager.getInstance().mConnectionState = STATE_CONNECTING;
                PoctApplication.mApp.sendBroadcast(intent);
            }
            else if(newState == BluetoothProfile.STATE_DISCONNECTING)
            {
                Intent intent = new Intent(ACTION_DEVICE_DISCONNECT);
                BluetoothSetManager.getInstance().mConnectionState = STATE_DISCONNECTED;
                PoctApplication.mApp.sendBroadcast(intent);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            // GATT Services discovered
            //发现新的服务
            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("---------------------------->发现服务");
                Intent intent = new Intent(ACTION_GATT_SERVICES_DISCOVERED);
                PoctApplication.mApp.sendBroadcast(intent);
            } else {

            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {


            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("onDescriptorWrite GATT_SUCCESS------------------->SUCCESS");
            } else if (status == BluetoothGatt.GATT_FAILURE) {
                System.out.println("onDescriptorWrite GATT_FAIL------------------->FAIL");
//                Intent intent = new Intent(ACTION_GATT_DESCRIPTORWRITE_RESULT);
//                intent.putExtra(Constants.EXTRA_DESCRIPTOR_WRITE_RESULT, status);
//                mContext.sendBroadcast(intent);
            }

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {


            System.out.println("onDescriptorRead ------------------->GATT_SUCC");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                UUID descriptorUUID = descriptor.getUuid();
                final Intent intent = new Intent("");
                Bundle mBundle = new Bundle();
                // Putting the byte value read for GATT Db
                mBundle.putByteArray(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE,
                        descriptor.getValue());


                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_UUID,
                        descriptor.getUuid().toString());
                mBundle.putString(Constants.EXTRA_DESCRIPTOR_BYTE_VALUE_CHARACTERISTIC_UUID,
                        descriptor.getCharacteristic().getUuid().toString());
                if (descriptorUUID.equals(UUIDDatabase.UUID_CLIENT_CHARACTERISTIC_CONFIG)) {
                    String valueReceived = DescriptorParser
                            .getClientCharacteristicConfiguration(descriptor);
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, valueReceived);
                }
                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_EXTENDED_PROPERTIES)) {
                    HashMap<String, String> receivedValuesMap = DescriptorParser
                            .getCharacteristicExtendedProperties(descriptor);
                    String reliableWriteStatus = receivedValuesMap.get(Constants.firstBitValueKey);
                    String writeAuxillaryStatus = receivedValuesMap.get(Constants.secondBitValueKey);
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, reliableWriteStatus + "\n"
                            + writeAuxillaryStatus);
                }
                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_USER_DESCRIPTION)) {
                    String description = DescriptorParser
                            .getCharacteristicUserDescription(descriptor);
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, description);
                }
                if (descriptorUUID.equals(UUIDDatabase.UUID_SERVER_CHARACTERISTIC_CONFIGURATION)) {
                    String broadcastStatus = DescriptorParser.
                            getServerCharacteristicConfiguration(descriptor);
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, broadcastStatus);
                }
                if (descriptorUUID.equals(UUIDDatabase.UUID_REPORT_REFERENCE)) {
                    ArrayList<String> reportReferencealues = DescriptorParser.getReportReference(descriptor);
                    String reportReference;
                    String reportReferenceType;
                    if (reportReferencealues.size() == 2) {
                        reportReference = reportReferencealues.get(0);
                        reportReferenceType = reportReferencealues.get(1);
                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_ID, reportReference);
                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_TYPE, reportReferenceType);
                        mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE, reportReference + "\n" +
                                reportReferenceType);
                    }

                }
                if (descriptorUUID.equals(UUIDDatabase.UUID_CHARACTERISTIC_PRESENTATION_FORMAT)) {
                    String value = DescriptorParser.getCharacteristicPresentationFormat(descriptor);
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_VALUE,
                            value);
                }
                intent.putExtras(mBundle);
                /**
                 * Sending the broad cast so that it can be received on
                 * registered receivers
                 */
                PoctApplication.mApp.sendBroadcast(intent);
            } else {
                System.out.println("onDescriptorRead ------------------->GATT_FAIL");
            }

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
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
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            System.out.println("onCharacteristicWrite ------------------->read");
//             GATT Characteristic read (读操作会调用该方法)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                UUID charUuid = characteristic.getUuid();
                final Intent intent = new Intent("");
                Bundle mBundle = new Bundle();
                // Putting the byte value read for GATT Db
                mBundle.putByteArray(Constants.EXTRA_BYTE_VALUE,
                        characteristic.getValue());
                mBundle.putString(Constants.EXTRA_BYTE_UUID_VALUE,
                        characteristic.getUuid().toString());

                System.out.println("onCharacteristicRead------------------->GATT_SUCC");

                // Body sensor location read value
                if (charUuid.equals(UUIDDatabase.UUID_BODY_SENSOR_LOCATION)) {
                    mBundle.putString(Constants.EXTRA_BSL_VALUE,
                            HRMParser.getBodySensorLocation(characteristic));
                }
                // Manufacture name read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_MANUFATURE_NAME_STRING)) {
                    mBundle.putString(Constants.EXTRA_MNS_VALUE,
                            Utils.getManufacturerNameString(characteristic));
                }
                // Model number read value
                else if (charUuid.equals(UUIDDatabase.UUID_MODEL_NUMBER_STRING)) {
                    mBundle.putString(Constants.EXTRA_MONS_VALUE,
                            Utils.getModelNumberString(characteristic));
                }
                // Serial number read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_SERIAL_NUMBER_STRING)) {
                    mBundle.putString(Constants.EXTRA_SNS_VALUE,
                            Utils.getSerialNumberString(characteristic));
                }
                // Hardware revision read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_HARDWARE_REVISION_STRING)) {
                    mBundle.putString(Constants.EXTRA_HRS_VALUE,
                            Utils.getHardwareRevisionString(characteristic));
                }
                // Firmware revision read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_FIRWARE_REVISION_STRING)) {
                    mBundle.putString(Constants.EXTRA_FRS_VALUE,
                            Utils.getFirmwareRevisionString(characteristic));
                }
                // Software revision read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_SOFTWARE_REVISION_STRING)) {
                    mBundle.putString(Constants.EXTRA_SRS_VALUE,
                            Utils.getSoftwareRevisionString(characteristic));
                }
                // Battery level read value
                else if (charUuid.equals(UUIDDatabase.UUID_BATTERY_LEVEL)) {
                    mBundle.putString(Constants.EXTRA_BTL_VALUE,
                            Utils.getBatteryLevel(characteristic));
                }
                // PNP ID read value
                else if (charUuid.equals(UUIDDatabase.UUID_PNP_ID)) {
                    mBundle.putString(Constants.EXTRA_PNP_VALUE,
                            Utils.getPNPID(characteristic));
                }
                // System ID read value
                else if (charUuid.equals(UUIDDatabase.UUID_SYSTEM_ID)) {
                    mBundle.putString(Constants.EXTRA_SID_VALUE,
                            Utils.getSYSID(characteristic));
                }
                // Regulatory data read value
                else if (charUuid.equals(UUIDDatabase.UUID_IEEE)) {
                    mBundle.putString(Constants.EXTRA_RCDL_VALUE,
                            Utils.ByteArraytoHex(characteristic.getValue()));
                }
                // Health thermometer sensor location read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_HEALTH_THERMOMETER_SENSOR_LOCATION)) {
                    mBundle.putString(Constants.EXTRA_HSL_VALUE, HTMParser
                            .getHealthThermoSensorLocation(characteristic));
                }
                // CapSense proximity read value
                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_PROXIMITY) ||
                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_PROXIMITY_CUSTOM)) {
                    mBundle.putInt(Constants.EXTRA_CAPPROX_VALUE,
                            CapSenseParser.getCapSenseProximity(characteristic));
                }
                // CapSense slider read value
                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_SLIDER) ||
                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_SLIDER_CUSTOM)) {
                    mBundle.putInt(Constants.EXTRA_CAPSLIDER_VALUE,
                            CapSenseParser.getCapSenseSlider(characteristic));
                }
                // CapSense buttons read value
                else if (charUuid.equals(UUIDDatabase.UUID_CAPSENSE_BUTTONS) ||
                        charUuid.equals(UUIDDatabase.UUID_CAPSENSE_BUTTONS_CUSTOM)) {
                    mBundle.putIntegerArrayList(
                            Constants.EXTRA_CAPBUTTONS_VALUE,
                            CapSenseParser.getCapSenseButtons(characteristic));
                }
                // Alert level read value
                else if (charUuid.equals(UUIDDatabase.UUID_ALERT_LEVEL)) {
                    mBundle.putString(Constants.EXTRA_ALERT_VALUE,
                            Utils.getAlertLevel(characteristic));
                }
                // Transmission power level read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_TRANSMISSION_POWER_LEVEL)) {
                    mBundle.putInt(Constants.EXTRA_POWER_VALUE,
                            Utils.getTransmissionPower(characteristic));
                }
                // RGB Led read value
                else if (charUuid.equals(UUIDDatabase.UUID_RGB_LED) ||
                        charUuid.equals(UUIDDatabase.UUID_RGB_LED_CUSTOM)) {
                    mBundle.putString(Constants.EXTRA_RGB_VALUE,
                            RGBParser.getRGBValue(characteristic));
                }
                // Glucose read value
                else if (charUuid.equals(UUIDDatabase.UUID_GLUCOSE)) {
                    mBundle.putStringArrayList(Constants.EXTRA_GLUCOSE_VALUE,
                            GlucoseParser.getGlucoseHealth(characteristic));
                }
                // Running speed read value
                else if (charUuid.equals(UUIDDatabase.UUID_RSC_MEASURE)) {
                    mBundle.putStringArrayList(Constants.EXTRA_RSC_VALUE,
                            RSCParser.getRunningSpeednCadence(characteristic));
                }
                // Accelerometer X read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_X)) {
                    mBundle.putInt(Constants.EXTRA_ACCX_VALUE, SensorHubParser
                            .getAcceleroMeterXYZReading(characteristic));
                }
                // Accelerometer Y read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_Y)) {
                    mBundle.putInt(Constants.EXTRA_ACCY_VALUE, SensorHubParser
                            .getAcceleroMeterXYZReading(characteristic));
                }
                // Accelerometer Z read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_READING_Z)) {
                    mBundle.putInt(Constants.EXTRA_ACCZ_VALUE, SensorHubParser
                            .getAcceleroMeterXYZReading(characteristic));
                }
                // Temperature read value
                else if (charUuid.equals(UUIDDatabase.UUID_TEMPERATURE_READING)) {
                    mBundle.putFloat(Constants.EXTRA_STEMP_VALUE,
                            SensorHubParser
                                    .getThermometerReading(characteristic));
                }
                // Barometer read value
                else if (charUuid.equals(UUIDDatabase.UUID_BAROMETER_READING)) {
                    mBundle.putInt(Constants.EXTRA_SPRESSURE_VALUE,
                            SensorHubParser.getBarometerReading(characteristic));
                }
                // Accelerometer scan interval read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_SENSOR_SCAN_INTERVAL)) {
                    mBundle.putInt(
                            Constants.EXTRA_ACC_SENSOR_SCAN_VALUE,
                            SensorHubParser
                                    .getSensorScanIntervalReading(characteristic));
                }
                // Accelerometer analog sensor read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_ANALOG_SENSOR)) {
                    mBundle.putInt(Constants.EXTRA_ACC_SENSOR_TYPE_VALUE,
                            SensorHubParser
                                    .getSensorTypeReading(characteristic));
                }
                // Accelerometer data accumulation read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_ACCELEROMETER_DATA_ACCUMULATION)) {
                    mBundle.putInt(Constants.EXTRA_ACC_FILTER_VALUE,
                            SensorHubParser
                                    .getFilterConfiguration(characteristic));
                }
                // Temperature sensor scan read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_TEMPERATURE_SENSOR_SCAN_INTERVAL)) {
                    mBundle.putInt(
                            Constants.EXTRA_STEMP_SENSOR_SCAN_VALUE,
                            SensorHubParser
                                    .getSensorScanIntervalReading(characteristic));
                }
                // Temperature analog sensor read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_TEMPERATURE_ANALOG_SENSOR)) {
                    mBundle.putInt(Constants.EXTRA_STEMP_SENSOR_TYPE_VALUE,
                            SensorHubParser
                                    .getSensorTypeReading(characteristic));
                }
                // Barometer sensor scan interval read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_BAROMETER_SENSOR_SCAN_INTERVAL)) {
                    mBundle.putInt(
                            Constants.EXTRA_SPRESSURE_SENSOR_SCAN_VALUE,
                            SensorHubParser
                                    .getSensorScanIntervalReading(characteristic));
                }
                // Barometer digital sensor
                else if (charUuid
                        .equals(UUIDDatabase.UUID_BAROMETER_DIGITAL_SENSOR)) {
                    mBundle.putInt(Constants.EXTRA_SPRESSURE_SENSOR_TYPE_VALUE,
                            SensorHubParser
                                    .getSensorTypeReading(characteristic));
                }
                // Barometer threshold for indication read value
                else if (charUuid
                        .equals(UUIDDatabase.UUID_BAROMETER_THRESHOLD_FOR_INDICATION)) {
                    mBundle.putInt(Constants.EXTRA_SPRESSURE_THRESHOLD_VALUE,
                            SensorHubParser.getThresholdValue(characteristic));
                }
                intent.putExtras(mBundle);

                /**
                 * Sending the broad cast so that it can be received on
                 * registered receivers
                 */

                PoctApplication.mApp.sendBroadcast(intent);

            } else {

            }
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
            System.out.println("onCharacteristicChanged -------------------> changed");
            //notify 会回调用此方法
            broadcastNotifyUpdate(characteristic);
        }
    };

    public void connectDevice(EquipmentItem device, Context context) {
        String currentDevAddress = device.mAddress;
        String currentDevName = device.id;
        //如果是连接状态，断开，重新连接
        if (blueToothAdapter.isEnabled() == false) {
            blueToothAdapter.enable();
        }
        if (mConnectionState != STATE_DISCONNECTED || mBluetoothGatt != null) {
            if (mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
                mConnectionState = STATE_DISCONNECTED;
            } else {
                mConnectionState = STATE_DISCONNECTED;
            }
        }
        if (mBluetoothGatt != null)
            mBluetoothGatt.close();
        netBluetoothDevice = blueToothAdapter.getRemoteDevice(currentDevAddress);
        device.device = netBluetoothDevice;
        BleConnectThread mBleConnectThread = new BleConnectThread(context);
        mBleConnectThread.start();
        mConnectionState = STATE_CONNECTING;
    }

    private String mBtMac;

    public boolean connect(Context context,String btMac) {
        if (blueToothAdapter == null || btMac == null) {
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBtMac != null && btMac.equals(mBtMac)
                && mBluetoothGatt != null) {
            return mBluetoothGatt.connect();
        }

        BluetoothDevice device = blueToothAdapter.getRemoteDevice(btMac);
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        mBtMac = btMac;

        return true;
    }

    public void disConnectDevice() {

        if (mConnectionState != STATE_DISCONNECTED || mBluetoothGatt != null) {
            if(mBluetoothGatt != null)
            {
                mBluetoothGatt.disconnect();
                mBluetoothGatt.close();
            }
        }

    }



    public void prepareServiceData(Context context) {

        List<BluetoothGattService> gattServices = mBluetoothGatt.getServices();
        if (gattServices == null)
            return;
        for (BluetoothGattService gattService : gattServices) {
            String uuid = gattService.getUuid().toString();
            if (uuid.equals(GattAttributes.GENERIC_ACCESS_SERVICE) || uuid.equals(GattAttributes.GENERIC_ATTRIBUTE_SERVICE))
                continue;
            String name = GattAttributes.lookup(gattService.getUuid().toString(), "UnkonwService");
            MService mService = new MService(name, gattService);
            if (gattService.getUuid().toString().equals(GattAttributes.USR_SERVICE)) {
                mPadaService = mService;
                ArrayList<BluetoothGattCharacteristic> clist = new ArrayList<BluetoothGattCharacteristic>();
                clist.addAll(gattService.getCharacteristics());
                for (int j = 0; j < clist.size(); j++) {
                    BluetoothGattCharacteristic characteristic = clist.get(j);
                    int charaProp = characteristic.getProperties();
                    if (!characteristic.getUuid().toString().equals(GattAttributes.USR_SERVICE)) {
                        String properties = Utils.getPorperties(context, characteristic);
                        initProperties(properties);
                        if (properties.contains("Notify")) {
                            if (characteristic.getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG)) != null) {
                                if (enabled == true) {
                                    BluetoothGattDescriptor descriptor = characteristic
                                            .getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    mBluetoothGatt.writeDescriptor(descriptor);
                                } else {
                                    BluetoothGattDescriptor descriptor = characteristic
                                            .getDescriptor(UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
                                    descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                                    mBluetoothGatt.writeDescriptor(descriptor);
                                }
                            }
                            mPadacharacteristic = characteristic;
                            mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
                            context.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_READY));
                            return;
                        }
                    }
                }
            }

        }
    }

    private void initProperties(String properties) {
        if (TextUtils.isEmpty(properties))
            return;
        String[] property = properties.split("&");

        if (property.length == 1) {
        } else {
            for (int i = 0; i < property.length; i++) {
                String p = property[i];
            }
        }
    }

    private static void broadcastNotifyUpdate(final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(GattUpdateReceiver.ACTION_DATA_AVAILABLE);
        Bundle mBundle = new Bundle();
        mBundle.putByteArray(Constants.EXTRA_BYTE_VALUE,
                characteristic.getValue());
        mBundle.putString(Constants.EXTRA_BYTE_UUID_VALUE, characteristic.getUuid().toString());
        // Heart rate Measurement notify value
        if (UUIDDatabase.UUID_HEART_RATE_MEASUREMENT.equals(characteristic
                .getUuid())) {
            String heart_rate = HRMParser.getHeartRate(characteristic);
            String energy_expended = HRMParser
                    .getEnergyExpended(characteristic);
            ArrayList<Integer> rrintervel = HRMParser
                    .getRRInterval(characteristic);
            mBundle.putString(Constants.EXTRA_HRM_VALUE, heart_rate);
            mBundle.putString(Constants.EXTRA_HRM_EEVALUE, energy_expended);
            mBundle.putIntegerArrayList(Constants.EXTRA_HRM_RRVALUE, rrintervel);
        }
        // Health thermometer notify value
        if (UUIDDatabase.UUID_HEALTH_THERMOMETER.equals(characteristic
                .getUuid())) {
            ArrayList<String> health_temp = HTMParser.getHealthThermo(characteristic);
            mBundle.putStringArrayList(Constants.EXTRA_HTM_VALUE, health_temp);
        }

        // CapSense Proximity notify value
        if (UUIDDatabase.UUID_CAPSENSE_PROXIMITY.equals(characteristic.getUuid()) ||
                UUIDDatabase.UUID_CAPSENSE_PROXIMITY_CUSTOM.equals(characteristic.getUuid())) {
            int capsense_proximity = CapSenseParser
                    .getCapSenseProximity(characteristic);
            mBundle.putInt(Constants.EXTRA_CAPPROX_VALUE, capsense_proximity);
        }
        // CapSense slider notify value
        if (UUIDDatabase.UUID_CAPSENSE_SLIDER.equals(characteristic.getUuid()) ||
                UUIDDatabase.UUID_CAPSENSE_SLIDER_CUSTOM.equals(characteristic.getUuid())) {
            int capsense_slider = CapSenseParser
                    .getCapSenseSlider(characteristic);
            mBundle.putInt(Constants.EXTRA_CAPSLIDER_VALUE, capsense_slider);

        }
        // CapSense buttons notify value
        if (UUIDDatabase.UUID_CAPSENSE_BUTTONS.equals(characteristic.getUuid()) ||
                UUIDDatabase.UUID_CAPSENSE_BUTTONS_CUSTOM.equals(characteristic.getUuid())) {
            ArrayList<Integer> capsense_buttons = CapSenseParser
                    .getCapSenseButtons(characteristic);
            mBundle.putIntegerArrayList(Constants.EXTRA_CAPBUTTONS_VALUE,
                    capsense_buttons);

        }
        // Glucose notify value
        if (UUIDDatabase.UUID_GLUCOSE.equals(characteristic.getUuid())) {
            ArrayList<String> glucose_values = GlucoseParser
                    .getGlucoseHealth(characteristic);
            mBundle.putStringArrayList(Constants.EXTRA_GLUCOSE_VALUE,
                    glucose_values);

        }
        // Blood pressure measurement notify value
        if (UUIDDatabase.UUID_BLOOD_PRESSURE_MEASUREMENT.equals(characteristic
                .getUuid())) {
            String blood_pressure_systolic = BloodPressureParser
                    .getSystolicBloodPressure(characteristic);
            String blood_pressure_diastolic = BloodPressureParser
                    .getDiaStolicBloodPressure(characteristic);
//            String blood_pressure_systolic_unit = BloodPressureParser
//                    .getSystolicBloodPressureUnit(mPadacharacteristic, mContext);
//            String blood_pressure_diastolic_unit = BloodPressureParser
//                    .getDiaStolicBloodPressureUnit(mPadacharacteristic, mContext);
//            mBundle.putString(
//                    Constants.EXTRA_PRESURE_SYSTOLIC_UNIT_VALUE,
//                    blood_pressure_systolic_unit);
//            mBundle.putString(
//                    Constants.EXTRA_PRESURE_DIASTOLIC_UNIT_VALUE,
//                    blood_pressure_diastolic_unit);
            mBundle.putString(
                    Constants.EXTRA_PRESURE_SYSTOLIC_VALUE,
                    blood_pressure_systolic);
            mBundle.putString(
                    Constants.EXTRA_PRESURE_DIASTOLIC_VALUE,
                    blood_pressure_diastolic);

        }
        // Running speed measurement notify value
        if (UUIDDatabase.UUID_RSC_MEASURE.equals(characteristic.getUuid())) {
            ArrayList<String> rsc_values = RSCParser
                    .getRunningSpeednCadence(characteristic);
            mBundle.putStringArrayList(Constants.EXTRA_RSC_VALUE, rsc_values);

        }
        // Cycling speed Measurement notify value
        if (UUIDDatabase.UUID_CSC_MEASURE.equals(characteristic.getUuid())) {
            ArrayList<String> csc_values = CSCParser
                    .getCyclingSpeednCadence(characteristic);
            mBundle.putStringArrayList(Constants.EXTRA_CSC_VALUE, csc_values);

        }
        // Accelerometer x notify value
        if (UUIDDatabase.UUID_ACCELEROMETER_READING_X.equals(characteristic
                .getUuid())) {
            mBundle.putInt(Constants.EXTRA_ACCX_VALUE,
                    SensorHubParser.getAcceleroMeterXYZReading(characteristic));

        }
        // Accelerometer Y notify value
        if (UUIDDatabase.UUID_ACCELEROMETER_READING_Y.equals(characteristic
                .getUuid())) {
            mBundle.putInt(Constants.EXTRA_ACCY_VALUE,
                    SensorHubParser.getAcceleroMeterXYZReading(characteristic));
        }
        // Accelerometer Z notify value
        if (UUIDDatabase.UUID_ACCELEROMETER_READING_Z.equals(characteristic
                .getUuid())) {
            mBundle.putInt(Constants.EXTRA_ACCZ_VALUE,
                    SensorHubParser.getAcceleroMeterXYZReading(characteristic));

        }
        // Temperature notify value
        if (UUIDDatabase.UUID_TEMPERATURE_READING.equals(characteristic
                .getUuid())) {
            mBundle.putFloat(Constants.EXTRA_STEMP_VALUE,
                    SensorHubParser.getThermometerReading(characteristic));

        }
        // Barometer notify value
        if (UUIDDatabase.UUID_BAROMETER_READING
                .equals(characteristic.getUuid())) {
            mBundle.putInt(Constants.EXTRA_SPRESSURE_VALUE,
                    SensorHubParser.getBarometerReading(characteristic));
        }
        // Battery level read value
        if (UUIDDatabase.UUID_BATTERY_LEVEL
                .equals(characteristic.getUuid())) {
            mBundle.putString(Constants.EXTRA_BTL_VALUE,
                    Utils.getBatteryLevel(characteristic));
        }
        //RDK mPadacharacteristic
        if (UUIDDatabase.UUID_REP0RT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor
                    (UUIDDatabase.UUID_REPORT_REFERENCE);
            if (descriptor != null) {
                BluetoothSetManager.getInstance().mBluetoothGatt.readDescriptor(characteristic.getDescriptor(
                        UUIDDatabase.UUID_REPORT_REFERENCE));
                ArrayList<String> reportReferenceValues = DescriptorParser.getReportReference(characteristic.
                        getDescriptor(UUIDDatabase.UUID_REPORT_REFERENCE));
                if (reportReferenceValues.size() == 2) {
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_ID,
                            reportReferenceValues.get(0));
                    mBundle.putString(Constants.EXTRA_DESCRIPTOR_REPORT_REFERENCE_TYPE,
                            reportReferenceValues.get(1));
                }


            }

        }
        //case for OTA mPadacharacteristic received
        if (UUIDDatabase.UUID_OTA_UPDATE_CHARACTERISTIC
                .equals(characteristic.getUuid())) {
            //do noting now

        }


        intent.putExtras(mBundle);
        /**
         * Sending the broad cast so that it can be received on registered
         * receivers
         */

        PoctApplication.mApp.sendBroadcast(intent);
    }


    public void scanLeDevice() {
        if(blueToothAdapter.isEnabled())
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
                mBluetoothLeScanner.startScan(mScanCallback);
                PoctApplication.mApp.sendBroadcast(new Intent(LoginActivity.ACTION_END_DISCOVER_DEVICE));
            }
        }
    }

    public ScanCallback mScanCallback = new ScanCallback()
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(result != null){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if(EquipMentManager.getInstance().deviceFada.id.equals(result.getDevice().getName()))
                    {
                        stopLeScan();
                        if(EquipMentManager.getInstance().deviceFada.device == null)
                        {
                            EquipMentManager.getInstance().deviceFada.device = result.getDevice();
                            EquipMentManager.getInstance().deviceFada.mAddress = result.getDevice().getAddress();
                            PoctApplication.mApp.sendBroadcast(new Intent(BluetoothSetManager.ACTION_DEVICE_FADA_GET));
                        }

                    }
                }

            }
            else
            {
                stopLeScan();
            }
        }
    };

    public void stopLeScan()
    {
        BluetoothLeScanner mBluetoothLeScanner = blueToothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.stopScan(mScanCallback);
    }
}
