package com.milyfe.untitled;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.milyfe.untitled.model.Message;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 8/21/13
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageRowAdapter implements ListAdapter {
        Map<String,Bitmap> bitmapMap = new HashMap<String, Bitmap>();
        List<Message> items;
        Activity context;
    public MessageRowAdapter(Activity context, List<Message> items) {
        super();
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Message m = items.get(i);
        View view = context.getLayoutInflater().inflate(R.layout.message_row_layout,null);
        TextView body = (TextView) view.findViewById(R.id.messagebody);
        TextView date = (TextView) view.findViewById(R.id.messageDate);
        ImageView image = (ImageView) view.findViewById(R.id.messageImage);
        final ImageView fromPicture = (ImageView) view.findViewById(R.id.senderImage);
        if(m.getType() != null) {
            switch (m.getType()) {
                case TWEET:
                    image.setImageResource(R.drawable.twitter_logo);
                    break;
                case FACEBOOK_INBOX:
                    image.setImageResource(R.drawable.com_facebook_inverse_icon);
                    break;
                case PLUS_ACTIVITY:
                    image.setImageResource(R.drawable.common_signin_btn_icon_dark);
                    break;
                default:
                    break;

            }
        }

        body.setText(m.getBody());
        if(m.getFromPicture() != null) {
            final String url = m.getFromPicture();
            if(!bitmapMap.containsKey(url)) {
                new ImageRetrievalAsync() {
                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        fromPicture.setImageBitmap(bitmapMap.get(url));
                    }
                }.execute(url);
            } else {
                fromPicture.setImageBitmap(bitmapMap.get(url));
            }

        }
        if(m.getCreatedDate() != null) {
            date.setText(DateFormat.format("MM/dd/yyyy HH:mm",m.getCreatedDate()));
        }
        return view;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return  1;
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    class ImageRetrievalAsync extends AsyncTask<String,Void,Void> {
        @Override
        protected Void doInBackground(String... urls) {
            Bitmap bitmap = getBitmapFromURL(urls[0]);
            if(bitmap != null) {
                bitmapMap.put(urls[0],bitmap);
            }
            return null;
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
}