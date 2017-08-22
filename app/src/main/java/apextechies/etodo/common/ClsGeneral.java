package apextechies.etodo.common;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClsGeneral {
	public static Context mContext;

	public static String NAME = "name";
	public static String EMAIL = "email";
	public static String CARLICENSENUMBER = "carLicenseNumber";
	public static String USERID = "userid";
	public static String SCANNEDUSER = "scannedUser";
	public static String ANDROIDID = "androidId";

	public static String SUCCESS = "success";
	public static String TRUE = "true";
	public static String USER_ID = "user_id";
	public static String USER_NAME = "user_name";
	public static String USER_EMAIL = "user_email";
	public static String USER_PHONENUMBER = "user_phonenumber";
	public static String USER_LOCATION = "user_location";
	public static String USER_DOB = "user_dob";
	public static String USER_GENDER = "user_gender";
	public static String USER_PROFILEPIC = "user_profilepic";
	public static String USER_STATUS = "user_status";
	public static String USER_TYPE = "user_type";
	public static String OTP = "user_status";
	public static String DEVICE_ID = "device_id";




	public static void setPreferences(Context context, String key, String value) {
		mContext = context;
		SharedPreferences.Editor editor = mContext.getSharedPreferences(
				"WED_APP", Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getPreferences(Context context, String key) {
		mContext = context;
		SharedPreferences prefs = mContext.getSharedPreferences("WED_APP",
				Context.MODE_PRIVATE);
		String text = prefs.getString(key, "");
		return text;
	}




	public static boolean emailValidator(String email) {
		Pattern pattern;
		Matcher matcher;
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean isNetworkAvailable(Context context)
	{

		ConnectivityManager connectivity  = null;
		boolean isNetworkAvail = false;

		try
		{
			connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (connectivity != null)
			{
				NetworkInfo[] info = connectivity.getAllNetworkInfo();

				if (info != null)
				{
					for (int i = 0; i < info.length; i++)
					{
						if (info[i].getState() == NetworkInfo.State.CONNECTED)
						{

							return true;
						}
					}
				}
			}
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(connectivity !=null)
			{
				connectivity = null;
			}
		}
		return isNetworkAvail;
	}


}
