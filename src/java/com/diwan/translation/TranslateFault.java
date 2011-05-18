package com.diwan.translation;

import org.apache.axis2.AxisFault;

public class TranslateFault extends AxisFault {
	public TranslateFault(String msg) {
		super(msg);
	}

}
