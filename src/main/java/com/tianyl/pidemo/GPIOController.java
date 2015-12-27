package com.tianyl.pidemo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class GPIOController {

	public static void main(String[] args)throws Exception {
		
		System.out.println("----demo start----");
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,"MyLED",PinState.HIGH);
		pin.setShutdownOptions(true,PinState.LOW);
		System.out.println("--> GPIO state should be: ON");
		Thread.sleep(5000);
		pin.low();
		System.out.println("--> GPIO state should be: OFF");
		Thread.sleep(5000);
		pin.toggle();
		Thread.sleep(5000);
		System.out.println("--> GPIO state should be: ON for blocking call");
		pin.pulse(3000,PinState.HIGH,true);
		
		gpio.shutdown();
		
		System.out.println("----demo end----");
	}
	
}
