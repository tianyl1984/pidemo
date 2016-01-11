package com.tianyl.pidemo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class GPIOListen {

	public static void main(String[] args) throws Exception {
		System.out.println("-----start-----");
		final GpioController gpio = GpioFactory.getInstance();
		final GpioPinDigitalInput pin2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);
		pin2.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				System.out.println(String.format("PinName:${0},EventType:${1},State:${2}", event.getPin().getName(), event.getEventType().name(), event.getState().getValue()));
			}
		});
		System.out.println("add listerer...");
		while (true) {
			Thread.sleep(1000);
		}
	}
}
