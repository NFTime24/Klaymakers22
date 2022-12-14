package com.nftime.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.klipwallet.app2app.api.Klip;
import com.nftime.app.actions.KlipAction;
import com.nftime.app.objects.NftWorkObj;
import com.nftime.app.util.ApplicationConstants;
import com.nftime.app.util.asyncTasks.AsyncResponse;
import com.nftime.app.util.asyncTasks.MintWithWorkIdTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.bootpay.android.Bootpay;
import kr.co.bootpay.android.events.BootpayEventListener;
import kr.co.bootpay.android.models.BootExtra;
import kr.co.bootpay.android.models.BootItem;
import kr.co.bootpay.android.models.BootUser;
import kr.co.bootpay.android.models.Payload;

public class NFTPurchaseActivity extends AppCompatActivity {
    private Context context;
    private NftWorkObj nft;

    private ImageView nftImage;
    private TextView nftTitle;
    private TextView nftArtist;
    private TextView nftPrice;

    private TextView contractTitle1;
    private TextView contractTitle2;
    private TextView contractDetail;
    private CheckBox contractCheck1;
    private CheckBox contractCheck2;
    private CheckBox contractCheck3;
    private CheckBox contractCheck4;
    private TextView contractCaution;
    private TextView contractPaymentCaution;
    private RadioButton contractPaymentMethod1;

    private Button btnContractCheck;
    private Button btnPurchase;
    private Button btnCancel;

    private String PhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nftpurchase);

        context = this;

        Intent intent = getIntent();

        nft = new NftWorkObj(
                intent.getIntExtra("work_id", 0),
                intent.getIntExtra("work_price", 0),
                intent.getIntExtra("filesize", 0),
                intent.getIntExtra("exhibition_id", 0),
                intent.getStringExtra("work_name"),
                intent.getStringExtra("description"),
                intent.getStringExtra("category"),
                intent.getStringExtra("filename"),
                intent.getStringExtra("filetype"),
                intent.getStringExtra("path"),
                intent.getStringExtra("thumbnail_path"),
                intent.getStringExtra("artist_name"),
                intent.getStringExtra("artist_profile_path"),
                intent.getStringExtra("artist_address")
        );

        nftImage = findViewById(R.id.image_nft_info);
        nftTitle = findViewById(R.id.tv_nft_title);
        nftArtist = findViewById(R.id.tv_nft_artist);
        nftPrice = findViewById(R.id.tv_price);

        contractTitle1 = findViewById(R.id.tv_contract_title);
        contractTitle2 = findViewById(R.id.tv_contract_title2);
        contractDetail = findViewById(R.id.tv_contract_detail);
        contractCheck1 = findViewById(R.id.tv_contract_check1);
        contractCheck2 = findViewById(R.id.tv_contract_check2);
        contractCheck3 = findViewById(R.id.tv_contract_check3);
        contractCheck4 = findViewById(R.id.tv_contract_check4);
        contractCaution = findViewById(R.id.tv_contract_check_caution);
        contractPaymentCaution = findViewById(R.id.tv_contract_payment_caution);
        contractPaymentMethod1 = findViewById(R.id.tv_contract_payment_method1);
        btnContractCheck = findViewById(R.id.btn_contract_check);
        btnPurchase = findViewById(R.id.btn_purchase_ok);
        btnCancel = findViewById(R.id.btn_purchase_cancel);

        nftTitle.setText(nft.work_name);
        nftArtist.setText(nft.artist_name);
        nftPrice.setText("4,500");

        String thumbnail_url = ApplicationConstants.AWS_URL + nft.path;

        Glide.with(context)
                .load(thumbnail_url)
                .centerCrop()
                .placeholder(R.drawable.app_icon_nftime)
                .into(nftImage);

        // English Text
        contractTitle1.setText("1. Confirm and agree to terms and conditions");
        contractTitle2.setText("2. Select payment method");

        contractDetail.setText("The target of purchase is digital art, and the specific terms of purchase are in accordance with the transfer contract. \n\n NFT-based ownership certificates are issued solely to prove digital art purchases, and cannot be arbitrarily traded, transferred, or disposed of.");
        contractCheck1.setText("[Requirement] Payment has been confirmed and agreed");
        contractCheck2.setText("[Requirement] The terms and precautions of the transfer contract have been checked and agreed");
        contractCheck3.setText("[Requirement] I have confirmed and agreed on the non-withdrawal and non-refundable regulations");
        contractCheck4.setText("[Requirement] I agree to provide personal information to a third party for the conclusion of the transfer contract");
        contractPaymentCaution.setText("If you apply for withdrawal or withdrawal is completed in the following month of purchase, you will be refunded after deducting the payment fee (6.9% of the payment amount).");
        contractCaution.setText("nftime is a mail order broker and is not a party to mail order. Each seller is responsible for any obligation to trade digital works (such as price and quantity of works sold, orders for works, withdrawal of subscriptions, delivery, exchange, return and refund), and nftime is not responsible.");
        contractPaymentMethod1.setText("Mobile Phone Payment");
        btnContractCheck.setText("Check and agree to the transfer contract");
        btnPurchase.setText("Purchase NFT");
        btnCancel.setText("Cancel");

        // Korean Text
