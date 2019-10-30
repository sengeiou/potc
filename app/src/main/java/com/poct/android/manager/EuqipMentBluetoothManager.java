package com.poct.android.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.poct.android.entity.EquipmentItem;
import com.poct.android.entity.MService;
import com.poct.android.net.NetUtils;
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
import com.poct.android.utils.Constants;
import com.poct.android.utils.DBHelper;
import com.poct.android.utils.GattAttributes;
import com.poct.android.utils.UUIDDatabase;
import com.poct.android.utils.Utils;
import com.poct.android.view.PoctApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EuqipMentBluetoothManager {

    public static final String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public static final String DEVICE_POTC = "DEVICE_POTC";
    public static final String DEVICE_FADA = "DEVICE_FADA";
    public static final String DEVICE_PRINT = "DEVICE_PRINT";
    public static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public static EuqipMentBluetoothManager mEuqipMentBluetoothManager;
    public EquipmentItem devicePotc;
    public EquipmentItem deviceFada;
    public EquipmentItem devicePrint;
    public BluetoothAdapter blueToothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothLeScanner bleScanner;
    private int mConnectionState = STATE_DISCONNECTED;
    public boolean enabled = true;
    public Intent mMyPrintService;

    public EuqipMentBluetoothManager()
    {
        devicePotc = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_POTC);
        deviceFada = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_FADA);
        devicePrint = DBHelper.getInstance(PoctApplication.mApp).getDevice(DEVICE_PRINT);
//        if(devicePotc != null || deviceFada != null)
//        {
//            blueToothAdapter.cancelDiscovery();
//            blueToothAdapter.startDiscovery();
//        }

    }

    public void doPrint(final String srcFilePath) {

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    int initLLD_IntResult = PoctApplication.mApp.mPrintUtil.initLLD(PoctApplication.mApp.json_Str, 4);
                    int t = PoctApplication.mApp.mPrintUtil.connectToServer(devicePrint.id,9100,1);
                    File file 						= new File(srcFilePath);
                    FileInputStream fin 			= null;
                    fin = new FileInputStream(file);
                    int length 						= fin.available();
                    byte[] pdfBuffer 				= new byte[length];

                    fin.read(pdfBuffer);
                    fin.close();

                    String serverIp					= devicePrint.id;
                    int serverPort 					= 9100;
                    int printPageWPixel 			= 4736;
                    int printPageHPixel 			= 6784;

                    int printNumCopies 				= 1;
                    int srcImgWPixel 				= 0;
                    int srcImgHPixel 				= 0;
                    int srcImgBitCount				= 24;

                    int srcImgSize 					= pdfBuffer.length;
                    char[] printDataArray 			= new char[pdfBuffer.length];

                    for(int i=0; i<pdfBuffer.length; i++){
                        printDataArray[i] 			= (char)pdfBuffer[i];
                    }



                    int printDataSize 				= printDataArray.length;
                    int CombinationPackageResult 	= PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataSize);

                    srcImgSize 						= CombinationPackageResult;

                    int sendDataToServerResult 				= PoctApplication.mApp.mPrintUtil.sendDataToServer(serverIp, serverPort, printPageWPixel, printPageHPixel, printNumCopies, 1, srcImgSize, srcImgWPixel, srcImgHPixel, srcImgBitCount, 0, 0);

                    int b = sendDataToServerResult;
                    int isFinishCombinationPackage 	= PoctApplication.mApp.mPrintUtil.getCurrentPrintFinishState();
                    int c = isFinishCombinationPackage;
                    PoctApplication.mApp.mPrintUtil.initParameter();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public static synchronized EuqipMentBluetoothManager getInstance() {
        if (mEuqipMentBluetoothManager == null) {
            mEuqipMentBluetoothManager = new EuqipMentBluetoothManager();
        }
        return mEuqipMentBluetoothManager;
    }

    public void addDevice(BluetoothDevice device) {
        if(devicePotc != null)
        {
            if(devicePotc.device == null)
            {
                if(devicePotc.id.equals(device.getName()))
                {
                    devicePotc.device = device;
                    if(deviceFada == null)
                    {
                        blueToothAdapter.cancelDiscovery();
                    }
                    else
                    {
                        if(deviceFada != null)
                        {
                            if(deviceFada.device != null)
                            {
                                blueToothAdapter.cancelDiscovery();
                            }
                        }
                    }
                    return;
                }
            }
        }
        if(deviceFada != null)
        {
            if(deviceFada.device == null)
            {
                if(deviceFada.id.equals(device.getName()))
                {
                    deviceFada.device = device;
                    if(devicePotc == null)
                    {
                        blueToothAdapter.cancelDiscovery();
                    }
                    else
                    {
                        if(devicePotc != null)
                        {
                            if(devicePotc.device != null)
                            {
                                blueToothAdapter.cancelDiscovery();
                            }
                        }
                    }
                    return;
                }
            }
        }

    }


    public void startScan() {
        if(devicePotc != null)
        {
            if(devicePotc.device == null) {
                blueToothAdapter.startDiscovery();
            }
        }
        else if(deviceFada != null)
        {
            if(deviceFada.device == null) {
                blueToothAdapter.startDiscovery();
            }
        }
        else
        {

        }
    }


    public void offDisvery() {
       blueToothAdapter.cancelDiscovery();
    }

    private final static BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
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
                Intent intent = new Intent(ACTION_GATT_SERVICES_DISCOVERED);
                PoctApplication.mApp.sendBroadcast(intent);
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

    private void scanPrevious21Version(Handler handler, final BluetoothAdapter.LeScanCallback mLeScanCallback) {
        //2.8秒后停止扫描
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                blueToothAdapter.stopLeScan(mLeScanCallback);
            }
        }, 2800);

        blueToothAdapter.startLeScan(mLeScanCallback);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void scanAfter21Version(Handler handler) {

        handler.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                bleScanner.stopScan(new ScanCallback() {
                    @Override
                    public void onScanResult(int callbackType, ScanResult result) {
                        super.onScanResult(callbackType, result);
                    }
                });
            }
        }, 2800);

        if (bleScanner == null)
            bleScanner = blueToothAdapter.getBluetoothLeScanner();

        bleScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
