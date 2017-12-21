package com.omneagate.lip.Model;

import java.util.List;

/**
 * Created by Sathya Rathinavelu on 5/8/2016.
 */
public class DeviceDto {
    public List<DeviceDetailsDto> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDetailsDto> devices) {
        this.devices = devices;
    }

    List<DeviceDetailsDto> devices;
}
