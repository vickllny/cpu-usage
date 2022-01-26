package com.vickllny.cpu.usage;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

@SpringBootApplication
public class CpuUsageApplication implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(CpuUsageApplication.class);
	
	
	SystemInfo si = new SystemInfo();
	HardwareAbstractionLayer hal = si.getHardware();
	CentralProcessor cpu = hal.getProcessor();
	
	long[] oldTicks = cpu.getSystemCpuLoadTicks();

	public static void main(String[] args) {
		SpringApplication.run(CpuUsageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		new Thread(() -> {
			while(true) {
				try {
					cpuUsage();
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void cpuUsage() {
		float value = floatArrayPercent(cpuData(cpu));
		logger.debug("the cpu usage is : {}", value);
	}

	private static float floatArrayPercent(double d) {
		return (float) (100d * d);
	}

	private double cpuData(CentralProcessor proc) {
		double d = proc.getSystemCpuLoadBetweenTicks(oldTicks);
		oldTicks = proc.getSystemCpuLoadTicks();
		return d;
	}

}
