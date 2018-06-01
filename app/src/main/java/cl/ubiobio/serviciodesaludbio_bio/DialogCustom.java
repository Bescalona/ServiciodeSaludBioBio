package cl.ubiobio.serviciodesaludbio_bio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogCustom extends DialogFragment implements View.OnClickListener {

    public interface ListenerButton{
        void onClickNegButton(int idDialog);
        void onClickPosButton(int idDialog);
    }

    private ListenerButton myListener;

    private Button btn_neg;
    private Button btn_pos;
    private TextView title;
    private TextView msg;

    private String txt_btn_neg;
    private String txt_btn_pos;
    private String txt_title;
    private String txt_msg;

    private int idDialog;

    public int getIdDialog() {
        return idDialog;
    }



    public String getTxt_btn_neg() {
        return txt_btn_neg;
    }

    public void setTxt_btn_neg(String txt_btn_neg) {
        this.txt_btn_neg = txt_btn_neg;
    }

    public String getTxt_btn_pos() {
        return txt_btn_pos;
    }

    public void setTxt_btn_pos(String txt_btn_pos) {
        this.txt_btn_pos = txt_btn_pos;
    }

    public String getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(String txt_title) {
        this.txt_title = txt_title;
    }

    public String getTxt_msg() {
        return txt_msg;
    }

    public void setTxt_msg(String txt_msg) {
        this.txt_msg = txt_msg;
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //setear la vista
        View rootView = inflater.inflate(R.layout.dialog_custom, null);

        btn_neg = rootView.findViewById(R.id.btn_neg);
        btn_pos = rootView.findViewById(R.id.btn_pos);
        title = rootView.findViewById(R.id.title);
        msg = rootView.findViewById(R.id.msg);

        if(getTxt_title() != null){
            title.setText(getTxt_title());
        }else{
            title.setText("TITULO DEFAULT");
        }

        if(getTxt_msg() != null){
            msg.setText(getTxt_msg());
        }else{
            msg.setText("MENSAJE DEFAULT");
        }

        if(getTxt_btn_neg() != null){
            btn_neg.setText(getTxt_btn_neg());
        }else{
            btn_neg.setText("CANCELAR");
        }

        if(getTxt_btn_pos() != null){
            btn_pos.setText(getTxt_btn_pos());
        }else{
            btn_pos.setText("ACEPTAR");
        }

        builder.setView(rootView);

        btn_neg.setOnClickListener(this);
        btn_pos.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_neg:
                myListener.onClickNegButton(getIdDialog());
                break;
            case R.id.btn_pos:
                myListener.onClickPosButton(getIdDialog());
                break;
        }
    }

    public void setIdDialog(int idDialog) {
        this.idDialog = idDialog;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            myListener = (ListenerButton) activity;
        }catch(ClassCastException e){
            Log.d("Error!!", "Ha ocurrido un error");
        }
    }

}
