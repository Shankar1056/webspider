package apextechies.etodo.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;


public class Download_web extends AsyncTask<String, Void, String>
    {
        private final Context context;
        private String response="";
        private OnTaskCompleted listener;
        private boolean isGet=true;
        private boolean isAuth=false;
        private ArrayList<NameValuePair> data;



        public Download_web(Context context, OnTaskCompleted listener)
        {
            this.context=context;
            this.listener=listener;
        }
        public void setReqType(boolean isGet)
        {
            this.isGet=isGet;
        }
        public void setData(ArrayList<NameValuePair> data)
        {
            this.data=data;
        }



        @Override
        protected String doInBackground(String... params)
        {

            for(String url:params)
            {
                if(isGet)
                {
                    response=doGet(url);
                }
                else
                {
                    response=doPost(url,data);
                }
            }

            return response;
        }
        @Override
        protected void onPreExecute()
        {

            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String result)
        {

            if(!result.equals(""))
            {
                if(listener!=null)
                listener.onTaskCompleted(result);
            }
            else
            {

                Toast.makeText(context,  "something wrong", Toast.LENGTH_LONG).show();
                if (listener!=null)
                listener.onTaskCompleted("");
            }

        }

        private String doGet(String url)
        {
            try
            {
    
                response = Utilz.executeHttpGet(url);

            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
                response="";
                return response;
            }
            return response;
        }
        private String doPost(String url,ArrayList<NameValuePair> data)
        {
            try
            {
               response = Utilz.executeHttpPost(url,data);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Log.e("Exception",e.getMessage());
                response="";
                return response;
            }
            return response;
        }



    }