//                EquipmentItem mDev = new EquipmentItem(result.getDevice(), result.getRssi());
//                if (bluetooths.contains(mDev))
//                    return;
//                bluetooths.add(mDev);
            }
        });
    }

    public void connectDevice(BluetoothDevice device,Context context) {
        String currentDevAddress = device.getAddress();
        String currentDevName = device.getName();
        //如果是连接状态，断开，重新连接
        if (mConnectionState != STATE_DISCONNECTED)
        {
            if(BluetoothSetManager.getInstance().mBluetoothGatt != null)
            {
                BluetoothSetManager.getInstance().mBluetoothGatt.disconnect();
                BluetoothSetManager.getInstance().mBluetoothGatt.close();
            }
            else
            {
                mConnectionState = STATE_DISCONNECTED;
            }
        }
        BluetoothSetManager.getInstance().netBluetoothDevice = blueToothAdapter.getRemoteDevice(currentDevAddress);
        BluetoothSetManager.getInstance().mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        mConnectionState = STATE_CONNECTING;
    }


    private void initProperties(String properties) {
        if (TextUtils.isEmpty(properties))
            return;
//        tvProperties.setText(properties);
        String[] property = properties.split("&");

        if (property.length == 1) {
//            btnOptions.setVisibility(View.GONE);
//            Option option = new Option(properties.trim(),Option.OPTIONS_MAP.get(properties.trim()));
//            setOption(option);
        } else {
            for (int i=0;i<property.length;i++){
                String p = property[i];
//                Option option = new Option();
//                option.setName(p.trim());
//                option.setPropertyType(Option.OPTIONS_MAP.get(p.trim()));
//                options.add(option);
//                if (i==0){
//                    setOption(option);
//                }
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
//                    .getSystolicBloodPressureUnit(characteristic, mContext);
//            String blood_pressure_diastolic_unit = BloodPressureParser
//                    .getDiaStolicBloodPressureUnit(characteristic, mContext);
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
        //RDK characteristic
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
        //case for OTA characteristic received
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

    public void connectFada(Context context) {
        if(deviceFada.device == null)
        {
            blueToothAdapter.cancelDiscovery();
            blueToothAdapter.startDiscovery();
        }
        else
        {
            connectDevice(deviceFada.device,context);
        }
    }

    private int unPack(String printDataIn)
    {
        int PackageResult = 0;

        System.out.println("log_pt_ui:" + printDataIn.length());

        if (printDataIn.length() <= 10240) {
            char[] printDataArray = printDataIn.toCharArray();

            int printDataSize = printDataArray.length;
            Runtime.getRuntime().gc();
            PackageResult = PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataSize);
        }
        else {
            int countsend = 0;
            for (countsend = 0; printDataIn.length() / 10240 > countsend; countsend++) {

                char[] printDataArray = printDataIn.substring(10240 * (countsend), 10240 * (countsend + 1)).toCharArray();
                int printDataSize = printDataArray.length;
                PackageResult += PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataIn.length());
            }
            if (printDataIn.length() % 10240 != 0) {
                char[] printDataArray = printDataIn.substring((printDataIn.length() / 10240) * 10240, printDataIn.length())
                        .toCharArray();
                int printDataSize = printDataArray.length;
                PackageResult += PoctApplication.mApp.mPrintUtil.combinationPackage(printDataArray, printDataSize, printDataSize, printDataIn.length());
            }
        }
        return PackageResult;
    }

    public byte[] getBmpToByte(Bitmap bitmap)
    {

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pixels = new int[w * h];
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);
        byte[] rgb = addBMP_RGB_888(pixels, w, h);
        byte[] header = addBMPImageHeader(rgb.length);
        byte[] infos = addBMPImageInfosHeader(rgb.length, w, h);

        byte[] buffer = new byte[54 + rgb.length];
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(infos, 0, buffer, 14, infos.length);
        System.arraycopy(rgb, 0, buffer, 54, rgb.length);

        return buffer;
    }

    private byte[] addBMP_RGB_888(int[] b, int w, int h)
    {
        int len = b.length;
        System.out.println(b.length);
        byte[] buffer = new byte[w * h * 3];
        int offset = 0;

        for (int i = len - 1; i >= w - 1; i -= w)
        {
            int end = i, start = i - w + 1;
            for (int j = start; j <= end; j++)
            {
                buffer[offset] = (byte) (b[j]);
                buffer[offset + 1] = (byte) (b[j] >> 8);
                buffer[offset + 2] = (byte) (b[j] >> 16);
                offset += 3;
            }
        }

        return buffer;

    }

    private byte[] addBMPImageHeader(int size)
    {
        byte[] buffer = new byte[14];
        buffer[0] = 0x42;
        buffer[1] = 0x4D;
        buffer[2] = (byte) ((size + 54)>> 0);
        buffer[3] = (byte) ((size + 54)>> 8);
        buffer[4] = (byte) ((size + 54)>> 16);
        buffer[5] = (byte) ((size + 54)>> 24);
        buffer[6] = 0x00;
        buffer[7] = 0x00;
        buffer[8] = 0x00;
        buffer[9] = 0x00;
        buffer[10] = 0x36;
        buffer[11] = 0x00;
        buffer[12] = 0x00;
        buffer[13] = 0x00;
        return buffer;
    }

    private byte[] addBMPImageInfosHeader(int size, int w, int h)
    {
        byte[] buffer = new byte[40];

        // 这个是固定的 BMP 信息头要40个字节
        buffer[0] = 0x28;
        buffer[1] = 0x00;
        buffer[2] = 0x00;
        buffer[3] = 0x00;

        // 宽度 地位放在序号前的位置 高位放在序号后的位置
        buffer[4] = (byte) (w >> 0);
        buffer[5] = (byte) (w >> 8);
        buffer[6] = (byte) (w >> 16);
        buffer[7] = (byte) (w >> 24);

        // 长度 同上
        buffer[8] = (byte) (h >> 0);
        buffer[9] = (byte) (h >> 8);
        buffer[10] = (byte) (h >> 16);
        buffer[11] = (byte) (h >> 24);

        // 总是被设置为1
        buffer[12] = 0x01;
        buffer[13] = 0x00;

        // 比特数 像素 32位保存一个比特 这个不同的方式(ARGB 32位 RGB24位不同的!!!!
        buffer[14] = 0x18;
        buffer[15] = 0x00;

        // 0-不压缩 1-8bit位图
        // 2-4bit位图 3-16/32位图
        // 4 jpeg 5 png
        buffer[16] = 0x00;
        buffer[17] = 0x00;
        buffer[18] = 0x00;
        buffer[19] = 0x00;

        // 说明图像大小
        buffer[20] = (byte)size;
        buffer[21] = (byte)(size>>8);
        buffer[22] = (byte)(size>>16);
        buffer[23] = (byte)(size>>24);

        // 水平分辨率
        buffer[24] = 0x00;
        buffer[25] = 0x00;
        buffer[26] = 0x00;
        buffer[27] = 0x00;

        // 垂直分辨率
        buffer[28] = 0x00;
        buffer[29] = 0x00;
        buffer[30] = 0x00;
        buffer[31] = 0x00;

        // 0 使用所有的调色板项
        buffer[32] = 0x00;
        buffer[33] = 0x00;
        buffer[34] = 0x00;
        buffer[35] = 0x00;

        // 不开颜色索引
        buffer[36] = 0x00;
        buffer[37] = 0x00;
        buffer[38] = 0x00;
        buffer[39] = 0x00;
        return buffer;
    }
}
