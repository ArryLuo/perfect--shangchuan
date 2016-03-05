package com.example.shangchuan;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView img;
	private IWXAPI iwxapi;
	private Button share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		iwxapi = WXAPIFactory.createWXAPI(this, "wxe84825b94f4432ed");
		iwxapi.registerApp("wxe84825b94f4432ed");
		img = (ImageView) findViewById(R.id.img);
		share=(Button) findViewById(R.id.send);
		img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// PICK选择
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				// MediaStore媒体库,
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 100);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==100){
			if(data!=null){
				img.setImageURI(data.getData());
			}
			
		}
	}
	public void click(View v){
		Toast.makeText(this, "你点击了", 0).show();
		wecharShar();
		img.setImageBitmap(generate());
		share.setVisibility(View.VISIBLE);
	}

	private void wecharShar() {
		// TODO Auto-generated method stub
		WXWebpageObject webpageObject=new WXWebpageObject();
		WXMediaMessage mediaMessage=new WXMediaMessage();
		//获取图片
		mediaMessage.mediaObject=new WXImageObject(generate());
		SendMessageToWX.Req req=new SendMessageToWX.Req();
		req.transaction=String.valueOf(System.currentTimeMillis());
		req.message=mediaMessage;
		req.scene=SendMessageToWX.Req.WXSceneTimeline;
		iwxapi.sendReq(req);
	}

	//获取图片的信息
	private Bitmap generate() {
		// TODO Auto-generated method stub
		share.setVisibility(View.GONE);
		View view=getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}
}
