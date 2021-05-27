package com.lgdisplay.bigdata.api.service.glue.jobs;

import com.lgdisplay.bigdata.api.service.glue.model.ServerHealth;
import com.lgdisplay.bigdata.api.service.glue.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Glue API Server의 리소스를 주기적으로 체크하는 Scheduled Task.
 */
@Slf4j
@Component
public class ServerHealthCheckTask {

    @Autowired
    ResourceService resourceService;

    long[] prevTicks = new long[CentralProcessor.TickType.values().length];

    @Scheduled(fixedRate = 5000)
    public void execute() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor cpu = hal.getProcessor();

        int logicalProcessorCount = cpu.getLogicalProcessorCount();
        int physicalProcessorCount = cpu.getPhysicalProcessorCount();

        GlobalMemory memory = hal.getMemory();
        long totalMemory = memory.getTotal();
        long availableMemory = memory.getAvailable();

        int cpuLoadRatio = (int) (cpu.getSystemCpuLoadBetweenTicks(prevTicks) * 100);
        int memoryRatio = (int) (((double) availableMemory / (double) totalMemory) * 100);

        ServerHealth serverHealth = ServerHealth.builder()
                .physicalProcessorCount(physicalProcessorCount)
                .logicalProcessorCount(logicalProcessorCount)
                .totalMemory(totalMemory)
                .availableMemory(availableMemory)
                .cpuLoadRatio(cpuLoadRatio)
                .memoryRatio(memoryRatio)
                .build();
        resourceService.setApiServerHealth(serverHealth);

       // log.debug("{}", serverHealth);

        this.prevTicks = cpu.getSystemCpuLoadTicks();
    }

}