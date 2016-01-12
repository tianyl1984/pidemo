package com.tianyl.pidemo;

import java.util.concurrent.Callable;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

public class GPIOTrigger {

	public static void main(String[] args) throws Exception {
		System.out.println("trigger example start");
		GpioController gpio = GpioFactory.getInstance();
		GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

		GpioPinDigitalOutput[] leds = new GpioPinDigitalOutput[] {
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW),
				gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW),
		};
		button.addTrigger(new GpioSetStateTrigger(PinState.HIGH, leds[0], PinState.HIGH));
		button.addTrigger(new GpioSetStateTrigger(PinState.LOW, leds[0], PinState.LOW));

		button.addTrigger(new GpioSyncStateTrigger(leds[1]));

		button.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, leds[2], 1000));

		button.addTrigger(new GpioCallbackTrigger(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				System.out.println("custom call back");
				return null;
			}

		}));

		while (true) {
			Thread.sleep(500);
		}
	}
}