/*        contractDetail.setText("?????? ????????? ????????? ????????????, ???????????? ?????? ????????? ????????? ???????????? ????????????.\n\n NFT ????????? ?????????????????? ????????? ????????? ?????? ????????? ???????????? ?????? ????????? ????????????, ????????? ??????, ??????, ?????? ?????? ??? ??? ????????????.");
        contractCheck1.setText("[??????] ?????? ????????? ??????????????? ?????????");
        contractCheck2.setText("[??????] ????????? ?????? ?????? ??? ??????????????? ?????? ???????????? ?????????");
        contractCheck3.setText("[??????] ?????? ?????? ?????? ??? ?????? ????????? ?????? ????????? ?????????????????? ?????????");
        contractCheck4.setText("[??????] ??????????????? ????????? ?????? ???????????? ???3??? ????????? ?????????");
        contractPaymentCaution.setText("?????? ????????? ?????? ?????? ?????? ?????? ?????? ??? PG??? ?????? ????????? (?????? ????????? 6.9%) ?????? ??? ???????????????.");
        contractCaution.setText("?????????????????? ??????????????????????????? ??????????????? ???????????? ????????????. ????????? ????????? ??????(?????? ?????? ??? ?????? ??????, ?????? ??????, ?????? ??????, ??????, ??????, ?????? ??? ?????? ???)??? ?????? ????????? ????????? ????????? ??? ??????????????? ?????????, ?????????????????? ????????? ?????? ????????????.");
        contractPaymentMethod1.setText("????????? ??????");*/

        btnContractCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TransferContractActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        contractCheck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    btnPurchase.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#216FEB")));
                } else {
                    btnPurchase.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DAE2E5")));
                }
            }
        });


        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contractCheck1.isChecked() && contractCheck2.isChecked() && contractCheck3.isChecked() && contractCheck4.isChecked())
                    PaymentTest(v);
            }
        });

        TelephonyManager telManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PhoneNum = telManager.getLine1Number().toString();
        if(PhoneNum.startsWith("+82")){
            PhoneNum = PhoneNum.replace("+82", "0");
        }

    }

    public void PaymentTest(View v) {
        BootUser user = new BootUser().setPhone(PhoneNum); // ????????? ??????

        BootExtra extra = new BootExtra()
                .setCardQuota("0,2,3"); // ?????????, 2??????, 3?????? ?????? ??????, ????????? ?????? 12???????????? ????????? (5?????? ?????? ????????? ???????????? ??????)


        List<BootItem> items = new ArrayList<>();
        BootItem item1 = new BootItem().setName("??????'s ???").setId("ITEM_CODE_MOUSE").setQty(1).setPrice(2500d);
        BootItem item2 = new BootItem().setName("?????????").setId("ITEM_KEYBOARD_MOUSE").setQty(1).setPrice(2000d);
        items.add(item1);
        items.add(item2);

        Payload payload = new Payload();
        payload.setApplicationId("63200c09d01c7e001cfa3bf0")
                .setOrderName(nft.work_name)
                .setPg("??????") // ????????? ????????? PG???
                .setMethod("?????????") // ????????? ??????
                .setOrderId("1234")
                .setPrice(4500d)
                .setUser(user)
                .setExtra(extra)
                .setItems(items);

        Map<String, Object> map = new HashMap<>();
        map.put("1", "abcdef");
        map.put("2", "abcdef55");
        map.put("3", 1234);
        payload.setMetadata(map);
//        payload.setMetadata(new Gson().toJson(map));

        Bootpay.init(getSupportFragmentManager(), getApplicationContext())
                .setPayload(payload)
                .setEventListener(new BootpayEventListener() {
                    @Override
                    public void onCancel(String data) {
                        Log.d("bootpay", "cancel: " + data);
                        finish();
                    }

                    @Override
                    public void onError(String data) {
                        Log.d("bootpay", "error: " + data);
                    }

                    @Override
                    public void onClose(String data) {
                        Log.d("bootpay", "close: " + data);
                        finish();
                    }

                    @Override
                    public void onIssued(String data) {
                        Log.d("bootpay", "issued: " + data);
                    }

                    @Override
                    public boolean onConfirm(String data) {
                        Log.d("bootpay", "confirm: " + data);
//                        Bootpay.transactionConfirm(data); //????????? ????????? ????????? ???????????? ?????? true (?????? 1)
                        return true; //????????? ????????? ????????? ???????????? ?????? true (?????? 2)
//                        return false; //????????? ???????????? ????????? false
                    }

                    @Override
                    public void onDone(String data) {
                        Log.d("bootpay", "done: " + data);
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);

                        new MintWithWorkIdTask(context, new AsyncResponse<String>() {
                            @Override
                            public void onAsyncSuccess(String result) {
                                Log.d("bootpay", "Mint result: " + result);
                                Toast doneToast = Toast.makeText(context, "NFT purchase completed. Check out the Collection tab.", Toast.LENGTH_LONG);
                                doneToast.show();
                            }
                        }).execute(nft.work_id);


                        /*KlipAction klipAction = new KlipAction(context, Klip.getInstance(context));
                        klipAction.mintWithKlip(MainActivity.klaytnAddress, nft.work_id, nft.artist_address);*/
                    }
                }).requestPayment();
    }
}